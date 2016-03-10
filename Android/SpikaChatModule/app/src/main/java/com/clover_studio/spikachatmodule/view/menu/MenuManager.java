package com.clover_studio.spikachatmodule.view.menu;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.utils.AnimUtils;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.view.circularview.animation.SupportAnimator;
import com.clover_studio.spikachatmodule.view.circularview.animation.ViewAnimationUtils;

/**
 * Created by ubuntu_ivo on 24.07.15..
 */
public class MenuManager {

    private RelativeLayout rlMenuMain;
    private SupportAnimator menuAnimator;
    private LinearLayout location;
    private LinearLayout camera;
    private LinearLayout gallery;
    private LinearLayout audio;
    private LinearLayout video;
    private LinearLayout file;
    private LinearLayout contact;

    private OnMenuManageListener listener;
    private OnMenuButtonsListener buttonsListener;

    public void setMenuLayout(Activity activity, int menuLayoutId, OnMenuManageListener listener, final OnMenuButtonsListener buttonsListener){

        rlMenuMain = (RelativeLayout) activity.findViewById(menuLayoutId);
        location = (LinearLayout) activity.findViewById(R.id.location);
        camera = (LinearLayout) activity.findViewById(R.id.camera);
        gallery = (LinearLayout) activity.findViewById(R.id.gallery);
        audio = (LinearLayout) activity.findViewById(R.id.audio);
        video = (LinearLayout) activity.findViewById(R.id.video);
        contact = (LinearLayout) activity.findViewById(R.id.contact);
        file = (LinearLayout) activity.findViewById(R.id.file);

        this.listener = listener;
        this.buttonsListener = buttonsListener;

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onLocationClicked();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onCameraClicked();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onGalleryClicked();
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onAudioClicked();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onVideoClicked();
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onFileClicked();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onContactClicked();
            }
        });


    }

    public void openMenu(ImageButton btnSend){

        ((View)rlMenuMain.getParent().getParent()).setVisibility(View.VISIBLE);

        // get the center for the clipping circle
        int cx = btnSend.getLeft();
        int cy = rlMenuMain.getBottom();

        // get the final radius for the clipping circle
        int finalRadius = Math.max(rlMenuMain.getWidth(), rlMenuMain.getHeight());

        menuAnimator = ViewAnimationUtils.createCircularReveal(rlMenuMain, cx, cy, 0, finalRadius + 300);
        menuAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        menuAnimator.setDuration(Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        menuAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                listener.onMenuOpened();
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        menuAnimator.start();

        handleButtonsOnOpen();

    }

    protected void handleButtonsOnOpen(){

        int start = 150;
        int offset = 50;

        singleButtonAnimationOn(audio, start + 0 * offset);
        singleButtonAnimationOn(contact, start + 1 * offset);
        singleButtonAnimationOn(location, start + 2 * offset);
        singleButtonAnimationOn(video, start + 3 * offset);
        singleButtonAnimationOn(gallery, start + 4 * offset);
        singleButtonAnimationOn(file, start + 5 * offset);
        singleButtonAnimationOn(camera, start + 6 * offset);

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

        if(menuAnimator == null) {
            return;
        }

        menuAnimator = menuAnimator.reverse();
        if(menuAnimator != null){
            menuAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {}

                @Override
                public void onAnimationEnd() {
                    listener.onMenuClosed();
                    ((View)rlMenuMain.getParent().getParent()).setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel() {}

                @Override
                public void onAnimationRepeat() {}
            });

            menuAnimator.start();
        }

        handleButtonsOnClose();

    }

    protected void handleButtonsOnClose(){

        singleButtonAnimationOff(audio, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        singleButtonAnimationOff(location, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        singleButtonAnimationOff(video, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        singleButtonAnimationOff(gallery, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        singleButtonAnimationOff(contact, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        singleButtonAnimationOff(camera, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
        singleButtonAnimationOff(file, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);

    }

    protected void singleButtonAnimationOff(final View view, int offset){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        }, offset);
    }

}
