package local.hal.st31.android.itarticlecollection90032;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ErrorInfoDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        Bundle extras = getArguments();
        String msg = extras.getString("msg");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.msg_dialog_title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.btn_dialog_positive, new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){}
    }
}
