package com.example.oracleconnect;

import java.sql.*;
public class ConnectionClass {

    public Connection Conn() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

        //    String url = "jdbc:oracle:thin:@192.168.254.17:1521:SRV-ORA12";
            String url = "jdbc:oracle:thin:@//192.168.254.17:1521/ORCL.WORLD";
        //    String url = "jdbc:oracle:thin:@//192.168.254.17:1521/ORCL";

            String user = "GL";
            String pass = "GL";

            //データベースに接続
            conn = DriverManager.getConnection(url,user,pass);
        } catch (Exception e){
            System.out.println("接続エラー");
            System.out.println(e);
        }
        return conn;
    }
}
