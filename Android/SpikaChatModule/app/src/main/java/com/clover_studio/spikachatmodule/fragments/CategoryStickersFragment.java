package com.clover_studio.spikachatmodule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clover_studio.spikachatmodule.ChatActivity;
import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.adapters.RecyclerStickersAdapter;
import com.clover_studio.spikachatmodule.models.Sticker;
import com.clover_studio.spikachatmodule.models.StickerCategory;
import com.clover_studio.spikachatmodule.utils.Const;

import java.util.ArrayList;

/**
 * Created by ubuntu_ivo on 23.03.16..
 */
public class CategoryStickersFragment extends Fragment{

    private StickerCategory category;
    private RecyclerView rvStickers;
    private RecyclerStickersAdapter adapter;

    public static CategoryStickersFragment newInstance(StickerCategory category) {
        CategoryStickersFragment fragment = new CategoryStickersFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.Extras.STICKERS, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CategoryStickersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = (StickerCategory) getArguments().getSerializable(Const.Extras.STICKERS);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category_stickers, container, false);

        //20 is sum of left and right margin of layout stickers menu
        int width = (int) (getResources().getDisplayMetrics().widthPixels - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));

        //80 is sum of item width and left and right padding
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        int numColumns = width / size;

        rvStickers = (RecyclerView) rootView.findViewById(R.id.rvStickers);
        rvStickers.setLayoutManager(new GridLayoutManager(getActivity(), numColumns));
        adapter = new RecyclerStickersAdapter(category.list);
        rvStickers.setAdapter(adapter);

        adapter.setListener(new RecyclerStickersAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(Sticker sticker) {
                if(getActivity() instanceof ChatActivity){
                    ((ChatActivity)getActivity()).selectStickers(sticker);
                }
            }
        });

        return rootView;

    }

    public void refreshData(StickerCategory category){
        this.category.list.clear();
        this.category.list.addAll(category.list);
        if(rvStickers != null && adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

}
