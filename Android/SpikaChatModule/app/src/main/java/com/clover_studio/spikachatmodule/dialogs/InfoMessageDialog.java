package com.clover_studio.spikachatmodule.dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.SeenByModel;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.AnimUtils;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.clover_studio.spikachatmodule.view.CustomTextView;

import org.w3c.dom.Text;

public class InfoMessageDialog extends Dialog {

    Message message;
    int width;
    RelativeLayout parentLayout;
    OnInfoListener listener;
    User activeUser;

    //options
    private boolean showCopy;
    private boolean showDelete;
    private boolean showShare;

    public static InfoMessageDialog startDialog(Context context, Message message, User activeUser, OnInfoListener listener){
        InfoMessageDialog dialog = new InfoMessageDialog(context, message, true, true, false, listener, activeUser);
        return dialog;
    }

    public static InfoMessageDialog startDialogWithOptions(Context context, Message message, User activeUser, boolean showCopy, boolean showDelete, boolean showShare, OnInfoListener listener){
        InfoMessageDialog dialog = new InfoMessageDialog(context, message, showCopy, showDelete, showShare, listener, activeUser);
        return dialog;
    }

    public InfoMessageDialog(Context context, Message message, boolean showCopy, boolean showDelete, boolean showShare, OnInfoListener listener, User activeUser) {
        super(context, R.style.Theme_Dialog_no_dim);

        setOwnerActivity((Activity) context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        width = context.getResources().getDisplayMetrics().widthPixels;
        this.message = message;
        this.listener = listener;
        this.activeUser = activeUser;

        //options
        this.showCopy = showCopy;
        this.showDelete = showDelete;
        this.showShare = showShare;

        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_message);

        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        LinearLayout llInsideScrollView = (LinearLayout) findViewById(R.id.llInsideScrollViewInMessageOption);

        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics());

        //add copy text
        if(showCopy){
            CustomTextView copyTV = new CustomTextView(getContext());
            copyTV.setText(getContext().getString(R.string.copy));
            copyTV.setTextSize(18);
            copyTV.setTextColor(ContextCompat.getColor(getContext(), R.color.devil_gray_color));
            copyTV.setPadding(padding, padding, padding, padding);
            copyTV.setBackgroundResource(R.drawable.selector_trans_to_light_light_gray);
            llInsideScrollView.addView(copyTV);

            View viewBelowCopy = new View(getContext());
            viewBelowCopy.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_light_light_color));
            llInsideScrollView.addView(viewBelowCopy);
            viewBelowCopy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewBelowCopy.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics());
            ((LinearLayout.LayoutParams)viewBelowCopy.getLayoutParams()).leftMargin = padding;
            ((LinearLayout.LayoutParams)viewBelowCopy.getLayoutParams()).rightMargin = padding;

            copyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        dismissWithCopy();
                    }
                }
            });
        }

        //add delete text
        if(showDelete){
            CustomTextView deleteTV = new CustomTextView(getContext());
            deleteTV.setText(getContext().getString(R.string.delete));
            deleteTV.setTextSize(18);
            deleteTV.setTextColor(ContextCompat.getColor(getContext(), R.color.devil_gray_color));
            padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics());
            deleteTV.setPadding(padding, padding, padding, padding);
            deleteTV.setBackgroundResource(R.drawable.selector_trans_to_light_light_gray);
            llInsideScrollView.addView(deleteTV);

            View viewBelowDelete = new View(getContext());
            viewBelowDelete.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_light_light_color));
            llInsideScrollView.addView(viewBelowDelete);
            viewBelowDelete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewBelowDelete.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics());
            ((LinearLayout.LayoutParams)viewBelowDelete.getLayoutParams()).leftMargin = padding;
            ((LinearLayout.LayoutParams)viewBelowDelete.getLayoutParams()).rightMargin = padding;


            deleteTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        dismissWithDelete();
                    }
                }
            });
        }

        //add share text
        if(showShare){
            CustomTextView shareTv = new CustomTextView(getContext());
            shareTv.setText(getContext().getString(R.string.share));
            shareTv.setTextSize(18);
            shareTv.setTextColor(ContextCompat.getColor(getContext(), R.color.devil_gray_color));
            padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics());
            shareTv.setPadding(padding, padding, padding, padding);
            shareTv.setBackgroundResource(R.drawable.selector_trans_to_light_light_gray);
            llInsideScrollView.addView(shareTv);

            View viewBelowShare = new View(getContext());
            viewBelowShare.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_light_light_color));
            llInsideScrollView.addView(viewBelowShare);
            viewBelowShare.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewBelowShare.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics());
            ((LinearLayout.LayoutParams)viewBelowShare.getLayoutParams()).leftMargin = padding;
            ((LinearLayout.LayoutParams)viewBelowShare.getLayoutParams()).rightMargin = padding;


            shareTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        onShare();
                    }
                }
            });
        }

        CustomTextView detailsTV = new CustomTextView(getContext());
        detailsTV.setText(getContext().getString(R.string.details));
        detailsTV.setTextSize(18);
        detailsTV.setTextColor(ContextCompat.getColor(getContext(), R.color.devil_gray_color));
        padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics());
        detailsTV.setPadding(padding, padding, padding, padding);
        detailsTV.setBackgroundResource(R.drawable.selector_trans_to_light_light_gray);
        llInsideScrollView.addView(detailsTV);

        detailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDetailsClicked(message, InfoMessageDialog.this);
                }
            }
        });

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void onShare() {
        if (listener != null) {
            listener.onShareClicked(message, InfoMessageDialog.this);
        }
    }

    private void dismissWithDelete() {
        AnimUtils.fade(parentLayout, 1, 0, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onDeleteMessage(message, InfoMessageDialog.this);
                }
                InfoMessageDialog.super.dismiss();
            }
        });
    }

    private void dismissWithCopy() {
        AnimUtils.fade(parentLayout, 1, 0, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Tools.copyTextFromTextViewAndShowToast(message.message, getContext());
                InfoMessageDialog.super.dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        AnimUtils.fade(parentLayout, 1, 0, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                InfoMessageDialog.super.dismiss();
            }
        });

    }

    @Override
    public void show() {
        super.show();
        AnimUtils.fade(parentLayout, 0, 1, 300, null);
    }

    public interface OnInfoListener{
        void onDeleteMessage(Message message, Dialog dialog);
        void onDetailsClicked(Message message, Dialog dialog);
        void onShareClicked(Message message, Dialog dialog);
    }

}