package com.clover_studio.spikachatmodule.utils;

import com.clover_studio.spikachatmodule.models.Message;

import java.util.Comparator;

/**
 * Created by ubuntu_ivo on 10.08.15..
 */
public class MessageSortByCreated implements Comparator<Message>{

    @Override
    public int compare(Message lhs, Message rhs) {

        long postedTime1 = lhs.created;
        long postedTime2 = rhs.created;

        if (postedTime1 > postedTime2) {
            return 1;
        } else if (postedTime1 < postedTime2) {
            return -1;
        }

        return 0;
    }

}
