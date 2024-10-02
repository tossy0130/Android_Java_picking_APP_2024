package com.example.oracleconnect;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstActivity extends AppCompatActivity {

    // === secondactivity
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1002;
    // TextView
    private TextView date_view_top, app_title_01, sagyou_sentaku_view;

    // button
    private Button sagyou_btn, hokuriku_btn, syutoken_btn, pikking_start_btn, app_end_btn, ykk_data_btn_01,data_reset_btn,list_view_btn;

    // String
    private String get_date_str, get_year, get_month, get_day, Now_date_str;

    private boolean isHokurikuSelected = false;
    private boolean isSyutokenSelected = false;

    private String Get_Sagyou_Day;

    /**
     * DB 接続用
     */
    private GlOpenHelper helper;
    private SQLiteDatabase db;

    /**
     * Oracle 接続用
     */
    private ConnectionClass connectionClass;
    private Connection con;
    private String str, name;

    /**
     *  YKK データダウンロード用　フラグ
     */
    private boolean YkkDataFlg = false;

    /**
     *  「北陸」「首都圏DC」
     */
    private String YKK_Code_Hokuriku = "";
    private String[] YKK_Code_Syutoken = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /**
         *   Oracle　接続設定
         */
        connectionClass = new ConnectionClass();
        connect();

        /**
         *  初期設定 function init()
         */
        init();

        // =============================================================================================
        // ================================= ボタン　イベント処理　・無名関数　一覧　開始 =====================
        // =============================================================================================

        /**
         *  「作業日　選択　指定　ボタン」
         */
        sagyou_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //****************** 年　月　日
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(FirstActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                //------- 日付　格納
                                get_year = String.format("%02d", year); //yyyy
                                get_month = String.format("%02d", monthOfYear + 1); // MM
                                get_day = String.format("%02d", dayOfMonth); // dd

                                Now_date_str = get_year + get_month + get_day;

                                // Setした日付を取得する
                                sagyou_sentaku_view.setText(String.format(Locale.US, "%02d年%02d月%02d日", year, monthOfYear + 1, dayOfMonth));

                                System.out.println("作業日選択：" + sagyou_sentaku_view.getText().toString());
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        }); // ================== DatePickerDialog datePickerDialog  END =============>

        /**
         *  YKK データダウンロードボタン
         */
        ykk_data_btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 「ダウンロード日を選択」の値の日付を取得
                String sagyou_sentaku_view_STR = sagyou_sentaku_view.getText().toString();
                System.out.println("作業日 選択 値:::" + sagyou_sentaku_view_STR);
                if (!(sagyou_sentaku_view_STR.equals(""))) {

                    /**
                     *  YKK データアラートダイアログ　出現
                     */
                    YkkBtnDataDialog();

                    System.out.println("ダウンロード日が選択 OK");
                    return;

                } else {

                    CustomDialog Erro_dialog_data = new CustomDialog(FirstActivity.this);
                    Erro_dialog_data.showDialog_Error(
                            "作業日選択エラー",
                            "ダウンロード日が選択されていません。",
                            "閉じる"
                    );

                    System.out.println("ダウンロード日が選択されていません");
                    return;
                }

            }
        });

        /**
         *  「北陸 ボタン」
         */
        hokuriku_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 北陸ボタンが選択されたとき
                if (!isHokurikuSelected) {
                    hokuriku_btn.setAlpha(0.4f); // 透過度を下げる
                    hokuriku_btn.setTextColor(Color.parseColor("#000000")); // フォント色を変更

                    // 首都圏ボタンの状態をリセット
                    syutoken_btn.setAlpha(1.0f); // 透過度を元に戻す
                    syutoken_btn.setTextColor(Color.parseColor("#FFFFFF")); // フォント色を元に戻す

                    // 状態を更新
                    isHokurikuSelected = true;
                    isSyutokenSelected = false;

                    // YKK コード
                    YKK_Code_Hokuriku = "325952";

                    // YKK コード 首都圏
                    YKK_Code_Syutoken[0] = "";
                    YKK_Code_Syutoken[1] = "";
                    YKK_Code_Syutoken[2] = "";
                    YKK_Code_Syutoken[3] = "";

                    System.out.println("選択:北陸:::" + YKK_Code_Hokuriku.toString());

                } else {
                    // 北陸ボタンが再度押されたとき、元の色に戻す
                    hokuriku_btn.setAlpha(1.0f);
                    hokuriku_btn.setTextColor(Color.parseColor("#FFFFFF"));

                    isHokurikuSelected = false;

                    // YKK コード
                    YKK_Code_Hokuriku = "";
                }
            }
        }); // ================================================ 「北陸 ボタン」 END

        /**
         *  「首都圏 ボタン」
         */
        syutoken_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首都圏ボタンが選択されたとき
                if (!isSyutokenSelected) {
                    syutoken_btn.setAlpha(0.4f); // 透過度を下げる
                    syutoken_btn.setTextColor(Color.parseColor("#000000")); // フォント色を変更

                    // 北陸ボタンの状態をリセット
                    hokuriku_btn.setAlpha(1.0f); // 透過度を元に戻す
                    hokuriku_btn.setTextColor(Color.parseColor("#FFFFFF")); // フォント色を元に戻す

                    // 状態を更新
                    isSyutokenSelected = true;
                    isHokurikuSelected = false;

                    // YKK コード 北陸
                    YKK_Code_Hokuriku = "";

                    // YKK コード 首都圏
                    YKK_Code_Syutoken[0] = "315125";
                    YKK_Code_Syutoken[1] = "315130";
                    YKK_Code_Syutoken[2] = "315135";
                    YKK_Code_Syutoken[3] = "315150";

                    System.out.println("選択:首都圏:::" + YKK_Code_Syutoken.toString());

                } else {
                    // 首都圏ボタンが再度押されたとき、元の色に戻す
                    syutoken_btn.setAlpha(1.0f);
                    syutoken_btn.setTextColor(Color.parseColor("#FFFFFF"));

                    isSyutokenSelected = false;

                    // YKK コード 首都圏
                    YKK_Code_Syutoken[0] = "";
                    YKK_Code_Syutoken[1] = "";
                    YKK_Code_Syutoken[2] = "";
                    YKK_Code_Syutoken[3] = "";

                }
            }
        });  // ================================================ 「首都圏 ボタン」 END

        /**
         *   「ピッキング開始　ボタン」　
         */
        pikking_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSyutokenSelected == false && isHokurikuSelected == false) {

                    CustomDialog Erro_dialog_select = new CustomDialog(FirstActivity.this);
                    Erro_dialog_select.showDialog_Error(
                            "出荷先選択エラー",
                            "「北陸」か「首都圏」どちらかを選択してください。",
                            "閉じる"
                    );

                } else {
                    // === QR 起動
                    Intent intent = new Intent(getApplication(), SecondActivity.class);

                    intent.putExtra("YKK_Code_Hokuriku", YKK_Code_Hokuriku); // YKK 北陸
                    intent.putExtra("YKK_Code_Syutoken", YKK_Code_Syutoken); // YKK 首都圏

                    // SecondActivity を起動して結果を待つ
                    startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
                }

            }
        }); // ================================================ 「ピッキング開始 ボタン」 END

        /**
         *  「終了」ボタン
         */
        app_end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // アラート表示
                CustomDialog dialog = new CustomDialog(FirstActivity.this);
                // ダイアログを表示
                dialog.showDialog_APP_END(
                        "アプリケーションの終了",       // タイトル
                        "お疲れ様でした。アプリケーションを終了しますか？",  // メッセージ
                        "終了しない",                    // ポジティブボタンのテキスト
                        "終了する"                      // ネガティブボタンのテキスト
                );
            }
        }); // ================================================ 「終了 ボタン」 END

        /**
         *  データ削除
         */
        data_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog dialog_Data_DELETE = new CustomDialog(FirstActivity.this);
                dialog_Data_DELETE.showDialog_Data_DELETE(
                        "作業データ確認",
                        "データを削除しますか？",
                        "いいえ",
                        "はい"
                );
            }
        });

        /**
         * データ一覧　ボタン
         */
        list_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),SelectList.class);
                startActivity(intent);

            }
        });


        /**
         *  コールバック処理
         */
        // 端末 「戻るボタン」制御
        OnBackPressedCallback callback_app_finish = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // アラート表示
                CustomDialog dialog = new CustomDialog(FirstActivity.this);
                // ダイアログを表示
                dialog.showDialog_APP_END(
                        "アプリケーションの終了",       // タイトル
                        "お疲れ様でした。アプリケーションを終了しますか？",  // メッセージ
                        "終了しない",                    // ポジティブボタンのテキスト
                        "終了する"                      // ネガティブボタンのテキスト
                );
            }
        };

        // コールバックを登録
        getOnBackPressedDispatcher().addCallback(this, callback_app_finish);

    } // ========================================================== END OnCreate


    /**
     * 初期設定
     */
    private void init() {

        /**
         *  DB , テーブル作成 function : Create_Master_DB()
         */
        Create_Master_DB();

        // 時刻表示
        date_view_top = (TextView) findViewById(R.id.date_view_top);
        // アプリtitle　表示
        app_title_01 = findViewById(R.id.app_title_01);

        // YKK データダウンロードボタン
        ykk_data_btn_01 = findViewById(R.id.ykk_data_btn_01);
        // 北陸ボタン
        hokuriku_btn = findViewById(R.id.hokuriku_btn);
        // 首都圏ボタン
        syutoken_btn = findViewById(R.id.syutoken_btn);
        // ピッキング開始　ボタン
        pikking_start_btn = findViewById(R.id.pikking_start_btn);
        // 終了 ボタン
        app_end_btn = findViewById(R.id.app_end_btn);
        // データ削除　ボタン
        data_reset_btn = findViewById(R.id.data_reset_btn);
        // データ一覧　ボタン
        list_view_btn = findViewById(R.id.list_view_btn);

        //　＊＊＊　日付設定 表示
        get_date_str = getNowDate();
        get_year = get_date_str.substring(0, 4); //yyyy
        get_month = get_date_str.substring(4, 6); // MM
        get_day = get_date_str.substring(6, 8); // dd

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        date_view_top.setText(get_year + "年" + get_month + "月" + get_day + "日" + '(' + dayOfTheWeek + ')');
        //　＊＊＊　日付設定 表示 END

        //------------ アプリ タイトル ------------ Start
        String title_01 = "YKK社 出荷商品検索システム";
        app_title_01.setText(title_01);
        //------------ アプリ タイトル ------------ END

        // 作業日　選択ボタン
        sagyou_btn = findViewById(R.id.sagyou_btn);
        // 作業日　表示
        sagyou_sentaku_view = findViewById(R.id.sagyou_sentaku_view);

        // YKKデータ　ダウンロード用フラグ
        YkkDataFlg = false;
    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する。
     */
    private static String getNowDate() {

        final DateFormat df = new SimpleDateFormat("yyyyMMddHH");
        final Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    /**
     * 作業開始日
     */
    private static String getNowDate_Sagyou_Start() {
        // DateFormatを修正して、"yyyy-MM-dd HH:mm.ss"のフォーマットを適用
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm::ss");
        final Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }


    /**
     * DB作成 (Master.db) AND テーブル作成 (Master_table)
     */
    private void Create_Master_DB() {
        if (helper == null) {
            // Master.db の作成
            System.out.println("Master用 DB　データベース作成　完了");
            helper = new GlOpenHelper(getApplicationContext());
        } else {
            System.out.println("Master用 DB　データベース作成　済み");
            return;
        }

        // DB が作成されていなければ作成
        if (db == null) {
            db = helper.getWritableDatabase();
            System.out.println("Master用 DB　データベース作成　完了");

        } else {
            System.out.println("Master用 DB　データベース作成　済み");
            return;
        }

    } // ===================== END function

    public void Select_Download() {

        // === インサート用　オブジェクト
        GlOpenHelper helper_insert = new GlOpenHelper(getApplicationContext());
        SQLiteDatabase db_insert = helper_insert.getReadableDatabase();

        //----------- 削除処理 ------------
        // トランザクション 開始 ------>
        db_insert.beginTransaction();
        try {
            db_insert.delete(GlOpenHelper.TABLE_NAME, null, null);
            // トランザクション成功処理
            db_insert.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            //----------- トランザクション　完了
            db_insert.endTransaction();
        }


        String Row_Data[] = new String[12];

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            // === トランザクション開始
            db_insert.beginTransaction();

            try {

                con = connectionClass.Conn();
                String query = "";

                ResultSet rs = null;
                // 「北陸」

                System.out.println("出荷先フラグ:::" + isHokurikuSelected);

                // 作業日　取得
                Get_Sagyou_Day = sagyou_sentaku_view.getText().toString();

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年MM月dd日");
                // 変換後の日付形式を指定する（yyyy-MM-dd）
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = inputFormat.parse(Get_Sagyou_Day);
                String outputDate_Sagyou_Day = outputFormat.format(date);

                System.out.println("バインド　作業日:::" + outputDate_Sagyou_Day);

                query = "SELECT SK.伝票ＳＥＱ,SK.伝票行番号,SK.商品Ｃ,SK.品名,SH.品番,SK.納入先Ｃ, SK.納入先名,SUM(SL.数量) AS 数量01,SJ.出荷日\n" +
                        "                        FROM SJTR SJ\n" +
                        "                        JOIN SKTR SK ON SJ.伝票ＳＥＱ = SK.出荷ＳＥＱ\n" +
                        "                        JOIN SLTR SL ON SK.伝票ＳＥＱ = SL.伝票ＳＥＱ AND SK.伝票行番号 = SL.伝票行番号\n" +
                        "                        JOIN SHMF SH ON SL.商品Ｃ = SH.商品Ｃ\n" +
                        "                        WHERE SK.出荷日 = ?\n" +
                        "                        AND SK.納入先Ｃ = ? OR SK.納入先Ｃ = ?\n" +
                        "                        GROUP BY SK.伝票ＳＥＱ,SK.伝票行番号,SK.商品Ｃ,SK.品名,SH.品番,SK.納入先Ｃ, SK.納入先名, SJ.出荷日\n" +
                        "                        ORDER BY SK.商品Ｃ ASC";

                PreparedStatement stmt = (PreparedStatement) con.prepareStatement(query);
                // バインド
                stmt.setString(1, outputDate_Sagyou_Day);
                stmt.setString(2,"013370");   // 納入先Ｃ => 北陸
                stmt.setString(3,"013371");   // 納入先Ｃ => 首都圏
                rs = stmt.executeQuery();

                // === 作業日取得
                String date_view_top_STR = date_view_top.getText().toString();

                int idx = 1;
                StringBuilder bStr = new StringBuilder("伝票ＳＥＱ,伝票行番号,商品Ｃ,品名,納入先Ｃ,納入先名,出荷日,SUM(SL.数量) AS 数量01\n");
                while (rs.next()) {
                    bStr.append(rs.getString("伝票ＳＥＱ")).append(",");
                    bStr.append(rs.getString("伝票行番号")).append(",");
                    bStr.append(rs.getString("商品Ｃ")).append(",");
                    bStr.append(rs.getString("品名")).append(",");
                    bStr.append(rs.getString("納入先Ｃ")).append(",");
                    bStr.append(rs.getString("納入先名")).append(",");
                    bStr.append(rs.getString("出荷日")).append(",");
                    bStr.append(rs.getString("数量01")).append("\n");

                    // === 値挿入
                    ContentValues values = new ContentValues();

                    // 【納入先Ｃ】
                    Row_Data[0] = rs.getString("納入先Ｃ");
                    values.put(GlOpenHelper.COLUMN_01, Row_Data[0]);
                    // 【納入先名】
                    Row_Data[1] = rs.getString("納入先名");
                    values.put(GlOpenHelper.COLUMN_02, Row_Data[1]);
                    // 【商品Ｃ】
                    Row_Data[2] = rs.getString("商品Ｃ");
                    values.put(GlOpenHelper.COLUMN_03, Row_Data[2]);
                    // 【品名】
                    Row_Data[3] = rs.getString("品名");
                    values.put(GlOpenHelper.COLUMN_04, Row_Data[3]);
                    // 【出荷日（ユーザ選択）】
                    Row_Data[4] = outputDate_Sagyou_Day;
                    values.put(GlOpenHelper.COLUMN_05, Row_Data[4]);
                    // 【出荷数量】
                    Row_Data[5] = rs.getString("数量01");
                    values.put(GlOpenHelper.COLUMN_06, Row_Data[5]);
                    // 【作業数量（ユーザスキャン）】
                    Row_Data[6] = "0";
                    values.put(GlOpenHelper.COLUMN_07, Row_Data[6]);
                    // 【作業状態フラグ】　１：未作業 , ２：作業中 , ９：作業完了
                    Row_Data[7] = "1";
                    values.put(GlOpenHelper.COLUMN_08, Row_Data[7]);

                    // 【YKK送り先コード】 YKK北陸DC => 325952 , YKK首都圏DC => 315125, 315130, 315135, 315150
                    Row_Data[8] = "";
                    values.put(GlOpenHelper.COLUMN_09, Row_Data[8]);

                    // 【品番】
                    Row_Data[9] = rs.getString("品番");
                    values.put(GlOpenHelper.COLUMN_10, Row_Data[9]);

                    // 作業開始日時
                    String getNowDate_Sagyou_Start_STR = getNowDate_Sagyou_Start();
                    Row_Data[10] = getNowDate_Sagyou_Start_STR;
                    values.put(GlOpenHelper.COLUMN_11, Row_Data[10]);

                    // 作業終了日時
                    Row_Data[11] = "";
                    values.put(GlOpenHelper.COLUMN_12, Row_Data[11]);


                    //******* インサート処理
                    db_insert.insert(GlOpenHelper.TABLE_NAME, null, values);

                    //******* インサート済み　ログ　出力
                    System.out.println("インサートデータ:" + idx + ":::" +
                            Row_Data[0] + "," + Row_Data[1] + ","  + Row_Data[2] + "," + Row_Data[3] + "," +
                            Row_Data[4] + "," + Row_Data[5] + ","  + Row_Data[6] + "," + Row_Data[7] + "," +
                            Row_Data[8] + "," + Row_Data[9] + ","  + Row_Data[10] + ","  + Row_Data[11]
                    );
                    // ログ用
                    idx += 1;
                }
                name = bStr.toString();

                // トランザクション OK
                db_insert.setTransactionSuccessful();

                YkkDataFlg = true;

            } catch (Exception e) {
                YkkDataFlg = false;
                throw new RuntimeException(e);
            } finally {
                // トランザクション　完了　閉じる
                db_insert.endTransaction();
            }

            runOnUiThread(() -> {
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //         TextView txtList = findViewById(R.id.textview);
                //         txtList.setText(name);

                if(YkkDataFlg) {
                    CustomDialog DownloadShow = new CustomDialog(FirstActivity.this);
                    DownloadShow.showDialogDefaultOk("ダウンロード完了",
                            Get_Sagyou_Day +
                                    "のデータダウロードが完了しました。" ,
                            "閉じる");
                } else {
                    CustomDialog DownloadShowNg = new CustomDialog(FirstActivity.this);
                    DownloadShowNg.showDialogDefaultNg("ダウンロード失敗",
                            Get_Sagyou_Day +
                                    "のデータダウロードに失敗しました。" ,
                            "閉じる");
                }

            });
        });
    }  // ============================= END Select

    public void connect() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = connectionClass.Conn();
                if (con == null) {
                    str = "Oracle コネクト error";
                } else {
                    str = "Connect 接続OK";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            runOnUiThread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                }
            });
        });
    } // ============================= END connect

    /**
     *   アクティビティ　バック処理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ===  SecondActivity　から戻ってきた場合
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY) {
            if (resultCode == RESULT_CANCELED && data != null) {
                // SecondActivityからのエラーメッセージを受け取る
                String errorMessage = data.getStringExtra("error_message_back");

                // QR を読み取らず戻るボタンを押した場合
                String error_message_back_QR_nullStr = data.getStringExtra("error_message_back_QR_null");
                if (errorMessage != null) {
                    // エラーメッセージを表示 (例: トーストメッセージ)

                    // ************* アラートダイアログを出す **************
                    //   Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    CustomDialog Erro_dialog_data_ScanData = new CustomDialog(FirstActivity.this);
                    Erro_dialog_data_ScanData.showDialog_Error(
                            "QR スキャンデータエラー",
                            "スキャンしたデータはありません。",
                            "閉じる"
                    );

                    System.out.println("onActivityResult::: != null if");

                    /**
                     *  QR データが空の場合　(QR を読み取らずに　戻るボタンを押した場合)
                     */
                } else if(error_message_back_QR_nullStr != null) {

                    // ************* アラートダイアログを出す **************
                    //   Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    CustomDialog Erro_dialog_data_ScanData = new CustomDialog(FirstActivity.this);
                    Erro_dialog_data_ScanData.showDialog_Error(
                            "QR スキャンデータエラー",
                            "QR データが空です。",
                            "閉じる"
                    );

                } else {
                    System.out.println("onActivityResult::: != null else");
                }
            } else {
                System.out.println("onActivityResult::: != data else");
            }
        } else {
            return;
        }

    }

    /**
     *  YKK データダウンロード　ボタン用　ダイアログ
     */
    private void YkkBtnDataDialog()
    {
        // タイトルビューを作成
        TextView titleView = new TextView(FirstActivity.this);
        titleView.setText("ダウロード日確認");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.btn_01));
        titleView.setPadding(20, 20, 20, 20);
        titleView.setGravity(Gravity.CENTER);

        // アラートダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
        builder.setCustomTitle(titleView);  // タイトルセット

        builder.setMessage("選択された作業日以外のデータは削除されます。\n\n" +
                "選択した日付のデータをダウンロードを行いますか？");  // メッセージセット

        // ボタン設定
        builder.setPositiveButton("ダウロードする", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ポジティブボタンの処理
                // YKK データダウンロード
                Select_Download();

                return;
            }
        });

        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                return;
            }
        });


        AlertDialog dialog = builder.show();
        dialog.show();

        // ボタンのスタイルを変更
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
    }



}