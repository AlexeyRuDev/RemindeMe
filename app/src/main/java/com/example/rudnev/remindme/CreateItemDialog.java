package com.example.rudnev.remindme;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class CreateItemDialog extends DialogFragment implements TextView.OnEditorActionListener {

    int DIALOG_DATE = 1;
    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private Button mOkBtn;
    private Button mCloseBtn;
    private Calendar date;
    private Date formatDate;
    private long itemID;
    private boolean fromEditDialog;

    public interface EditNameDialogListener {
        void onFinishEditDialog(long itemID, String inputText, String note, Date date, boolean fromEditDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_item_dialog, container);
        //date = CalendarDay.today();
        if(date == null)
            date = Calendar.getInstance();
        Date testDate = date.getTime();
        mEditTextTitle = (EditText) view.findViewById(R.id.titleText);
        mEditTextNote = (EditText) view.findViewById(R.id.noteText);
        mTextViewDate = (TextView) view.findViewById(R.id.dateText);
        mTextViewTime = (TextView) view.findViewById(R.id.timeText);
        mOkBtn = (Button) view.findViewById(R.id.createBtn);
        mCloseBtn = (Button) view.findViewById(R.id.closeBtn);
        //Set style
        mTextViewDate.setBackgroundResource(R.drawable.edit_text_bg);
        mTextViewTime.setBackgroundResource(R.drawable.edit_text_bg);
        mEditTextTitle.setBackgroundResource(R.drawable.edit_text_bg);
        mEditTextNote.setBackgroundResource(R.drawable.edit_text_bg);
        //if open edit dialog
        if(getArguments()!=null){
            mEditTextTitle.setText(getArguments().getString("title"));
            mEditTextNote.setText(getArguments().getString("note"));
            //formatDate = getArguments().getString("date");
            //itemID = getArguments().getLong("itemID");
            fromEditDialog = true;
        }else{
            //formatDate = date.getYear() + "/" + (date.getMonth() + 1) + "/" + date.getDay();
            //formatDate = date.getTime();
            fromEditDialog = false;
        }

        mTextViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        mTextViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(view);
            }
        });

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments()!=null) {
                    Intent intent = new Intent();
                    intent.putExtra("title", mEditTextTitle.getText().toString());
                    intent.putExtra("note", mEditTextNote.getText().toString());
                    intent.putExtra("date", date.getTimeInMillis());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }else{
                    if(getTargetRequestCode() == 2){
                        EditNameDialogListener dialog = (EditNameDialogListener) getTargetFragment();
                        dialog.onFinishEditDialog(itemID, mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate, fromEditDialog);
                    }else{
                        EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                        activity.onFinishEditDialog(itemID, mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate, fromEditDialog);
                    }

                }
                dismiss();
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setInitialDateTime();
        return view;
    }

    public void setTime(View v) {
        new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, t,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE), true)
                .show();
    }

    public void setDate(View v) {
        new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setDateField(Calendar date){
        this.date = date;
    }

    private void setInitialDateTime() {

        mTextViewDate.setText(DateUtils.formatDateTime(getContext(),
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        mTextViewTime.setText(DateUtils.formatDateTime(getContext(),
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
        formatDate = date.getTime();
    }

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        long itemID = 0;
        if (EditorInfo.IME_ACTION_DONE == i) {
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            if(getArguments()!=null){
                itemID = getArguments().getLong("itemID");
            }
            activity.onFinishEditDialog(itemID, mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate, fromEditDialog);
            this.dismiss();
            return true;
        }
        return false;
    }
}
