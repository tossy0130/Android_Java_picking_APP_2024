package com.example.oracleconnect;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Size;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity {

    // テキストビュー
    private TextView unsou_text,syukka_saki_text,laveru_scan_text,laberu_scan_view,kenpin_suuryou_text,syukka_suuryou_text;

    // QR 読取り
    private IntentIntegrator integrator;

    // QR 送り先コード格納用
    String Okurisaki_Code;
    // YKK データ

    private String Syukasaki_Select_CODE;


    private ArrayList<String> ArrNounyuuSakiCode_And_Hinbann = new ArrayList<>();
    private HashMap<String, List<String>> NounyuuSakiCode_And_Hinbann_HashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /**
         *  初期設定
         */
        init();

        /**
         *  FirstActivity から　YKK のコードの値を取得
         */
        Intent intent = getIntent();
        // 北陸
        String YKK_Code_Hokuriku = intent.getStringExtra("YKK_Code_Hokuriku");
        // 首都圏
        String[] YKK_Code_Syutoken = intent.getStringArrayExtra("YKK_Code_Syutoken");

        System.out.println("********* YKK_Code_Hokuriku *********" + YKK_Code_Hokuriku);
        System.out.println("********* YKK_Code_Syutoken *********" + YKK_Code_Syutoken);

        // 北陸が空じゃない場合
        if (YKK_Code_Hokuriku != null && !YKK_Code_Hokuriku.isEmpty()) {
            System.out.println(" ゲット intent if");
            unsou_text.setText("北陸");
            Syukasaki_Select_CODE = "013370";
        } else if(YKK_Code_Syutoken != null && YKK_Code_Syutoken.length > 0) {
            System.out.println(" ゲット intent else if");
            unsou_text.setText("首都圏");
            Syukasaki_Select_CODE = "013371";
        } else {
            // === 値が何もない（エラー処理）
            System.out.println(" ゲット else");
            finish();
            return;
        }

        /**
         *  QR スキャン 起動
         */
        QR_Scan_Read();
      //  scanBarcode();

        /**
         *   // 端末 「戻るボタン」制御
         */
        OnBackPressedCallback callback_app_finish = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
                return;

            }
        };
        // コールバックを登録
        getOnBackPressedDispatcher().addCallback(this, callback_app_finish);

    }

    /**
     *  QR スキャン起動
     */
    private void QR_Scan_Read() {

        integrator = new IntentIntegrator(this);
        integrator.setPrompt("戻るボタン タップで「キャンセル」できます。");
        // 画面の向きをポートレートに固定（オプション）
        integrator.setOrientationLocked(false);
        // QRコードのスキャンを起動
        integrator.initiateScan();

    }


    /**
     *   =========================== QR 読取り ==================================
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // === QR データが空の場合は、処理を返す
        if (scanResult.getContents() == null) {
            return;
        }

        /**
         /* ********** QR データ　取得 **********
         */
        if (scanResult != null) {

            String get_QR = scanResult.getContents();
            System.out.println("QR値:::" + get_QR);

            /**
             * 　■ ========= YKK送り先コード =========
             *
             *  325952 = YKK北陸DC
             *  -----------------
             *  315125 = YKK首都圏DC
             *  315130 = YKK首都圏DC
             *  315135 = YKK首都圏DC
             *  315150 = YKK首都圏DC
             *
             */

            // YKK送り先コード 取得
            String Okurisaki_CODE = Bytes_CUT(get_QR,66,6);
            System.out.println("送り先コード:::" + Okurisaki_CODE);

            // 製品コード
            String SeiHinCode_tmp = Bytes_CUT(get_QR,13,31);
            System.out.println("製品コード:::" + SeiHinCode_tmp);

            // 本番はこれ trim()　で　OK
           // String SeiHinCode = SeiHinCode_tmp.trim();
            String SeiHinCode = "";

            // テスト 製品コード加工
            String regex = "[a-zA-Z0-9-]+";
            Pattern pattern = Pattern.compile(regex);
            // マッチャ―作成
            Matcher matcher = pattern.matcher(SeiHinCode_tmp);
            if(matcher.find()) {
                System.out.println("抽出結果: " + matcher.group());
                SeiHinCode = matcher.group();
            } else {
                System.out.println("マッチした文字列なし");
            }

            /**
             *    ************************* ラベル印字納入文字列　で分岐 *************************
             */
            switch (Okurisaki_CODE) {
                // ==================== 北陸 =======================
                case "325952":

                    /**
                     * 　関数：SyukaSakiCehck　=> 出荷先チェック
                     *  引数：1, 出荷先コード（選択したボタンとの比較用） 2,表示用テキスト 3,エラーメッセージ
                     */
                    SyukaSakiCehck("013370", "北陸DC滑川第2","選択された出荷先コードと違っています。");

                    // 品番判定
                    HanteiHinbanHashMap(NounyuuSakiCode_And_Hinbann_HashMap, "013370",SeiHinCode);

                    break;

                // ==================== 首都圏 ====================
                // ========== 2F Aフロア
                case "315125":
                    SyukaSakiCehck("013371", "YKK首都圏DC 2F Aフロア","選択された出荷先コードと違っています。");

                    // 品番判定
                    HanteiHinbanHashMap(NounyuuSakiCode_And_Hinbann_HashMap, "013371",SeiHinCode);
                    break;
                // ========== 2F Bフロア
                case "315130":
                    SyukaSakiCehck("013371", "YKK首都圏DC 2F Bフロア","選択された出荷先コードと違っています。");

                    // 品番判定
                    HanteiHinbanHashMap(NounyuuSakiCode_And_Hinbann_HashMap, "013371",SeiHinCode);
                    break;
                // ========== 2F Cフロア
                case "315135":
                    SyukaSakiCehck("013371", "YKK首都圏DC 2F Cフロア","選択された出荷先コードと違っています。");

                    // 品番判定
                    HanteiHinbanHashMap(NounyuuSakiCode_And_Hinbann_HashMap, "013371",SeiHinCode);
                    break;
                // ========== 2F Dフロア
                case "315150":
                    SyukaSakiCehck("013371", "YKK首都圏DC 2F Dフロア","選択された出荷先コードと違っています。");

                    // 品番判定
                    HanteiHinbanHashMap(NounyuuSakiCode_And_Hinbann_HashMap, "013371",SeiHinCode);
                    break;

            }

        } // ====================== scanResult END

    } // ========================== onActivityResult END

    /**
     *   cut_start から、get_length 分の値を取得する
     */
    public static String Bytes_CUT(String str, int start, int get_length) {

        // string => byte へ変換
        byte[] get_def = str.getBytes(StandardCharsets.UTF_8);

        int cut_start = start - 1; // index 考慮用に -1 をした値
        int cut_end = (start + get_length) - 1;    // cut_start から、get_length 分の値を取得する


        byte[] result_def = new byte[get_length];
        int idx = 0;
        for (int i = 0; i <= 190; i++) {
            if (i >= cut_start && i < cut_end) {
                result_def[idx] = get_def[i];
                idx = idx + 1;
            }
        }

        // String で上で切り取った値を返す
        String return_str = new String(result_def, StandardCharsets.UTF_8);
        return return_str;

    }

    /**
     *  初期設定　用
     */
    private void init()
    {
        // 運送便　【北陸】or【首都圏】表示
        unsou_text = findViewById(R.id.unsou_text);
        // 出荷先　表示用
        syukka_saki_text = findViewById(R.id.syukka_saki_text);

        // ラベルスキャン view
        laberu_scan_view = findViewById(R.id.laberu_scan_view);
        // ラベルスキャンテキスト
        laveru_scan_text = findViewById(R.id.laveru_scan_text);

        // 出荷数量テキスト
        syukka_suuryou_text = findViewById(R.id.syukka_suuryou_text);
        // 検品数量テキスト
        kenpin_suuryou_text = findViewById(R.id.kenpin_suuryou_text);

        /**
         *  品番チェック用　HashMap 作成
         */
        CreateNounyuuCode_And_Hinbann();

    }

    /**
     *  出荷先　チェック
     */
    private void SyukaSakiCehck(String SyukaSakiCode, String SyukaSakiText , String ErrMessage)
    {
        System.out.println("========= function SyukaSakiCehck 開始 =========");

        System.out.println("function SyukaSakiCode　内:::" + SyukaSakiCode);
        System.out.println("function SyukaSakiText　内:::" + SyukaSakiText);

        if(Syukasaki_Select_CODE.equals(SyukaSakiCode)) {
            syukka_saki_text.setText(SyukaSakiText);
        } else {
            //=== エラーで FirstActivity へ返す
            Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
            intent.putExtra("error_message_back", ErrMessage);
            setResult(RESULT_CANCELED, intent);
            finish();
            return;
        }
    }

    /**
     *  製品コード 判別用 HashMap 作成 init() で呼び出し
     */
    private void CreateNounyuuCode_And_Hinbann() {

        GlOpenHelper helper_select = new GlOpenHelper(getApplicationContext());
        SQLiteDatabase db_select = helper_select.getReadableDatabase();

        String[] arr_item = new String[3];

        //---- リストを空にする
        NounyuuSakiCode_And_Hinbann_HashMap.clear();

        int num = 0;
        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db_select.rawQuery("SELECT Master_column_01,Master_column_10,Master_column_06 " +
                    "FROM Master_table WHERE Master_column_01 = '013370' OR " +
                    "Master_column_01 = '013371' ORDER BY Master_column_01;", null);

            while (cursor.moveToNext()) {

                //------- 9
                int idx = cursor.getColumnIndex("Master_column_01");
                arr_item[0] = cursor.getString(idx);

                idx = cursor.getColumnIndex("Master_column_10");
                arr_item[1] = cursor.getString(idx);

                idx = cursor.getColumnIndex("Master_column_06");
                arr_item[2] = cursor.getString(idx);

                // 比較用にハッシュマップに挿入
                if (!NounyuuSakiCode_And_Hinbann_HashMap.containsKey(arr_item[0])) {
                    // キーが存在しない場合は新しいリストを作成
                    NounyuuSakiCode_And_Hinbann_HashMap.put(arr_item[0], new ArrayList<>());
                }
                // リストに品番を追加
                NounyuuSakiCode_And_Hinbann_HashMap.get(arr_item[0]).add(arr_item[1]);
                NounyuuSakiCode_And_Hinbann_HashMap.get(arr_item[0]).add(arr_item[2]);

                num++;

            } //-------- while END

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db_select != null) {
                db_select.close();
            }
        }

    } // ================== CreateNounyuuCode_And_Hinbann END

    /**
     *  ハッシュマップ : 出力
     */
    private void PrintHashMap(HashMap<String, List<String>> map)
    {
        for(Map.Entry<String, List<String>> entry :map.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            System.out.println("ハッシュ key:::" + key);
            for(String val : values) {
                System.out.println("ハッシュ value:::" + val);
            }
        }
    } // =============================== PrintHashMap END




    /**
     *  品番　比較 判定（形式：ハッシュマップ）
     */
    private void HanteiHinbanHashMap(HashMap<String, List<String>> map, String NounyuuSakiCode,String HinBan) {

        for(Map.Entry<String, List<String>> entry :map.entrySet()) {
            String key = entry.getKey();

            // ===　納入先コード　指定
            if(key.equals(NounyuuSakiCode)) {
                List<String> values = entry.getValue();

                for(String val : values) {
                    if(val.equals(HinBan)) {
                        // ラベルスキャン表示
                        laveru_scan_text.setText(HinBan);
                        System.out.println("比較OK 含まれている:::key:::" + key + ":::val:::" + val);

                        // ===
                        if (values.size() > 1) {
                            System.out.println("2つ目の値::: " + values.get(1));
                            syukka_suuryou_text.setText(values.get(1));

                            return;
                        } else {
                            System.out.println("2つ目の値は存在しません");
                        }

                    } else {
                        System.out.println("比較NG 含まれていない:::key" + key + ":::val:::" + val);

                        // エラーで戻す処理を入れる

                    }
                }
            }

        }

    } // =============================== HanteiHinbanHashMap END

}