package logbook.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import logbook.internal.BattleLogs;
import logbook.internal.BattleLogs.SimpleBattleLog;
import logbook.internal.Config;
import logbook.internal.Logs;
import logbook.internal.MissionLogs;
import logbook.internal.MissionLogs.SimpleMissionLog;
import lombok.Data;
import lombok.val;

/**
 * 任務の受託期間を管理します。
 *
 */
@Data
public class AppQuestDuration {

    /** 期限をキーにするマップ */
    private ConcurrentHashMap<String, Map<Integer, List<Duration>>> map = new ConcurrentHashMap<>();

    /**
     * 受託します。
     * 
     * @param quest 任務
     */
    @JsonIgnore
    public void set(AppQuest quest) {
        if (quest.getExpire() == null) {
            return;
        }
        val durationMap = this.map.computeIfAbsent(quest.getExpire(), k -> new ConcurrentHashMap<>());
        val durations = durationMap.computeIfAbsent(quest.getNo(), k -> new ArrayList<>());
        synchronized (durations) {
            for (Duration duration : durations) {
                if (duration.getTo() == null) {
                    return;
                }
            }
            Duration duration = new Duration();
            duration.setFrom(Logs.nowString());
            durations.add(duration);
        }
        // 重複があれば削除
        for (val entry : this.map.entrySet()) {
            if (!entry.getKey().equals(quest.getExpire())) {
                entry.getValue().remove(quest.getNo());
            }
        }
        // 期限切れの削除
        String now = Logs.nowString();
        val iterator = this.map.entrySet().iterator();
        for (; iterator.hasNext();) {
            val entry = iterator.next();
            if (now.compareTo(entry.getKey()) > 0) {
                iterator.remove();
            }
        }
    }

    /**
     * 受託を停止します。
     * 
     * @param questId 任務ID
     */
    @JsonIgnore
    public void unset(Integer questId) {
        for (val entry : this.map.entrySet()) {
            val durations = entry.getValue().get(questId);
            if (durations != null) {
                synchronized (durations) {
                    for (Duration duration : durations) {
                        if (duration.getTo() == null) {
                            duration.setTo(Logs.nowString());
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 受託を完了します。
     * 
     * @param questId
     */
    @JsonIgnore
    public void remove(Integer questId) {
        for (val entry : this.map.entrySet()) {
            entry.getValue().remove(questId);
        }
    }

    @JsonIgnore
    public Optional<List<SimpleBattleLog>> getCondition(AppQuest quest) {
        val durationMap = this.map.get(quest.getExpire());
        if (durationMap == null)
            return Optional.empty();

        val durations = durationMap.get(quest.getNo());
        if (durations == null)
            return Optional.empty();

        return Optional.of(BattleLogs.readSimpleLog(log -> {
            String date = log.getDateString();
            for (Duration duration : durations) {
                String from = duration.getFrom();
                String to = duration.getTo();
                if (date.compareTo(from) >= 0 && (to == null || date.compareTo(to) <= 0)) {
                    return true;
                }
            }
            return false;
        }));
    }

    @JsonIgnore
    public Optional<List<SimpleMissionLog>> getMissionCondition(AppQuest quest) {
        val durationMap = this.map.get(quest.getExpire());
        if (durationMap == null)
            return Optional.empty();

        val durations = durationMap.get(quest.getNo());
        if (durations == null)
            return Optional.empty();

        return Optional.of(MissionLogs.readSimpleLog(log -> {
            String date = log.getDateString();
            for (Duration duration : durations) {
                String from = duration.getFrom();
                String to = duration.getTo();
                if (date.compareTo(from) >= 0 && (to == null || date.compareTo(to) <= 0)) {
                    return true;
                }
            }
            return false;
        }));
    }

    /**
     * 受託期間
     *
     */
    @Data
    public static class Duration {
        private String from;
        private String to;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>AppQuestCondition</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppQuestCondition.class, AppQuestCondition::new)</code>
     * </blockquote>
     *
     * @return <code>AppQuestCondition</code>
     */
    public static AppQuestDuration get() {
        return Config.getDefault().get(AppQuestDuration.class, AppQuestDuration::new);
    }
}
