package com.engc.smartedu.ui.actionmenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.engc.smartedu.R;

import com.engc.smartedu.bean.CommentBean;
import com.engc.smartedu.support.utils.GlobalContext;
import com.engc.smartedu.ui.browser.BrowserCommentActivity;
import com.engc.smartedu.ui.send.WriteReplyToCommentActivity;

/**
 * User: qii
 * Date: 12-12-6
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class CommentByIdFloatingMenu extends DialogFragment {

    private CommentBean bean;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("bean", bean);
    }

    public CommentByIdFloatingMenu() {

    }


    public CommentByIdFloatingMenu(CommentBean bean) {
        this.bean = bean;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            bean = (CommentBean) savedInstanceState.get("bean");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.bean.getUser().getScreen_name());
        String[] str = {getString(R.string.reply_to_comment), getString(R.string.view_comment)};
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case 0:
                        intent = new Intent(getActivity(), WriteReplyToCommentActivity.class);
                        intent.putExtra("token", GlobalContext.getInstance().getSpecialToken());
                        intent.putExtra("msg", bean);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), BrowserCommentActivity.class);
                        intent.putExtra("comment", bean);
                        intent.putExtra("token", GlobalContext.getInstance().getSpecialToken());
                        getActivity().startActivity(intent);
                        break;
                }

            }
        });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}

