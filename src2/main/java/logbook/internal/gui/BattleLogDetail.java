package logbook.internal.gui;

import java.time.ZoneId;
import java.util.StringJoiner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.internal.BattleLogs.SimpleBattleLog;
import logbook.internal.Logs;

/**
 * 戦闘ログの詳細行
 *
 */
public class BattleLogDetail {

    /** 日付 */
    private StringProperty date;
    /** 海域 */
    private StringProperty area;
    /** マス */
    private StringProperty cell;
    /** ボス */
    private StringProperty boss;
    /** ランク */
    private StringProperty rank;
    /** 艦隊行動 */
    private StringProperty intercept;
    /** 味方陣形 */
    private StringProperty fformation;
    /** 敵陣形 */
    private StringProperty eformation;
    /** 制空権 */
    private StringProperty dispseiku;
    /** 味方触接 */
    private StringProperty ftouch;
    /** 敵触接 */
    private StringProperty etouch;
    /** 敵艦隊 */
    private StringProperty efleet;
    /** ドロップ艦種 */
    private StringProperty dropType;
    /** ドロップ艦娘 */
    private StringProperty dropShip;

    /**
     * 日付を取得します。
     * @return 日付
     */
    public String getDate() {
        return this.date.get();
    }

    /**
     * 日付を設定します。
     * @param date 日付
     */
    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }

    /**
     * 日付を取得します。
     * @return 日付
     */
    public StringProperty dateProperty() {
        return this.date;
    }

    /**
     * 海域を取得します。
     * @return 海域
     */
    public String getArea() {
        return this.area.get();
    }

    /**
     * 海域を設定します。
     * @param area 海域
     */
    public void setArea(String area) {
        this.area = new SimpleStringProperty(area);
    }

    /**
     * 海域を取得します。
     * @return 海域
     */
    public StringProperty areaProperty() {
        return this.area;
    }

    /**
     * マスを取得します。
     * @return マス
     */
    public String getCell() {
        return this.cell.get();
    }

    /**
     * マスを設定します。
     * @param cell マス
     */
    public void setCell(String cell) {
        this.cell = new SimpleStringProperty(cell);
    }

    /**
     * マスを取得します。
     * @return マス
     */
    public StringProperty cellProperty() {
        return this.cell;
    }

    /**
     * ボスを取得します。
     * @return ボス
     */
    public String getBoss() {
        return this.boss.get();
    }

    /**
     * ボスを設定します。
     * @param boss ボス
     */
    public void setBoss(String boss) {
        this.boss = new SimpleStringProperty(boss);
    }

    /**
     * ボスを取得します。
     * @return ボス
     */
    public StringProperty bossProperty() {
        return this.boss;
    }

    /**
     * ランクを取得します。
     * @return ランク
     */
    public String getRank() {
        return this.rank.get();
    }

    /**
     * ランクを設定します。
     * @param rank ランク
     */
    public void setRank(String rank) {
        this.rank = new SimpleStringProperty(rank);
    }

    /**
     * ランクを取得します。
     * @return ランク
     */
    public StringProperty rankProperty() {
        return this.rank;
    }

    /**
     * 艦隊行動を取得します。
     * @return 艦隊行動
     */
    public String getIntercept() {
        return this.intercept.get();
    }

    /**
     * 艦隊行動を設定します。
     * @param intercept 艦隊行動
     */
    public void setIntercept(String intercept) {
        this.intercept = new SimpleStringProperty(intercept);
    }

    /**
     * 艦隊行動を取得します。
     * @return 艦隊行動
     */
    public StringProperty interceptProperty() {
        return this.intercept;
    }

    /**
     * 味方陣形を取得します。
     * @return 味方陣形
     */
    public String getFformation() {
        return this.fformation.get();
    }

    /**
     * 味方陣形を設定します。
     * @param fformation 味方陣形
     */
    public void setFformation(String fformation) {
        this.fformation = new SimpleStringProperty(fformation);
    }

    /**
     * 味方陣形を取得します。
     * @return 味方陣形
     */
    public StringProperty fformationProperty() {
        return this.fformation;
    }

    /**
     * 敵陣形を取得します。
     * @return 敵陣形
     */
    public String getEformation() {
        return this.eformation.get();
    }

    /**
     * 敵陣形を設定します。
     * @param eformation 敵陣形
     */
    public void setEformation(String eformation) {
        this.eformation = new SimpleStringProperty(eformation);
    }

    /**
     * 敵陣形を取得します。
     * @return 敵陣形
     */
    public StringProperty eformationProperty() {
        return this.eformation;
    }

    /**
     * 制空権を取得します。
     * @return 制空権
     */
    public String getDispseiku() {
        return this.dispseiku.get();
    }

    /**
     * 制空権を設定します。
     * @param dispseiku 制空権
     */
    public void setDispseiku(String dispseiku) {
        this.dispseiku = new SimpleStringProperty(dispseiku);
    }

    /**
     * 制空権を取得します。
     * @return 制空権
     */
    public StringProperty dispseikuProperty() {
        return this.dispseiku;
    }

    /**
     * 味方触接を取得します。
     * @return 味方触接
     */
    public String getFtouch() {
        return this.ftouch.get();
    }

    /**
     * 味方触接を設定します。
     * @param ftouch 味方触接
     */
    public void setFtouch(String ftouch) {
        this.ftouch = new SimpleStringProperty(ftouch);
    }

    /**
     * 味方触接を取得します。
     * @return 味方触接
     */
    public StringProperty ftouchProperty() {
        return this.ftouch;
    }

    /**
     * 敵触接を取得します。
     * @return 敵触接
     */
    public String getEtouch() {
        return this.etouch.get();
    }

    /**
     * 敵触接を設定します。
     * @param etouch 敵触接
     */
    public void setEtouch(String etouch) {
        this.etouch = new SimpleStringProperty(etouch);
    }

    /**
     * 敵触接を取得します。
     * @return 敵触接
     */
    public StringProperty etouchProperty() {
        return this.etouch;
    }

    /**
     * 敵艦隊を取得します。
     * @return 敵艦隊
     */
    public String getEfleet() {
        return this.efleet.get();
    }

    /**
     * 敵艦隊を設定します。
     * @param efleet 敵艦隊
     */
    public void setEfleet(String efleet) {
        this.efleet = new SimpleStringProperty(efleet);
    }

    /**
     * 敵艦隊を取得します。
     * @return 敵艦隊
     */
    public StringProperty efleetProperty() {
        return this.efleet;
    }

    /**
     * ドロップ艦種を取得します。
     * @return ドロップ艦種
     */
    public String getDropType() {
        return this.dropType.get();
    }

    /**
     * ドロップ艦種を設定します。
     * @param dropType ドロップ艦種
     */
    public void setDropType(String dropType) {
        this.dropType = new SimpleStringProperty(dropType);
    }

    /**
     * ドロップ艦種を取得します。
     * @return ドロップ艦種
     */
    public StringProperty dropTypeProperty() {
        return this.dropType;
    }

    /**
     * ドロップ艦娘を取得します。
     * @return ドロップ艦娘
     */
    public String getDropShip() {
        return this.dropShip.get();
    }

    /**
     * ドロップ艦娘を設定します。
     * @param dropShip ドロップ艦娘
     */
    public void setDropShip(String dropShip) {
        this.dropShip = new SimpleStringProperty(dropShip);
    }

    /**
     * ドロップ艦娘を取得します。
     * @return ドロップ艦娘
     */
    public StringProperty dropShipProperty() {
        return this.dropShip;
    }

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(this.date.get())
                .add(this.area.get())
                .add(this.cell.get())
                .add(this.boss.get())
                .add(this.rank.get())
                .add(this.intercept.get())
                .add(this.fformation.get())
                .add(this.eformation.get())
                .add(this.dispseiku.get())
                .add(this.ftouch.get())
                .add(this.etouch.get())
                .add(this.efleet.get())
                .add(this.dropType.get())
                .add(this.dropShip.get())
                .toString();
    }

    /**
     * 出撃統計のベースから戦闘ログの詳細行を作成します
     *
     * @param log 出撃統計のベース
     * @return 戦闘ログの詳細行
     */
    public static BattleLogDetail toBattleLogDetail(SimpleBattleLog log) {
        BattleLogDetail detail = new BattleLogDetail();
        // GMT+04:00のタイムゾーンになっているので日本時間に戻す
        String date = Logs.DATE_FORMAT.format(log.getDate().withZoneSameInstant(ZoneId.of("Asia/Tokyo")));
        detail.setDate(date);
        detail.setArea(log.getArea());
        detail.setCell(log.getCell());
        detail.setBoss(log.getBoss());
        detail.setRank(log.getRank());
        detail.setIntercept(log.getIntercept());
        detail.setFformation(log.getFformation());
        detail.setEformation(log.getEformation());
        detail.setDispseiku(log.getDispseiku());
        detail.setFtouch(log.getFtouch());
        detail.setEtouch(log.getEtouch());
        detail.setEfleet(log.getEfleet());
        detail.setDropType(log.getDropType());
        detail.setDropShip(log.getDropShip());
        return detail;
    }
}
