package local.hal.st31.android.showms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

/**
 * ST31 Androidサンプル06 インテント
 *
 * 初期画面表示アクティビティクラス。
 * 選択リストを表示する。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvMS = findViewById(R.id.lvMS);
        lvMS.setOnItemClickListener(new ListItemClickListener(this));
    }

}
