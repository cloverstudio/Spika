package com.clover_studio.democloverapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.clover_studio.democloverapp.base.BaseActivity;
import com.clover_studio.spikachatmodule.ChatActivity;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.Const;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class LoginActivity extends BaseActivity{

    public static void start(Context c) {
        c.startActivity(new Intent(c, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final EditText id = (EditText) findViewById(R.id.idOfUser);
        final EditText name = (EditText) findViewById(R.id.nameOfUser);
        final EditText avatar = (EditText) findViewById(R.id.avatar);
        final EditText roomId = (EditText) findViewById(R.id.roomId);

        id.requestFocus();

        final String model = Build.MODEL;

        id.setText(model);
        name.setText(model);
        avatar.setText("https://pbs.twimg.com/profile_images/1314621221/pavel-nedved1_400x400.jpg");
        roomId.setText("danas");

        //CONFIG
        final EditText socket = (EditText) findViewById(R.id.socketUrl);
        final EditText api = (EditText) findViewById(R.id.apiUrl);
        final Switch configSwitch = (Switch) findViewById(R.id.switchEnableConfig);

        configSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    socket.setEnabled(true);
                    api.setEnabled(true);
                }else{
                    socket.setEnabled(false);
                    api.setEnabled(false);
                }
            }
        });

        api.setText(Const.Api.BASE_URL);
        socket.setText(Const.Socket.SOCKET_URL);

        Button startChat = (Button) findViewById(R.id.startChat);

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkParams(id.getText().toString(), name.getText().toString(), roomId.getText().toString(), configSwitch.isChecked(), api.getText().toString(), socket.getText().toString())) {
                    return;
                }
                User user = generateUserModel(id.getText().toString(), name.getText().toString(), avatar.getText().toString(), roomId.getText().toString());
                if(configSwitch.isChecked()){
                    Config config = generateConfigModel(api.getText().toString(), socket.getText().toString());
                    DemoChatActivity.startChatActivityWithConfig(LoginActivity.this, user, config);
                }else{
                    DemoChatActivity.starChatActivity(LoginActivity.this, user);
                }
            }
        });

    }

    private User generateUserModel (String userID, String name, String avatar, String roomId){
        User user = new User();

        user.userID = userID;
        user.name = name;
        user.avatarURL = avatar;
        user.roomID = roomId;

        return user;
    }

    private Config generateConfigModel (String api, String socket){
        Config config = new Config(api, socket);
        return config;
    }

    private boolean checkParams(String id, String name, String roomId, boolean isEnabledConfig, String api, String socket){

        if(TextUtils.isEmpty(id)){
            NotifyDialog.startInfo(this, "Error", "Please enter user id");
            return false;
        }

        if(TextUtils.isEmpty(name)){
            NotifyDialog.startInfo(this, "Error", "Please enter user name");
            return false;
        }

        if(TextUtils.isEmpty(roomId)){
            NotifyDialog.startInfo(this, "Error", "Please enter room id");
            return false;
        }

        if(isEnabledConfig){

            if(TextUtils.isEmpty(api)){
                NotifyDialog.startInfo(this, "Error", "Please enter api rul");
                return false;
            }

            if(TextUtils.isEmpty(socket)){
                NotifyDialog.startInfo(this, "Error", "Please enter socket url");
                return false;
            }
        }

        return true;
    }

}
