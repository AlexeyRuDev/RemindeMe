package com.example.rudnev.remindme.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.rudnev.remindme.NotificationReceiver;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateItemActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.create_item_activity;

    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private Intent resultIntent;
    private RemindDTO remindItem;
    private Notes noteItem;
    private boolean isNote;
    private boolean fillFromNote;
    private boolean charFromNote;
    private TextWatcher generalTextWatcher;


    private Date formatDate;
    private Calendar date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppDefault);
        setContentView(LAYOUT);

        charFromNote = false;
        fillFromNote = true;

        initTextWatcher();
        initTextFields();

        checkIncomingItemAndFillInTheFields();

        initButtons();

    }

    private void checkIncomingItemAndFillInTheFields(){
        LinearLayout dateLayout = (LinearLayout) findViewById(R.id.dateLayout);
        resultIntent = getIntent();
        if (resultIntent != null) {
            setDateField((Calendar) resultIntent.getSerializableExtra("mDateField"));
            isNote = resultIntent.getBooleanExtra("isNote", false);
            if (isNote) {
                dateLayout.setVisibility(View.GONE);
            } else {
                dateLayout.setVisibility(View.VISIBLE);
            }
            remindItem = (RemindDTO) resultIntent.getParcelableExtra("mRemindItem");
            noteItem = (Notes) resultIntent.getParcelableExtra("mNoteItem");
            if (remindItem != null) {
                date = Calendar.getInstance();
                date.setTime(remindItem.getDate().toDate());
                mEditTextTitle.setText(remindItem.getTitle());
                mEditTextNote.setText(remindItem.getNote());
            } else if (noteItem != null) {
                mEditTextTitle.setText(noteItem.getTitle());
                mEditTextNote.setText(noteItem.getNote());
            }
        }

        if (!isNote) {
            if (date == null)
                date = Calendar.getInstance();
            setInitialDateTime();
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
        }
    }

    private void initButtons(){
        Button mOkBtn = (Button) findViewById(R.id.createBtn);
        Button mCloseBtn = (Button) findViewById(R.id.closeBtn);

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditTextTitle.getText().toString().isEmpty()){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    mEditTextTitle.setText(sdf.format(date.getTime()));
                }
                setResultFromActivity(RESULT_OK);
            }
        });
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResultFromActivity(RESULT_CANCELED);
            }
        });
    }

    //Init fields and set them style
    private void initTextFields(){
        mEditTextTitle = (EditText) findViewById(R.id.titleText);
        mEditTextTitle.addTextChangedListener(generalTextWatcher);
        mEditTextNote = (EditText) findViewById(R.id.noteText);
        mEditTextNote.addTextChangedListener(generalTextWatcher);
        mTextViewDate = (TextView) findViewById(R.id.dateText);
        mTextViewTime = (TextView) findViewById(R.id.timeText);
        setFieldsStyle();
    }

    private void setResultFromActivity(int resultCode) {
        if (resultIntent == null) {
            resultIntent = new Intent();
        }
        if (!isNote) {
            if (remindItem == null) {
                remindItem = new RemindDTO(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), LocalDateTime.fromDateFields(formatDate));
                resultIntent.putExtra("updateItem", false);
            } else {
                cancelOldNotification(remindItem);
                remindItem.setTitle(mEditTextTitle.getText().toString());
                remindItem.setNote(mEditTextNote.getText().toString());
                remindItem.setDate(LocalDateTime.fromDateFields(date.getTime()));
                resultIntent.putExtra("updateItem", true);
            }
            if(resultCode < 0) {
                scheduleNotification(getNotification(remindItem.getTitle(), remindItem.getDate().toDate().getTime()), remindItem);
            }
            resultIntent.putExtra("mRemindItem", remindItem);

        } else {
            if (noteItem == null) {
                noteItem = new Notes(mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate);
                resultIntent.putExtra("updateItem", false);
            } else {
                noteItem.setTitle(mEditTextTitle.getText().toString());
                noteItem.setNote(mEditTextNote.getText().toString());
                resultIntent.putExtra("updateItem", true);
            }
            resultIntent.putExtra("mNoteItem", noteItem);
        }
        setResult(resultCode, resultIntent);
        finish();
    }

    public void setTime(View v) {
        new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, t,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE), true){
                    @Override
                    public void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                }
                .show();
    }

    public void setDate(View v) {
        new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)){
                    @Override
                    public void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                }
                .show();
    }

    //Add field style
    private void setFieldsStyle() {
        mTextViewDate.setBackgroundResource(R.drawable.edit_text_bg);
        mTextViewTime.setBackgroundResource(R.drawable.edit_text_bg);
        mEditTextTitle.setBackgroundResource(R.drawable.edit_text_bg);
        mEditTextNote.setBackgroundResource(R.drawable.edit_text_bg);
    }

    public void setDateField(Calendar date) {
        this.date = date;
    }

    private void setInitialDateTime() {

        mTextViewDate.setText(DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        mTextViewTime.setText(DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
        formatDate = date.getTime();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };


    //if title is empty then fill them from the note field
    private void initTextWatcher(){
         generalTextWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (mEditTextTitle.getText().hashCode() == s.hashCode())
                {
                    if(!mEditTextTitle.getText().toString().trim().isEmpty() && !charFromNote)
                        fillFromNote = false;
                }
                else if (mEditTextNote.getText().hashCode() == s.hashCode())
                {
                    charFromNote = true;
                    if(fillFromNote)
                        mEditTextTitle.setText(s);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditTextTitle.getText().hashCode() == s.hashCode())
                {

                }
                else if (mEditTextNote.getText().hashCode() == s.hashCode())
                {
                    charFromNote =false;
                }
            }

        };
    }

    //Create new notification
    private void scheduleNotification(Notification notification, RemindDTO remindItem) {
        int notificationID = remindItem.getDate().getYear() + remindItem.getDate().getMonthOfYear() + remindItem.getDate().getDayOfMonth() +
                remindItem.getDate().getHourOfDay() + remindItem.getDate().getMinuteOfHour() + remindItem.getDate().getSecondOfMinute();
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        long futureInMillis = remindItem.getDate().toDate().getTime();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, futureInMillis,AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

    }

    private Notification getNotification(String content, long when) {

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.ScheduledTitle));
        builder.setContentText(content);
        builder.setWhen(when);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        return builder.build();
    }

    //Remove notification if item has been deleted
    private void cancelOldNotification(RemindDTO remindItem){
        int notificationID = remindItem.getDate().getYear() + remindItem.getDate().getMonthOfYear() + remindItem.getDate().getDayOfMonth() +
                remindItem.getDate().getHourOfDay() + remindItem.getDate().getMinuteOfHour() + remindItem.getDate().getSecondOfMinute();
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, notificationID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        pendingIntent.cancel();
    }


}
