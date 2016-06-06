package com.clover_studio.spikachatmodule.utils;

/**
 * Created by ubuntu_ivo on 21.07.15..
 */
public class Const {

    public static final class Api{
//        public static final String BASE_URL = "http://192.168.1.126:8080/v1/"; //test url
//        public static final String BASE_URL = "http://52.91.91.133/spika/v1/"; //production url
        public static final String BASE_URL = "http://ossdemo.spika.chat/spika/v1/"; //new server url
        public static final String USER_LOGIN = "user/login";
        public static final String USER_LIST = "user/list/{roomId}";
        public static final String MESSAGES = "message/list/{roomId}/{lastMessageId}";
        public static final String LATEST = "message/latest/{roomId}/{lastMessageId}";
        public static final String UPLOAD_FILE = "file/upload";
        public static final String DOWNLOAD_FILE = "file/download";
        public static final String STICKERS = "stickers";
    }

    public static final class Socket{
//        public static final String SOCKET_URL = "http://192.168.1.126:8080"; //production url
//        public static final String SOCKET_URL = "http://52.91.91.133/spika"; //test url
    public static final String SOCKET_URL = "http://ossdemo.spika.chat/spika"; //new server url
    }

    public static final class Extras{
        public static final String USER = "USER";
        public static final String CONFIG = "CONFIG";
        public static final String ROOM_ID = "ROOM_ID";
        public static final String TYPE_OF_PHOTO_INTENT = "TYPE_OF_PHOTO";
        public static final String UPLOAD_MODEL = "UPLOAD_MODEL";
        public static final String ADDRESS = "ADDRESS";
        public static final String LATLNG = "LATLNG";
        public static final String STICKERS = "STICKERS";
    }

    public static final class Preferences{
        public static final String TOKEN = "TOKEN";
        public static final String USER_ID = "USER_ID";
        public static final String SOCKET_URL = "SOCKET_URL";
        public static final String BASE_URL = "BASE_URL";
        public static final String STICKERS_COUNT = "STICKERS_COUNT7";
    }

    public static final class Params{
        public static final String TOKEN = "TOKEN";
        public static final String FILE = "file";
        public static final String ACCESS_TOKEN = "access-token";
        public static final String LINK_DATA = "linkData";
        public static final String ROOM_ID = "roomId";
        public static final String LAST_MESSAGE_ID = "lastMessageId";
    }

    public static final class AnimationDuration{
        public static final int MENU_BUTTON_ANIMATION_DURATION = 150;
        public static final int MENU_LAYOUT_ANIMATION_DURATION = 400;
        public static final int RECORDING_ANIMATION_DURATION = 1000;
    }

    public static final class EmitKeyWord{
        public static final String LOGIN = "login";
        public static final String SEND_MESSAGE = "sendMessage";
        public static final String NEW_USER = "newUser";
        public static final String NEW_MESSAGE = "newMessage";
        public static final String SEND_TYPING = "sendTyping";
        public static final String OPEN_MESSAGE = "openMessage";
        public static final String MESSAGE_UPDATED = "messageUpdated";
        public static final String DELETE_MESSAGE = "deleteMessage";
        public static final String USER_LEFT = "userLeft";
        public static final String SOCKET_ERROR = "socketerror";
    }

    public static final class MessageStatus{
        public static final int SENT = 1;
        public static final int DELIVERED = 2;
        public static final int RECEIVED = 0;
    }

    public static final class MessageType{
        public static final int TYPE_TEXT = 1;
        public static final int TYPE_FILE = 2;
        public static final int TYPE_LOCATION = 3;
        public static final int TYPE_CONTACT = 4;
        public static final int TYPE_STICKER = 5;
        public static final int TYPE_NEW_USER = 1000;
        public static final int TYPE_USER_LEAVE = 1001;
    }

    public static final class DateFormats{
        public static final String USER_JOINED_DATE_FORMAT = "yyyy/MM/dd kk:mm:ss";
    }

    public static final class TypingStatus{
        public static final int TYPING_ON = 1;
        public static final int TYPING_OFF = 0;
    }

    public static final class CacheFolder{
        public static final String APP_FOLDER = "Spika";
        public static final String LAZY_FOLDER = "lazy";
        public static final String VIDEO_FOLDER = "Video";
        public static final String IMAGE_FOLDER = "Image";
        public static final String DOWNLOAD_FOLDER = "Download";
        public static final String AUDIO_FOLDER = "Audio";
        public static final String TEMP_FOLDER = "temp";
    }

    public static final class FilesName{
        public static final String CAMERA_TEMP_FILE_NAME = "camera.jpg";
        public static final String AUDIO_TEMP_FILE_NAME = "voice.wav";
        public static final String VIDEO_TEMP_FILE_NAME = "video.mp4";
        public static final String SCALED_PREFIX = "scaled_";
        public static final String IMAGE_TEMP_FILE_NAME = "image_spika";
        public static final String TEMP_FILE_NAME = "temp.spika";
    }

