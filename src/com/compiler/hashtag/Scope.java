package com.compiler.hashtag;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
public class Scope extends HashMap<String, Data> {
    //hierarchy of scopes
    //each scope acts like a table internally, hence the 'extends HashMap'
    private Scope previous = null;
    private ArrayList<Scope> sub_scopes;

    public Scope(Scope prev, Data dat) {
        this.previous = prev;
        this.put(dat.getLexeme(), dat);
        sub_scopes = new ArrayList();
    }

    public Scope getPrevious() {
        return previous;
    }


    public static boolean is_in_previous(Scope scope, String key) {
        //if previous scope contains the key
        if (scope.containsKey(key)) {
            return true;
        } else {
            if (scope.getPrevious() == null) {
                return false;
            } else {
                return is_in_previous(scope.getPrevious(), key);
            }
        }
    }
}
