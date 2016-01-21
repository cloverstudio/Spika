package com.clover_studio.democloverapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.clover_studio.spikachatmodule.ChatActivity;
import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.Const;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class DemoChatActivity extends ChatActivity{

    public static void starChatActivity(Context context, User user){
        Intent intent = new Intent(context, DemoChatActivity.class);
        intent.putExtra(Const.Extras.USER, user);
        context.startActivity(intent);
    }

    public static void startChatActivityWithConfig(Context context, User user, Config config){
        Intent intent = new Intent(context, DemoChatActivity.class);
        intent.putExtra(Const.Extras.USER, user);
        intent.putExtra(Const.Extras.CONFIG, config);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        changeToolbarColor("#0000ff");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
