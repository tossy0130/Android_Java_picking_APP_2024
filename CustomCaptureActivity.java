package com.example.oracleconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.Size;

import java.util.List;

public class CustomCaptureActivity extends CaptureActivity {

    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_capture_layout);

        // カメラプレビュー用のDecoratedBarcodeViewを取得
        barcodeView = findViewById(R.id.zxing_barcode_scanner);

        // スキャンを開始する（これがカメラのプレビューを開始します）
        BarcodeCallback callback = null;
        barcodeView.decodeContinuous(callback);

    }


    // スキャン結果のコールバック
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            // スキャンされた結果を処理する
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            // オプションで結果のポイントを処理する
        }
    };



}