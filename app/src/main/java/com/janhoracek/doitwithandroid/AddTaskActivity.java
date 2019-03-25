package com.janhoracek.doitwithandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.janhoracek.doitwithandroid.R;

public class AddTaskActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.janhoracek.doitwithandroid.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.janhoracek.doitwithandroid.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.janhoracek.doitwithandroid.EXTRA_PRIORITX";

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mEditTextTitle = findViewById(R.id.edit_task_title);
        mEditTextDescription = findViewById(R.id.edit_task_desciption);
        mNumberPickerPriority = findViewById(R.id.number_pickser_priority);
        mToolbar = findViewById(R.id.add_task_toolbar);

        setSupportActionBar(mToolbar);


        mNumberPickerPriority.setMinValue(1);
        mNumberPickerPriority.setMaxValue(3);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Task");

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
