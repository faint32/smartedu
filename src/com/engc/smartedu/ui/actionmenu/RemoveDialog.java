package com.engc.smartedu.ui.actionmenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.engc.smartedu.R;

import com.engc.smartedu.ui.interfaces.IRemoveItem;

/**
 * User: qii
 * Date: 12-9-11
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class RemoveDialog extends DialogFragment {


    private int positon;

    public RemoveDialog() {

    }

    public RemoveDialog(int positon) {

        this.positon = positon;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("positon", positon);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

            positon = savedInstanceState.getInt("position");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.askdelete))
                .setMessage(getString(R.string.askdeletemessage))
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IRemoveItem iRemoveItem = (IRemoveItem) getTargetFragment();
                        iRemoveItem.removeItem(positon);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IRemoveItem iRemoveItem = (IRemoveItem) getTargetFragment();
                        iRemoveItem.removeCancel();
                    }
                });

        return builder.create();
    }
}
