package com.example.wyz.everynews1.mvp.entity;

import java.util.List;

/**
 * Created by Wyz on 2016/11/19.
 */
public class GirlData {
    private boolean isError;
    private List<PhotoGirl> results;

    public void setError(boolean error) {
        isError = error;
    }

    public void setResults(List<PhotoGirl> results) {
        this.results = results;
    }

    public List<PhotoGirl> getResults() {
        return results;
    }
    public boolean isError() {
        return isError;
    }
}
