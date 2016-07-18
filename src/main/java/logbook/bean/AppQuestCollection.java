package logbook.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import logbook.bean.QuestList.Quest;
import logbook.internal.Config;
import logbook.internal.Logs;
import lombok.Data;

/**
 * 任務のコレクション
 */
@Data
public class AppQuestCollection implements Serializable {

    private static final long serialVersionUID = 1439562010375402524L;

    /** 任務 */
    private Map<Integer, AppQuest> quest = new TreeMap<>();

    /**
     * 任務を更新します
     */
    public void update() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));

        for (Iterator<Entry<Integer, AppQuest>> iterator = this.quest.entrySet().iterator(); iterator.hasNext();) {
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
    }

    /**
     * 任務を更新します
     *
     * @param questList 任務
     */
    public void update(QuestList questList) {
        this.update();

        for (Quest quest : questList.getList()) {
            if (quest != null) {
                this.quest.remove(quest.getNo());

                if (quest.getState() == 2) {
                    this.quest.put(quest.getNo(), AppQuest.toAppQuest(quest));
                }
            }
        }
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
