package logbook.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import logbook.bean.AppQuest;
import logbook.bean.AppQuestCondition;
import logbook.bean.AppQuestCondition.FilterCondition;
import logbook.bean.AppQuestDuration;
import logbook.bean.BattleLog;
import logbook.bean.Enemy;
import logbook.bean.MapinfoMst;
import logbook.bean.MapinfoMstCollection;
import logbook.bean.ShipMst;
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
        private Map<String, Rank> cell = new HashMap<>();
        private Rank boss = new Rank();

        public void add(Count count) {
            this.start += count.start;
            this.all.add(count.all);
            for (Entry<String, Rank> entry : count.cell.entrySet()) {
                this.cell.merge(entry.getKey(), entry.getValue(), Rank::add);
            }
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

        public Rank add(Rank rank) {
            this.s += rank.s;
            this.a += rank.a;
            this.b += rank.b;
            this.c += rank.c;
            this.d += rank.d;
            this.e += rank.e;
            return this;
        }
    }

    public static QuestCollect collect(AppQuest quest, AppQuestCondition condition) {
        QuestCollect collect = new QuestCollect();
        List<SimpleBattleLog> logs = AppQuestDuration.get().getCondition(quest);
        if (logs == null) {
            return null;
        }
        Collection<MapinfoMst> mapinfo = MapinfoMstCollection.get().getMapinfo().values();
        for (SimpleBattleLog log : logs) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            // 海域
            String map = mapinfo.stream().filter(i -> log.getArea().equals(i.getName()))
                    .map(i -> i.getMapareaId() + "-" + i.getNo())
                    .findFirst()
                    .orElse(null);

            // 戦闘ログ
            BattleLog battleLog = null;
            PhaseState p = null;

            // フィルター条件
            FilterCondition filter = condition.getFilter();
            if (filter != null) {
                if (!filter.getArea().contains(map)) {
                    continue;
                }
                if (filter.getFleet() != null) {
                    battleLog = BattleLogs.read(log.getDateString());
                    p = new PhaseState(battleLog);

                    List<ShipMst> ships = p.getAfterFriend().stream()
                            .filter(Objects::nonNull)
                            .map(Ships::shipMst)
                            .map(s -> s.orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (!filter.getFleet().test(ships)) {
                        continue;
                    }
                }
            }

            if (condition.isCollectStypeInternal()) {
                if (battleLog == null)
                    battleLog = BattleLogs.read(log.getDateString());
                if (p == null)
                    p = new PhaseState(battleLog);

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
            if (map != null) {
                // 海域ごとのカウント
                count(map, log, collect.getArea().computeIfAbsent(map, k -> new Count()));
            }
            // 合計
            count(map, log, collect.getTotal());

            if (condition.test(collect)) {
                break;
            }
        }
        return collect;
    }

    private static void count(String map, SimpleBattleLog log, Count count) {
        boolean isStart = log.getBoss().contains("出撃");
        boolean isBoss = log.getBoss().contains("ボス");
        String cell = log.getCell();
        String rank = log.getRank();

        if (isStart) {
            count.start++;
        }
        if (isBoss) {
            rank(map, rank, count.getBoss());
        }
        rank(map, rank, count.getCell().computeIfAbsent(map + "-" + cell, i -> new Rank()));
        rank(map, rank, count.getAll());
    }

    private static void rank(String map, String rank, Rank rankCount) {
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
