package local.hal.st31.android.favoriteshops90032;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShopEditActivity extends AppCompatActivity {
    private int _mode = MainActivity.MODE_INSERT;
    private long _idNo = 0;
    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit);
        _helper = new DatabaseHelper(ShopEditActivity.this);
        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", MainActivity.MODE_INSERT);
        if(_mode == MainActivity.MODE_INSERT){
            TextView tvTitleEdit = findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_title_insert);

            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setText(R.string.btn_insert);

            Button btnDelete = findViewById(R.id.btnDelete);
            btnDelete.setVisibility(View.INVISIBLE);
        }else{
            _idNo = intent.getLongExtra("idNo", 0);
            SQLiteDatabase db = _helper.getWritableDatabase();
            Shop shopData = DataAccess.findByPK(db, _idNo);

            EditText etInputName = findViewById(R.id.etInputName);
            etInputName.setText(shopData.getName());
            EditText etInputTel = findViewById(R.id.etInputTel);
            etInputTel.setText(shopData.getTel());
            EditText etInputUrl = findViewById(R.id.etInputUrl);
            etInputUrl.setText(shopData.getUrl());
            EditText etInputNote = findViewById(R.id.etInputNote);
            etInputNote.setText(shopData.getNote());
        }
    }

    @Override
    protected void onDestroy(){
        _helper.close();
        super.onDestroy();
    }

    public void onSaveButtonClick(View view){
        EditText etInputName = findViewById(R.id.etInputName);
        String inputName = etInputName.getText().toString();
        if(inputName.equals("")){
            Toast.makeText(ShopEditActivity.this, R.string.msg_input_name, Toast.LENGTH_SHORT).show();
        }else{
            EditText etInputTel = findViewById(R.id.etInputTel);
            String inputTel = etInputTel.getText().toString();
            EditText etInputUrl = findViewById(R.id.etInputUrl);
            String inputUrl = etInputUrl.getText().toString();
            EditText etInputNote = findViewById(R.id.etInputNote);
            String inputNote = etInputNote.getText().toString();
            SQLiteDatabase db = _helper.getWritableDatabase();
            if(_mode == MainActivity.MODE_INSERT){
                DataAccess.insert(db, inputName, inputTel, inputUrl, inputNote);
            }else{
                DataAccess.update(db, _idNo, inputName, inputTel, inputUrl, inputNote);
            }
            finish();
        }
    }

    public void onBackButtonClick(View view){
        finish();
    }

    public void onDeleteButtonClick(View view){
        SQLiteDatabase db = _helper.getWritableDatabase();
        DataAccess.delete(db, _idNo);
        finish();
    }
}