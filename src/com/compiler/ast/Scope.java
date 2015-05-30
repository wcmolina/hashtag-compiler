package com.compiler.ast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
public class Scope extends HashMap<String, Data> {
    //hierarchy of scopes
    //each scope acts like a table internally, hence the 'extends HashMap'
    private Scope previous = null;
    private String label = "";
    private String ID = "";
    private ArrayList<Scope> sub_scopes;

    public Scope(Scope prev) {
        this.previous = prev;
        this.sub_scopes = new ArrayList<Scope>();
    }

    public Scope getPrevious() {
        return previous;
    }

    public String getID() {
        if (this.ID.isEmpty()) {
            this.ID = labelFromPrevious(this, this.label);
        }
        return this.ID;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }


    public static boolean isInPrevious(Scope scope, String key) {
        //if previous scope contains the key
        if (scope.containsKey(key)) {
            return true;
        } else {
            if (scope.getPrevious() == null) {
                return false;
            } else {
                return isInPrevious(scope.getPrevious(), key);
            }
        }
    }

    public static Data findInPrevious(Scope scope, String key) {
        //if previous scope contains the key
        if (scope.containsKey(key)) {
            return scope.get(key);
        } else {
            if (scope.getPrevious() == null) {
                return null;
            } else {
                return findInPrevious(scope.getPrevious(), key);
            }
        }
    }

    public void addScope(Scope sc) {
        if (this.sub_scopes == null) { //from some reason
            this.sub_scopes = new ArrayList<Scope>();
        }
        this.sub_scopes.add(sc);
    }

    public static String labelFromPrevious(Scope scope, String label) {
        if (scope.getPrevious() == null) {
            return scope.getLabel();
        } else {
            return labelFromPrevious(scope.getPrevious(), scope.getPrevious().getLabel()) + "." + label;
        }
    }

    @Override
    public String toString() {
        return getID();
    }
}
