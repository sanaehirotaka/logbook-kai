package logbook.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import logbook.bean.QuestList.Quest;
import logbook.internal.Config;
import logbook.internal.Logs;
import lombok.Data;
import lombok.val;

/**
 * 任務のコレクション
 */
@Data
public class AppQuestCollection implements Serializable {

    private static final long serialVersionUID = 9151098623774557766L;

    /** 任務 */
    private ConcurrentSkipListMap<Integer, AppQuest> quest = new ConcurrentSkipListMap<>();

    /**
     * 任務を更新します
     */
    public void update() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));

        val copyMap = new ConcurrentSkipListMap<>(this.quest);
        for (Iterator<Entry<Integer, AppQuest>> iterator = copyMap.entrySet().iterator(); iterator.hasNext();) {
            Entry<Integer, AppQuest> entry = iterator.next();

            String exp = entry.getValue().getExpire();
            if (exp != null) {
                TemporalAccessor ta = Logs.DATE_FORMAT.parse(exp);
                ZonedDateTime exptime = ZonedDateTime.of(LocalDateTime.from(ta), ZoneId.of("Asia/Tokyo"));
                // 期限切れを削除
                if (now.compareTo(exptime) > 0) {
                    iterator.remove();
                }
            }
        }
        this.quest = copyMap;
    }

    /**
     * 任務を更新します
     *
     * @param questList 任務
     */
    public void update(QuestList questList, boolean all) {
        this.update();

        // 今はすべての Quest が一度に送られてくるので、all のタブのデータの場合は既にある map を保持しておく必要はない
        ConcurrentSkipListMap<Integer, AppQuest> copyMap  = all ? new ConcurrentSkipListMap<>() : new ConcurrentSkipListMap<>(this.quest);
        if (questList.getList() != null) {
            for (Quest quest : questList.getList()) {
                if (quest != null) {
                    AppQuest appQuest = AppQuest.toAppQuest(quest);
                    copyMap.put(quest.getNo(), appQuest);
                    if (quest.getState() == 2) {
                        AppQuestDuration.get().set(appQuest);
                    } else {
                        AppQuestDuration.get().unset(quest.getNo());
                    }
                }
            }
        }
        this.quest = copyMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>AppQuestCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppQuestCollection.class, AppQuestCollection::new)</code>
     * </blockquote>
     *
     * @return <code>AppQuestCollection</code>
     */
    public static AppQuestCollection get() {
        return Config.getDefault().get(AppQuestCollection.class, AppQuestCollection::new);
    }
}
