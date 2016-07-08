package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.internal.JsonHelper;

/**
 * 任務
 */
public class QuestList implements Serializable {

    private static final long serialVersionUID = 7042549795095230008L;

    /** api_count */
    private Integer count;

    /** api_completed_kind */
    private Integer completedKind;

    /** api_page_count */
    private Integer pageCount;

    /** api_disp_page */
    private Integer dispPage;

    /** api_list */
    private List<Quest> list;

    /** api_exec_count */
    private Integer execCount;

    /**
     * api_countを取得します。
     * @return api_count
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * api_countを設定します。
     * @param count api_count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * api_completed_kindを取得します。
     * @return api_completed_kind
     */
    public Integer getCompletedKind() {
        return this.completedKind;
    }

    /**
     * api_completed_kindを設定します。
     * @param completedKind api_completed_kind
     */
    public void setCompletedKind(Integer completedKind) {
        this.completedKind = completedKind;
    }

    /**
     * api_page_countを取得します。
     * @return api_page_count
     */
    public Integer getPageCount() {
        return this.pageCount;
    }

    /**
     * api_page_countを設定します。
     * @param pageCount api_page_count
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * api_disp_pageを取得します。
     * @return api_disp_page
     */
    public Integer getDispPage() {
        return this.dispPage;
    }

    /**
     * api_disp_pageを設定します。
     * @param dispPage api_disp_page
     */
    public void setDispPage(Integer dispPage) {
        this.dispPage = dispPage;
    }

    /**
     * api_listを取得します。
     * @return api_list
     */
    public List<Quest> getList() {
        return this.list;
    }

    /**
     * api_listを設定します。
     * @param list api_list
     */
    public void setList(List<Quest> list) {
        this.list = list;
    }

    /**
     * api_exec_countを取得します。
     * @return api_exec_count
     */
    public Integer getExecCount() {
        return this.execCount;
    }

    /**
     * api_exec_countを設定します。
     * @param execCount api_exec_count
     */
    public void setExecCount(Integer execCount) {
        this.execCount = execCount;
    }

    /**
     * api_list[n]
     */
    public static class Quest implements Serializable {

        private static final long serialVersionUID = 257861496728265702L;

        /** api_no */
        private Integer no;

        /** api_category */
        private Integer category;

        /** api_type */
        private Integer type;

        /** api_state */
        private Integer state;

        /** api_title */
        private String title;

        /** api_detail */
        private String detail;

        /** api_get_material */
        private List<Integer> getMaterial;

        /** api_bonus_flag */
        private Integer bonusFlag;

        /** api_progress_flag */
        private Integer progressFlag;

        /** api_invalid_flag */
        private Integer invalidFlag;

        /**
         * api_noを取得します。
         * @return api_no
         */
        public Integer getNo() {
            return this.no;
        }

        /**
         * api_noを設定します。
         * @param no api_no
         */
        public void setNo(Integer no) {
            this.no = no;
        }

        /**
         * api_categoryを取得します。
         * @return api_category
         */
        public Integer getCategory() {
            return this.category;
        }

        /**
         * api_categoryを設定します。
         * @param category api_category
         */
        public void setCategory(Integer category) {
            this.category = category;
        }

        /**
         * api_typeを取得します。
         * @return api_type
         */
        public Integer getType() {
            return this.type;
        }

        /**
         * api_typeを設定します。
         * @param type api_type
         */
        public void setType(Integer type) {
            this.type = type;
        }

        /**
         * api_stateを取得します。
         * @return api_state
         */
        public Integer getState() {
            return this.state;
        }

        /**
         * api_stateを設定します。
         * @param state api_state
         */
        public void setState(Integer state) {
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
        public Integer getBonusFlag() {
            return this.bonusFlag;
        }

        /**
         * api_bonus_flagを設定します。
         * @param bonusFlag api_bonus_flag
         */
        public void setBonusFlag(Integer bonusFlag) {
            this.bonusFlag = bonusFlag;
        }

        /**
         * api_progress_flagを取得します。
         * @return api_progress_flag
         */
        public Integer getProgressFlag() {
            return this.progressFlag;
        }

        /**
         * api_progress_flagを設定します。
         * @param progressFlag api_progress_flag
         */
        public void setProgressFlag(Integer progressFlag) {
            this.progressFlag = progressFlag;
        }

        /**
         * api_invalid_flagを取得します。
         * @return api_invalid_flag
         */
        public Integer getInvalidFlag() {
            return this.invalidFlag;
        }

        /**
         * api_invalid_flagを設定します。
         * @param invalidFlag api_invalid_flag
         */
        public void setInvalidFlag(Integer invalidFlag) {
            this.invalidFlag = invalidFlag;
        }

        /**
         * JsonObjectから{@link Quest}を構築します
         *
         * @param json JsonObject
         * @return {@link Quest}
         */
        public static Quest toQuest(JsonValue value) {
            if (value instanceof JsonObject) {
                Quest bean = new Quest();
                JsonHelper.bind((JsonObject) value)
                        .setInteger("api_no", bean::setNo)
                        .setInteger("api_category", bean::setCategory)
                        .setInteger("api_type", bean::setType)
                        .setInteger("api_state", bean::setState)
                        .setString("api_title", bean::setTitle)
                        .setString("api_detail", bean::setDetail)
                        .set("api_get_material", bean::setGetMaterial, JsonHelper::toIntegerList)
                        .setInteger("api_bonus_flag", bean::setBonusFlag)
                        .setInteger("api_progress_flag", bean::setProgressFlag)
                        .setInteger("api_invalid_flag", bean::setInvalidFlag);
                return bean;
            } else {
                return null;
            }
        }
    }

    /**
     * JsonObjectから{@link QuestList}を構築します
     *
     * @param json JsonObject
     * @return {@link QuestList}
     */
    public static QuestList toQuestList(JsonObject json) {
        QuestList bean = new QuestList();
        JsonHelper.bind(json)
                .setInteger("api_count", bean::setCount)
                .setInteger("api_completed_kind", bean::setCompletedKind)
                .setInteger("api_page_count", bean::setPageCount)
                .setInteger("api_disp_page", bean::setDispPage)
                .set("api_list", bean::setList, JsonHelper.toList(Quest::toQuest))
                .setInteger("api_exec_count", bean::setExecCount);
        return bean;
    }
}
