package com.clover_studio.spikachatmodule.utils;

import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ubuntu_ivo on 10.08.15..
 */
public class EmitJsonCreator {

    /**
     *
     * format login message to JSONObject for socket communication
     *
     * @param user data for login
     * @return JSONObject
     */
    public static JSONObject createEmitLoginMessage(User user){

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", user.name);
            obj.put("avatar", user.avatarURL);
            obj.put("roomID", user.roomID);
            obj.put("userID", user.userID);

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;

    }

    /**
     *
     * format message to JSONObject for socket communication
     *
     * @param message message to sent
     * @return JSONObject
     */
    public static JSONObject createEmitSendMessage(Message message){

        JSONObject obj = new JSONObject();
        try {
            obj.put("message", message.message);
            obj.put("type", message.type);
            obj.put("roomID", message.roomID);
            obj.put("userID", message.userID);
            obj.put("localID", message.localID);

            if(message.file != null){
                JSONObject fileModel = new JSONObject();

                if(message.file.file != null){
                    JSONObject file = new JSONObject();
                    file.put("id", message.file.file.id);
                    file.put("name", message.file.file.name);
                    file.put("size", message.file.file.size);
                    file.put("mimeType", message.file.file.mimeType);

                    fileModel.put("file", file);
                }

                if(message.file.thumb != null){
                    JSONObject file = new JSONObject();
                    file.put("id", message.file.thumb.id);
                    file.put("name", message.file.thumb.name);
                    file.put("size", message.file.thumb.size);
                    file.put("mimeType", message.file.thumb.mimeType);

                    fileModel.put("thumb", file);
                }

                obj.put("file", fileModel);
            }

            if(message.location != null){
                JSONObject locationModel = new JSONObject();
                locationModel.put("lat", message.location.lat);
                locationModel.put("lng", message.location.lng);

                obj.put("location", locationModel);
            }

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;

    }

    /**
     *
     * format typing message to JSONObject for socket communication
     *
     * @param user
     * @param type 1 is typing, 0 stop typing
     * @return JSONObject
     */
    public static JSONObject createEmitSendTypingMessage(User user, int type){

        JSONObject obj = new JSONObject();
        try {
            obj.put("type", type);
            obj.put("roomID", user.roomID);
            obj.put("userID", user.userID);

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;

    }

    /**
     * format open message to JSONObject for socket communication
     *
     * @param messagesIds ids of new unseen messages
     * @param userId user id of logged in user
     * @return JSONObject
     */
    public static JSONObject createEmitOpenMessage(List<String> messagesIds, String userId){
        JSONObject obj = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for(String item : messagesIds){
                jsonArray.put(item);
            }
            obj.put("messageIDs", jsonArray);
            obj.put("userID", userId);

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * format delete message to JSONObject for socket communication
     *
     * @param userId id of logged user
     * @param messageId id of message to delete
     * @return JSONObject
     */
    public static JSONObject createEmitDeleteMessage(String userId, String messageId){

        JSONObject obj = new JSONObject();
        try {
            obj.put("userID", userId);
            obj.put("messageID", messageId);

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;

    }

}
