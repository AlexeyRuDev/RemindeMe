package com.example.rudnev.remindme;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class CreateItemDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private Button mOkBtn;
    private Button mCloseBtn;
    private Date date;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText, String note, String date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_item_dialog, container);
        date = new Date();
        mEditTextTitle = (EditText) view.findViewById(R.id.titleText);
        mEditTextNote = (EditText) view.findViewById(R.id.noteText);
        mOkBtn = (Button) view.findViewById(R.id.createBtn);
        mCloseBtn = (Button) view.findViewById(R.id.closeBtn);

        mEditTextTitle.setBackgroundResource(R.drawable.edit_text_bg);
        mEditTextNote.setBackgroundResource(R.drawable.edit_text_bg);

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNameDialogListener activity = (EditNameDialogListener) getActivity();

                activity.onFinishEditDialog(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), date.toString());
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == i) {
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            activity.onFinishEditDialog(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), date.toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
