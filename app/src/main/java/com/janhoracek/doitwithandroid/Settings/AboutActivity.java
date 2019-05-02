package com.janhoracek.doitwithandroid.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.janhoracek.doitwithandroid.BuildConfig;
import com.janhoracek.doitwithandroid.R;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
/**
 * Activity which contains information about application to be shown to user
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTextViewVersion;
    private TextView mTextViewMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mToolbar = findViewById(R.id.settings_toolbar);
        mTextViewVersion = findViewById(R.id.about_version_number);
        mTextViewMail = findViewById(R.id.about_contact_data);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        mTextViewVersion.setText(String.valueOf(BuildConfig.VERSION_CODE));

        mTextViewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send feedback via email
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(getString(R.string.me_mail)) +
                        "?subject=" + Uri.encode(getString(R.string.mail_subject)) +
                        "&body=" + Uri.encode(getString(R.string.mail_text));
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, getString(R.string.mail_choose)));
            }
        });
        setTitle(getString(R.string.action_about));
    }


}
