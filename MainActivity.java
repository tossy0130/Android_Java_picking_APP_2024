package com.example.oracleconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oracle.net.aso.e;

public class MainActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    Connection con;
    String str, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionClass = new ConnectionClass();
        connect();

        Button btnConnect = (Button)findViewById(R.id.button);
        btnConnect.setOnClickListener(onClick_button);

    }

    private View.OnClickListener onClick_button = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          //  Procedure();
            Select();

        }
    };

    public void Procedure(){
        String SQL = "{call test(?,?)}";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                CallableStatement cs = null;
                con = connectionClass.Conn();
                cs = con.prepareCall(SQL);
                cs.setString(1, "p1");
                cs.registerOutParameter(2, Types.VARCHAR, 1000);
                cs.executeQuery();
                name = cs.getString(2);
                System.out.println(cs.getString(2));
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            runOnUiThread(() -> {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                TextView txtList = findViewById(R.id.textview);
                txtList.setText(name);
            });
        });

    }

    public void Select(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = connectionClass.Conn();
                String query = "SELECT * FROM HTPK";
                PreparedStatement stmt = (PreparedStatement) con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                StringBuilder bStr = new StringBuilder("処理ＳＥＱ,入力担当,倉庫Ｃ,運送Ｃ,出荷予定数量,ピッキング数量,処理Ｆ\n");
                while(rs.next()){
                    bStr.append(rs.getString("処理ＳＥＱ")).append(",");
                    bStr.append(rs.getString("入力担当")).append(",");
                    bStr.append(rs.getString("倉庫Ｃ")).append(",");
                    bStr.append(rs.getString("運送Ｃ")).append(",");
                    bStr.append(rs.getString("出荷予定数量")).append(",");
                    bStr.append(rs.getString("ピッキング数量")).append(",");
                    bStr.append(rs.getString("処理Ｆ")).append("\n");
                }
                name = bStr.toString();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            runOnUiThread(() -> {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                TextView txtList = findViewById(R.id.textview);
                txtList.setText(name);
            });
        });
    }

    public void connect(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = connectionClass.Conn();
                if (con == null){
                    str = "ERROR";
                } else {
                    str = "Connect";
                }
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            runOnUiThread(() -> {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}