package com.travel.travelguide.Object;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by user on 5/9/16.
 */
@Root(name = "languages")
public class Languages {
    @ElementList(entry = "language", required = false, inline = true)
    ArrayList<String> list;

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        if(list.isEmpty()){
            throw new IllegalArgumentException("Empty collection");
        }
        this.list = list;
    }
}
