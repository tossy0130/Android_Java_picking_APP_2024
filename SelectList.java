package com.example.oracleconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectList extends AppCompatActivity {

    private GlBaseAdapter glBaseAdapter;
    private  List<GlListItem> items;
    private ListView GlListView;
    private GlListItem glListItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_list);

        //
        items = new ArrayList<>();

        // GlBaseAdapter の コンストラクタを呼び出す
        glBaseAdapter = new GlBaseAdapter(this, items);

        GlListView = (ListView) findViewById(R.id.GlListView);

        /**
         *  DB 読み込み, 更新 処理
         */
        GlloadMyList();
    }


    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */
    private void GlloadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        // === インサート用　オブジェクト
        GlOpenHelper helperListSelect = new GlOpenHelper(getApplicationContext());
        SQLiteDatabase db_ListSelect = helperListSelect.getReadableDatabase();

        try {

            Cursor cursor = db_ListSelect.rawQuery("SELECT * FROM Master_table", null);

            if(cursor.moveToFirst()) {

                do {
                    // GlListItemのコンストラクタ呼び出し(GlListItemのオブジェクト生成)
                    glListItem = new GlListItem(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getString(10),
                            cursor.getString(11),
                            cursor.getString(12));

                    //------- ログの取得
                    Log.d("取得したCursor 00", String.valueOf(cursor.getInt(0)));
                    Log.d("取得したCursor 01:", cursor.getString(1));
                    Log.d("取得したCursor 02", cursor.getString(2));
                    Log.d("取得したCursor 03", cursor.getString(3));
                    Log.d("取得したCursor 04", cursor.getString(4));
                    Log.d("取得したCursor 05", cursor.getString(5));
                    Log.d("取得したCursor 06", cursor.getString(6));
                    Log.d("取得したCursor 07", cursor.getString(7));
                    Log.d("取得したCursor 08", cursor.getString(8));
                    Log.d("取得したCursor 09", cursor.getString(9));
                    Log.d("取得したCursor 10", cursor.getString(10));
                    Log.d("取得したCursor 11", cursor.getString(11));
                    Log.d("取得したCursor 12", cursor.getString(12));

                    items.add(glListItem);

                } while(cursor.moveToNext());

            }

            cursor.close();
            db_ListSelect.close();

            GlListView.setAdapter(glBaseAdapter);

            //****************** 処理フラグによって色を変える処理を入れる ***************//

            glBaseAdapter.notifyDataSetChanged();   // Viewの更新


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_ListSelect.close();
        }

    } // =====================  GlloadMyList END

    /**
     *
     * BaseAdapterを継承したクラス
     * GlBaseAdapter
     *
     */
    public class GlBaseAdapter extends BaseAdapter {

        private Context context;
        private List<GlListItem> items;

        private class ViewHolder {

            FrameLayout rowselectHeader; // フラグでの色変え　用

            TextView rowText_label_01; // 品番ラベル
            TextView rowText_label_02; // 出荷ラベル
            TextView rowText_label_03; // 検品ラベル

            TextView rowText_input_01; // 品番
            TextView rowText_input_02; // 出荷数量
            TextView rowText_input_03; // 検品数量

        }

            /**
             * コンストラクタ
             */
            public GlBaseAdapter(Context context, List<GlListItem> items)
            {
                this.context = context;
                this.items = items;
            }

            // Listの数
            @Override
            public int getCount() {
                return items.size();
            }

            // index or オブジェクトを返す
            @Override
            public Object getItem(int position) {
                return items.get(position);
            }

            // index を　他の index に返す
            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = convertView;
                ViewHolder holder;

                // データ取得
                glListItem = items.get(position);

                if(view == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.rowselectlist, parent, false);

                    FrameLayout rowselectHeader = (FrameLayout)view.findViewById(R.id.rowselectHeader);

                    // ラベル 表示用
                    TextView rowText_label_01 = (TextView)view.findViewById(R.id.rowText_label_01); // 品番
                    TextView rowText_label_02 = (TextView)view.findViewById(R.id.rowText_label_02); // 出荷数量
                    TextView rowText_label_03 = (TextView)view.findViewById(R.id.rowText_label_03); // 検品数量

                    // 値　表示用
                    TextView rowText_input_01 = (TextView) view.findViewById(R.id.rowText_input_01); // 品番（値）
                    TextView rowText_input_02 = (TextView) view.findViewById(R.id.rowText_input_02); // 出荷数量（値）
                    TextView rowText_input_03 = (TextView) view.findViewById(R.id.rowText_input_03); // 検品数量（値）

                    holder = new ViewHolder();

                    holder.rowselectHeader = rowselectHeader;

                    holder.rowText_label_01 = rowText_label_01;
                    holder.rowText_label_02 = rowText_label_02;
                    holder.rowText_label_03 = rowText_label_03;

                    holder.rowText_input_01 = rowText_input_01;
                    holder.rowText_input_02 = rowText_input_02;
                    holder.rowText_input_03 = rowText_input_03;

                    view.setTag(holder);

                } else {

                    holder = (ViewHolder) view.getTag();
                    convertView.setBackgroundColor(getResources().getColor(R.color.back_color_02));
                }

                String HibanListStr = glListItem.getList_Master_column_10(); // 品番

                String SyukaLlistNum = glListItem.getList_Master_column_06(); // 出荷数量
                String KnpynListNum = glListItem.getList_Master_column_07(); // 検品数量

                // === 品番
                if (HibanListStr == null) {
                    holder.rowText_input_01.setText("");
                } else {
                    holder.rowText_input_01.setText(HibanListStr);
                }

                // 出荷数量
                if (SyukaLlistNum == null) {
                    holder.rowText_input_02.setText("");
                } else {
                    holder.rowText_input_02.setText(SyukaLlistNum);
                }

                // 検品数量
                if (KnpynListNum == null) {
                    holder.rowText_input_03.setText("");
                } else {
                    holder.rowText_input_03.setText(KnpynListNum);
                }

                // ****************** フラグの分岐処理 ****************
                String FlgList = glListItem.getList_Master_column_08();

                if(FlgList.equals("1")) {
                    holder.rowselectHeader.setBackgroundColor(context.getResources().getColor(R.color.footer_btn));

                    /*
                    TextView newTextView = new TextView(context);
                    newTextView.setText("作業未完了");
                    newTextView.setTextColor(Color.WHITE);
                    newTextView.setTextSize(16);
                    holder.rowselectHeader.addView(newTextView);
                     */

                } else if(FlgList.equals("2")) {
                    holder.rowselectHeader.setBackgroundColor(context.getResources().getColor(R.color.syutokenn));

                    /*
                    TextView newTextView = new TextView(context);
                    newTextView.setText("作業中");
                    newTextView.setTextColor(Color.WHITE);
                    newTextView.setTextSize(16);
                    holder.rowselectHeader.addView(newTextView);

                     */

                } else if(FlgList.equals("9")) {
                    holder.rowselectHeader.setBackgroundColor(context.getResources().getColor(R.color.btn_01));

                    /*
                    TextView newTextView = new TextView(context);
                    newTextView.setText("作業完了");
                    newTextView.setTextColor(Color.WHITE);
                    newTextView.setTextSize(16);
                    holder.rowselectHeader.addView(newTextView);
                     */

                } else {
                    holder.rowselectHeader.setBackgroundColor(context.getResources().getColor(R.color.white));
                }

                return view;

            }// =========== END getView


    } // ================================ GlBaseAdapter END


}