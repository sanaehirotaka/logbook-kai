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

            if (Items.isAircraft(itemMst)) {
                tyku += airSuperiority(item, plane.getCount(), isIntercept);
            }
            if (isIntercept && (Items.isReconAircraft(itemMst))) {
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

    /**
     * スロット内制空値
     *
     * @param item 装備
     * @param onSlot 機数
     * @param isIntercept 防空の場合true
     * @return 制空値
     */
    public static int airSuperiority(SlotItem item, int onSlot, boolean isIntercept) {
        if (onSlot <= 0)
            return 0;
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        SlotitemMst itemMst = itemMstMap.get(item.getSlotitemId());
        if (itemMst == null)
            return 0;

        // 防空の場合
        // [(対空 ＋ 迎撃 ＋ 対爆 × 2) × √(搭載数) ＋ 熟練度補正]
        // 出撃の場合
        // [(対空 ＋ 迎撃 × 1.5) × √(搭載数) ＋ 熟練度補正]
        double slotLocal = itemMst.getTyku() + Ships.airSuperiorityTykuAdditional(itemMst, item);
        if (SlotItemType.局地戦闘機.equals(itemMst)) {
            if (isIntercept) {
                slotLocal += itemMst.getHouk() + itemMst.getHoum() * 2D;
            } else {
                slotLocal += itemMst.getHouk() * 1.5D;
            }
        }
        slotLocal *= Math.sqrt(onSlot);
        slotLocal += Ships.airSuperiorityBonus(itemMst, item);
        return (int) slotLocal;
    }

    /**
     * 加算される行動半径を計算します。
     *
     * @param distance 最小行動半径
     * @param reconDistance 偵察機の行動半径
     * @return 加算される行動半径
     */
    public static int distanceAdditional(int distance, int reconDistance) {
        if (distance >= reconDistance) {
            return 0;
        }
        return (int) Math.min(Math.sqrt(reconDistance - distance), 3);
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
