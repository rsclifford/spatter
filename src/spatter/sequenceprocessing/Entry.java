/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatter.sequenceprocessing;

/**
 *
 * @author faroq
 */

/*
 * Copyright (C) 2011-2 Faroq AL-Tam and Hamid Reza Shahbazkia, 2015 Sean Clifford
 * This program is part of spatter.
 * 
 * If you want to improve the software, this is free software for non-commercial purposes.
 */

public class Entry {

    String name;
    String value;
    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Entry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Entry{" + "name=" + name + ", value=" + value + ", type=" + '}';
    }

    @Override
    public boolean equals(Object o) {
        return (value.equals(((Entry) o).value));
    }

}
