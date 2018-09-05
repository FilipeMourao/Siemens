package de.tum.ipraktikum.utils;

public class Triple<A,B,C> {

    private A a;
    private B b;
    private C c;

    public Triple(A a, B b, C c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getFirst(){
        return a;
    }

    public B getSecond(){
        return b;
    }

    public C getThird() {
        return c;
    }

}
