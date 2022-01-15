package local.hal.st31.android.post2db90032;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class ResultDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle extras = getArguments();
        String msg = extras.getString("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dlg_msg_title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.dlg_btn_close, new MsgDialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class MsgDialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    }
}
