package com.clover_studio.spikachatmodule.models;

import android.content.res.Resources;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.ParseUrlLinkMetadata;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class ParsedUrlData extends BaseModel {

    public String title;
    public String desc;
    public String host;
    public String url;
    public String imageUrl;

    @Override
    public String toString() {
        return "ParsedUrlData{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", host='" + host + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
