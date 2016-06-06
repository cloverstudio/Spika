package com.clover_studio.spikachatmodule.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.UtilsImage;
import com.clover_studio.spikachatmodule.view.roundimage.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class UsersInChatRecyclerViewAdapter extends RecyclerView.Adapter<UsersInChatRecyclerViewAdapter.ViewHolder>{

    private List<User> data;

    public UsersInChatRecyclerViewAdapter(List<User> data){
        this.data = data;
    }

    public void setData (List<User> data){
        if(this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ProgressBar progressForAvatar;
        ImageView avatar;

        ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.nameOfUser);
            progressForAvatar = (ProgressBar) v.findViewById(R.id.avatarProgressBar);
            avatar = (ImageView) v.findViewById(R.id.avatar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_in_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final User user = data.get(position);
        holder.name.setText(user.name);

        holder.progressForAvatar.setVisibility(View.VISIBLE);
        ((RoundImageView)holder.avatar).setBorderColor(ContextCompat.getColor(holder.avatar.getContext(), R.color.default_color));

        holder.avatar.setImageDrawable(null);

        UtilsImage.setImageWithLoader(holder.avatar, -1, holder.progressForAvatar, user.avatarURL);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
