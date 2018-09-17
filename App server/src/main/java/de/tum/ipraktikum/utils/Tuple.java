package de.tum.ipraktikum.utils;

/**
 * Generic Tuple class, consisting of two elements (A,B)
 * @param <A> Type for first element
 * @param <B> Type for second element
 */
public class Tuple<A, B> {

    public final A a;
    public final B b;
 

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * gets first element
     * @return first element
     */
    public A getFirst() {
        return a;
    }

    /**
     * gets second element
     * @return second element
     */
    public B getSecond() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        if (!a.equals(tuple.a)) return false;
        return b.equals(tuple.b);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }
}
