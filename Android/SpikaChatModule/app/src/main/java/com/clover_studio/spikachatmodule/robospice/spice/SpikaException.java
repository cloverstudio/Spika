package com.clover_studio.spikachatmodule.robospice.spice;

import com.octo.android.robospice.persistence.exception.SpiceException;

import org.json.JSONArray;

public class SpikaException extends SpiceException {

    private JSONArray errorArray;

    public SpikaException(String detailMessage) {
        super(detailMessage);
    }

    public JSONArray getErrorArray() {
        return errorArray;
    }

    public void setErrorArray(JSONArray errorArray) {
        this.errorArray = errorArray;
    }
}
