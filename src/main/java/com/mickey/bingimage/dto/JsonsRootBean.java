package com.mickey.bingimage.dto;

import java.util.List;

/**
 * #Description
 *
 * @author wangmeng
 * @date 2020-06-11
 */
public class JsonsRootBean {
    private List<Image> images;
    private Tooltips tooltips;

    public List<Image> getImages() {
        return images;
    }

    public Tooltips getTooltips() {
        return tooltips;
    }

    public void setTooltips(Tooltips tooltips) {
        this.tooltips = tooltips;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
