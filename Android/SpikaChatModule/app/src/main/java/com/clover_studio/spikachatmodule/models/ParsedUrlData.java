package com.clover_studio.spikachatmodule.models;

import com.clover_studio.spikachatmodule.base.BaseModel;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class ParsedUrlData extends BaseModel {

    public String title;
    public String desc;
    public String host;
    public String url;
    public String imageUrl;
    public String siteName;

    @Override
    public String toString() {
        return "ParsedUrlData{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", host='" + host + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", siteName='" + siteName + '\'' +
                '}';
    }
}
