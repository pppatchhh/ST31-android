package local.hal.st31.android.prefmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ST31 Androidサンプル09 都道府県メモアプリ
 *
 * 第2画面表示用アクティビティクラス。
 * 都道府県メモ編集画面を表示する。
 *
 * @author Shinzo SAITO
 */
public class PrefEditActivity extends AppCompatActivity {
    /**
     * 都道府県リスト画面で選択されたリストの行番号。
     */
    private int _selectedPrefNo = 0;
    /**
     * 都道府県リスト画面で選択された都道府県名。
     */
    private String _selectedPrefName = "";
    /**
     * データベースヘルパーオブジェクト
     */
    private DatabaseHelper _helper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_edit);
    
        Intent intent = getIntent();
        _selectedPrefNo = intent.getIntExtra("selectedPrefNo", 0);
        _selectedPrefName = intent.getStringExtra("selectedPrefName");
    
        _helper = new DatabaseHelper(getApplicationContext());
    
        TextView tvPref = findViewById(R.id.tvPref);
        tvPref.setText(_selectedPrefName);
    
        SQLiteDatabase db = _helper.getWritableDatabase();
        Memo memo = DataAccess.findByPK(db, _selectedPrefNo);
        if(memo != null) {
            String content = memo.getContent();
            EditText etContent = findViewById(R.id.etContent);
            etContent.setText(content);
        }
    }
    
    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }
    
    /**
     * 保存ボタンがタップされた時の処理メソッド。
     * @param view タップされた画面部品。
     */
    public void onSaveButtonClick(View view) {
        EditText etContent = findViewById(R.id.etContent);
        String content = etContent.getText().toString();
        SQLiteDatabase db = _helper.getWritableDatabase();
        boolean exist = DataAccess.findRowByPK(db, _selectedPrefNo);
        if(exist) {
            DataAccess.update(db, _selectedPrefNo, _selectedPrefName, content);
        }
        else {
            DataAccess.insert(db, _selectedPrefNo, _selectedPrefName, content);
        }
    
        finish();
    }
}
