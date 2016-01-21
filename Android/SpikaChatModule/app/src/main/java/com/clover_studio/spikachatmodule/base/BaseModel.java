package com.clover_studio.spikachatmodule.base;

import com.clover_studio.spikachatmodule.models.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ubuntu_ivo on 21.07.15..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel {

    public int code;

    public BaseModel() {
    }

}
