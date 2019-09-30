package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import lombok.Data;

@Data
public class Createitem implements Serializable {

    private static final long serialVersionUID = 27343263577644496L;

    /** api_item1 */
    private Integer item1;

    /** api_item2 */
    private Integer item2;

    /** api_item3 */
    private Integer item3;

    /** api_item4 */
    private Integer item4;

    /** api_create_flag */
    private Boolean createFlag;

    /** api_material */
    private List<Integer> material;

    /** api_get_items */
    private List<SlotItem> getItems;

    /** api_unset_items */
    private List<UnsetItems> unsetItems;
    
    /** SlotItem */
    private SlotItem slotItem;

    /** 秘書艦 */
    private Ship secretary;

    /**
     * <code>JsonObject</code>と<code>RequestMetaData</code>から{@link Createitem}を構築します
     *
     * @param json JsonObject
     * @param req RequestMetaData
     * @return {@link Createitem}
     */
    public static Createitem toCreateitem(JsonObject json, RequestMetaData req) {
        Createitem bean = new Createitem();
        bean.setItem1(Integer.valueOf(req.getParameter("api_item1", "0")));
        bean.setItem2(Integer.valueOf(req.getParameter("api_item2", "0")));
        bean.setItem3(Integer.valueOf(req.getParameter("api_item3", "0")));
        bean.setItem4(Integer.valueOf(req.getParameter("api_item4", "0")));

        JsonHelper.bind(json)
                .setBoolean("api_create_flag", bean::setCreateFlag)
                .setIntegerList("api_material", bean::setMaterial)
                .set("api_get_items", bean::setGetItems, JsonHelper.toList(SlotItem::toSlotItem))
                .set("api_unset_items", bean::setUnsetItems, JsonHelper.toList(UnsetItems::toUnsetItems));

        Ship secretary = null;
        DeckPort port = DeckPortCollection.get()
                .getDeckPortMap()
                .get(1);
        if (port != null) {
            List<Integer> ships = port.getShip();
            if (ships != null) {
                Integer id = ships.get(0);
                secretary = ShipCollection.get()
                        .getShipMap()
                        .get(id);
            }
        }
        bean.setSecretary(secretary);

        return bean;
    }

    @Data
    public static class UnsetItems {

        /** api_type3 */
        private Integer type3;

        /** api_unsetslot */
        private List<Integer> unsetslot;

        /**
         * JsonObjectから{@link UnsetItems}を構築します
         *
         * @param json JsonObject
         * @return {@link UnsetItems}
         */
        public static UnsetItems toUnsetItems(JsonObject json) {
            UnsetItems bean = new UnsetItems();
            JsonHelper.bind(json)
                    .setInteger("api_type3", bean::setType3)
                    .setIntegerList("api_unsetslot", bean::setUnsetslot);
            return bean;
        }
    }
}
