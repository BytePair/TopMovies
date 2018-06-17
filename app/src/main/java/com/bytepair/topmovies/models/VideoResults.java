/**
 * Class generated on
 * http://www.jsonschema2pojo.org/
 */

package com.bytepair.topmovies.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VideoResults {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Video> results = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

}
