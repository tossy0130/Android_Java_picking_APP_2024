package com.example.oracleconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;

/**
 *  ダイアログ　クラス
 */
public class CustomDialog {

    private Context context;

    // コンストラクタ
    public CustomDialog(Context context) {
        this.context = context;
    }

    //　アプリケーション終了 ダイアログの確認
    public void showDialog_APP_END(String title, String message, String positiveText, String negativeText)
    {
        // タイトルビューを作成
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(context.getResources().getColor(R.color.btn_01));
        titleView.setPadding(20, 20, 20, 20);
        titleView.setGravity(Gravity.CENTER);

        // アラートダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(titleView);  // タイトルセット
        builder.setMessage(message);  // メッセージセット

        // ボタン設定
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ポジティブボタンの処理
                return;
            }
        });

        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // アプリ終了の処理
                ((Activity) context).finish();
            }
        });

        // ダイアログ表示
        AlertDialog dialog = builder.create();

        if (!((Activity) context).isFinishing()) {
            dialog.show();  // ここでダイアログを表示
        }

        // ボタンのスタイルを変更
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));

    } // ========= Function END

    /**
     *  エラー　アラート 01
     */
    public void showDialog_Error(String title, String message, String positiveText)
    {

        // タイトルビューを作成
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(context.getResources().getColor(R.color.hokuriku));
        titleView.setPadding(20, 20, 20, 20);
        titleView.setGravity(Gravity.CENTER);

        // アラートダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(titleView);  // タイトルセット
        builder.setMessage(message);  // メッセージセット

        // ボタン設定
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ポジティブボタンの処理
                return;
            }
        });

        // ダイアログ表示
        AlertDialog dialog = builder.create();

        if (!((Activity) context).isFinishing()) {
            dialog.show();  // ここでダイアログを表示
        }

        // ボタンのスタイルを変更
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FE4A49"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FE4A49"));

    }

    /**
     * 　データ削除　ダイアログ
     */
    public void showDialog_Data_DELETE(String title, String message, String positiveText, String negativeText)
    {
        // タイトルビューを作成
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(context.getResources().getColor(R.color.hokuriku));
        titleView.setPadding(20, 20, 20, 20);
        titleView.setGravity(Gravity.CENTER);

        // アラートダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(titleView);  // タイトルセット
        builder.setMessage(message);  // メッセージセット

        // ボタン設定
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ポジティブボタンの処理
                return;
            }
        });

        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // アプリ終了の処理
                ((Activity) context).finish();
            }
        });

        // ダイアログ表示
        AlertDialog dialog = builder.create();

        if (!((Activity) context).isFinishing()) {
            dialog.show();  // ここでダイアログを表示
        }

        // ボタンのスタイルを変更
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FE4A49"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FE4A49"));

    } // ========= Function END

}
