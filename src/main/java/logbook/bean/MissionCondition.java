package logbook.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import logbook.internal.Items;
import logbook.internal.Ships;
import lombok.Data;

@Data
public class MissionCondition implements Predicate<List<Ship>> {

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type;

    @JsonProperty("ship_type")
    private Set<String> shipType;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("item")
    private String item;

    @JsonProperty("order")
    private Integer order;

    @JsonProperty("count_type")
    private String countType;

    @JsonProperty("conditions")
    private List<MissionCondition> conditions;

    @JsonProperty("value")
    private Integer value;

    @JsonProperty("operator")
    private String operator;

    private Boolean result;

    @Override
    public boolean test(List<Ship> ships) {
        this.result = false;
        if (this.type != null) {
            if (this.type.equals("艦娘")) {
                this.result = this.testShip(ships);
            } else if (this.type.equals("艦隊")) {
                this.result = this.testFleet(ships);
            }
        } else if (this.operator != null) {
            this.result = this.testOperator(ships);
        }
        return this.result;
    }

    @Override
    public String toString() {
        if (this.type != null) {
            if (this.type.equals("艦娘")) {
                return this.toStringShip();
            } else if (this.type.equals("艦隊")) {
                return this.toStringFleet();
            }
        } else if (this.operator != null) {
            return this.toStringOperator();
        }
        return "";
    }

    /**
     * 条件(type=艦娘)
     *
     * @param ships 艦隊
     * @return 条件に一致する場合true
     */
    private boolean testShip(List<Ship> ships) {
        boolean result = false;
        if (this.order != null) {
            // 序列の指定有り
            if (ships.size() >= this.order)
                result = this.testShip0(ships.get(this.order - 1));
            if (result) {
                ships.set(this.order - 1, null);
            }
        } else {
            // 序列の指定無し
            for (int i = 0; i < ships.size(); i++) {
                result = this.testShip0(ships.get(i));
                if (result) {
                    ships.set(i, null);
                    break;
                }
            }
        }
        return result;
    }

    private boolean testShip0(Ship ship) {
        boolean result = true;
        if (ship == null) {
            result = false;
        }
        // 艦種条件
        if (result && this.shipType != null) {
            String stype = Ships.stype(ship)
                    .map(Stype::getName)
                    .orElse(null);
            result &= this.shipType.contains(stype)
                    || (this.shipType.contains("護衛空母")
                            && Ships.shipMst(ship).map(ShipMst::getTais).orElse(null) != null);
        }
        // レベル条件
        if (result && this.level != null) {
            result &= ship.getLv() >= this.level;
        }
        // 装備条件
        if (result && this.item != null) {
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                    .getSlotitemMap();
            result &= ship.getSlot().stream()
                    .map(itemMap::get)
                    .map(Items::slotitemMst)
                    .map(i -> i.orElse(null))
                    .filter(Objects::nonNull)
                    .map(SlotitemMst::getName)
                    .anyMatch(this.item::equals);
        }
        return result;
    }

    /**
     * 条件(type=艦隊)
     *
     * @param ships 艦隊
     * @return 条件に一致する場合true
     */
    private boolean testFleet(List<Ship> ships) {
        if ("レベル".equals(this.countType)) {
            return this.fleetStatus(ships, Ship::getLv) >= this.value;
        }
        if ("対潜".equals(this.countType)) {
            return this.fleetStatus(ships, ship -> ship.getTaisen().get(0)) >= this.value;
        }
        if ("対空".equals(this.countType)) {
            return this.fleetStatus(ships, ship -> ship.getTaiku().get(0)) >= this.value;
        }
        if ("索敵".equals(this.countType)) {
            return this.fleetStatus(ships, ship -> ship.getSakuteki().get(0)) >= this.value;
        }
        if ("装備".equals(this.countType)) {
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                    .getSlotitemMap();
            return ships.stream()
                    .filter(Objects::nonNull)
                    .map(Ship::getSlot)
                    .flatMap(Collection::stream)
                    .map(itemMap::get)
                    .map(Items::slotitemMst)
                    .map(i -> i.orElse(null))
                    .filter(Objects::nonNull)
                    .map(SlotitemMst::getName)
                    .filter(this.item::equals)
                    .count() >= this.value;
        }
        return false;
    }

    /**
     * ステータス合計
     *
     * @param ships 艦隊
     * @param function ステータス取得関数
     * @return ステータス合計
     */
    private int fleetStatus(List<Ship> ships, ToIntFunction<Ship> function) {
        return ships.stream()
                .filter(Objects::nonNull)
                .mapToInt(function)
                .sum();
    }

    /**
     * 条件(論理演算)
     *
     * @param ships 艦隊
     * @return 条件に一致する場合true
     */
    private boolean testOperator(List<Ship> ships) {
        Predicate<List<Ship>> predicate = null;
        for (MissionCondition condition : this.conditions) {
            if (predicate == null) {
                predicate = condition;
            } else {
                predicate = this.operator.endsWith("AND")
                        ? predicate.and(condition)
                        : predicate.or(condition);
            }
        }
        if ("NAND".equals(this.operator) || "NOR".equals(this.operator)) {
            predicate = predicate.negate();
        }
        return predicate.test(new ArrayList<>(ships));
    }

    private String toStringShip() {
        StringBuilder sb = new StringBuilder();
        if (this.level != null) {
            sb.append("レベル" + this.level + "以上の");
        }
        if (this.shipType != null) {
            sb.append(this.shipType.stream().collect(Collectors.joining("または")));
        } else {
            sb.append("任意の艦種");
        }
        if (this.order != null) {
            sb.append("を");
            if (this.order == 1) {
                sb.append("旗艦");
            } else {
                sb.append("序列" + this.order + "位");
            }
        }
        if (this.item != null) {
            sb.append("かつ" + this.item + "を装備");
        }
        return sb.toString();
    }

    private String toStringFleet() {
        StringBuilder sb = new StringBuilder();
        if ("装備".equals(this.countType)) {
            sb.append(this.item);
        } else {
            sb.append("艦隊" + this.countType + "合計");
        }
        sb.append(this.value);
        sb.append("以上");
        return sb.toString();
    }

    private String toStringOperator() {
        switch (this.operator) {
        case "AND":
            return "次の条件を全て満たす";
        case "OR":
            return "次の条件のいずれか少なくとも1つを満たす";
        case "NAND":
            return "次の条件のいずれか少なくとも1つを満たさない";
        case "NOR":
            return "次の条件を全て満たさない";
        default:
            return "";
        }
    }
}
