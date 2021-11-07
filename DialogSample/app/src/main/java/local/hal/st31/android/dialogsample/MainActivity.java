package local.hal.st31.android.dialogsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * ST31 Androidサンプル12 ダイアログ
 *
 * アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
    /**
     * このMainActivityが起動したタイムスタンプを保持するフィールド。
     */
    private LocalDateTime _createdAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _createdAt = LocalDateTime.now();
    }

    /**
     * シンプルダイアログ表示ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showSimpleDialog(View view) {
        SimpleDialogFragment dialog = new SimpleDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "SimpleDialogFragment");
    }

    /**
     * 通常ダイアログ表示ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showDialog(View view) {
        FullDialogFragment dialog = new FullDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "FullDialogFragment");
    }

    /**
     * メッセージ表示ダイアログ表示ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showMsgDialog(View view) {
        EditText etMsg = findViewById(R.id.etMsg);
        String msg = etMsg.getText().toString();
        Bundle extras = new Bundle();
        extras.putString("msg", msg);
        MsgDialogFragment dialog = new MsgDialogFragment();
        dialog.setArguments(extras);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "MsgDialogFragment");
    }

    /**
     * MainActivityの起動からどれくらい経過したかを表示するダイアログ表示ボタンが押された時のイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showTimeDiffDialog(View view) {
        TimeDiffDialogFragment dialog = new TimeDiffDialogFragment(_createdAt);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "TimeDiffDialogFragment");
    }

    /**
     * 日付選択ダイアログ表示ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showDatePickerDialog(View view) {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH);
        int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
        dialog.show();
    }

    /**
     * 時間選択ダイアログ表示ボタンが押されたときのイベント処理用メソッド。
     *
     * @param view 画面部品。
     */
    public void showTimePickerDialog(View view) {
        TimePickerDialog dialog = new TimePickerDialog(MainActivity.this, new TimePickerDialogTimeSetListener(), 0, 0, true);
        dialog.show();
    }

    /**
     * 日付選択ダイアログの完了ボタンが押されたときの処理が記述されたメンバクラス。
     */
    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String msg = "日付選択ダイアログ: " + year + "年" + (month + 1) + "月" + dayOfMonth + "日";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 時間選択ダイアログの完了ボタンが押されたときの処理が記述されたメンバクラス。
     */
    private class TimePickerDialogTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String msg = "時間選択ダイアログ: " + hourOfDay + "時" + minute + "分";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
