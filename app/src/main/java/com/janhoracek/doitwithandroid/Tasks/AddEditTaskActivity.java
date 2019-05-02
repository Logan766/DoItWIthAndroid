package com.janhoracek.doitwithandroid.Tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.janhoracek.doitwithandroid.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.graphics.Color.rgb;
import static java.time.format.FormatStyle.LONG;
import static java.time.format.FormatStyle.MEDIUM;
/**
 * Activity which is used to create, reopen or edit task
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class AddEditTaskActivity extends AppCompatActivity {
    public static final String REOPEN_REQUEST = "com.janhoracek.doitwithandroid.REOPEN_REQUEST";
    public static final String EXTRA_ID = "com.janhoracek.doitwithandroid.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.janhoracek.doitwithandroid.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.janhoracek.doitwithandroid.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.janhoracek.doitwithandroid.EXTRA_PRIORITY";
    public static final String EXTRA_DURATION = "com.janhoracek.doitwithandroid.EXTRA_DURATION";
    public static final String EXTRA_DEADLINE = "com.janhoracek.doitwithandroid.EXTRA_DEADLINE";
    public static final String EXTRA_COMPLETED = "com.janhoracek.doitwithandroid.EXTRA_COMPLETED";

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;
    private NumberPicker mNumberPickerHours;
    private NumberPicker mNumberPickerMinutes;
    private Toolbar mToolbar;
    private TextView mTextViewAddDate;
    private SimpleDateFormat mDateFormat;
    private Date mDeadline;
    private ImageView mPriorityCircle;
    private SimpleDateFormat mDateFormatIso8601;

    private int mCompleted;

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
        mPriorityCircle = findViewById(R.id.circle_priority);
        mDateFormat = new SimpleDateFormat(getString(R.string.add_edit_task_date_format));

        mNumberPickerPriority.setMinValue(1);
        mNumberPickerPriority.setMaxValue(3);
        mNumberPickerPriority.setDisplayedValues(new String[] {getString(R.string.add_edit_task_priority_high), getString(R.string.add_edit_task_priority_medium), getString(R.string.add_edit_task_priority_low)});
        mNumberPickerPriority.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mNumberPickerPriority.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal){
                    case 1:
                        mPriorityCircle.getDrawable().setColorFilter(getResources().getColor(R.color.PastelRed), PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        mPriorityCircle.getDrawable().setColorFilter(getResources().getColor(R.color.PastelYellow), PorterDuff.Mode.SRC_IN);
                        break;
                    case 3:
                        mPriorityCircle.getDrawable().setColorFilter(getResources().getColor(R.color.PastelGreen), PorterDuff.Mode.SRC_IN);
                        break;
                }
            }
        });

        mNumberPickerHours.setMinValue(0);
        mNumberPickerHours.setMaxValue(72);
        mNumberPickerHours.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 15;
                return String.valueOf(temp);
            }
        };
        mNumberPickerMinutes.setFormatter(formatter);

        mNumberPickerMinutes.setMinValue(0);
        mNumberPickerMinutes.setMaxValue(3);
        mNumberPickerMinutes.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mTextViewAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SingleDateAndTimePickerDialog.Builder(v.getContext())
                        .mainColor(getResources().getColor(R.color.colorPrimaryDark))
                        .curved()
                        .mainColor(getResources().getColor(R.color.colorPrimaryDark))
                        .titleTextColor(Color.BLACK)
                        .mustBeOnFuture()
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })

                        .title(getString(R.string.add_edit_task_deadline_pick_header))
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                mDeadline = date;
                                String myString = mDateFormat.format(date);
                                mTextViewAddDate.setText(myString);
                            }
                        }).display();
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Intent intent = getIntent();


        if(intent.hasExtra(EXTRA_ID)) {
            if(intent.hasExtra(REOPEN_REQUEST)) {
                setTitle(R.string.add_edit_task_title_reopen_task);
            } else {
                setTitle(R.string.add_edit_task_title_edit_task);
            }

            switch (intent.getIntExtra(EXTRA_PRIORITY, 1)){
                case 1:
                    mPriorityCircle.getDrawable().setColorFilter(getResources().getColor(R.color.PastelRed), PorterDuff.Mode.SRC_IN);
                    break;
                case 2:
                    mPriorityCircle.getDrawable().setColorFilter(getResources().getColor(R.color.PastelYellow), PorterDuff.Mode.SRC_IN);
                    break;
                case 3:
                    mPriorityCircle.getDrawable().setColorFilter(getResources().getColor(R.color.PastelGreen), PorterDuff.Mode.SRC_IN);
                    break;
            }
            mDeadline = (Date) intent.getSerializableExtra(EXTRA_DEADLINE);
            mEditTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mEditTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            mTextViewAddDate.setText(mDateFormat.format(mDeadline));
            mNumberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
            mNumberPickerHours.setValue(intent.getIntExtra(EXTRA_DURATION, 1)/60);
            mNumberPickerMinutes.setValue((intent.getIntExtra(EXTRA_DURATION, 1) % 60) / 15 );
            mCompleted = intent.getIntExtra(EXTRA_COMPLETED, 0);


            //Number picker show value fix
            try {
                Method method = mNumberPickerMinutes.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                method.setAccessible(true);
                method.invoke(mNumberPickerMinutes, true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            setTitle(R.string.add_edit_task_title_add_task);
        }
    }

    /**
     * Collects data and passes them back to FragmentTask to be saved
     */
    private void saveTask() {
        String title = mEditTextTitle.getText().toString();
        String description = mEditTextDescription.getText().toString();
        int priority = mNumberPickerPriority.getValue();
        Date deadline = mDeadline;
        int duration_time = mNumberPickerHours.getValue() * 60 + mNumberPickerMinutes.getValue() * 15;

        //validation
        if (title.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.add_edit_task_title_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.trim().isEmpty()) {
            description = "-";
        }

        if (duration_time == 0) {
            Toast.makeText(this, getString(R.string.add_edit_task_duration_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (deadline == null) {
            Toast.makeText(this, getString(R.string.add_edit_task_deadline_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        //put data to Intent
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DURATION, duration_time);
        data.putExtra(EXTRA_DEADLINE, deadline);
        data.putExtra(EXTRA_COMPLETED, mCompleted);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        //catch error
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDeadline);
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
