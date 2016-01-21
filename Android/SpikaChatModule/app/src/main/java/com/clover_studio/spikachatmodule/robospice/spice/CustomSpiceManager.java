package com.clover_studio.spikachatmodule.robospice.spice;

import android.util.Log;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

import roboguice.util.temp.Ln;

public class CustomSpiceManager extends SpiceManager {

    public CustomSpiceManager(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
        Ln.getConfig().setLoggingLevel(Log.ERROR);
    }

}
