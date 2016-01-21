package com.clover_studio.spikachatmodule.managers.socket;

import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.SendTyping;
import com.clover_studio.spikachatmodule.models.User;

import java.util.List;

/**
 * Created by ubuntu_ivo on 11.01.16..
 */
public interface SocketManagerListener {

    /**
     * when socket connected
     */
    public void onConnect();

    /**
     * socket failed while trying to connect
     */
    public void onSocketFailed();

    /**
     * socket connected, connect to room
     */
    public void onLoginWithSocket();

    /**
     * user left from room
     */
    public void onUserLeft(User user);

    /**
     * received typing
     */
    public void onTyping(SendTyping typing);

    /**
     * received message
     */
    public void onMessageReceived(Message message);

    /**
     * received messages update
     */
    public void onMessagesUpdated(List<Message> messages);

    /**
     * receive new user connect to room
     */
    public void onNewUser(Object... args);

    /**
     * receive socket error
     */
    public void onSocketError(int code);

}
