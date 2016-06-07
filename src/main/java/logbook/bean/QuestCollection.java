/**
 * 
 */
package logbook.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import logbook.internal.Config;

/**
 * 任務のコレクション
 *
 */
public class QuestCollection implements Serializable {

    private static final long serialVersionUID = 567858529993655792L;

    /** 任務 */
    private Map<Integer, Quest> quest = new LinkedHashMap<>();

    /**
     * 任務を取得します。
     * @return 任務
     */
    public void addQuest(Map<Integer, Quest> quests, int start_idx) {
        // 見えてるクエストの更新
        this.quest.putAll(quests);

        // クエストの削除
        // TODO 運が悪いとPAGE境界のクエストが消えない．
        // TODO api_completed_kindが1のときクエストが消えたのを利用する．
        List<Integer> no = quests.keySet().stream()
                .sorted()
                .collect(Collectors.toList());
        int minv = no.get(0);
        int maxv = no.get(no.size() - 1);
        Collection<Integer> c = quest.keySet().stream()
                .filter(n -> minv <= n && n <= maxv)
                .collect(Collectors.toList());
        c.removeAll(quests.keySet());
        c.forEach(i -> quest.get(i).setState(0));
    }

    /**
     * 任務を取得します。
     * @return 任務
     */
    public Map<Integer, Quest> getQuestMap() {
        return this.quest;
    }

    /**
     * 任務を設定します。
     * @param ndockMap 任務
     */
    public void setQuestMap(Map<Integer, Quest> questMap) {
        this.quest = questMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>QuestCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(QuestCollection.class, QuestCollection::new)</code>
     * </blockquote>
     *
     * @return <code>QuestCollection</code>
     */
    public static QuestCollection get() {
        return Config.getDefault().get(QuestCollection.class, QuestCollection::new);
    }
}
