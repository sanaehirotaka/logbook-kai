package logbook.internal;

public enum Operator {
    /** greater than or equal to */
    GE("以上"),
    /** less than or equal to */
    LE("以下"),
    /** equal */
    EQ("等しい"),
    /** not equal */
    NE("等しくない"),
    /** greater than */
    GT("より大きい"),
    /** less than */
    LT("より小さい");

    private String name;

    private Operator(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
