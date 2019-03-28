package com.janhoracek.doitwithandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.time.format.FormatStyle.LONG;
import static java.time.format.FormatStyle.MEDIUM;

public class AddTaskActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.janhoracek.doitwithandroid.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.janhoracek.doitwithandroid.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.janhoracek.doitwithandroid.EXTRA_PRIORITY";
    public static final String EXTRA_DURATION = "com.janhoracek.doitwithandroid.EXTRA_DURATION";
    public static final String EXTRA_DEADLINE = "com.janhoracek.doitwithandroid.EXTRA_DEADLINE";

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;
    private NumberPicker mNumberPickerHours;
    private NumberPicker mNumberPickerMinutes;
    private Toolbar mToolbar;
    private TextView mTextViewAddDate;
    private SimpleDateFormat mDateFormat;
    private Date mDeadline;
    private SimpleDateFormat mDateFormatIso8601;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTextTitle = findViewById(R.id.edit_task_title);
        mEditTextDescription = findViewById(R.id.edit_task_desciption);
        mNumberPickerPriority = findViewById(R.id.number_pickser_priority);
        mNumberPickerHours = findViewById(R.id.number_picker_duration_hours);
        mNumberPickerMinutes = findViewById(R.id.number_picker_duration_minutes);
        mToolbar = findViewById(R.id.add_task_toolbar);
        mTextViewAddDate = findViewById(R.id.text_view_add_date);
        mDateFormat = new SimpleDateFormat("d. MMMM yyyy    HH:mm");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Task");

        mNumberPickerPriority.setMinValue(1);
        mNumberPickerPriority.setMaxValue(3);

        mNumberPickerHours.setMinValue(0);
        mNumberPickerHours.setMaxValue(72);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 15;
                return "" + temp;
            }
        };
        mNumberPickerMinutes.setFormatter(formatter);

        mNumberPickerMinutes.setMinValue(0);
        mNumberPickerMinutes.setMaxValue(3);

        mTextViewAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SingleDateAndTimePickerDialog.Builder(v.getContext())
                        //.bottomSheet()
                        .curved()
                        //.minutesStep(15)
                        //.displayHours(false)
                        //.displayMinutes(false)
                        //.todayText("aujourd'hui")
                        //.backgroundColor(Color.BLACK)
                        //.mainColor(Color.GREEN)
                        //.titleTextColor(Color.GREEN)
                        .mustBeOnFuture()
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })

                        .title("Simple")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                mDeadline = date;
                                //String myString = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(date);
                                String myString = mDateFormat.format(date);
                                mTextViewAddDate.setText(myString);
                            }
                        }).display();
            }
        });

    }

    private void saveTask() {
        String title = mEditTextTitle.getText().toString();
        String description = mEditTextDescription.getText().toString();
        int priority = mNumberPickerPriority.getValue();
        Date deadline = mDeadline;
        int duration_time = mNumberPickerHours.getValue() * 60 + mNumberPickerMinutes.getValue() * 15;


        if (title.trim().isEmpty() || description.trim().isEmpty() || duration_time == 0 || deadline == null) {
            Toast.makeText(this, "Please insert title and description and duration", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DURATION, duration_time);
        data.putExtra(EXTRA_DEADLINE, deadline);

        setResult(RESULT_OK, data);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDeadline);

        Log.d("DIWD", "date year: " + calendar.get(Calendar.YEAR));
        Log.d("DIWD", "date month: " + calendar.get(Calendar.MONTH));
        Log.d("DIWD", "date day: " + calendar.get(Calendar.DAY_OF_MONTH) + 1);
        Log.d("DIWD", "time: " + calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));


        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                saveTask();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
