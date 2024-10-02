package com.example.oracleconnect;

import android.util.Log;

public class GlListItem {

    protected int id;
    protected  String List_Master_column_01;
    protected  String List_Master_column_02;
    protected  String List_Master_column_03;
    protected  String List_Master_column_04;
    protected  String List_Master_column_05;
    protected  String List_Master_column_06;
    protected  String List_Master_column_07;
    protected  String List_Master_column_08;
    protected  String List_Master_column_09;
    protected  String List_Master_column_10;
    protected  String List_Master_column_11;
    protected  String List_Master_column_12;

    /**
     *  Setter
     * @param id
     * @param List_Master_column_01
     * @param List_Master_column_02
     * @param List_Master_column_03
     * @param List_Master_column_04
     * @param List_Master_column_05
     * @param List_Master_column_06
     * @param List_Master_column_07
     * @param List_Master_column_08
     * @param List_Master_column_09
     * @param List_Master_column_10
     * @param List_Master_column_11
     * @param List_Master_column_12
     */
    public GlListItem(int id, String List_Master_column_01,String List_Master_column_02,String List_Master_column_03,
                      String List_Master_column_04,String List_Master_column_05,String List_Master_column_06,
                      String List_Master_column_07,String List_Master_column_08,String List_Master_column_09,
                      String List_Master_column_10,String List_Master_column_11,String List_Master_column_12)
    {
        this.id = id;
        this.List_Master_column_01 = List_Master_column_01;
        this.List_Master_column_02 = List_Master_column_02;
        this.List_Master_column_03 = List_Master_column_03;
        this.List_Master_column_04 = List_Master_column_04;
        this.List_Master_column_05 = List_Master_column_05;
        this.List_Master_column_06 = List_Master_column_06;
        this.List_Master_column_07 = List_Master_column_07;
        this.List_Master_column_08 = List_Master_column_08;
        this.List_Master_column_09 = List_Master_column_09;
        this.List_Master_column_10 = List_Master_column_10;
        this.List_Master_column_11 = List_Master_column_11;
        this.List_Master_column_12 = List_Master_column_12;
    }

    /**
     * ==================== Getter ==================
     */

    public int getId()
    {
        Log.d("ログ: 取得したID：", String.valueOf(id));
        return id;
    }

    // 納入先Ｃ
    public String getList_Master_column_01() {
        return List_Master_column_01;
    }

    // 納入先名
    public String getList_Master_column_02() {
        return List_Master_column_02;
    }

    // 商品Ｃ
    public String getList_Master_column_03() {
        return List_Master_column_03;
    }

    // 品名
    public String getList_Master_column_04() {
        return List_Master_column_04;
    }

    // 出荷日（ユーザ選択）
    public String getList_Master_column_05() {
        return List_Master_column_05;
    }

    // 出荷数量
    public String getList_Master_column_06() {
        return List_Master_column_06;
    }

    // 作業数量（ユーザスキャン）
    public String getList_Master_column_07() {
        return List_Master_column_07;
    }

    // 作業状態フラグ , １：未作業 , ２：作業中 , ９：作業完了
    public String getList_Master_column_08() {
        return List_Master_column_08;
    }

    // YKK送り先コード , YKK北陸DC => 325952 , YKK首都圏DC => 315125, 315130, 315135, 315150
    public String getList_Master_column_09() {
        return List_Master_column_09;
    }

    // 品番
    public String getList_Master_column_10() {
        return List_Master_column_10;
    }

    // 作業開始日（端末）
    public String getList_Master_column_11() {
        return List_Master_column_11;
    }

    // 作業終了日（端末）
    public String getList_Master_column_12() {
        return List_Master_column_12;
    }



}
