package com.clover_studio.spikachatmodule.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu_ivo on 23.03.16..
 */
public class StickersPagerAdapter extends FragmentStatePagerAdapter{

    private Context ctx;
    private List<Fragment> data = new ArrayList<>();

    public StickersPagerAdapter(FragmentManager fm, Context context, List<Fragment> newData) {
        super(fm);

        this.ctx = context;
        this.data = newData;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

}
