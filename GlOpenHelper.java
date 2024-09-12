package com.example.oracleconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GlOpenHelper extends SQLiteOpenHelper {

        public SQLiteOpenHelper db;
        private GlOpenHelper dbHelper = null;

    /**
     *   DBバージョン
     */
    public static final int DATABASE_VERSION = 1;

        /**
         *  DB名：Master.db
         */
        public static final String DATABASE_NAME = "Master.db";

        /**
         *  テーブル名：Master_table
         *
         *  Master_column_01
         *  Master_column_02
         *  Master_column_03
         *  Master_column_04
         *  Master_column_05
         *  Master_column_06
         *  Master_column_07
         *  Master_column_08
         */
        public static final String MASTER_TB = "CREATE TABLE Master_table(Master_column_01,Master_column_02,Master_column_03," +
                "Master_column_04,Master_column_05, Master_column_06, Master_column_07,Master_column_08);";

        //------------------------ コンストラクタ -----------------------
        GlOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         *  【テーブル作成】
         * @param db The database.　
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            // === マスターテーブル　作成
            db.execSQL(MASTER_TB);
            System.out.println("MASTER_TB テーブル 作成完了");

        }

        /**
         *  【アップグレード】
         * @param db The database.
         * @param oldVersion The old database version.
         * @param newVersion The new database version.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // アップデート判別
            db.execSQL(MASTER_TB);
            System.out.println("MASTER_TB  テーブル アップグレード完了");

        }

}
