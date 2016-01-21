package com.clover_studio.spikachatmodule.utils;

import android.content.res.Resources;

import com.clover_studio.spikachatmodule.R;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class ErrorHandle {

    /**
     * checked if android version is above given version
     *
     * @param errorCode code of error
     * @param res code of error
     * @return message to show
     */
    public static String getMessageForCode(int errorCode, Resources res) {
        switch (errorCode) {
            case Const.ErrorCodes.ERROR_CODE_LOGIN_NO_NAME:
                return res.getString(R.string.error_login_no_name);
            case Const.ErrorCodes.ERROR_CODE_LOGIN_NO_ROOM_ID:
                return res.getString(R.string.error_login_no_room_id);
            case Const.ErrorCodes.ERROR_CODE_LOGIN_NO_USER_ID:
                return res.getString(R.string.error_login_no_user_id);
            case Const.ErrorCodes.ERROR_CODE_USER_LIST_NO_ROOM_ID:
                return res.getString(R.string.error_user_list_no_room_id);
            case Const.ErrorCodes.ERROR_CODE_MESSAGE_LIST_NO_ROOM_ID:
                return res.getString(R.string.error_message_list_no_room_id);
            case Const.ErrorCodes.ERROR_CODE_MESSAGE_LIST_NO_LAST_MESSAGE_ID:
                return res.getString(R.string.error_message_list_no_last_message_id);
            case Const.ErrorCodes.ERROR_CODE_SEND_MESSAGE_NO_FILE:
                return res.getString(R.string.error_send_message_no_file);
            case Const.ErrorCodes.ERROR_CODE_SEND_MESSAGE_NO_ROOM_ID:
                return res.getString(R.string.error_send_message_no_room_id);
            case Const.ErrorCodes.ERROR_CODE_SEND_MESSAGE_NO_USER_ID:
                return res.getString(R.string.error_send_message_no_user_id);
            case Const.ErrorCodes.ERROR_CODE_SEND_MESSAGE_NO_TYPE:
                return res.getString(R.string.error_send_message_no_type);
            case Const.ErrorCodes.ERROR_CODE_FILE_UPLOAD_NO_FILE:
                return res.getString(R.string.error_file_upload_no_file);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_UNKNOWN_ERROR:
                return res.getString(R.string.error_socket_unknown_error);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_DELETE_MESSAGE_NO_USER_ID:
                return res.getString(R.string.error_socket_delete_message_no_user_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_DELETE_MESSAGE_NO_MESSAGE_ID:
                return res.getString(R.string.error_socket_delete_message_no_message_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_SEND_MESSAGE_NO_ROOM_ID:
                return res.getString(R.string.error_socket_send_message_no_room_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_SEND_MESSAGE_NO_USER_ID:
                return res.getString(R.string.error_socket_send_message_no_user_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_SEND_MESSAGE_NO_TYPE:
                return res.getString(R.string.error_socket_send_message_no_type);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_SEND_MESSAGE_NO_MESSAGE:
                return res.getString(R.string.error_socket_send_message_no_message);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_SEND_MESSAGE_NO_LOCATION:
                return res.getString(R.string.error_socket_send_message_no_location);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_SEND_MESSAGE_FAILED:
                return res.getString(R.string.error_socket_send_message_fail);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_TYPING_NO_USER_ID:
                return res.getString(R.string.error_socket_typing_no_user_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_TYPING_NO_ROOM_ID:
                return res.getString(R.string.error_socket_typing_no_room_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_TYPING_NO_TYPE:
                return res.getString(R.string.error_socket_typing_no_type);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_TYPING_FAILED:
                return res.getString(R.string.error_socket_typing_failed);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_LOGIN_NO_USER_ID:
                return res.getString(R.string.error_socket_login_no_user_id);
            case Const.ErrorCodes.ERROR_CODE_SOCKET_LOGIN_NO_ROOM_ID:
                return res.getString(R.string.error_socket_login_no_room_id);
            default:
                return res.getString(R.string.error_connection_error);

        }
    }

}
