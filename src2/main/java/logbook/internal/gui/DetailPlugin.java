package logbook.internal.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.plugin.JarBasedPlugin;

/**
 * プラグイン詳細
 *
 */
public class DetailPlugin {

    /** 名称 */
    private StringProperty name;

    /** 作者 */
    private StringProperty vendor;

    /** バージョン */
    private StringProperty version;

    /** ライセンス */
    private StringProperty license;

    /** 場所 */
    private StringProperty location;

    /**
     * 名称を取得します。
     * @return 名称
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * 名称を設定します。
     * @param name 名称
     */
    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * 作者を取得します。
     * @return 作者
     */
    public StringProperty vendorProperty() {
        return this.vendor;
    }

    /**
     * 作者を設定します。
     * @param vendor 作者
     */
    public void setVendor(String vendor) {
        this.vendor = new SimpleStringProperty(vendor);
    }

    /**
     * バージョンを取得します。
     * @return バージョン
     */
    public StringProperty versionProperty() {
        return this.version;
    }

    /**
     * バージョンを設定します。
     * @param version バージョン
     */
    public void setVersion(String version) {
        this.version = new SimpleStringProperty(version);
    }

    /**
     * ライセンスを取得します。
     * @return ライセンス
     */
    public StringProperty licenseProperty() {
        return this.license;
    }

    /**
     * ライセンスを設定します。
     * @param license ライセンス
     */
    public void setLicense(String license) {
        this.license = new SimpleStringProperty(license);
    }

    /**
     * 場所を取得します。
     * @return 場所
     */
    public StringProperty locationProperty() {
        return this.location;
    }

    /**
     * 場所を設定します。
     * @param location 場所
     */
    public void setLocation(String location) {
        this.location = new SimpleStringProperty(location);
    }

    /**
     * Jarファイルベースのプラグインからプラグイン詳細を生成します
     *
     * @param plugin Jarファイルベースのプラグイン
     * @return プラグイン詳細
     */
    public static DetailPlugin toDetailPlugin (JarBasedPlugin plugin) {
        DetailPlugin detail = new DetailPlugin();
        detail.setName(plugin.getName());
        detail.setVendor(plugin.getVendor());
        detail.setVersion(plugin.getVersion());
        detail.setLicense(plugin.getLicense());
        detail.setLocation(plugin.getURL().toString());
        return detail;
    }
}
