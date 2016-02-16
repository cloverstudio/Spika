package com.clover_studio.spikachatmodule;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.adapters.MessageRecyclerViewAdapter;
import com.clover_studio.spikachatmodule.adapters.SettingsAdapter;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.dialogs.DownloadFileDialog;
import com.clover_studio.spikachatmodule.dialogs.InfoMessageDialog;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.dialogs.PreviewAudioDialog;
import com.clover_studio.spikachatmodule.dialogs.PreviewMessageDialog;
import com.clover_studio.spikachatmodule.dialogs.PreviewPhotoDialog;
import com.clover_studio.spikachatmodule.dialogs.PreviewVideoDialog;
import com.clover_studio.spikachatmodule.dialogs.UploadFileDialog;
import com.clover_studio.spikachatmodule.managers.socket.SocketManager;
import com.clover_studio.spikachatmodule.managers.socket.SocketManagerListener;
import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.models.GetMessagesModel;
import com.clover_studio.spikachatmodule.models.LocationModel;
import com.clover_studio.spikachatmodule.models.Login;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.SendTyping;
import com.clover_studio.spikachatmodule.models.UploadFileResult;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.robospice.api.DownloadFileManager;
import com.clover_studio.spikachatmodule.robospice.api.LoginApi;
import com.clover_studio.spikachatmodule.robospice.api.MessagesApi;
import com.clover_studio.spikachatmodule.robospice.api.UploadFileManagement;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceListener;
import com.clover_studio.spikachatmodule.utils.AnimUtils;
import com.clover_studio.spikachatmodule.utils.ApplicationStateManager;
import com.clover_studio.spikachatmodule.utils.BuildTempFileAsync;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.EmitJsonCreator;
import com.clover_studio.spikachatmodule.utils.ErrorHandle;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.clover_studio.spikachatmodule.utils.OpenDownloadedFile;
import com.clover_studio.spikachatmodule.utils.SeenByUtils;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.clover_studio.spikachatmodule.view.menu.MenuManager;
import com.clover_studio.spikachatmodule.view.menu.OnMenuButtonsListener;
import com.clover_studio.spikachatmodule.view.menu.OnMenuManageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends BaseActivity {

    private User activeUser;

    private ListView settingsListView;
    protected RecyclerView rvMessages;
    protected TextView tvTyping;

    private EditText etMessage;
    private ImageButton btnSend;
    private ButtonType buttonType = ButtonType.MENU;
    private TypingType typingType = TypingType.BLANK;
    private TextView newMessagesButton;

    protected MenuManager menuManager;
    protected List<String> sentMessages = new ArrayList<>();
    protected List<User> typingUsers = new ArrayList<>();

    //data from last paging
    protected List<Message> lastDataFromServer = new ArrayList<>();

    //for scroll when keyboard opens
    protected int lastVisibleItem = 0;

    // is socket closed on pause
    private boolean pausedForSocket = false;
    // don't close socket when open camera or location or audio activity
    private boolean forceStaySocket = false;
    // first time resume called
    private boolean firstTime = true;

    //message queue for unsent message when socket is not connected
    private List<Message> unSentMessageList = new ArrayList<>();

    //message queue for new message from latest api when listView is not at bottom
    private List<Message> unReadMessage = new ArrayList<>();

    public enum ButtonType {
        MENU, SEND, MENU_OPENED, IN_ANIMATION;
    }

    public enum TypingType {
        TYPING, BLANK;
    }

    private LocationModel tempLocationForPermission;

    /**
     * start chat activity with user data
     *
     * @param context
     * @param user    user to login
     */
    public static void starChatActivity(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Const.Extras.USER, user);
        context.startActivity(intent);
    }

    public static void startChatActivityWithConfig(Context context, User user, Config config) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Const.Extras.USER, user);
        intent.putExtra(Const.Extras.CONFIG, config);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SingletonLikeApp.getInstance().setApplicationState(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PermissionCode.CHAT_STORAGE);
        }

        if (getIntent().hasExtra(Const.Extras.CONFIG)) {
            Config config = getIntent().getParcelableExtra(Const.Extras.CONFIG);
            SingletonLikeApp.getInstance().getSharedPreferences(getActivity()).setConfig(config);
            SingletonLikeApp.getInstance().setConfig(config);
        } else {
            Config config = new Config("", "");
            SingletonLikeApp.getInstance().getSharedPreferences(getActivity()).setConfig(config);
            SingletonLikeApp.getInstance().setConfig(null);
        }

        setToolbar(R.id.tToolbar, R.layout.custom_chat_toolbar);
        setMenuLikeBack();
        onSettingsButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingsClicked();
            }
        });

        rvMessages = (RecyclerView) findViewById(R.id.rvMain);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rvMessages.setLayoutManager(llm);

        settingsListView = (ListView) findViewById(R.id.settings_list_view);
        SettingsAdapter adapter = new SettingsAdapter(this);
        settingsListView.setAdapter(adapter);
        adapter.setSettings();
        settingsListView.setOnItemClickListener(onSettingItemClick);

        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMenuButtonClicked();
            }
        });

        etMessage = (EditText) findViewById(R.id.etMessage);
        etMessage.addTextChangedListener(etMessageTextWatcher);

        tvTyping = (TextView) findViewById(R.id.toolbarSubtitle);

        menuManager = new MenuManager();
        menuManager.setMenuLayout(this, R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

        //check for user
        if (!getIntent().hasExtra(Const.Extras.USER)) {
            noUserDialog();
            return;
        } else {
            activeUser = getIntent().getParcelableExtra(Const.Extras.USER);
            if (activeUser == null) {
                noUserDialog();
                return;
            }
        }

        tvTyping.setText(activeUser.userID);

        rvMessages.setAdapter(new MessageRecyclerViewAdapter(new ArrayList<Message>(), activeUser));
        ((MessageRecyclerViewAdapter) rvMessages.getAdapter()).setLastItemListener(onLastItemAndClickItemListener);

        setToolbarTitle(activeUser.roomID);

        findViewById(R.id.viewForSettingBehind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSettings();
            }
        });

        findViewById(R.id.viewForMenuBehind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonMenuOpenedClicked();
            }
        });

        login(activeUser);

        rvMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastVisibleItem = ((LinearLayoutManager) rvMessages.getLayoutManager()).findLastVisibleItemPosition();
                if (newMessagesButton.getVisibility() == View.VISIBLE) {
                    AnimUtils.fadeThenGoneOrVisible(newMessagesButton, 1, 0, 250);
                }
            }
        });

        newMessagesButton = (TextView) findViewById(R.id.newMessagesButton);
        newMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollRecyclerToBottomWithAnimation();
            }
        });

        //for background state
        IntentFilter intentFilter = new IntentFilter(ApplicationStateManager.APPLICATION_PAUSED);
        intentFilter.addAction(ApplicationStateManager.APPLICATION_RESUMED);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverImplementation, intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(firstTime){
            //progress is visible, and it is showed in login method
            doNotShowProgressNow = true;
            boolean isInit = true;
            String lastMessageId = null;
            getMessages(isInit, lastMessageId);

            firstTime = false;
        }else{
            if(pausedForSocket){
                MessageRecyclerViewAdapter adapter = (MessageRecyclerViewAdapter) rvMessages.getAdapter();
                String lastMessageId = adapter.getNewestMessageId();
                getLatestMessages(lastMessageId);
            }
        }

        if (pausedForSocket) {
            SocketManager.getInstance().setListener(socketListener);
            SocketManager.getInstance().tryToReconnect(getActivity());

            pausedForSocket = false;
        }
        forceStaySocket = false;

    }

    @Override
    protected void onPause() {
        if (!forceStaySocket) {
            SocketManager.getInstance().closeAndDisconnectSocket();
            pausedForSocket = true;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SocketManager.getInstance().closeAndDisconnectSocket();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverImplementation);
        super.onDestroy();
    }

    protected OnMenuManageListener onMenuManagerListener = new OnMenuManageListener() {
        @Override
        public void onMenuOpened() {
            buttonType = ButtonType.MENU_OPENED;
        }

        @Override
        public void onMenuClosed() {
            buttonType = ButtonType.MENU;
            etMessage.setEnabled(true);
            findViewById(R.id.viewForMenuBehind).setVisibility(View.GONE);
        }
    };

    protected OnMenuButtonsListener onMenuButtonsListener = new OnMenuButtonsListener() {
        @Override
        public void onCameraClicked() {
            forceStaySocket = true;
            CameraPhotoPreviewActivity.starCameraPhotoPreviewActivity(getActivity());
            onButtonMenuOpenedClicked();
        }

        @Override
        public void onAudioClicked() {
            forceStaySocket = true;
            onButtonMenuOpenedClicked();
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PermissionCode.MICROPHONE);
            } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, Const.PermissionCode.MICROPHONE);
            } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PermissionCode.MICROPHONE);
            } else {
                RecordAudioActivity.starRecordAudioActivity(getActivity());
            }
        }

        @Override
        public void onFileClicked() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            forceStaySocket = true;
            startActivityForResult(intent, Const.RequestCode.PICK_FILE);
            onButtonMenuOpenedClicked();
        }

        @Override
        public void onVideoClicked() {
            forceStaySocket = true;
            RecordVideoActivity.starVideoPreviewActivity(getActivity());
            onButtonMenuOpenedClicked();
        }

        @Override
        public void onLocationClicked() {
            forceStaySocket = true;
            onButtonMenuOpenedClicked();
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Const.PermissionCode.LOCATION_MY);
            } else {
                LocationActivity.startLocationActivity(getActivity());
            }
        }

        @Override
        public void onGalleryClicked() {
            forceStaySocket = true;
            CameraPhotoPreviewActivity.starCameraFromGalleryPhotoPreviewActivity(getActivity());
            onButtonMenuOpenedClicked();
        }

        @Override
        public void onContactClicked() {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, Const.PermissionCode.READ_CONTACTS);
            } else {
                requestContacts();
            }
            onButtonMenuOpenedClicked();
        }
    };

    private AdapterView.OnItemClickListener onSettingItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                forceStaySocket = true;
                UsersInChatActivity.starUsersInChatActivity(getActivity(), activeUser.roomID);
            }
            hideSettings();
        }
    };

    protected TextWatcher etMessageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                animateSendButton(false);
            } else {
                animateSendButton(true);
            }
            sendTypingType(s.length());
        }
    };

    /**
     * login user api
     *
     * @param user user to login
     */
    private void login(User user) {
        handleProgress(true);
        LoginApi.Login spice = new LoginApi.Login(user, getActivity());

        getSpiceManager().execute(spice, new CustomSpiceListener<Login>(this) {

            @Override
            public void onRequestSuccess(Login result) {
                doNotHideProgressNow = true;
                super.onRequestSuccess(result);
                if (result.code == 1) {
                    SingletonLikeApp.getInstance().getSharedPreferences(getActivity()).setToken(result.data.token);
                    SingletonLikeApp.getInstance().getSharedPreferences(getActivity()).setUserId(result.data.token);

                    if (TextUtils.isEmpty(activeUser.avatarURL)) {
                        activeUser.avatarURL = result.data.user.avatarURL;
                    }
                    connectToSocket();
                }
            }
        });
    }

    /**
     * get messages from server
     *
     * @param isInit        true - initial call, false call on paging or on resume
     * @param lastMessageId id of last message (for paging can be null)
     */
    private void getMessages(final boolean isInit, final String lastMessageId) {
        handleProgress(true);
        MessagesApi.GetMessages spice = new MessagesApi.GetMessages(activeUser.roomID, lastMessageId, getActivity());

        getSpiceManager().execute(spice, new CustomSpiceListener<GetMessagesModel>(this) {

            @Override
            public void onRequestSuccess(GetMessagesModel result) {
                super.onRequestSuccess(result);
                if (result.code == 1) {
                    lastDataFromServer.clear();
                    lastDataFromServer.addAll(result.data.messages);
                    MessageRecyclerViewAdapter adapter = (MessageRecyclerViewAdapter) rvMessages.getAdapter();
                    if (isInit) {
                        adapter.clearMessages();
                        lastVisibleItem = result.data.messages.size();
                    }
                    boolean isPaging = !isInit;
                    adapter.addMessages(result.data.messages, isPaging);
//                    if (isInit) {
//                        scrollRecyclerToBottom();
//                    } else {
//                        int scrollToPosition = lastDataFromServer.size();
//                        scrollRecyclerToPosition(scrollToPosition);
//                    }

                    List<String> unReadMessages = SeenByUtils.getUnSeenMessages(result.data.messages, activeUser);
                    sendOpenMessage(unReadMessages);

                }
            }
        });
    }

    /**
     * get new messages
     *
     * @param lastMessageId id of newest message
     */
    private void getLatestMessages(final String lastMessageId) {
        MessagesApi.GetLatestMessages spice = new MessagesApi.GetLatestMessages(activeUser.roomID, lastMessageId, getActivity());

        boolean showErrorDialog = false;
        getSpiceManager().execute(spice, new CustomSpiceListener<GetMessagesModel>(this, showErrorDialog) {

            @Override
            public void onRequestSuccess(GetMessagesModel result) {
                super.onRequestSuccess(result);
                if (result.code == 1) {

                    if(result.data.messages.size() == 0){
                        return;
                    }

                    boolean toScrollBottom = false;
                    LinearLayoutManager llManager = (LinearLayoutManager) rvMessages.getLayoutManager();
                    if(llManager.findLastVisibleItemPosition() == rvMessages.getAdapter().getItemCount() - 1){
                        toScrollBottom = true;
                    }

                    MessageRecyclerViewAdapter adapter = (MessageRecyclerViewAdapter) rvMessages.getAdapter();
                    adapter.addLatestMessages(result.data.messages);
                    lastVisibleItem = adapter.getItemCount();

                    List<String> unReadMessages = SeenByUtils.getUnSeenMessages(result.data.messages, activeUser);
                    sendOpenMessage(unReadMessages);

                    if(toScrollBottom) {
                        scrollRecyclerToBottom();
                    }else{
                        if(newMessagesButton.getVisibility() == View.GONE){
                            AnimUtils.fadeThenGoneOrVisible(newMessagesButton, 0, 1, 250);
                        }
                    }

                }
            }
        });
    }

    protected MessageRecyclerViewAdapter.OnLastItemAndOnClickListener onLastItemAndClickItemListener = new MessageRecyclerViewAdapter.OnLastItemAndOnClickListener() {
        @Override
        public void onLastItem() {
            LogCS.e("LOG", "LAST ITEM");
            if (lastDataFromServer.size() < 50) {
                //no more paging
                LogCS.e("LOG", "NO MORE MESSAGES");
            } else {
                if (lastDataFromServer.size() > 0) {
                    String lastMessageId = lastDataFromServer.get(lastDataFromServer.size() - 1)._id;
                    boolean isInit = false;
                    getMessages(isInit, lastMessageId);
                }
            }
        }

        @Override
        public void onClickItem(final Message item) {
            if (item.deleted != -1 && item.deleted != 0) {
                return;
            }
            if (item.type == Const.MessageType.TYPE_FILE) {
                if (Tools.isMimeTypeImage(item.file.file.mimeType)) {
                    PreviewPhotoDialog.startDialog(getActivity(), Tools.getFileUrlFromId(item.file.file.id, getActivity()), item);
                } else if (Tools.isMimeTypeVideo(item.file.file.mimeType)) {
                    PreviewVideoDialog.startDialog(getActivity(), item.file);
                } else if (Tools.isMimeTypeAudio(item.file.file.mimeType)) {
                    PreviewAudioDialog.startDialog(getActivity(), item.file);
                } else {
                    downloadFile(item);
                }
            } else if (item.type == Const.MessageType.TYPE_LOCATION) {
                forceStaySocket = true;
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    tempLocationForPermission = item.location;
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Const.PermissionCode.LOCATION_THEIR);
                } else {
                    LocationActivity.startShowLocationActivity(getActivity(), item.location.lat, item.location.lng);
                }
            } else if (item.type == Const.MessageType.TYPE_CONTACT) {
                OpenDownloadedFile.selectedContactDialog(item.message, getActivity());
            } else {
                // do nothing for now
            }
        }

        @Override
        public void onLongClick(Message item) {
            boolean showDelete = true;
            if(!activeUser.userID.equals(item.user.userID) ||
                    item.type == Const.MessageType.TYPE_NEW_USER ||
                    item.type == Const.MessageType.TYPE_USER_LEAVE){

                showDelete = false;
            }

            boolean showCopy = true;
            if(item.type != Const.MessageType.TYPE_TEXT){
                showCopy = false;
            }

            boolean showShare = false;
            if(item.type == Const.MessageType.TYPE_FILE && Tools.isMimeTypeImage(item.file.file.mimeType)){
                showShare = true;
            }

            InfoMessageDialog.startDialogWithOptions(getActivity(), item, activeUser, showCopy, showDelete, showShare, new InfoMessageDialog.OnInfoListener() {
                @Override
                public void onDeleteMessage(Message message, Dialog dialog) {
                    confirmDeleteMessage(message);
                }

                @Override
                public void onDetailsClicked(Message message, Dialog dialog) {
                    openMessageInfoDialog(message);
                }

                @Override
                public void onShareClicked(Message message, Dialog dialog) {
                    handleProgress(true);
                    File file = new File(Tools.getImageFolderPath() + "/" + message.created + message.file.file.name);

                    if(file.exists()){
                        Tools.shareImage(getActivity(), file);
                    }else{
                        DownloadFileManager.downloadVideo(getActivity(), Tools.getFileUrlFromId(message.file.file.id, getActivity()), file, new DownloadFileManager.OnDownloadListener() {
                            @Override
                            public void onStart() {}

                            @Override
                            public void onSetMax(int max) {}

                            @Override
                            public void onProgress(int current) {}

                            @Override
                            public void onFinishDownload() {}

                            @Override
                            public void onResponse(boolean isSuccess, String path) {
                                Tools.shareImage(getActivity(), new File(path));
                            }
                        });
                    }
                }
            });

        }
    };

    private void openMessageInfoDialog(Message message) {
        PreviewMessageDialog.startDialog(getActivity(), message, activeUser);
    }

    private void confirmDeleteMessage(final Message message) {
        NotifyDialog dialog = NotifyDialog.startConfirm(getActivity(), getString(R.string.delete_message_title), getString(R.string.delete_message_text));
        dialog.setTwoButtonListener(new NotifyDialog.TwoButtonDialogListener() {
            @Override
            public void onOkClicked(NotifyDialog dialog) {
                dialog.dismiss();
                sendDeleteMessage(message._id);
            }

            @Override
            public void onCancelClicked(NotifyDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setButtonsText(getString(R.string.NO_CAPITAL), getString(R.string.YES_CAPITAL));
    }

    private void animateSendButton(final boolean toSend) {
        if (toSend && buttonType == ButtonType.SEND) {
            return;
        }
        if (toSend) {
            buttonType = ButtonType.SEND;
        } else {
            buttonType = ButtonType.MENU;
        }
        AnimUtils.fade(btnSend, 1, 0, 100, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (toSend) {
                    btnSend.setImageResource(R.drawable.send);
                } else {
                    btnSend.setImageResource(R.drawable.attach);
                }
                AnimUtils.fade(btnSend, 0, 1, 100, null);
            }
        });
    }

    protected void onSendMenuButtonClicked() {
        if (buttonType == ButtonType.MENU) {
            onButtonMenuClicked();
        } else if (buttonType == ButtonType.MENU_OPENED) {
            onButtonMenuOpenedClicked();
        } else if (buttonType == ButtonType.SEND) {
            onButtonSendClicked();
        }
    }

    private void onButtonMenuClicked() {
        if (buttonType == ButtonType.IN_ANIMATION) {
            return;
        }
        etMessage.setEnabled(false);
        buttonType = ButtonType.IN_ANIMATION;

        menuManager.openMenu(btnSend);
        findViewById(R.id.viewForMenuBehind).setVisibility(View.VISIBLE);
    }

    private void onButtonMenuOpenedClicked() {
        if (buttonType == ButtonType.IN_ANIMATION) {
            return;
        }
        buttonType = ButtonType.IN_ANIMATION;

        menuManager.closeMenu();
    }

    protected void onButtonSendClicked() {
        sendMessage();
    }

    private void showSettings() {
        settingsListView.setVisibility(View.VISIBLE);
        AnimUtils.fade(settingsListView, 0, 1, 300, null);
        findViewById(R.id.viewForSettingBehind).setVisibility(View.VISIBLE);
        AnimUtils.fade(findViewById(R.id.viewForSettingBehind), 0, 1, 300, null);
    }

    private void hideSettings() {
        AnimUtils.fade(settingsListView, 1, 0, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                settingsListView.setVisibility(View.INVISIBLE);
                findViewById(R.id.viewForSettingBehind).setVisibility(View.GONE);
            }
        });
        AnimUtils.fade(findViewById(R.id.viewForSettingBehind), 1, 0, 300, null);
    }

    protected void onSettingsClicked() {
        if (settingsListView.getVisibility() == View.VISIBLE) {
            hideSettings();
        } else {
            showSettings();
        }
    }

    private void scrollRecyclerToBottom() {
        rvMessages.scrollToPosition(rvMessages.getAdapter().getItemCount() - 1);
    }

    private void scrollRecyclerToBottomWithAnimation() {
        rvMessages.smoothScrollToPosition(rvMessages.getAdapter().getItemCount() - 1);
    }

    private void scrollRecyclerToPosition(int pos) {
        int offset = getResources().getDisplayMetrics().heightPixels;
        ((LinearLayoutManager) rvMessages.getLayoutManager()).scrollToPositionWithOffset(pos, 0);
    }

    //*********** send message to socket method

    /**
     * send message type text
     */
    protected void sendMessage() {

        Message message = new Message();
        message.fillMessageForSend(activeUser, etMessage.getText().toString(), Const.MessageType.TYPE_TEXT, null, null);

        etMessage.setText("");

        if(SocketManager.getInstance().isSocketConnect()){
            JSONObject emitMessage = EmitJsonCreator.createEmitSendMessage(message);
            SocketManager.getInstance().emitMessage(Const.EmitKeyWord.SEND_MESSAGE, emitMessage);
        }else{
            unSentMessageList.add(message);
        }

        onMessageSent(message);

    }

    /**
     * send message type file
     *
     * @param result upload file data
     */
    protected void sendFile(UploadFileResult result) {
        Message message = new Message();
        message.fillMessageForSend(activeUser, "", Const.MessageType.TYPE_FILE, result.data, null);

        etMessage.setText("");

        if(SocketManager.getInstance().isSocketConnect()){
            JSONObject emitMessage = EmitJsonCreator.createEmitSendMessage(message);
            SocketManager.getInstance().emitMessage(Const.EmitKeyWord.SEND_MESSAGE, emitMessage);
        }else{
            unSentMessageList.add(message);
        }

        onMessageSent(message);

    }

    /**
     * send message type location
     *
     * @param address location address
     * @param latLng  location latitude and longitude
     */
    private void sendLocation(String address, LatLng latLng) {

        Message message = new Message();
        message.fillMessageForSend(activeUser, address, Const.MessageType.TYPE_LOCATION, null, latLng);

        etMessage.setText("");

        if(SocketManager.getInstance().isSocketConnect()){
            JSONObject emitMessage = EmitJsonCreator.createEmitSendMessage(message);
            SocketManager.getInstance().emitMessage(Const.EmitKeyWord.SEND_MESSAGE, emitMessage);
        }else{
            unSentMessageList.add(message);
        }

        onMessageSent(message);

    }

    /**
     * send contact
     *
     * @param name            name of contact
     * @param vCardLikeString vCard in string format
     */
    protected void sendContact(String name, String vCardLikeString) {
        Message message = new Message();
        message.fillMessageForSend(activeUser, vCardLikeString, Const.MessageType.TYPE_CONTACT, null, null);

        etMessage.setText("");

        if(SocketManager.getInstance().isSocketConnect()){
            JSONObject emitMessage = EmitJsonCreator.createEmitSendMessage(message);
            SocketManager.getInstance().emitMessage(Const.EmitKeyWord.SEND_MESSAGE, emitMessage);
        }else{
            unSentMessageList.add(message);
        }

        onMessageSent(message);

    }

    private void loginWithSocket() {
        JSONObject emitLogin = EmitJsonCreator.createEmitLoginMessage(activeUser);
        SocketManager.getInstance().emitMessage(Const.EmitKeyWord.LOGIN, emitLogin);

        ChatActivity.this.sendUnSentMessages();
    }

    private void sendTypingType(int length) {
        if (length > 0 && typingType == TypingType.BLANK) {
            setTyping(Const.TypingStatus.TYPING_ON);
            typingType = TypingType.TYPING;
        } else if (length == 0 && typingType == TypingType.TYPING) {
            setTyping(Const.TypingStatus.TYPING_OFF);
            typingType = TypingType.BLANK;
        }
    }

    private void setTyping(int type) {
        JSONObject emitSendTyping = EmitJsonCreator.createEmitSendTypingMessage(activeUser, type);
        SocketManager.getInstance().emitMessage(Const.EmitKeyWord.SEND_TYPING, emitSendTyping);
    }

    private void sendOpenMessage(String messageId) {
        List<String> messagesIds = new ArrayList<>();
        messagesIds.add(messageId);
        sendOpenMessage(messagesIds);
    }

    private void sendOpenMessage(List<String> messagesIds) {
        JSONObject emitOpenMessage = EmitJsonCreator.createEmitOpenMessage(messagesIds, activeUser.userID);
        SocketManager.getInstance().emitMessage(Const.EmitKeyWord.OPEN_MESSAGE, emitOpenMessage);
    }

    protected void sendDeleteMessage(String messageId) {
        JSONObject emitDeleteMessage = EmitJsonCreator.createEmitDeleteMessage(activeUser.userID, messageId);
        SocketManager.getInstance().emitMessage(Const.EmitKeyWord.DELETE_MESSAGE, emitDeleteMessage);
    }
    //****************************************************

    //on received message from socket

    private void onUserLeft(User user) {
        if (typingUsers.contains(user)) {
            typingUsers.remove(user);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (typingUsers.size() < 1) {
                        tvTyping.setText(activeUser.userID);
                    } else {
                        generateTypingString();
                    }
                }
            });
        }
    }

    private void onMessageSent(Message sendMessage) {
        MessageRecyclerViewAdapter adapter = (MessageRecyclerViewAdapter) rvMessages.getAdapter();
        adapter.addSentMessage(sendMessage);
        sentMessages.add(sendMessage.localID);
        lastVisibleItem = adapter.getItemCount();
        scrollRecyclerToBottom();
    }

    private void onMessageReceived(final Message message) {
        final MessageRecyclerViewAdapter adapter = (MessageRecyclerViewAdapter) rvMessages.getAdapter();
        if (sentMessages.contains(message.localID)) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setDeliveredMessage(message);
                }
            });
            sentMessages.remove(message.localID);
        } else {
            message.status = Const.MessageStatus.RECEIVED;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    boolean toScrollBottom = false;
                    LinearLayoutManager llManager = (LinearLayoutManager) rvMessages.getLayoutManager();
                    if(llManager.findLastVisibleItemPosition() == rvMessages.getAdapter().getItemCount() - 1){
                        toScrollBottom = true;
                    }

                    adapter.addReceivedMessage(message);

                    if(toScrollBottom) {
                        scrollRecyclerToBottom();
                    }else{
                        if(newMessagesButton.getVisibility() == View.GONE){
                            AnimUtils.fadeThenGoneOrVisible(newMessagesButton, 0, 1, 250);
                        }
                    }
                }
            });
            if (!message.user.userID.equals(activeUser.userID)) {
                sendOpenMessage(message._id);
            }
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lastVisibleItem = rvMessages.getAdapter().getItemCount();
            }
        });
    }

    private void onMessagesUpdated(final List<Message> messages) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageRecyclerViewAdapter adapter = (MessageRecyclerViewAdapter) rvMessages.getAdapter();
                adapter.updateMessages(messages);
            }
        });

    }

    private void onSocketError(final int code) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotifyDialog dialog = NotifyDialog.startInfo(getActivity(), getString(R.string.error), ErrorHandle.getMessageForCode(code, getResources()));
            }
        });

    }

    private void onTyping(final SendTyping typing) {
        if (typing.user.userID.equals(activeUser.userID)) {
            return;
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (typing.type == Const.TypingStatus.TYPING_OFF) {

                    if (typingUsers.contains(typing.user)) {
                        typingUsers.remove(typing.user);
                    }

                    if (typingUsers.size() < 1) {
                        tvTyping.setText(activeUser.userID);
                    } else {
                        generateTypingString();
                    }
                } else {

                    if (typingUsers.contains(typing.user)) {
                        typingUsers.remove(typing.user);
                    }

                    typingUsers.add(typing.user);
                    generateTypingString();
                }
            }
        });
    }

    private void checkToRemoveUser(User user) {
        for (User item : typingUsers) {
            if (item.userID.equals(user.userID)) {
                typingUsers.remove(item);
                return;
            }
        }
    }

    //******************************************

    /**
     * connect to socket
     */
    private void connectToSocket() {

        SocketManager.getInstance().setListener(socketListener);
        SocketManager.getInstance().connectToSocket(getActivity());

    }

    private SocketManagerListener socketListener = new SocketManagerListener() {
        @Override
        public void onConnect() {
            LogCS.w("LOG", "CONNECTED TO SOCKET");
        }

        @Override
        public void onSocketFailed() {
            ChatActivity.this.socketFailedDialog();
        }

        @Override
        public void onNewUser(Object... args) {
            Log.w("LOG", "new user, args" + args[0].toString());
        }

        @Override
        public void onLoginWithSocket() {
            ChatActivity.this.loginWithSocket();
        }

        @Override
        public void onUserLeft(User user) {
            ChatActivity.this.onUserLeft(user);
        }

        @Override
        public void onTyping(SendTyping typing) {
            ChatActivity.this.onTyping(typing);
        }

        @Override
        public void onMessageReceived(Message message) {
            ChatActivity.this.onMessageReceived(message);
        }

        @Override
        public void onMessagesUpdated(List<Message> messages) {
            ChatActivity.this.onMessagesUpdated(messages);
        }

        @Override
        public void onSocketError(int code) {
            ChatActivity.this.onSocketError(code);
        }
    };

    private void generateTypingString() {
        String typingText = "";
        for (User item : typingUsers) {
            typingText = typingText + item.name + ", ";
        }
        typingText = typingText.substring(0, typingText.length() - 2);

        if (typingUsers.size() > 1) {
            tvTyping.setText(typingText + " " + getString(R.string.are_typing));
        } else {
            tvTyping.setText(typingText + " " + getString(R.string.is_typing));
        }
    }

    protected void noUserDialog() {
        NotifyDialog dialog = NotifyDialog.startInfo(this, getString(R.string.user_error_title), getString(R.string.user_error_not_sent));
        dialog.setOneButtonListener(new NotifyDialog.OneButtonDialogListener() {
            @Override
            public void onOkClicked(NotifyDialog dialog) {
                dialog.dismiss();
                finish();
            }
        });
    }

    protected void socketFailedDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotifyDialog dialog = NotifyDialog.startInfo(getActivity(), getString(R.string.socket_error_title), getString(R.string.socket_error_connect_failed));
                dialog.setOneButtonListener(new NotifyDialog.OneButtonDialogListener() {
                    @Override
                    public void onOkClicked(NotifyDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });
    }

    public void sendUnSentMessages(){
        for(Message item : unSentMessageList){
            JSONObject emitMessage = EmitJsonCreator.createEmitSendMessage(item);
            SocketManager.getInstance().emitMessage(Const.EmitKeyWord.SEND_MESSAGE, emitMessage);
        }
        unSentMessageList.clear();
    }

    @Override
    public void onBackPressed() {
        if (settingsListView.getVisibility() == View.VISIBLE) {
            hideSettings();
            return;
        }
        if (buttonType == ButtonType.MENU_OPENED) {
            onButtonMenuOpenedClicked();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.RequestCode.PHOTO_CHOOSE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras().containsKey(Const.Extras.UPLOAD_MODEL)) {
                    UploadFileResult model = data.getExtras().getParcelable(Const.Extras.UPLOAD_MODEL);
                    sendFile(model);
                }
            }
        } else if (requestCode == Const.RequestCode.PICK_FILE) {
            if (resultCode == RESULT_OK) {
                getFile(data);
            }
        } else if (requestCode == Const.RequestCode.VIDEO_CHOOSE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras().containsKey(Const.Extras.UPLOAD_MODEL)) {
                    UploadFileResult model = data.getExtras().getParcelable(Const.Extras.UPLOAD_MODEL);
                    sendFile(model);
                }
            }
        } else if (requestCode == Const.RequestCode.AUDIO_CHOOSE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras().containsKey(Const.Extras.UPLOAD_MODEL)) {
                    UploadFileResult model = data.getExtras().getParcelable(Const.Extras.UPLOAD_MODEL);
                    sendFile(model);
                }
            }
        } else if (requestCode == Const.RequestCode.LOCATION_CHOOSE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras().containsKey(Const.Extras.LATLNG)) {
                    String address = null;
                    if (data.getExtras().containsKey(Const.Extras.ADDRESS)) {
                        address = data.getExtras().getString(Const.Extras.ADDRESS);
                    }
                    LatLng latLng = data.getExtras().getParcelable(Const.Extras.LATLNG);
                    sendLocation(address, latLng);
                }
            }
        } else if (requestCode == Const.RequestCode.CONTACT_CHOOSE) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                try {
                    int nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    if (cursor.moveToFirst()) {
                        String name = cursor.getString(nameColumn);
                        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
                        AssetFileDescriptor fd;
                        fd = getContentResolver().openAssetFileDescriptor(uri, "r");
                        FileInputStream fis = fd.createInputStream();
                        byte[] b = new byte[(int) fd.getDeclaredLength()];
                        fis.read(b);
                        String vCard = new String(b);

                        sendContact(name, vCard);
                    } else {
                        NotifyDialog.startInfo(getActivity(), getString(R.string.contact_error_title), getString(R.string.contact_error_select));
                    }
                    cursor.close();
                } catch (Exception ex) {
                    cursor.close();
                    NotifyDialog.startInfo(getActivity(), getString(R.string.contact_error_title), getString(R.string.contact_error_select));
                }


            }
        }
    }

    //************** ui customization methods
    protected void changeToolbarColor(String color) {
        super.changeToolbarColor(color);
    }
    //******************************************

    //************** download and upload file

    private void getFile(Intent data) {
        Uri fileUri = data.getData();

        String fileName = null;
        String filePath = null;

        if (fileUri.getScheme().equals("content")) {

            String proj[];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                proj = new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME};
            } else {
                proj = new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME};
            }
            Cursor cursor = getContentResolver().query(fileUri, proj, null, null, null);
            cursor.moveToFirst();

            int column_index_name = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
            int column_index_path = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

            fileName = cursor.getString(column_index_name);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    new BuildTempFileAsync(this, fileName, new BuildTempFileAsync.OnTempFileCreatedListener() {
                        @Override
                        public void onTempFileCreated(String path, String name) {
                            if (TextUtils.isEmpty(path)) {
                                onFileSelected(RESULT_CANCELED, null, null);
                            } else {
                                onFileSelected(RESULT_OK, name, path);
                            }
                        }
                    }).execute(getContentResolver().openInputStream(fileUri));
                    // async task initialized, exit
                    return;
                } catch (FileNotFoundException ignored) {
                    filePath = "";
                }
            } else {
                filePath = cursor.getString(column_index_path);
            }

        } else if (fileUri.getScheme().equals("file")) {

            File file = new File(URI.create(fileUri.toString()));
            fileName = file.getName();
            filePath = file.getAbsolutePath();

            if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(filePath)) {
                onFileSelected(RESULT_OK, fileName, filePath);
            } else {
                onFileSelected(RESULT_CANCELED, null, null);
            }
        }

    }

    private void onFileSelected(int resultOk, String fileName, String filePath) {
        if (resultOk == RESULT_OK) {
            uploadFile(fileName, filePath);
        }
    }

    private void uploadFile(String fileName, String filePath) {

        String mimeType = Tools.getMimeType(filePath);
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = Const.ContentTypes.OTHER;
        }

        final UploadFileDialog dialog = UploadFileDialog.startDialog(getActivity());

        UploadFileManagement tt = new UploadFileManagement();
        tt.new BackgroundUploader(SingletonLikeApp.getInstance().getConfig(getActivity()).apiBaseUrl + Const.Api.UPLOAD_FILE, new File(filePath), mimeType, new UploadFileManagement.OnUploadResponse() {
            @Override
            public void onStart() {
                LogCS.d("LOG", "START UPLOADING");
            }

            @Override
            public void onSetMax(final int max) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setMax(max);
                    }
                });
            }

            @Override
            public void onProgress(final int current) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setCurrent(current);
                    }
                });
            }

            @Override
            public void onFinishUpload() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.fileUploaded();
                    }
                });
            }

            @Override
            public void onResponse(final boolean isSuccess, final String result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (!isSuccess) {
                            onResponseFailed();
                        } else {
                            onResponseFinish(result);
                        }
                    }
                });
            }
        }).execute();
    }

    private void onResponseFailed() {
        NotifyDialog.startInfo(getActivity(), getString(R.string.error), getString(R.string.file_not_found));
    }

    private void onResponseFinish(String result) {
        ObjectMapper mapper = new ObjectMapper();
        UploadFileResult data = null;
        try {
            data = mapper.readValue(result, UploadFileResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null) {
            sendFile(data);
        }
    }

    private void downloadFile(Message item) {

        File file = new File(Tools.getDownloadFolderPath() + "/" + item.created + item.file.file.name);

        if (file.exists()) {
            OpenDownloadedFile.downloadedFileDialog(file, getActivity());
        } else {

            final DownloadFileDialog dialog = DownloadFileDialog.startDialog(getActivity());

            DownloadFileManager.downloadVideo(getActivity(), Tools.getFileUrlFromId(item.file.file.id, getActivity()), file, new DownloadFileManager.OnDownloadListener() {
                @Override
                public void onStart() {
                    LogCS.d("LOG", "START UPLOADING");
                }

                @Override
                public void onSetMax(final int max) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setMax(max);
                        }
                    });
                }

                @Override
                public void onProgress(final int current) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setCurrent(current);
                        }
                    });
                }

                @Override
                public void onFinishDownload() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.fileDownloaded();
                        }
                    });
                }

                @Override
                public void onResponse(boolean isSuccess, final String path) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            OpenDownloadedFile.downloadedFileDialog(new File(path), getActivity());
                        }
                    });
                }
            });

        }

    }

    //************************************************

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Const.PermissionCode.CHAT_STORAGE: {
                if (grantResults.length > 0 && Tools.checkGrantResults(grantResults)) {
                } else {
                    finish();
                }
                return;
            }
            case Const.PermissionCode.READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestContacts();
                }
                return;
            }
            case Const.PermissionCode.LOCATION_MY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationActivity.startLocationActivity(getActivity());
                }
                return;
            }
            case Const.PermissionCode.LOCATION_THEIR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationActivity.startShowLocationActivity(getActivity(), tempLocationForPermission.lat, tempLocationForPermission.lng);
                }
                return;
            }
            case Const.PermissionCode.MICROPHONE: {
                if (grantResults.length > 0 && Tools.checkGrantResults(grantResults)) {
                    RecordAudioActivity.starRecordAudioActivity(getActivity());
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //************************************************

    public void requestContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        forceStaySocket = true;
        startActivityForResult(intent, Const.RequestCode.CONTACT_CHOOSE);
    }

    //******stuff for check if application is enter background
    private BroadcastReceiverImplementation broadcastReceiverImplementation = new BroadcastReceiverImplementation();
    private class BroadcastReceiverImplementation extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ApplicationStateManager.APPLICATION_PAUSED)) {
                LogCS.e("******* PAUSE *******");
                SocketManager.getInstance().closeAndDisconnectSocket();
                pausedForSocket = true;
            } else if (intent.getAction().equals(ApplicationStateManager.APPLICATION_RESUMED)) {
                LogCS.e("******* RESUMED *******" + getActivity().getClass().getName());
                if (pausedForSocket) {
                    SocketManager.getInstance().setListener(socketListener);
                    SocketManager.getInstance().tryToReconnect(getActivity());
                    pausedForSocket = false;
                }
            }
        }
    }
}
