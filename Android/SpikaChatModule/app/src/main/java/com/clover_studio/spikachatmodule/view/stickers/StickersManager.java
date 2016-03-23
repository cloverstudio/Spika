package com.clover_studio.spikachatmodule.view.stickers;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.adapters.StickersPagerAdapter;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.fragments.CategoryStickersFragment;
import com.clover_studio.spikachatmodule.models.GetStickersData;
import com.clover_studio.spikachatmodule.models.StickerCategory;
import com.clover_studio.spikachatmodule.utils.AnimUtils;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.view.circularview.animation.SupportAnimator;
import com.clover_studio.spikachatmodule.view.circularview.animation.ViewAnimationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu_ivo on 24.07.15..
 */
public class StickersManager {

    private RelativeLayout rlStickersMain;
    private LinearLayout llForStickersCategory;
    private ViewPager vpStickers;
    private HorizontalScrollView hsvStickers;
    private SupportAnimator stickersAnimator;

    private OnStickersManageListener listener;

    public void setStickersLayout(Activity activity, int stickersLayoutId, OnStickersManageListener listener){

        rlStickersMain = (RelativeLayout) activity.findViewById(stickersLayoutId);
        llForStickersCategory = (LinearLayout) rlStickersMain.findViewById(R.id.llStickersCategory);
        vpStickers = (ViewPager) rlStickersMain.findViewById(R.id.stickersViewPager);
        hsvStickers = (HorizontalScrollView) rlStickersMain.findViewById(R.id.hvStickersCategory);
        this.listener = listener;

    }

    public void openMenu(ImageButton btnStickers){

        ((View)rlStickersMain.getParent().getParent()).setVisibility(View.VISIBLE);

        // get the center for the clipping circle
        int cx = btnStickers.getRight();
        int cy = rlStickersMain.getBottom();

        // get the final radius for the clipping circle
        int finalRadius = Math.max(rlStickersMain.getWidth(), rlStickersMain.getHeight());

        stickersAnimator = ViewAnimationUtils.createCircularReveal(rlStickersMain, cx, cy, 0, finalRadius + 300);
        stickersAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        stickersAnimator.setDuration(Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        stickersAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                listener.onStickersOpened();
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        stickersAnimator.start();

        handleButtonsOnOpen();

    }

    protected void handleButtonsOnOpen(){

        int start = 150;
        int offset = 50;

//        singleButtonAnimationOn(audio, start + 0 * offset);
//        singleButtonAnimationOn(contact, start + 1 * offset);
//        singleButtonAnimationOn(location, start + 2 * offset);
//        singleButtonAnimationOn(video, start + 3 * offset);
//        singleButtonAnimationOn(gallery, start + 4 * offset);
//        singleButtonAnimationOn(file, start + 5 * offset);
//        singleButtonAnimationOn(camera, start + 6 * offset);

    }

    protected void singleButtonAnimationOn(final View view, int offset){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                AnimUtils.scale(view, 0.6f, 1, Const.AnimationDuration.MENU_BUTTON_ANIMATION_DURATION, null);
            }
        }, offset);
    }

    public void closeMenu(){

        if(stickersAnimator == null) {
            return;
        }

        stickersAnimator = stickersAnimator.reverse();
        if(stickersAnimator != null){
            stickersAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {}

                @Override
                public void onAnimationEnd() {
                    listener.onStickersClosed();
                    ((View)rlStickersMain.getParent().getParent()).setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel() {}

                @Override
                public void onAnimationRepeat() {}
            });

            stickersAnimator.start();
        }

        handleButtonsOnClose();

    }

    protected void handleButtonsOnClose(){

//        singleButtonAnimationOff(audio, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
//        singleButtonAnimationOff(location, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
//        singleButtonAnimationOff(video, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
//        singleButtonAnimationOff(gallery, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
//        singleButtonAnimationOff(contact, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
//        singleButtonAnimationOff(camera, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
//        singleButtonAnimationOff(file, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);

    }

    protected void singleButtonAnimationOff(final View view, int offset){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        }, offset);
    }

    public void setStickers(GetStickersData data, FragmentManager fm){
        llForStickersCategory.getChildAt(0).setSelected(true);
        llForStickersCategory.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOfCategory(-1);
            }
        });
        int position = 0;

        List<Fragment> stickersFragmentList = new ArrayList<>();
        StickerCategory recentCategory = SingletonLikeApp.getInstance().getSharedPreferences(llForStickersCategory.getContext()).getStickersLikeObject();
        stickersFragmentList.add(CategoryStickersFragment.newInstance(recentCategory));

        for(final StickerCategory category : data.data.stickers){
            ImageView ivStickerCategory = new ImageView(llForStickersCategory.getContext());
            llForStickersCategory.addView(ivStickerCategory);
            float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, llForStickersCategory.getContext().getResources().getDisplayMetrics());
            float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, llForStickersCategory.getContext().getResources().getDisplayMetrics());
            ivStickerCategory.getLayoutParams().height = (int) size;
            ivStickerCategory.getLayoutParams().width = (int) size;
            ivStickerCategory.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
            ivStickerCategory.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivStickerCategory.setBackgroundResource(R.drawable.selector_stickers_category);
            Picasso.with(ivStickerCategory.getContext()).load(category.mainPic).into(ivStickerCategory);
            ivStickerCategory.setTag(position);
            position++;
            ivStickerCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickOfCategory((int) v.getTag());
                }
            });

            stickersFragmentList.add(CategoryStickersFragment.newInstance(category));
        }

        StickersPagerAdapter pagerAdapter = new StickersPagerAdapter(fm, llForStickersCategory.getContext(), stickersFragmentList);
        vpStickers.setAdapter(pagerAdapter);
        vpStickers.addOnPageChangeListener(onPageChanged);

        if(recentCategory == null && vpStickers.getChildCount() > 0){
            vpStickers.setCurrentItem(1);
        }
    }

    private ViewPager.OnPageChangeListener onPageChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            selectStickersPack(position - 1);
            float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, llForStickersCategory.getContext().getResources().getDisplayMetrics());
            if(position >= 4){
                hsvStickers.smoothScrollTo((int) (size * position), 0);
            }else{
                hsvStickers.smoothScrollTo(0, 0);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    public void clickOfCategory(int position){
        vpStickers.setCurrentItem(position + 1);
        selectStickersPack(position);
    }

    public void selectStickersPack(int position){
        for(int i = 0; i < llForStickersCategory.getChildCount(); i++){
            View view = llForStickersCategory.getChildAt(i);
            view.setSelected(false);
        }
        llForStickersCategory.getChildAt(position + 1).setSelected(true);
    }

    public void refreshRecent(){
        StickerCategory recentCategory = SingletonLikeApp.getInstance().getSharedPreferences(llForStickersCategory.getContext()).getStickersLikeObject();
        ((CategoryStickersFragment) ((StickersPagerAdapter)vpStickers.getAdapter()).getItem(0)).refreshData(recentCategory);
    }
}
