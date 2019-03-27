package com.janhoracek.doitwithandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
    public static final String EXTRA_PRIORITY = "com.janhoracek.doitwithandroid.EXTRA_PRIORITX";

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;
    private Toolbar mToolbar;
    private TextView mTextViewAddDate;
    SimpleDateFormat mDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTextTitle = findViewById(R.id.edit_task_title);
        mEditTextDescription = findViewById(R.id.edit_task_desciption);
        mNumberPickerPriority = findViewById(R.id.number_pickser_priority);
        mToolbar = findViewById(R.id.add_task_toolbar);
        mTextViewAddDate = findViewById(R.id.text_view_add_date);
        mDateFormat = new SimpleDateFormat("d. MMMM yyyy    HH:mm");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNumberPickerPriority.setMinValue(1);
        mNumberPickerPriority.setMaxValue(3);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Task");


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
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })

                        .title("Simple")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
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

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        setResult(RESULT_OK, data);
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
