package logbook.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import logbook.bean.AppQuest;
import logbook.bean.AppQuestCondition;
import logbook.bean.AppQuestDuration;
import logbook.bean.BattleLog;
import logbook.bean.Enemy;
import logbook.bean.MapinfoMst;
import logbook.bean.MapinfoMstCollection;
import logbook.bean.Stype;
import logbook.internal.BattleLogs.SimpleBattleLog;
import lombok.Data;

/**
 * 任務進捗集計
 *
 */
@Data
public class QuestCollect {

    private Count total = new Count();

    private Map<String, Count> area = new HashMap<>();

    private Map<String, Integer> stype = new HashMap<>();

    @Data
    public static class Count {
        private int start;
        private Rank all = new Rank();
        private Rank boss = new Rank();

        public void add(Count count) {
            this.start += count.start;
            this.all.add(count.all);
            this.boss.add(count.boss);
        }
    }

    @Data
    public static class Rank {
        private int s;
        private int a;
        private int b;
        private int c;
        private int d;
        private int e;

        public void add(Rank rank) {
            this.s += rank.s;
            this.a += rank.a;
            this.b += rank.b;
            this.c += rank.c;
            this.d += rank.d;
            this.e += rank.e;
        }
    }

    public static QuestCollect collect(AppQuest quest, AppQuestCondition condition) {
        QuestCollect collect = new QuestCollect();
        List<SimpleBattleLog> logs = AppQuestDuration.get().getCondition(quest);
        Collection<MapinfoMst> mapinfo = MapinfoMstCollection.get().getMapinfo().values();
        for (SimpleBattleLog log : logs) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            if (condition.isCollectStypeInternal()) {
                BattleLog battleLog = BattleLogs.read(log.getDateString());
                PhaseState p = new PhaseState(battleLog);
                p.apply(battleLog.getBattle());
                p.apply(battleLog.getMidnight());

                List<Enemy> e = new ArrayList<>();
                e.addAll(p.getAfterEnemy());
                e.addAll(p.getAfterEnemyCombined());
                e.stream()
                        .filter(Objects::nonNull)
                        // 撃沈したか？
                        .filter(o -> o.getNowhp() <= 0)
                        // 艦種に変換
                        .map(Ships::stype)
                        .map(o -> o.orElse(null))
                        .filter(Objects::nonNull)
                        // 艦種の名前を取り出す
                        .map(Stype::getName)
                        // 艦種ごとにカウント
                        .forEach(key -> {
                            collect.getStype().merge(key, 1, (a, b) -> a + b);
                        });
            }
            // 海域
            String map = mapinfo.stream().filter(i -> log.getArea().equals(i.getName()))
                    .map(i -> i.getMapareaId() + "-" + i.getNo())
                    .findFirst()
                    .orElse(null);

            if (map != null) {
                // 海域ごとのカウント
                count(log, collect.getArea().computeIfAbsent(map, k -> new Count()));
            }
            // 合計
            count(log, collect.getTotal());

            if (condition.test(collect)) {
                break;
            }
        }
        return collect;
    }

    private static void count(SimpleBattleLog log, Count count) {
        boolean isStart = log.getBoss().contains("出撃");
        boolean isBoss = log.getBoss().contains("ボス");
        String rank = log.getRank();

        if (isStart) {
            count.start++;
        }
        if (isBoss) {
            rank(rank, count.getBoss());
        }
        rank(rank, count.getAll());
    }

    private static void rank(String rank, Rank rankCount) {
        if (rank.equals("S")) {
            rankCount.s++;
        }
        if (rank.equals("A")) {
            rankCount.a++;
        }
        if (rank.equals("B")) {
            rankCount.b++;
        }
        if (rank.equals("C")) {
            rankCount.c++;
        }
        if (rank.equals("D")) {
            rankCount.d++;
        }
        if (rank.equals("E")) {
            rankCount.e++;
        }
    }
}
