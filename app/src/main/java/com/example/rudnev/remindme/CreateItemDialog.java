package com.example.rudnev.remindme;


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

public class CreateItemDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private Button mOkBtn;
    private Button mCloseBtn;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_item_dialog, container);
        mEditTextTitle = (EditText) view.findViewById(R.id.titleText);
        mEditTextNote = (EditText) view.findViewById(R.id.noteText);
        mOkBtn = (Button) view.findViewById(R.id.createBtn);
        mCloseBtn = (Button) view.findViewById(R.id.closeBtn);
        getDialog().setTitle("Hello");
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                activity.onFinishEditDialog(mEditTextTitle.getText().toString());
                dismiss();
            }
        });
        return view;
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == i) {
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            activity.onFinishEditDialog(mEditTextTitle.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
