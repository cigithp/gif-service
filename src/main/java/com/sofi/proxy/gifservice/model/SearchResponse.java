package com.sofi.proxy.gifservice.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    List<GifData> data = new ArrayList<>();
    boolean last_page;

    public boolean isLast_page() {
        return last_page;
    }

    public void setLast_page(boolean last_page) {
        this.last_page = last_page;
    }

    public List<GifData> getData() {
        return data;
    }

    public void setData(List<GifData> data) {
        this.data = data;
    }

    public class GifData {
        String gif_id;
        String url;

        public String getGif_id() {
            return gif_id;
        }

        public void setGif_id(String gif_id) {
            this.gif_id = gif_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
