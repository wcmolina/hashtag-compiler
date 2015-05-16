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
            this.ID = label_from_previous(this, this.label);
        }
        return this.ID;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
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

    public static Data find_in_previous(Scope scope, String key) {
        //if previous scope contains the key
        if (scope.containsKey(key)) {
            return scope.get(key);
        } else {
            if (scope.getPrevious() == null) {
                return null;
            } else {
                return find_in_previous(scope.getPrevious(), key);
            }
        }
    }

    public void add_scope(Scope sc) {
        if (this.sub_scopes == null) { //from some reason
            this.sub_scopes = new ArrayList<Scope>();
        }
        this.sub_scopes.add(sc);
    }

    private String label_from_previous(Scope scope, String label) {
        if (scope.getPrevious() == null) {
            return scope.getLabel();
        } else {
            return label_from_previous(scope.getPrevious(), scope.getPrevious().getLabel()) + "." + label;
        }
    }
}
