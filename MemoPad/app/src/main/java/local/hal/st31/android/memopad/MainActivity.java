package local.hal.st31.android.memopad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * ST31 Androidサンプル11 メモ帳アプリ
 *
 * 第1画面表示用アクティビティクラス。
 * メモリストを表示する。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 新規登録モードを表す定数フィールド。
     */
    static final int MODE_INSERT = 1;
    /**
     * 更新モードを表す定数フィールド。
     */
    static final int MODE_EDIT = 2;
    /**
     * メモリスト用ListView。
     */
    private ListView _lvMemoList;
    /**
     * データベースヘルパーオブジェクト。
     */
    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _lvMemoList = findViewById(R.id.lvMemoList);
        _lvMemoList.setOnItemClickListener(new ListItemClickListener());

        _helper = new DatabaseHelper(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = _helper.getWritableDatabase();
        Cursor cursor = DataAccess.findAll(db);
        String[] from = { "title" };
        int[] to = { android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cursor, from, to, 0);
        _lvMemoList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }

    /**
     * 新規ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onNewButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, MemoEditActivity.class);
        intent.putExtra("mode", MODE_INSERT);
        startActivity(intent);
    }

    /**
     * リストがクリックされた時のリスナクラス。
     */
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor item = (Cursor) parent.getItemAtPosition(position);
            int idxId = item.getColumnIndex("_id");
            long idNo =  item.getLong(idxId);

            Intent intent = new Intent(MainActivity.this, MemoEditActivity.class);
            intent.putExtra("mode", MODE_EDIT);
            intent.putExtra("idNo", idNo);
            //          intent.putExtra("idNo", id);
            startActivity(intent);
        }
    }
}
