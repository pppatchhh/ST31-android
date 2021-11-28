package local.hal.st31.android.favoriteshops90032;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {
    private DatabaseHelper _helper;
    private long _idNo = 0;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle(R.string.dlg_full_title);
        builder.setMessage(R.string.dlg_full_msg);
        builder.setPositiveButton(R.string.dlg_btn_ok, new DeleteDialogButtonClickListener());
        builder.setNegativeButton(R.string.dlg_btn_cancel, new DeleteDialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DeleteDialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which){
            Activity parent = getActivity();
            switch(which){
                case DialogInterface.BUTTON_POSITIVE:
                    _helper = new DatabaseHelper(parent.getApplicationContext());
                    SQLiteDatabase db = _helper.getWritableDatabase();
                    Bundle extras = getArguments();
                    if(extras != null){
                        _idNo = extras.getLong("id");
                    }
                    DataAccess.delete(db, _idNo);
                    parent.finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
