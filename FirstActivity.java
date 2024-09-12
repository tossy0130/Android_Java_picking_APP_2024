package com.example.oracleconnect;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    // TextView
    private TextView date_view_top, app_title_01, sagyou_sentaku_view;

    // button
    private Button sagyou_btn, hokuriku_btn, syutoken_btn, pikking_start_btn, app_end_btn, ykk_data_btn_01,data_reset_btn;

    // String
    private String get_date_str, get_year, get_month, get_day, Now_date_str;

    private boolean isHokurikuSelected = false;
    private boolean isSyutokenSelected = false;

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

                    // YKK データダウンロード
                    Select_Download();

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

                } else {
                    // 北陸ボタンが再度押されたとき、元の色に戻す
                    hokuriku_btn.setAlpha(1.0f);
                    hokuriku_btn.setTextColor(Color.parseColor("#FFFFFF"));

                    isHokurikuSelected = false;
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

                } else {
                    // 首都圏ボタンが再度押されたとき、元の色に戻す
                    syutoken_btn.setAlpha(1.0f);
                    syutoken_btn.setTextColor(Color.parseColor("#FFFFFF"));

                    isSyutokenSelected = false;
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
                    startActivity(intent);
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



        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            try {
                con = connectionClass.Conn();
                String query = "";

                ResultSet rs = null;
                // 「北陸」

                    System.out.println("出荷先フラグ:::" + isHokurikuSelected);

                    // 作業日　取得
                    String Get_Sagyou_Day = sagyou_sentaku_view.getText().toString();

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    // 変換後の日付形式を指定する（yyyy-MM-dd）
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = inputFormat.parse(Get_Sagyou_Day);
                    String outputDate_Sagyou_Day = outputFormat.format(date);

                    System.out.println("バインド　作業日:::" + outputDate_Sagyou_Day);

                    query = "SELECT * FROM SJTR WHERE 納入先名 = ? AND 出荷日 = ?";
                 //   query = "SELECT * FROM SJTR WHERE 納入先名 like ? AND 出荷日 = ?";
                 //   query = "SELECT * FROM SJTR WHERE 納入先名 LIKE ? AND 出荷日 = TO_DATE(?, 'YYYY-MM-DD')";

                    PreparedStatement stmt = (PreparedStatement) con.prepareStatement(query);
                    // バインド
                    stmt.setString(1, "ＹＫＫ北陸ＤＣ");
                    stmt.setString(2,outputDate_Sagyou_Day);
                    rs = stmt.executeQuery();

                int idx = 1;
                StringBuilder bStr = new StringBuilder("伝票ＳＥＱ,伝票番号,出荷指示日時,得意先Ｃ,得意先名,納入先Ｃ,納入先名\n");
                while (rs.next()) {
                    bStr.append(rs.getString("伝票ＳＥＱ")).append(",");
                    bStr.append(rs.getString("伝票番号")).append(",");
                    bStr.append(rs.getString("出荷指示日時")).append(",");
                    bStr.append(rs.getString("得意先Ｃ")).append(",");
                    bStr.append(rs.getString("得意先名")).append(",");
                    bStr.append(rs.getString("納入先Ｃ")).append(",");
                    bStr.append(rs.getString("納入先名")).append("\n");

                    System.out.println(String.valueOf(idx) + ":" + bStr.toString());
                    // ログ用
                    idx += 1;
                }
                name = bStr.toString();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            runOnUiThread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
       //         TextView txtList = findViewById(R.id.textview);
       //         txtList.setText(name);
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


}