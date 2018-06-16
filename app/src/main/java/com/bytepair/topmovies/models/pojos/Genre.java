/**
 * Class generated on
 * http://www.jsonschema2pojo.org/
 */

package com.bytepair.topmovies.models.pojos;

import com.google.gson.annotations.SerializedName;

public class Genre {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
