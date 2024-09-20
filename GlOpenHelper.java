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
     * テーブル名
     */
    public static final String TABLE_NAME = "Master_table";

    /**
     * // カラム名 一覧  　テーブル Master_table
     */
    public static final String COLUMN_00 = "Master_table_ID";
    public static final String COLUMN_01 = "Master_column_01";
    public static final String COLUMN_02 = "Master_column_02";
    public static final String COLUMN_03 = "Master_column_03";
    public static final String COLUMN_04 = "Master_column_04";
    public static final String COLUMN_05 = "Master_column_05";
    public static final String COLUMN_06 = "Master_column_06";
    public static final String COLUMN_07 = "Master_column_07";
    public static final String COLUMN_08 = "Master_column_08";
    public static final String COLUMN_09 = "Master_column_09";
    public static final String COLUMN_10 = "Master_column_10";
    public static final String COLUMN_11 = "Master_column_11";
    public static final String COLUMN_12 = "Master_column_12";

    /**
     * テーブル名：Master_table
     * <p>
     * Master_column_00    => ID（プライマリーキー）
     * Master_column_01    => 納入先Ｃ
     * Master_column_02    => 納入先名
     * Master_column_03    => 商品Ｃ
     * Master_column_04    => 品名
     * Master_column_05    => 出荷日（ユーザ選択）
     * Master_column_06    => 出荷数量
     * Master_column_07    => 作業数量（ユーザスキャン）
     * Master_column_08    => 作業状態フラグ , １：未作業 , ２：作業中 , ９：作業完了
     * Master_column_09    => YKK送り先コード , YKK北陸DC => 325952 , YKK首都圏DC => 315125, 315130, 315135, 315150
     * Master_column_10    => 品番
     * Master_column_11    => 作業開始日（端末）
     * Master_column_12    => 作業終了日（端末）
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_00 + " INTEGER  primary key," +
                    COLUMN_01 + " TEXT," +
                    COLUMN_02 + " TEXT," +
                    COLUMN_03 + " TEXT," +
                    COLUMN_04 + " TEXT," +
                    COLUMN_05 + " TEXT," +
                    COLUMN_06 + " TEXT," +
                    COLUMN_07 + " TEXT," +
                    COLUMN_08 + " TEXT," +
                    COLUMN_09 + " TEXT," +
                    COLUMN_10 + " TEXT," +
                    COLUMN_11 + " TEXT," +
                    COLUMN_12 + " TEXT)";

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
            db.execSQL(SQL_CREATE_ENTRIES);
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
            db.execSQL(SQL_CREATE_ENTRIES);
            System.out.println("MASTER_TB  テーブル アップグレード完了");

        }

}
