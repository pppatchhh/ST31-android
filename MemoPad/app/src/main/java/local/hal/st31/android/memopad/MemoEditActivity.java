package local.hal.st31.android.memopad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ST31 Androidサンプル11 メモ帳アプリ
 *
 * 第2画面表示用アクティビティクラス。
 * メモ情報編集画面を表示する。
 *
 * @author Shinzo SAITO
 */
public class MemoEditActivity extends AppCompatActivity {
    /**
     * 新規登録モードか更新モードかを表すフィールド。
     */
    private int _mode = MainActivity.MODE_INSERT;
    /**
     * 更新モードの際、現在表示しているメモ情報のデータベース上の主キー値。
     */
    private long _idNo = 0;
    /**
     * データベースヘルパーオブジェクト。
     */
    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        _helper = new DatabaseHelper(MemoEditActivity.this);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", MainActivity.MODE_INSERT);

        if(_mode == MainActivity.MODE_INSERT) {
            TextView tvTitleEdit = findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_title_insert);

            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setText(R.string.btn_insert);

            Button btnDelete = findViewById(R.id.btnDelete);
            btnDelete.setVisibility(View.INVISIBLE);
        }
        else {
            _idNo = intent.getLongExtra("idNo", 0);
            SQLiteDatabase db = _helper.getWritableDatabase();
            Memo memoData = DataAccess.findByPK(db, _idNo);

            EditText etInputTitle = findViewById(R.id.etInputTitle);
            etInputTitle.setText(memoData.getTitle());

            EditText etInputContent = findViewById(R.id.etInputContent);
            etInputContent.setText(memoData.getContent());
        }
    }

    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }

    /**
     * 登録・更新ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onSaveButtonClick(View view) {
        EditText etInputTitle = findViewById(R.id.etInputTitle);
        String inputTitle = etInputTitle.getText().toString();
        if(inputTitle.equals("")) {
            Toast.makeText(MemoEditActivity.this, R.string.msg_input_title, Toast.LENGTH_SHORT).show();
        }
        else {
            EditText etInputContent = findViewById(R.id.etInputContent);
            String inputContent = etInputContent.getText().toString();
            SQLiteDatabase db = _helper.getWritableDatabase();
            if(_mode == MainActivity.MODE_INSERT) {
                DataAccess.insert(db, inputTitle, inputContent);
            }
            else {
                DataAccess.update(db, _idNo, inputTitle, inputContent);
            }
            finish();
        }
    }

    /**
     * 戻るボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onBackButtonClick(View view) {
        finish();
    }

    /**
     * 削除ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void onDeleteButtonClick(View view) {
        SQLiteDatabase db = _helper.getWritableDatabase();
        DataAccess.delete(db, _idNo);
        finish();
    }
}
