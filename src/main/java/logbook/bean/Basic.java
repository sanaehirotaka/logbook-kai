package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.Config;
import logbook.internal.JsonHelper;

/**
 * api_basic
 *
 */
public class Basic implements Serializable {

    private static final long serialVersionUID = -2392950337873034663L;

    /** api_comment */
    private String comment = "";

    /** api_count_deck */
    private Integer countDeck = 0;

    /** api_count_kdock */
    private Integer countKdock = 0;

    /** api_count_ndock */
    private Integer countNdock = 0;

    /** api_experience */
    private Integer experience = 0;

    /** api_fcoin */
    private Integer fcoin = 0;

    /** api_large_dock */
    private Boolean largeDock = Boolean.FALSE;

    /** api_level */
    private Integer level = 0;

    /** api_max_chara */
    private Integer maxChara = 0;

    /** api_max_slotitem */
    private Integer maxSlotitem = 0;

    /** api_medals */
    private Integer medals = 0;

    /** api_nickname */
    private String nickname = "";

    /**
     * api_commentを取得します。
     * @return api_comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * api_commentを設定します。
     * @param comment api_comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * api_count_deckを取得します。
     * @return api_count_deck
     */
    public Integer getCountDeck() {
        return this.countDeck;
    }

    /**
     * api_count_deckを設定します。
     * @param countDeck api_count_deck
     */
    public void setCountDeck(Integer countDeck) {
        this.countDeck = countDeck;
    }

    /**
     * api_count_kdockを取得します。
     * @return api_count_kdock
     */
    public Integer getCountKdock() {
        return this.countKdock;
    }

    /**
     * api_count_kdockを設定します。
     * @param countKdock api_count_kdock
     */
    public void setCountKdock(Integer countKdock) {
        this.countKdock = countKdock;
    }

    /**
     * api_count_ndockを取得します。
     * @return api_count_ndock
     */
    public Integer getCountNdock() {
        return this.countNdock;
    }

    /**
     * api_count_ndockを設定します。
     * @param countNdock api_count_ndock
     */
    public void setCountNdock(Integer countNdock) {
        this.countNdock = countNdock;
    }

    /**
     * api_experienceを取得します。
     * @return api_experience
     */
    public Integer getExperience() {
        return this.experience;
    }

    /**
     * api_experienceを設定します。
     * @param experience api_experience
     */
    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    /**
     * api_fcoinを取得します。
     * @return api_fcoin
     */
    public Integer getFcoin() {
        return this.fcoin;
    }

    /**
     * api_fcoinを設定します。
     * @param fcoin api_fcoin
     */
    public void setFcoin(Integer fcoin) {
        this.fcoin = fcoin;
    }

    /**
     * api_large_dockを取得します。
     * @return api_large_dock
     */
    public Boolean getLargeDock() {
        return this.largeDock;
    }

    /**
     * api_large_dockを設定します。
     * @param largeDock api_large_dock
     */
    public void setLargeDock(Boolean largeDock) {
        this.largeDock = largeDock;
    }

    /**
     * api_levelを取得します。
     * @return api_level
     */
    public Integer getLevel() {
        return this.level;
    }

    /**
     * api_levelを設定します。
     * @param level api_level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * api_max_charaを取得します。
     * @return api_max_chara
     */
    public Integer getMaxChara() {
        return this.maxChara;
    }

    /**
     * api_max_charaを設定します。
     * @param maxChara api_max_chara
     */
    public void setMaxChara(Integer maxChara) {
        this.maxChara = maxChara;
    }

    /**
     * api_max_slotitemを取得します。
     * @return api_max_slotitem
     */
    public Integer getMaxSlotitem() {
        return this.maxSlotitem;
    }

    /**
     * api_max_slotitemを設定します。
     * @param maxSlotitem api_max_slotitem
     */
    public void setMaxSlotitem(Integer maxSlotitem) {
        this.maxSlotitem = maxSlotitem;
    }

    /**
     * api_medalsを取得します。
     * @return api_medals
     */
    public Integer getMedals() {
        return this.medals;
    }

    /**
     * api_medalsを設定します。
     * @param medals api_medals
     */
    public void setMedals(Integer medals) {
        this.medals = medals;
    }

    /**
     * api_nicknameを取得します。
     * @return api_nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * api_nicknameを設定します。
     * @param nickname api_nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * JsonObjectから{@link Basic}を構築します
     *
     * @param bean Basic
     * @param json JsonObject
     * @return {@link Basic}
     */
    public static Basic updateBasic(Basic bean, JsonObject json) {
        JsonHelper.bind(json)
                .setString("api_comment", bean::setComment)
                .setInteger("api_count_deck", bean::setCountDeck)
                .setInteger("api_count_kdock", bean::setCountKdock)
                .setInteger("api_count_ndock", bean::setCountNdock)
                .setInteger("api_experience", bean::setExperience)
                .setInteger("api_fcoin", bean::setFcoin)
                .setBoolean("api_large_dock", bean::setLargeDock)
                .setInteger("api_level", bean::setLevel)
                .setInteger("api_max_chara", bean::setMaxChara)
                .setInteger("api_max_slotitem", bean::setMaxSlotitem)
                .setInteger("api_medals", bean::setMedals)
                .setString("api_nickname", bean::setNickname);
        return bean;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link Basic}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(Basic.class, Basic::new)</code>
     * </blockquote>
     *
     * @return {@link Basic}
     */
    public static Basic get() {
        return Config.getDefault().get(Basic.class, Basic::new);
    }
}
