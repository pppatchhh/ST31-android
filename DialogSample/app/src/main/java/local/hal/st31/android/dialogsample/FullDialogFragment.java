package local.hal.st31.android.dialogsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

/**
 * ST31 Androidサンプル12 ダイアログ
 *
 * 通常ダイアログクラス。
 *
 * @author Shinzo SAITO
 */
public class FullDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle(R.string.dlg_full_title);
        builder.setMessage(R.string.dlg_full_msg);
        builder.setPositiveButton(R.string.dlg_btn_ok, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dlg_btn_nu, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dlg_btn_ng, new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * ダイアログのボタンが押されたときの処理が記述されたメンバクラス。
     */
    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Activity parent = getActivity();
            String msg = "";
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    msg = getString(R.string.dlg_full_toast_ok);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    msg = getString(R.string.dlg_full_toast_nu);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    msg = getString(R.string.dlg_full_toast_ng);
                    break;
            }
            Toast.makeText(parent, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
