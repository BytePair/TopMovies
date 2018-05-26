/**
 * Class generated on
 * http://www.jsonschema2pojo.org/
 */

package com.bytepair.topmovies.models;

import com.google.gson.annotations.SerializedName;

public class ProductionCompany {

    @SerializedName("id")
    private int id;
    @SerializedName("logo_path")
    private Object logoPath;
    @SerializedName("name")
    private String name;
    @SerializedName("origin_country")
    private String originCountry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(Object logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

}
