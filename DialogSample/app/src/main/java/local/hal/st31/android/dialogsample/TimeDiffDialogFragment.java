package local.hal.st31.android.dialogsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.fragment.app.DialogFragment;

/**
 * ST31 Androidサンプル12 ダイアログ
 *
 * MainActivityの起動からどれくらい経過したかを表示するダイアログクラス。
 *
 * @author Shinzo SAITO
 */
public class TimeDiffDialogFragment extends DialogFragment {
    /**
     * MainActivityが起動したタイムスタンプを保持するフィールド。
     */
    private LocalDateTime _createdAt;
    /**
     * _createdAtをフォーマットした文字列。
     */
    private String _createdAtStr;
    /**
     * タイムスタンプのフォーマット用オブジェクト。
     */
    private DateTimeFormatter _dateFormatter;

    /**
     * コンストラクタ。
     *
     * @param createdAt MainActivityが起動したタイムスタンプ。
     */
    public TimeDiffDialogFragment(LocalDateTime createdAt) {
        _createdAt = createdAt;
        _dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss:SSS");
        _createdAtStr = _createdAt.format(_dateFormatter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(_createdAt, now);
        long diffSeconds = diff.getSeconds();
        String nowStr = now.format(_dateFormatter);
        String msg = "MainActivityの起動時刻: " + _createdAtStr + "\nダイアログ表示時刻: " + nowStr + "\n差: " + diffSeconds + "秒";
        Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.dlg_btn_ok, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dlg_btn_cancel, new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * ダイアログのボタンが押されたときの処理が記述されたメンバクラス。
     */
    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                LocalDateTime now = LocalDateTime.now();
                Duration diff = Duration.between(_createdAt, now);
                long diffSeconds = diff.getSeconds();
                String nowStr = now.format(_dateFormatter);
                String msg = "MainActivityの起動時刻: " + _createdAtStr + "\nトースト表示時刻: " + nowStr + "\n差: " + diffSeconds + "秒";
                Activity parent = getActivity();
                Toast.makeText(parent, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
