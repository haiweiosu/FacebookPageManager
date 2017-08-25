package haiweisu.facebookpagesmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by haiweisu on 8/24/17.
 */

public class PostMessages extends AppCompatActivity implements View.OnClickListener {

    public enum post_type {SCHEDULED, DRAFT, ADS_POST};
    private static final String PAGE_ID = "1908563859417632";

    private Button btnChooseDate, btnChooseTime;
    private EditText txtDate, txtTime;
    private int fbYear, fbMonth, fbDay, fbHour, fbMinute;
    private long unixTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_messages);

        btnChooseDate=(Button)findViewById(R.id.dateButton);
        btnChooseTime =(Button)findViewById(R.id.timeButton);
        txtDate=(EditText)findViewById(R.id.dateField);
        txtTime=(EditText)findViewById(R.id.timeField);

        btnChooseDate.setOnClickListener(this);
        btnChooseTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // If date is chosen
        if (v == btnChooseDate) {

            // Get Calendar instance
            Calendar calendar = Calendar.getInstance();
            fbYear = calendar.get(Calendar.YEAR);
            fbMonth = calendar.get(Calendar.MONTH);
            fbDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int datOfMonth) {
                            txtDate.setText(datOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, fbYear, fbMonth, fbDay);

            datePickerDialog.show();
        }

       // If Schedule is chosen
        if (v == btnChooseTime) {

            //Get Time
            Calendar calendar = Calendar.getInstance();
            fbHour = calendar.get(Calendar.HOUR_OF_DAY);
            fbMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, fbHour, fbMinute, false);
            timePickerDialog.show();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(fbYear, fbMonth, fbDay, fbHour, fbMinute);
        unixTimeStamp = calendar.getTimeInMillis()/1000;
    }

}
