package com.clover_studio.spikachatmodule.models;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class Message extends BaseModel {

    public String _id;
    public String userID;
    public String roomID;
    public User user;
    public int type;
    public String roomId;
    public String message;
    public long created;
    public FileModel file;
    public String localID;
    public LocationModel location;
    public List<SeenByModel> seenBy;
    public long deleted = -1;
    public Attributes attributes;

    public int status;

    //for date compare, this is used just in live adapter, do not save to base or make it parcelable
    public String timestampFormatted;
    public String timestampInfoFormatted;
    public String timestampDateSeparatorFormatted;
    public int yearOfCreated = -1;
    public int dayOfYearCreated = -1;


    public void copyMessage(Message message){
        _id = message._id;
        userID = message.userID;
        roomID = message.roomID;
        user = message.user;
        type = message.type;
        roomId = message.roomId;
        this.message = message.message;
        created = message.created;
        file = message.file;
        localID = message.localID;
        location = message.location;
        seenBy = message.seenBy;
        deleted = message.deleted;
        status = message.status;
        attributes = message.attributes;
    }

    @Override
    public String toString() {
        return "Message{" +
                "_id='" + _id + '\'' +
                ", userID='" + userID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", user=" + user +
                ", type=" + type +
                ", roomId='" + roomId + '\'' +
                ", message='" + message + '\'' +
                ", created=" + created +
                ", file=" + file +
                ", localID='" + localID + '\'' +
                ", location=" + location +
                ", seenBy=" + seenBy +
                ", status=" + status +
                ", attributes=" + attributes +
                '}';
    }

    public String getTimeCreated(Resources res){
        if(!TextUtils.isEmpty(timestampFormatted)){
            return timestampFormatted;
        }else{
            timestampFormatted = justTime(created);
            return timestampFormatted;
        }
    }

    public String getTimeInfoCreated(){
        if(!TextUtils.isEmpty(timestampInfoFormatted)){
            return timestampInfoFormatted;
        }else{
            timestampInfoFormatted = timeWithDate(created);
            return timestampInfoFormatted;
        }
    }

    public String getTimeDateSeparator(Context context){
        if(!TextUtils.isEmpty(timestampDateSeparatorFormatted)){
            return timestampDateSeparatorFormatted;
        }else{
            timestampDateSeparatorFormatted = timeSeparatorStyle(created, context);
            return timestampDateSeparatorFormatted;
        }
    }

    private String formatTime(long time, Resources res) {
        long currentTime = System.currentTimeMillis();

        long currentTimeDay = currentTime / 86400000;
        long timeDay = time / 86400000;
        if (currentTimeDay == timeDay) {
            return justTime(time);
        } else {
            return timeWithDate(time);
        }
    }

    private String justTime(long time){
        try {

            Timestamp stamp = new Timestamp(time);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String timeWithDate(long time){
        try {

            Timestamp stamp = new Timestamp(time);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String timeSeparatorStyle(long time, Context context){
        try {

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTimeInMillis(time);
            cal2.setTimeInMillis(System.currentTimeMillis());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if(sameDay){
                return context.getString(R.string.today);
            }
            boolean yesterday = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    (cal1.get(Calendar.DAY_OF_YEAR) + 1) == cal2.get(Calendar.DAY_OF_YEAR);
            if(yesterday){
                return context.getString(R.string.yesterday);
            }

            Timestamp stamp = new Timestamp(time);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public void fillMessageForSend(User activeUser, String messageString, int typeMessage, FileModel fileMessage, LatLng latLng){
        userID = activeUser.userID;
        roomID = activeUser.roomID;
        localID = Tools.generateRandomString(32);
        type = typeMessage;
        status = Const.MessageStatus.SENT;
        message = messageString;
        created = System.currentTimeMillis();

        if(fileMessage != null){
            file = fileMessage;
        }

        if(latLng != null){
            LocationModel locationMessage = new LocationModel();
            locationMessage.lat = latLng.latitude;
            locationMessage.lng = latLng.longitude;
            location = locationMessage;
        }

    }

}
