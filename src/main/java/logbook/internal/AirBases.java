package logbook.internal;

import java.util.Map;

import logbook.bean.Mapinfo.AirBase;
import logbook.bean.Mapinfo.PlaneInfo;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;

/**
 * 基地航空隊に関するメソッドを集めたクラス
 *
 */
public class AirBases {

    /**
     * 制空値
     *
     * @param airBase 基地航空隊
     * @return 制空値
     */
    public static int airSuperiority(AirBase airBase) {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();

        // 防空の場合true
        boolean isIntercept = airBase.getActionKind() == 2;
        // 対空値
        int tyku = 0;
        // 偵察機補正
        double magnification = 1;

        for (PlaneInfo plane : airBase.getPlaneInfo()) {
            if (plane.getCount() == null)
                continue;
            SlotItem item = itemMap.get(plane.getSlotid());
            if (item == null)
                continue;
            SlotitemMst itemMst = itemMstMap.get(item.getSlotitemId());
            if (itemMst == null)
                continue;

            if (SlotItemType.艦上戦闘機.equals(itemMst)
                    || SlotItemType.艦上攻撃機.equals(itemMst)
                    || SlotItemType.艦上爆撃機.equals(itemMst)
                    || SlotItemType.水上爆撃機.equals(itemMst)
                    || SlotItemType.水上戦闘機.equals(itemMst)
                    || SlotItemType.噴式戦闘爆撃機.equals(itemMst)
                    || SlotItemType.噴式戦闘機.equals(itemMst)
                    || SlotItemType.噴式攻撃機.equals(itemMst)
                    || SlotItemType.陸上攻撃機.equals(itemMst)
                    || SlotItemType.局地戦闘機.equals(itemMst)) {
                // 防空の場合
                // [(対空 ＋ 迎撃 ＋ 対爆 × 2) × √(搭載数) ＋ 熟練度補正]
                // 出撃の場合
                // [(対空 ＋ 迎撃 × 1.5) × √(搭載数) ＋ 熟練度補正]

                double local = itemMst.getTyku() + Ships.airSuperiorityTykuAdditional(itemMst, item);

                if (SlotItemType.局地戦闘機.equals(itemMst)) {
                    if (isIntercept) {
                        local += itemMst.getHouk() + itemMst.getHoum() * 2D;
                    } else {
                        local += itemMst.getHouk() * 1.5D;
                    }
                }
                local *= Math.sqrt(plane.getCount());
                local += Ships.airSuperiorityBonus(itemMst, item);

                tyku += (int) local;
            } else if (isIntercept && (SlotItemType.艦上偵察機.equals(itemMst)
                    || SlotItemType.艦上偵察機II.equals(itemMst)
                    || SlotItemType.水上偵察機.equals(itemMst)
                    || SlotItemType.大型飛行艇.equals(itemMst)
                    || SlotItemType.噴式偵察機.equals(itemMst))) {
                magnification = Math.max(magnification, magnification(itemMst));
            }
        }
        // 防空の場合
        //  [各中隊の制空値(防空) の総計 × 偵察機補正]
        if (isIntercept) {
            tyku = (int) (tyku * magnification);
        }
        return tyku;
    }

    private static double magnification(SlotitemMst itemMst) {
        int saku = itemMst.getSaku();

        if (SlotItemType.水上偵察機.equals(itemMst)
                || SlotItemType.大型飛行艇.equals(itemMst)) {
            if (saku >= 9)
                return 1.16D;
            if (saku >= 8)
                return 1.13D;
            return 1.1D;
        }
        if (SlotItemType.艦上偵察機.equals(itemMst)
                || SlotItemType.艦上偵察機II.equals(itemMst)
                || SlotItemType.噴式偵察機.equals(itemMst)) {
            if (saku >= 9)
                return 1.3D;
            if (saku >= 8)
                return 1.25D;
            return 1.2D;
        }

        return 1.0D;
    }
}
