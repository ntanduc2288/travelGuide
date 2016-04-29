package com.travel.travelguide.Object;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by user on 4/27/16.
 */
@DatabaseTable(tableName = "testTable")
public class TestObject {
    @DatabaseField(id = true)
    String id;
    @DatabaseField()
    String name;

    public TestObject(){

    }
    public TestObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