    public static final class RequestCode{
        public static final int GALLERY = 1;
        public static final int CAMERA = 2;
        public static final int PHOTO_CHOOSE = 3;
        public static final int PICK_FILE = 4;
        public static final int VIDEO_CHOOSE = 5;
        public static final int AUDIO_CHOOSE = 6;
        public static final int LOCATION_CHOOSE = 7;
        public static final int CONTACT_CHOOSE = 8;
    }

    public static final class PhotoIntents{
        public static final int GALLERY = 1;
        public static final int CAMERA = 2;
    }

    public static final class ContentTypes{
        public static final String IMAGE_JPG = "image/jpeg";
        public static final String IMAGE_PNG = "image/png";
        public static final String IMAGE_GIF = "image/gif";
        public static final String VIDEO_MP4 = "video/mp4";
        public static final String AUDIO_WAV = "audio/wav";
        public static final String AUDIO_MP3 = "audio/mp3";
        public static final String OTHER = "application/octet-stream";
    }

    public static final class ChildrenInFileLayout{
        public static final int ICON = 0;
        public static final int FILE_NAME = 1;
        public static final int FILE_SIZE = 2;
        public static final int FILE_DOWNLOAD = 3;
        public static final int INFO = 4;
    }

    public static final class Video{
        public static final int MAX_RECORDING_VIDEO_TIME = 60;  //seconds
        public static final int MAX_RECORDING_AUDIO_TIME = 300000;  //milisecond
    }

    public static final class IntConst{
        public static final int MAX_PIXELS_FOR_BITMAP_IN_LOADER = 12250000;  //3500 * 3500
        public static final int MAX_MB_FOR_BITMAP_IN_LOADER = 2097152;   // 2MB
        public static final int SCALE_IMAGE_IN_LOADER_IN_PIXELS = 4000000;  // 2000 * 2000
    }

    public static final class ContactData{
        public static final int NAME = 0;
        public static final int PHONE = 1;
        public static final int EMAIL = 2;
    }

    public static final class PermissionCode{
        public static final int CHAT_STORAGE = 1;
        public static final int MICROPHONE = 2;
        public static final int LOCATION_THEIR = 3;
        public static final int LOCATION_MY = 4;
        public static final int READ_CONTACTS = 5;
    }

    public static final class ErrorCodes{
        public static final int RESPONSE_CODE_SUCCEED = 1;

        public static final int ERROR_CODE_LOGIN_NO_NAME = 1000001;
        public static final int ERROR_CODE_LOGIN_NO_ROOM_ID = 1000002;
        public static final int ERROR_CODE_LOGIN_NO_USER_ID = 1000003;
        public static final int ERROR_CODE_USER_LIST_NO_ROOM_ID = 1000004;
        public static final int ERROR_CODE_MESSAGE_LIST_NO_ROOM_ID = 1000005;
        public static final int ERROR_CODE_MESSAGE_LIST_NO_LAST_MESSAGE_ID = 1000006;
        public static final int ERROR_CODE_SEND_MESSAGE_NO_FILE = 1000007;
        public static final int ERROR_CODE_SEND_MESSAGE_NO_ROOM_ID = 1000008;
        public static final int ERROR_CODE_SEND_MESSAGE_NO_USER_ID = 1000009;
        public static final int ERROR_CODE_SEND_MESSAGE_NO_TYPE = 1000010;
        public static final int ERROR_CODE_FILE_UPLOAD_NO_FILE = 1000011;
        public static final int ERROR_CODE_SOCKET_UNKNOWN_ERROR = 1000012;
        public static final int ERROR_CODE_SOCKET_DELETE_MESSAGE_NO_USER_ID = 1000013;
        public static final int ERROR_CODE_SOCKET_DELETE_MESSAGE_NO_MESSAGE_ID = 1000014;
        public static final int ERROR_CODE_SOCKET_SEND_MESSAGE_NO_ROOM_ID = 1000015;
        public static final int ERROR_CODE_SOCKET_SEND_MESSAGE_NO_USER_ID = 1000016;
        public static final int ERROR_CODE_SOCKET_SEND_MESSAGE_NO_TYPE = 1000017;
        public static final int ERROR_CODE_SOCKET_SEND_MESSAGE_NO_MESSAGE = 1000018;
        public static final int ERROR_CODE_SOCKET_SEND_MESSAGE_NO_LOCATION = 1000019;
        public static final int ERROR_CODE_SOCKET_SEND_MESSAGE_FAILED = 1000020;
        public static final int ERROR_CODE_SOCKET_TYPING_NO_USER_ID = 1000021;
        public static final int ERROR_CODE_SOCKET_TYPING_NO_ROOM_ID = 1000022;
        public static final int ERROR_CODE_SOCKET_TYPING_NO_TYPE = 1000023;
        public static final int ERROR_CODE_SOCKET_TYPING_FAILED = 1000024;
        public static final int ERROR_CODE_SOCKET_LOGIN_NO_USER_ID = 1000025;
        public static final int ERROR_CODE_SOCKET_LOGIN_NO_ROOM_ID = 1000026;
    }
}
