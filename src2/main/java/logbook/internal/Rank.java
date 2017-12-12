package logbook.internal;

/**
 * ランク
 *
 */
public enum Rank {

    S完全勝利("S完全勝利", 1.2d),
    S勝利("S勝利", 1.2d),
    A勝利("A勝利", 1.0d),
    B戦術的勝利("B戦術的勝利", 1.0d),
    C戦術的敗北("C戦術的敗北", 0.8d),
    D敗北("D敗北", 0.7d);

    /** 名前 */
    private String name;

    /** 経験値倍率 */
    private double ratio;

    Rank(String name, double ratio) {
        this.name = name;
        this.ratio = ratio;
    }

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 経験値倍率を取得します。
     * @return 経験値倍率
     */
    public double getRatio() {
        return this.ratio;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
