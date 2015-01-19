package org.lastresponders.uberassignment.data.model;

/**
 * Created by sjan on 1/15/2015.
 */
public class ImageSearchResult {

    private String tbUrl;
    private String url;
    private int tbHeight;
    private int tbWidth;
    private int height;
    private int width;


    public String getUrl() {
        return url;
    }

    public int getTbHeight() {
        return tbHeight;
    }

    public int getTbWidth() {
        return tbWidth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    public String getTbUrl() {
        return tbUrl;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTbWidth(int tbWidth) {
        this.tbWidth = tbWidth;
    }

    public void setTbHeight(int tbHeight) {
        this.tbHeight = tbHeight;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    @Override
    public String toString() {
        return "ImageSearchResult{" +
                "tbUrl='" + tbUrl + '\'' +
                ", url='" + url + '\'' +
                ", tbHeight=" + tbHeight +
                ", tbWidth=" + tbWidth +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
