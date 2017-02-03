package com.example.necozaki.mymemo;

import android.provider.BaseColumns;

/**
 * Created by necozaki on 2017/01/16.
 */

public final class MemoContract {

    public MemoContract(){}

    public static abstract class Memos implements BaseColumns{
        public static final String TABLE_NAME = "memos";
        public static final String COL_TITLE = "title";
        public static final String COL_BODY = "body";
        public static final String COL_CREATE = "created";
        public static final String COL_UPDATED = "updated";
    }
}
