package logbook.internal;

public enum Operator {
    /** greater than or equal to */
    GE("以上") {
        @Override
        public <T extends Comparable<T>> boolean compare(T a, T b) {
            return a.compareTo(b) >= 0;
        }
    },
    /** less than or equal to */
    LE("以下") {
        @Override
        public <T extends Comparable<T>> boolean compare(T a, T b) {
            return a.compareTo(b) <= 0;
        }
    },
    /** equal */
    EQ("等しい") {
        @Override
        public <T extends Comparable<T>> boolean compare(T a, T b) {
            return a == b || a.compareTo(b) == 0;
        }
    },
    /** not equal */
    NE("等しくない") {
        @Override
        public <T extends Comparable<T>> boolean compare(T a, T b) {
            return a != b && a.compareTo(b) != 0;
        }
    },
    /** greater than */
    GT("より大きい") {
        @Override
        public <T extends Comparable<T>> boolean compare(T a, T b) {
            return a.compareTo(b) > 0;
        }
    },
    /** less than */
    LT("より小さい") {
        @Override
        public <T extends Comparable<T>> boolean compare(T a, T b) {
            return a.compareTo(b) < 0;
        }
    };

    private String name;

    private Operator(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    abstract public <T extends Comparable<T>> boolean compare(T a, T b);
}
