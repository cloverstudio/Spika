package com.clover_studio.spikachatmodule.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.MessageSortByCreated;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.clover_studio.spikachatmodule.utils.UtilsImage;
import com.clover_studio.spikachatmodule.utils.VCardParser;
import com.clover_studio.spikachatmodule.view.roundimage.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.BaseViewHolder>{

    private List<Message> data;
    private User myUser;
    private OnLastItemAndOnClickListener lastItemListener;

    public MessageRecyclerViewAdapter (List<Message> data, User myUser){
        this.data = data;
        this.myUser = myUser;
    }

    /**
     * add sent message to adapter
     *
     * @param message sent message
     */
    public void addSentMessage(Message message){
        message.user = myUser;
        data.add(message);
        notifyItemInserted(data.size() - 1);
        notifyItemChanged(Math.max(0, data.size() - 2));
    }

    /**
     *  add received message to adapter
     *
     * @param message received message
     */
    public void addReceivedMessage(Message message){
        data.add(message);
        notifyItemInserted(data.size() - 1);
        notifyItemChanged(Math.max(0, data.size() - 2));
    }

    /**
     * replaced delivered message with sent message
     *
     * @param message delivered message
     */
    public void setDeliveredMessage(Message message){
        for(int i = 0; i < data.size(); i++) {
            Message item = data.get(i);
            if(item.localID != null && item.localID.equals(message.localID)){
                item.status = Const.MessageStatus.DELIVERED;
                item._id = message._id;
                item.created = message.created;
                item.timestampFormatted = "";
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * add messages to adapter
     *
     * @param data messages to add
     * @param isPaging is paging
     */
    public void addMessages (List<Message> data, boolean isPaging){
        if(isPaging){
            notifyItemRangeChanged(0, 1);
        }
        this.data.addAll(data);
        Collections.sort(this.data, new MessageSortByCreated());
        notifyItemRangeInserted(0, data.size());
    }

    /**
     * add latest messages to adapter
     *
     * @param data messages to add
     */
    public void addLatestMessages (List<Message> data){
        this.data.addAll(data);
        Collections.sort(this.data, new MessageSortByCreated());
//        notifyItemRangeInserted(0, data.size());
        notifyDataSetChanged();
    }

    /**
     * update messages with new given messages
     *
     * @param messagesForUpdate message for update
     */
    public void updateMessages (List<Message> messagesForUpdate){
        for (int i = 0; i < data.size(); i++) {
            Message item = data.get(i);
            for(Message itemNew : messagesForUpdate){
                if(item._id != null && item._id.equals(itemNew._id)){
                    item.copyMessage(itemNew);
                    notifyItemChanged(i);
                    continue;
                }
            }
        }
    }

    public void clearMessages(){
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * set on last item visible and on click listener
     *
     * @param listener
     */
    public void setLastItemListener(OnLastItemAndOnClickListener listener){
        lastItemListener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Message getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {

        Message message = getItem(position);

        // early out for info messages
        if (message.type == Const.MessageType.TYPE_NEW_USER || message.type == Const.MessageType.TYPE_USER_LEAVE) {
            return R.layout.item_message_info;
        }

        // early out for deleted messages
        if (message.deleted > 0) {
            if (isMessageFromUser(message, myUser)) {
                return R.layout.item_message_text_right;
            }
            else {
                return R.layout.item_message_text_left;
            }
        }

        int cellType = 0;
        switch (message.type) {
            case Const.MessageType.TYPE_TEXT:
                boolean isLink = false;
                if(message.attributes != null && message.attributes.linkData != null){
                    isLink = true;
                }
                if (isMessageFromUser(message, myUser)) {
                    if(isLink){
                        cellType = R.layout.item_message_link_right;
                    }else{
                        cellType = R.layout.item_message_text_right;
                    }
                }
                else {
                    if(isLink){
                        cellType = R.layout.item_message_link_left;
                    }else{
                        cellType = R.layout.item_message_text_left;
                    }
                }
                break;
            case Const.MessageType.TYPE_FILE:
                if (isMessageFromUser(message, myUser)) {
                    if (message.file != null && Tools.isMimeTypeImage(message.file.file.mimeType)) {
                        cellType = R.layout.item_message_image_right;
                    }
                    else {
                        cellType = R.layout.item_message_file_right;
                    }
                }
                else {
                    if (Tools.isMimeTypeImage(message.file.file.mimeType)) {
                        cellType = R.layout.item_message_image_left;
                    }
                    else {
                        cellType = R.layout.item_message_file_left;
                    }
                }
                break;
            case Const.MessageType.TYPE_LOCATION:
                if (isMessageFromUser(message, myUser)) {
                    cellType = R.layout.item_message_file_right;
                }
                else {
                    cellType = R.layout.item_message_file_left;
                }
                break;
            case Const.MessageType.TYPE_CONTACT:
                if (isMessageFromUser(message, myUser)) {
                    cellType = R.layout.item_message_file_right;
                }
                else {
                    cellType = R.layout.item_message_file_left;
                }
                break;
            case Const.MessageType.TYPE_STICKER:
                if (isMessageFromUser(message, myUser)) {
                    cellType = R.layout.item_message_sticker_right;
                }
                else {
                    cellType = R.layout.item_message_sticker_left;
                }
                break;
            default:
                cellType = R.layout.item_message_text_left;
                break;
        }
        return cellType;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);

        if (viewType == R.layout.item_message_info) {
            return new MessageInfoHolder(view);
        }
        else if (viewType == R.layout.item_message_text_left || viewType == R.layout.item_message_text_right) {
            return new TextViewHolder(view);
        }
        else if (viewType == R.layout.item_message_image_left || viewType == R.layout.item_message_image_right) {
            return new ImageViewHolder(view);
        }
        else if (viewType == R.layout.item_message_link_left || viewType == R.layout.item_message_link_right) {
            return new LinkViewHolder(view);
        }
        else if (viewType == R.layout.item_message_file_left || viewType == R.layout.item_message_file_right) {
            return new FileViewHolder(view);
        }
        else if (viewType == R.layout.item_message_sticker_left || viewType == R.layout.item_message_sticker_right) {
            return new StickerViewHolder(view);
        }
        else {
            return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        holder.bindItem(position);
    }

    public class MessageInfoHolder extends BaseViewHolder  {

        TextView infoMessageTV;
        TextView infoMessageDate;

        public MessageInfoHolder(View itemView) {
            super(itemView);

            infoMessageTV = (TextView) itemView.findViewById(R.id.info);
            infoMessageDate = (TextView) itemView.findViewById(R.id.date);

        }

        @Override
        public void bindItem (int position) {
            //do not call super from base view

            message = data.get(position);

            if (message.type == Const.MessageType.TYPE_NEW_USER) {
                String text = message.user.name + " " + infoMessageTV.getContext().getString(R.string.joined_to_conversation);
                infoMessageTV.setText(text);
            }
            else if (message.type == Const.MessageType.TYPE_USER_LEAVE) {
                String text = message.user.name + " " + infoMessageTV.getContext().getString(R.string.left_from_conversation);
                infoMessageTV.setText(text);
            }

            if(position == 0 || !isMessageInSameDate(message, data.get(Math.max(0, position - 1)))){
                rlDateSeparator.setVisibility(View.VISIBLE);
                dateSeparator.setText(message.getTimeDateSeparator(dateSeparator.getContext()));
            }else{
                rlDateSeparator.setVisibility(View.GONE);
            }

            infoMessageDate.setText(message.getTimeInfoCreated());

            if(position == 0){
                if(lastItemListener != null){
                    lastItemListener.onLastItem();
                }
            }

        }
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements Binder {

        ImageView avatar;
        TextView name;
        View parentView;
        TextView time;
        TextView dateSeparator;
        RelativeLayout rlDateSeparator;

        List<View> peakViews = new ArrayList<>();

        Message message;

        public BaseViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            parentView = itemView.findViewById(R.id.parentView);
            time = (TextView) itemView.findViewById(R.id.time);
            dateSeparator = (TextView) itemView.findViewById(R.id.dateSeparatorTV);
            rlDateSeparator = (RelativeLayout) itemView.findViewById(R.id.rlDateSeparator);

            peakViews.add(itemView.findViewById(R.id.peak_below));
            peakViews.add(itemView.findViewById(R.id.peak_off_and_align_bottom));
            peakViews.add(itemView.findViewById(R.id.peak_off_and_below));
            peakViews.add(itemView.findViewById(R.id.peak_fill_center));

        }

        @Override
        public void bindItem(int position) {

            avatar.setImageDrawable(null);

            message = data.get(position);

            if (!isMessageFromUser(data.get(Math.max(0, position - 1)), message.user) || position == 0) {
                name.setVisibility(View.VISIBLE);
                name.setText(message.user.name);
            }
            else {
                name.setVisibility(View.GONE);
            }

            if(!isMessageFromUser(data.get(Math.min(data.size() - 1, position + 1)), message.user) || position == data.size() - 1){
                avatar.setVisibility(View.VISIBLE);
                UtilsImage.setImageWithLoader(avatar, -1, null, message.user.avatarURL);

                for(View item : peakViews){
                    item.setVisibility(View.VISIBLE);
                }

            }else{
                avatar.setVisibility(View.INVISIBLE);
                for(View item : peakViews){
                    item.setVisibility(View.INVISIBLE);
                }
            }

            //date separator
            if(position == 0 || !isMessageInSameDate(message, data.get(Math.max(0, position - 1)))){
                rlDateSeparator.setVisibility(View.VISIBLE);
                dateSeparator.setText(message.getTimeDateSeparator(dateSeparator.getContext()));
            }else{
                rlDateSeparator.setVisibility(View.GONE);
            }
            //

            if (isMessageFromUser(message, myUser)) {
                if(message.seenBy != null && message.seenBy.size() > 0){
                    String text = time.getResources().getString(R.string.seen) + message.getTimeCreated(time.getResources());
                    time.setText(text);
                } else if (message.status == Const.MessageStatus.SENT) {
                    time.setText(time.getResources().getString(R.string.sending___));
                } else {
                    String text = time.getResources().getString(R.string.sent) + message.getTimeCreated(time.getResources());
                    time.setText(text);
                }
            }else{
                time.setText(message.getTimeCreated(time.getResources()));
            }

            if(position == 0){
                if(lastItemListener != null){
                    lastItemListener.onLastItem();
                }
            }

            parentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(message.deleted > 0){
                        return false;
                    }
                    if(lastItemListener != null){
                        lastItemListener.onLongClick(message);
                    }
                    return true;
                }
            });

            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (message.deleted > 0) {
                        return;
                    }
                    if (lastItemListener != null) {
                        lastItemListener.onClickItem(message);
                    }
                }
            });

        }
    }

    public class TextViewHolder extends BaseViewHolder {

        TextView messageTV;
        boolean isLongActivated;

        public TextViewHolder(View itemView) {
            super(itemView);

            messageTV = (TextView) itemView.findViewById(R.id.message);
            isLongActivated = false;
        }

        @Override
        public void bindItem (int position) {
            super.bindItem(position);

            if (message.deleted != -1 && message.deleted != 0) {
                String text = messageTV.getContext().getString(R.string.message_deleted_at) + " " + Tools.generateDate(Const.DateFormats.USER_JOINED_DATE_FORMAT, message.deleted);
                messageTV.setText(text);
            }
            else if (message.type == Const.MessageType.TYPE_NEW_USER) {
                String text = message.user.name + " " + messageTV.getContext().getString(R.string.joined_to_conversation);
                messageTV.setText(text);
            }
            else if (message.type == Const.MessageType.TYPE_USER_LEAVE) {
                String text = message.user.name + " " + messageTV.getContext().getString(R.string.left_from_conversation);
                messageTV.setText(text);
            }
            else {
                messageTV.setText(message.message);
            }

            messageTV.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (message.deleted > 0) {
                        return false;
                    }
                    if (lastItemListener != null) {
                        lastItemListener.onLongClick(message);
                    }
                    isLongActivated = true;
                    return false;
                }
            });

            messageTV.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (isLongActivated) {
                            isLongActivated = false;
                            return true;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        isLongActivated = false;
                    }
                    return false;
                }
            });
        }
    }

    public class LinkViewHolder extends BaseViewHolder {

        RoundedImageView image;
        TextView title;
        TextView desc;
        TextView host;
        TextView messageTV;
        boolean isLongActivated;

        public LinkViewHolder(View itemView) {
            super(itemView);

            image = (RoundedImageView) itemView.findViewById(R.id.linkImgView);
            image.setCornerRadius(10f);
            title = (TextView) itemView.findViewById(R.id.linkTitle);
            desc = (TextView) itemView.findViewById(R.id.linkDescription);
            host = (TextView) itemView.findViewById(R.id.linkHost);
            messageTV = (TextView) itemView.findViewById(R.id.textMessage);

            isLongActivated = false;

        }

        @Override
        public void bindItem (int position) {
            super.bindItem(position);

            image.setImageDrawable(null);

            if(message.attributes.linkData == null){
                return;
            }

            if(message.attributes.linkData.imageUrl != null){
                UtilsImage.setImageWithLoader(image, -1, null, message.attributes.linkData.imageUrl);
                image.setVisibility(View.VISIBLE);
            }else{
                image.setVisibility(View.GONE);
            }

            if(TextUtils.isEmpty(message.attributes.linkData.title)){
                title.setText(message.attributes.linkData.siteName);
            }else{
                title.setText(message.attributes.linkData.title);
            }            desc.setText(message.attributes.linkData.desc);
            host.setText(message.attributes.linkData.host);

            messageTV.setText(message.message);

            messageTV.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (message.deleted > 0) {
                        return false;
                    }
                    if (lastItemListener != null) {
                        lastItemListener.onLongClick(message);
                    }
                    isLongActivated = true;
                    return false;
                }
            });

            messageTV.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        if(isLongActivated){
                            isLongActivated = false;
                            return true;
                        }
                    }else if(event.getAction() == MotionEvent.ACTION_DOWN){
                        isLongActivated = false;
                    }
                    return false;
                }
            });
        }
    }

    public class ImageViewHolder extends BaseViewHolder {

        RoundedImageView imageIV;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageIV = (RoundedImageView) itemView.findViewById(R.id.image);
            imageIV.setCornerRadius(R.dimen.corners_for_bubble);
        }

        @Override
        public void bindItem(int position) {
            super.bindItem(position);
            imageIV.setImageDrawable(null);
            if(message.file.thumb != null){
                UtilsImage.setImageWithLoader(imageIV, -1, null, Tools.getFileUrlFromId(message.file.thumb.id, imageIV.getContext()));
            }else if(message.file.file != null){
                UtilsImage.setImageWithLoader(imageIV, -1, null, Tools.getFileUrlFromId(message.file.file.id, imageIV.getContext()));
            }else{
                imageIV.setImageDrawable(null);
            }
        }
    }

    public class StickerViewHolder extends BaseViewHolder {

        RoundedImageView imageIV;

        public StickerViewHolder(View itemView) {
            super(itemView);
            imageIV = (RoundedImageView) itemView.findViewById(R.id.image);
            imageIV.setCornerRadius(R.dimen.corners_for_bubble);
        }

        @Override
        public void bindItem(int position) {
            super.bindItem(position);
            imageIV.setImageDrawable(null);
            UtilsImage.setImageWithLoader(imageIV, -1, null, message.message);
        }
    }

    public class FileViewHolder extends BaseViewHolder {

        ImageView fileIcon;

        TextView title;
        TextView subTitle;
        TextView subSubTitle;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileIcon = (ImageView) itemView.findViewById(R.id.fileIcon);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.subTitle);
            subSubTitle = (TextView) itemView.findViewById(R.id.subSubTitle);
        }

        @Override
        public void bindItem(int position) {
            super.bindItem(position);

            int drawable = 0;
            subSubTitle.setVisibility(View.VISIBLE);
            if (message.type == Const.MessageType.TYPE_FILE && message.file != null) {

                title.setText(message.file.file.name);
                subTitle.setText(Tools.readableFileSize(Long.valueOf(message.file.file.size)));
                subSubTitle.setText(subSubTitle.getContext().getResources().getString(R.string.download));

                if (Tools.isMimeTypeVideo(message.file.file.mimeType)) {
                    if (isMessageFromUser(message, myUser)) {
                        drawable = R.drawable.video_white;
                    }
                    else {
                        drawable = R.drawable.video_color;
                    }
                }
                else if (Tools.isMimeTypeAudio(message.file.file.mimeType)) {
                    if (isMessageFromUser(message, myUser)) {
                        drawable = R.drawable.audio_white;
                    }
                    else {
                        drawable = R.drawable.audio_color;
                    }
                }else{
                    if (isMessageFromUser(message, myUser)) {
                        drawable = R.drawable.file_white;
                    }
                    else {
                        drawable = R.drawable.file_color;
                    }
                }
            }
            else if (message.type == Const.MessageType.TYPE_LOCATION) {

                title.setText(message.message);
                subTitle.setText(subTitle.getContext().getResources().getString(R.string.show_location_on_map));
                subSubTitle.setText("");
                subSubTitle.setVisibility(View.GONE);

                if (isMessageFromUser(message, myUser)) {
                    drawable = R.drawable.location_white;
                }
                else {
                    drawable = R.drawable.location_color;
                }
            }
            else if (message.type == Const.MessageType.TYPE_CONTACT) {

                String[] contactData = VCardParser.getNameAndFirstPhoneAndFirstEmail(message.message, title.getContext().getString(R.string.no_name));

                title.setText(contactData[Const.ContactData.NAME]);
                subTitle.setText(contactData[Const.ContactData.PHONE]);
                subSubTitle.setText(contactData[Const.ContactData.EMAIL]);

                if (isMessageFromUser(message, myUser)) {
                    drawable = R.drawable.contact_white;
                }
                else {
                    drawable = R.drawable.contact_color;
                }
            }
            else {
                if (isMessageFromUser(message, myUser)) {
                    drawable = R.drawable.file_white;
                }
                else {
                    drawable = R.drawable.file_color;
                }
            }
            fileIcon.setImageResource(drawable);
        }
    }

    /**
     * get newest message id
     * @return newest messageId
     */
    public String getNewestMessageId(){
        if(data.size() == 0) return "0";
        return data.get(data.size() - 1)._id;
    }

    /**
     * check is message from user
     * @param message - message with user
     * @param user - user for check
     * @return boolean is message from user
     */
    private boolean isMessageFromUser(Message message, User user){
        String userId = null;
        boolean isMyMessage = false;
        if(message.user != null && message.user.userID != null){
            userId = message.user.userID;
        }else{
            userId = message.userID;
        }

        if(userId.equals(user.userID)){
            isMyMessage = true;
        }

        if(message.type == Const.MessageType.TYPE_NEW_USER || message.type == Const.MessageType.TYPE_USER_LEAVE){
            isMyMessage = false;
        }

        return isMyMessage;
    }

    /**
     * check is messages in same day
     * @param message1 - message above
     * @param message2 - message below
     * @return boolean is messages in same day
     */
    private boolean isMessageInSameDate(Message message1, Message message2){
        boolean isSameDate = false;

        if(message1.yearOfCreated != -1 && message2.yearOfCreated != -1){
            isSameDate = message1.yearOfCreated == message2.yearOfCreated &&
                    message1.dayOfYearCreated == message2.dayOfYearCreated;
        }else{
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTimeInMillis(message1.created);
            cal2.setTimeInMillis(message2.created);
            isSameDate = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            message1.dayOfYearCreated = cal1.get(Calendar.DAY_OF_YEAR);
            message1.yearOfCreated = cal1.get(Calendar.YEAR);

            message2.dayOfYearCreated = cal2.get(Calendar.DAY_OF_YEAR);
            message2.yearOfCreated = cal2.get(Calendar.YEAR);
        }

        return isSameDate;
    }

    /**
     * set visibility of all view from given ViewGroup to visible
     * @param layout
     */
    private void showAllChildrenFromLayout(ViewGroup layout){
        for(int i = 0; i < layout.getChildCount(); i++){
            layout.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * listener for last item showed and for click on item
     */
    public interface OnLastItemAndOnClickListener{
        /**
         * triggered when last item show in adapter
         */
        public void onLastItem();

        /**
         * triggered when user click on item
         *
         * @param item data from clicked item
         */
        public void onClickItem(Message item);

        /**
         * triggered when user long click on item
         *
         * @param item data from clicked item
         */
        public void onLongClick(Message item);
    }

    public interface Binder {
        public void bindItem (int position);
    }
}
