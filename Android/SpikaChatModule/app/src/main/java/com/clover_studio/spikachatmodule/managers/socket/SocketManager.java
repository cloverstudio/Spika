package com.clover_studio.spikachatmodule.managers.socket;

import android.content.Context;

import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.SendTyping;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by ubuntu_ivo on 11.01.16..
 */
public class SocketManager {

    private static SocketManager socketManager;

    private Socket mSocket;
    private SocketManagerListener mListener;

    public static SocketManager getInstance(){
        if (socketManager == null)
        {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    public void setListener(SocketManagerListener listener){
        mListener = listener;
    }

    /**
     * connect to socket
     */
    public void connectToSocket(Context context){
        LogCS.e("LOG", "Connecting to socket");
        if(mSocket != null){
            mSocket.close();
            mSocket.disconnect();
            mSocket = null;
        }
        try {

            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.transports = new String[]{"websocket"};
            mSocket = IO.socket(SingletonLikeApp.getInstance().getConfig(context).socketUrl, opts);
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            socketFailedDialog();
            return;
        }

        if(mListener != null) mListener.onLoginWithSocket();

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(mListener != null) mListener.onConnect();
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketFailedDialog();
            }
        });

        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketFailedDialog();
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketFailedDialog();
            }
        });

        mSocket.on(Const.EmitKeyWord.NEW_USER, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(mListener != null) mListener.onNewUser(args);
            }
        });

        mSocket.on(Const.EmitKeyWord.USER_LEFT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String userLeft = args[0].toString();
                Gson gson = new Gson();
                try {
                    User user = gson.fromJson(userLeft, User.class);
                    if(mListener != null) mListener.onUserLeft(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on(Const.EmitKeyWord.SEND_TYPING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String sendTyping = args[0].toString();
                Gson gson = new Gson();
                try {
                    SendTyping typing = gson.fromJson(sendTyping, SendTyping.class);
                    if(mListener != null) mListener.onTyping(typing);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on(Const.EmitKeyWord.NEW_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogCS.w("LOG", "MESSAGE RECEIVED");
                String newMessage = args[0].toString();
                Gson gson = new Gson();
                try {
                    Message message = gson.fromJson(newMessage, Message.class);
                    if(mListener != null) mListener.onMessageReceived(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on(Const.EmitKeyWord.MESSAGE_UPDATED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String newMessage = args[0].toString();
                Gson gson = new Gson();
                try {
                    List<Message> messages = gson.fromJson(newMessage, new TypeToken<List<Message>>() {}.getType());
                    if(mListener != null) mListener.onMessagesUpdated(messages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on(Const.EmitKeyWord.SOCKET_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String response = args[0].toString();
                Gson gson = new Gson();
                try {
                    BaseModel responseModel = gson.fromJson(response, BaseModel.class);
                    if(mListener != null) mListener.onSocketError(responseModel.code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected void socketFailedDialog(){
        if(mListener != null) mListener.onSocketFailed();
    }

    /**
     * emit message to socket
     *
     * @param emitType type of emit message
     * @param jsonObject data for send to server
     */
    public void emitMessage(String emitType, JSONObject jsonObject){
        if(mSocket != null) mSocket.emit(emitType, jsonObject);
    }

    /**
     * close socket and disconnect to socket and set socket and listener to null
     */
    public void closeAndDisconnectSocket(){
        if(mSocket != null){
            LogCS.e("LOG", "Closing socket");
            mSocket.close();
            mSocket.disconnect();
            mSocket = null;
            mListener = null;
        }
    }

    /**
     * reconnect to socket if socket is null or disconnected
     */
    public void tryToReconnect(Context context){
        LogCS.e("LOG", "Check for socket reconnect");
        if(mSocket != null){
            if(mSocket.connected()){
            }else{
                connectToSocket(context);
            }
        }else{
            connectToSocket(context);
        }
    }

    /**
     * check if socket is connected
     */
    public boolean isSocketConnect(){
        if(mSocket == null) return false;
        return mSocket.connected();
    }

}
