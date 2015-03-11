package me.nithanim.longbuffer;

import java.util.List;

public class Disposer {
    public static void dispose(Disposable o) {
        o.dispose();
    }
    
    public static void dispose(List l) {
        for(Object o : l) {
            dispose(o);
        }
    }
    
    public static void dispose(Object o) {
        if(o instanceof Disposable) {
            dispose((Disposable)o);
        }
    }
    
    private Disposer() {
    }
}
