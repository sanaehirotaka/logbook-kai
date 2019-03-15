package logbook.internal;

/**
 * Tuple
 *
 */
public interface Tuple {

    static <A, B> Unit<A> of(A _1) {
        return new Unit<A>(_1);
    }

    static <A, B> Pair<A, B> of(A _1, B _2) {
        return new Pair<A, B>(_1, _2);
    }

    static <A, B, C> Triplet<A, B, C> of(A _1, B _2, C _3) {
        return new Triplet<A, B, C>(_1, _2, _3);
    }

    public static class Unit<A> implements Tuple {

        private final A _1;

        protected Unit(A _1) {
            this._1 = _1;
        }

        public A get1() {
            return _1;
        }

        public Unit<A> asUnit() {
            return this;
        }

        public <B> Pair<A, B> asPair() {
            return new Pair<A, B>(this._1, null);
        }

        public <B, C> Triplet<A, B, C> asTriplet() {
            return new Triplet<A, B, C>(this._1, null, null);
        }
    }

    public static class Pair<A, B> implements Tuple {

        private final A _1;
        private final B _2;

        protected Pair(A _1, B _2) {
            this._1 = _1;
            this._2 = _2;
        }

        public A get1() {
            return this._1;
        }

        public B get2() {
            return this._2;
        }

        public A getKey() {
            return this._1;
        }

        public B getValue() {
            return this._2;
        }

        public Unit<A> asUnit() {
            return new Unit<A>(this._1);
        }

        public Pair<A, B> asPair() {
            return this;
        }

        public <C> Triplet<A, B, C> asTriplet() {
            return new Triplet<A, B, C>(this._1, this._2, null);
        }
    }

    public static class Triplet<A, B, C> implements Tuple {

        private final A _1;
        private final B _2;
        private final C _3;

        protected Triplet(A _1, B _2, C _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        public A get1() {
            return this._1;
        }

        public B get2() {
            return this._2;
        }

        public C get3() {
            return this._3;
        }

        public Unit<A> asUnit() {
            return new Unit<A>(this._1);
        }

        public Pair<A, B> asPair() {
            return new Pair<A, B>(this._1, this._2);
        }

        public Triplet<A, B, C> asTriplet() {
            return this;
        }
    }
}
