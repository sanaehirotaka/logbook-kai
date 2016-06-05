/**
 * 
 */
package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_get_member/questlist
 *
 */
public final class Quest implements Serializable {

    private static final long serialVersionUID = 2359372032347671753L;

    /** api_no */
    private int no;

    /** api_category */
    private int category;

    /** api_type */
    private int type;

    /** api_state */
    private int state;

    /** api_title */
    private String title;

    /** api_detail */
    private String detail;

    /** api_get_material */
    private List<Integer> getMaterial;

    /** api_bonus_flag */
    private int bonusFlag;

    /** api_progress_flag */
    private int progressFlag;

    /**
     * api_noを取得します。
     * @return api_no
     */
    public int getNo() {
        return this.no;
    }

    /**
     * api_noを設定します。
     * @param no api_no
     */
    public void setNo(int no) {
        this.no = no;
    }

    /**
     * api_categoryを取得します。
     * @return api_category
     */
    public int getCategory() {
        return this.category;
    }

    /**
     * api_categoryを設定します。
     * @param category api_category
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * api_typeを取得します。
     * @return api_type
     */
    public int getType() {
        return this.type;
    }

    /**
     * api_typeを設定します。
     * @param type api_type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * api_stateを取得します。
     * @return api_state
     */
    public int getState() {
        return this.state;
    }

    /**
     * api_stateを設定します。
     * @param state api_state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * api_titleを取得します。
     * @return api_title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * api_titleを設定します。
     * @param title api_title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * api_detailを取得します。
     * @return api_detail
     */
    public String getDetail() {
        return this.detail;
    }

    /**
     * api_detailを設定します。
     * @param detail api_detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * api_get_materialを取得します。
     * @return api_get_material
     */
    public List<Integer> getGetMaterial() {
        return this.getMaterial;
    }

    /**
     * api_get_materialを設定します。
     * @param getMaterial api_get_material
     */
    public void setGetMaterial(List<Integer> getMaterial) {
        this.getMaterial = getMaterial;
    }

    /**
     * api_bonus_flagを取得します。
     * @return api_bonus_flag
     */
    public int getBonusFlag() {
        return this.bonusFlag;
    }

    /**
     * api_bonus_flagを設定します。
     * @param bonusFlag api_bonus_flag
     */
    public void setBonusFlag(int bonusFlag) {
        this.bonusFlag = bonusFlag;
    }

    /**
     * api_progress_flagを取得します。
     * @return api_progress_flag
     */
    public int getProgressFlag() {
        return this.progressFlag;
    }

    /**
     * api_progress_flagを設定します。
     * @param progressFlag api_progress_flag
     */
    public void setProgressFlag(int progressFlag) {
        this.progressFlag = progressFlag;
    }

    // tmp
    public String getCategoryString() {
        switch(category){
        case 1:
            return "編成";
        case 2:
        case 8:
            return "出撃";
        case 3:
            return "演習";
        case 5:
            return "補給";
        case 6:
            return "工廠";
        case 7:
            return "改装";
        default:
            return "undf";
        }
    }
    // tmp
    public static String getTypeString(int type) {
        switch(type){
        case 1:
            return "単発";
        case 2:
            return "デイリー";
        case 3:
            return "ウィークリー";
        case 6:
            return "マンスリー";
        default:
            return "undf";
        }
    }
    // tmp
    public String getTypeString() {
        return Quest.getTypeString(this.type);
    }
    // tmp
    public String getStateString() {
        switch (state) {
        case 0:
            return "";
        case 1:
            return "出現";
        case 2:
            return "遂行";
        case 3:
            return "達成";
        default:
            return "undf";
        }
    }
    // tmp
    public String toString(){
        return (state==2?"*":"") + getTypeString()+ getCategoryString() +':'+ title + (state==2? "(" + this.progressFlag + ")" : "");
    }

    /**
     * JsonObjectから{@link Quest}を構築します
     *
     * @param json JsonObject
     * @return {@link Ndock}
     */
    public static Quest toQuest(JsonObject json) {
        Quest bean = new Quest();
        JsonHelper.bind(json)
                .setInteger("api_no", bean::setNo)
                .setInteger("api_category", bean::setCategory)
                .setInteger("api_type", bean::setType)
                .setInteger("api_state", bean::setState)
                .setString("api_title", bean::setTitle)
                .setString("api_detail", bean::setDetail)
                .set("api_get_material", bean::setGetMaterial, JsonHelper::toIntegerList)
                .setInteger("api_bonus_flag", bean::setBonusFlag)
                .setInteger("api_progress_flag", bean::setProgressFlag);
//                .setInteger("api_invalid_flag", bean::setInvalidFlag);
        return bean;
    }
}
