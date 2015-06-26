package com.compiler.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
public class SymbolTable extends HashMap<String, Data> {
    //hierarchy of scopes (each SymbolTable is a scope)

    private SymbolTable previous = null;
    private String label = "";
    private String ID = "";
    private ArrayList<SymbolTable> children;
    private static HashMap<String, Data> singleTable = new HashMap<>();

    public SymbolTable(SymbolTable prev) {
        this.previous = prev;
        this.children = new ArrayList<>();
    }

    public SymbolTable getPrevious() {
        return previous;
    }

    public ArrayList<SymbolTable> getChildren() {
        return this.children;
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


    public static boolean isInPrevious(SymbolTable table, String key) {
        //if previous scope contains the key
        if (table.containsKey(key)) {
            return true;
        } else {
            if (table.getPrevious() == null) {
                return false;
            } else {
                return isInPrevious(table.getPrevious(), key);
            }
        }
    }

    public static Data findInPrevious(SymbolTable table, String key) {
        //if previous scope contains the key
        if (table.containsKey(key)) {
            return table.get(key);
        } else {
            if (table.getPrevious() == null) {
                return null;
            } else {
                return findInPrevious(table.getPrevious(), key);
            }
        }
    }

    public void addTable(SymbolTable sc) {
        if (this.children == null) { //for some weird reason...
            this.children = new ArrayList<>();
        }
        this.children.add(sc);
    }

    public static String labelFromPrevious(SymbolTable table, String label) {
        if (table.getPrevious() == null) {
            return table.getLabel();
        } else {
            return labelFromPrevious(table.getPrevious(), table.getPrevious().getLabel()) + "." + label;
        }
    }

    public static HashMap<String, Data> flatten(SymbolTable rootTable) {
        for (SymbolTable subTable : rootTable.getChildren()) {
            for (Map.Entry<String, Data> entry : subTable.entrySet()) {
                singleTable.put(entry.getKey(), entry.getValue());
            }
            flatten(subTable);
        }
        return singleTable;
    }

    @Override
    public String toString() {
        return getID();
    }
}
