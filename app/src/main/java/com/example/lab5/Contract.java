package com.example.lab5;

import android.provider.BaseColumns;

public final class Contract {
    public Contract() {
    }
    public static final class GuestEntry implements BaseColumns {
        public final static String TABLE_NAME = "accounts";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FIRST_NAME = "first_name";
        public final static String COLUMN_LAST_NAME = "last_name";
        public final static String COLUMN_EMAIL = "email";
        public final static String COLUMN_ADDRESS = "address";

    }
}
