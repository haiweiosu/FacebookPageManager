package haiweisu.facebookpagesmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by haiweisu on 8/24/17.
 */

public class PostMessages extends AppCompatActivity implements View.OnClickListener {

    public enum post_type {SCHEDULED, DRAFT, ADS_POST};
    private static final String PAGE_ID = "1908563859417632";

    private Button btnDatePicker, btnTimePicker;
    private EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long unixTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);

        btnDatePicker=(Button)findViewById(R.id.datebutton);
        btnTimePicker=(Button)findViewById(R.id.timebutton);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

    }

}
