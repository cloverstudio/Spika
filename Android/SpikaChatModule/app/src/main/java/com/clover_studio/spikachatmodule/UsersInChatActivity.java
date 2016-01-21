package com.clover_studio.spikachatmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.adapters.UsersInChatRecyclerViewAdapter;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.models.GetUserModel;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.robospice.api.UsersApi;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceListener;
import com.clover_studio.spikachatmodule.utils.Const;

import java.util.ArrayList;


public class UsersInChatActivity extends BaseActivity {

    private String roomId;
    RecyclerView rvMain;

    public static void starUsersInChatActivity(Context context, String roomId){
        Intent intent = new Intent(context, UsersInChatActivity.class);
        intent.putExtra(Const.Extras.ROOM_ID, roomId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_chat);

        setToolbar(R.id.tToolbar, R.layout.custom_users_in_chat_toolbar);
        setMenuLikeBack();

        rvMain = (RecyclerView) findViewById(R.id.rvMain);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setAdapter(new UsersInChatRecyclerViewAdapter(new ArrayList<User>()));

        //check for user
        if(!getIntent().hasExtra(Const.Extras.ROOM_ID)){
            noRoomIdDialog();
            return;
        }else{
            roomId = getIntent().getStringExtra(Const.Extras.ROOM_ID);
            if(TextUtils.isEmpty(roomId)){
                noRoomIdDialog();
                return;
            }
        }

        getUsers(roomId);

    }

    /**
     * get users from room
     * @param roomId id of room
     */
    private void getUsers(String roomId) {
        handleProgress(true);
        UsersApi.GetUsersInRoom spice = new UsersApi.GetUsersInRoom(roomId);
        getSpiceManager().execute(spice, new CustomSpiceListener<GetUserModel>(this){

            @Override
            public void onRequestSuccess(GetUserModel result) {
                super.onRequestSuccess(result);
                if (result.code == 1) {
                    ((UsersInChatRecyclerViewAdapter)rvMain.getAdapter()).setData(result.data);
                }
            }
        });

    }

    protected void noRoomIdDialog(){
        NotifyDialog dialog = NotifyDialog.startInfo(this, getString(R.string.room_error_title), getString(R.string.room_error_not_sent));
        dialog.setOneButtonListener(new NotifyDialog.OneButtonDialogListener() {
            @Override
            public void onOkClicked(NotifyDialog dialog) {
                dialog.dismiss();
                finish();
            }
        });
    }

}
