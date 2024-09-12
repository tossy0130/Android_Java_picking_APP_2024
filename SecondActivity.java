package com.example.oracleconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Size;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class SecondActivity extends AppCompatActivity {

    // QR 読取り
    private IntentIntegrator integrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /**
         *  QR スキャン 起動
         */
        QR_Scan_Read();
      //  scanBarcode();

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


        /*  ==========  zxing カスタム　方法 =============
        // 現在のActivity(SecondActivity)を渡す
        integrator = new IntentIntegrator(this);
        // カスタムキャプチャアクティビティを指定
        integrator.setCaptureActivity(CustomCaptureActivity.class);
        // キャプチャ画面の下方にメッセージを表示
        integrator.setPrompt("戻るボタン タップで「キャンセル」できます。");
        // 画面の向きをポートレートに固定（オプション）
        integrator.setOrientationLocked(true);
        // QRコードのスキャンを起動
        integrator.initiateScan();

         */
    }



    // test
    /*
    public void scanBarcode() {
        new IntentIntegrator(SecondActivity.this)
                // 読み取ったときにビープ音を鳴らさない
                .setBeepEnabled(false)
                // 画面の向きを固定しない
                .setOrientationLocked(true)
                // 読み取るバーコードのフォーマットを指定する
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                // アクティビティを設定する
           //     .setCaptureActivity(SecondActivity.class)
                .initiateScan();
    }
     */

    // test 02


}