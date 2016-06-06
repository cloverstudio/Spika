package com.clover_studio.spikachatmodule.dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.SeenByModel;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.AnimUtils;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.clover_studio.spikachatmodule.utils.UtilsImage;

public class PreviewMessageDialog extends Dialog {

    Message message;
    int width;
    RelativeLayout layoutContent;
    User activeUser;

    public static PreviewMessageDialog startDialog(Context context, Message message, User activeUser){
        PreviewMessageDialog dialog = new PreviewMessageDialog(context, message, activeUser);
        return dialog;
    }

    public PreviewMessageDialog(Context context, Message message, User activeUser) {
        super(context, R.style.Theme_Dialog_no_dim);

        setOwnerActivity((Activity) context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        width = context.getResources().getDisplayMetrics().widthPixels;
        this.message = message;
        this.activeUser = activeUser;

        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_preview_message);

        RecyclerView rvUsersSeen = (RecyclerView) findViewById(R.id.rvUsersSeen);
        rvUsersSeen.setLayoutManager(new LinearLayoutManager(getOwnerActivity()));

        rvUsersSeen.setAdapter(new UsersSeenAdapter());

        TextView sender = (TextView) findViewById(R.id.sender);
        sender.setText(message.user.name);

        TextView sentAt = (TextView) findViewById(R.id.sentAt);
        sentAt.setText(Tools.generateDate(Const.DateFormats.USER_JOINED_DATE_FORMAT, message.created));

        layoutContent = (RelativeLayout) findViewById(R.id.content);
        AnimUtils.translateX(layoutContent, width, 0, 300, null);

    }

    @Override
    public void dismiss() {
        AnimUtils.translateX(layoutContent, 0, width, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                PreviewMessageDialog.super.dismiss();
            }
        });

    }

    private class UsersSeenAdapter extends RecyclerView.Adapter<UsersSeenAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            ImageView avatar;
            TextView timestamp;

            ViewHolder(View v) {
                super(v);

                name = (TextView) v.findViewById(R.id.nameOfUser);
                avatar = (ImageView) v.findViewById(R.id.avatar);
                timestamp = (TextView) v.findViewById(R.id.timestamp);

            }
        }

        @Override
        public UsersSeenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_seen, parent, false));
        }

        @Override
        public void onBindViewHolder(UsersSeenAdapter.ViewHolder holder, int position) {
            SeenByModel model = message.seenBy.get(position);

            holder.avatar.setImageDrawable(null);

            holder.name.setText(model.user.name);
            holder.timestamp.setText(Tools.generateDate(Const.DateFormats.USER_JOINED_DATE_FORMAT, model.at));

            holder.avatar.setImageDrawable(null);
            UtilsImage.setImageWithLoader(holder.avatar, -1, null, model.user.avatarURL);
        }

        @Override
        public int getItemCount() {
            if(message.seenBy == null) return 0;
            return message.seenBy.size();
        }
    }

}