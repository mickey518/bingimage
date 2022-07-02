package com.mickey.bingimage.dto;

/**
 * #Description
 *
 * @author wangmeng
 * @date 2020-06-11
 */
public class Tooltips {
    private String loading;
    private String previous;
    private String next;
    private String walle;
    private String walls;

    public String getLoading() {
        return loading;
    }

    public String getWalls() {
        return walls;
    }

    public void setWalls(String walls) {
        this.walls = walls;
    }

    public String getWalle() {
        return walle;
    }

    public void setWalle(String walle) {
        this.walle = walle;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setLoading(String loading) {
        this.loading = loading;
    }
}
