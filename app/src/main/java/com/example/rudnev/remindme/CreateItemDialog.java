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

import com.example.rudnev.remindme.dto.RemindDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private RemindDTO mUpdateRemindItem;


    public interface EditNameDialogListener {
        void onFinishEditDialog(RemindDTO remindItem, boolean fromEditDialog);
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
        if(mUpdateRemindItem!=null){
            mEditTextTitle.setText(mUpdateRemindItem.getTitle());
            mEditTextNote.setText(mUpdateRemindItem.getNote());
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                //edit item clicked
                if(mUpdateRemindItem!=null) {
                    mUpdateRemindItem.setTitle(mEditTextTitle.getText().toString());
                    mUpdateRemindItem.setNote(mEditTextNote.getText().toString());
                    mUpdateRemindItem.setDate(date.getTime());
                    EditNameDialogListener fragment = (EditNameDialogListener) getTargetFragment();
                    fragment.onFinishEditDialog(mUpdateRemindItem, fromEditDialog);
                }else{
                    if(getTargetRequestCode() == 2){
                        EditNameDialogListener dialog = (EditNameDialogListener) getTargetFragment();
                        RemindDTO mNewRemindItem = new RemindDTO(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate);
                        dialog.onFinishEditDialog(mNewRemindItem, fromEditDialog);
                    }else{
                        EditNameDialogListener fragment = (EditNameDialogListener) getTargetFragment();
                        RemindDTO mNewRemindItem = new RemindDTO(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate);
                        fragment.onFinishEditDialog(mNewRemindItem, fromEditDialog);
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
            EditNameDialogListener fragment = (EditNameDialogListener) getTargetFragment();
            if(getArguments()!=null){
                itemID = mUpdateRemindItem.getId();
            }
            RemindDTO remindItem = new RemindDTO(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate);
            fragment.onFinishEditDialog(remindItem, fromEditDialog);
            this.dismiss();
            return true;
        }
        return false;
    }

    public void setmUpdateRemindItem(RemindDTO mUpdateRemindItem) {
        this.mUpdateRemindItem = mUpdateRemindItem;
    }
}
