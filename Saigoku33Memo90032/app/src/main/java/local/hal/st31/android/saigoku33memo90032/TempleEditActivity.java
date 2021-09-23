package local.hal.st31.android.saigoku33memo90032;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TempleEditActivity extends AppCompatActivity {
    private int _selectedTempleNo = 0;
    private String _selectedTempleName = "";
    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_edit);

        Intent intent = getIntent();
        _selectedTempleNo = intent.getIntExtra("selectedTempleNo", 0);
        _selectedTempleName = intent.getStringExtra("selectedTempleName");
        _helper = new DatabaseHelper(TempleEditActivity.this);

        TextView tvTemple = findViewById(R.id.tvTemple);
        tvTemple.setText(_selectedTempleName);

        SQLiteDatabase db = _helper.getWritableDatabase();
        Memo memo = DataAccess.findByPK(db, _selectedTempleNo);
        if(memo != null) {
            String honzon = memo.getHonzon();
            String shushi = memo.getShushi();
            String address = memo.getAddress();
            String url = memo.getUrl();
            String note = memo.getNote();

            EditText etHonzon = findViewById(R.id.etHonson);
            etHonzon.setText(honzon);
            EditText etShushi = findViewById(R.id.etShushi);
            etShushi.setText(shushi);
            EditText etAddress = findViewById(R.id.etAddress);
            etAddress.setText(address);
            EditText etURL = findViewById(R.id.etURL);
            etURL.setText(url);
            EditText etNote = findViewById(R.id.etNote);
            etNote.setText(note);
        }
    }

    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }

    public void onSaveButtonClick(View view) {
        EditText etHonson = findViewById(R.id.etHonson);
        String honson = etHonson.getText().toString();
        EditText etShushi = findViewById(R.id.etShushi);
        String shushi = etShushi.getText().toString();
        EditText etAddress = findViewById(R.id.etAddress);
        String address = etAddress.getText().toString();
        EditText etUrl = findViewById(R.id.etURL);
        String url = etUrl.getText().toString();
        EditText etNote = findViewById(R.id.etNote);
        String note = etNote.getText().toString();

        SQLiteDatabase db = _helper.getWritableDatabase();
        boolean exist = DataAccess.findRowByPK(db, _selectedTempleNo);
        if(exist) {
            DataAccess.update(db, _selectedTempleNo, _selectedTempleName, honson, shushi, address, url, note);
        }else{
            DataAccess.insert(db, _selectedTempleNo, _selectedTempleName, honson, shushi, address, url, note);
        }
        finish();
    }
}