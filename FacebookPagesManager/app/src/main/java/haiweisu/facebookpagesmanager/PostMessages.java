package haiweisu.facebookpagesmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by haiweisu on 8/24/17.
 */

public class PostMessages extends AppCompatActivity implements View.OnClickListener {

    private static final String pageId = "1908563859417632";
    private static final String accountPath = "/me/accounts";
    private static final String getPageFeed = "/" + pageId + "/feed";
    private Button btnChooseDate, btnChooseTime;
    private EditText txtDate, txtTime;
    private int fbYear, fbMonth, fbDay, fbHour, fbMinute;
    private long unixTimeStamp;
    private static final String shareFbPage = "Shared on Facebook Page.";
    private static final String saveFbPage = "Saved on facebook page.";
    private static final String errorMessage = "Error Messages :";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_messages);

        btnChooseDate = (Button) findViewById(R.id.dateButton);
        btnChooseTime = (Button) findViewById(R.id.timeButton);
        txtDate = (EditText) findViewById(R.id.dateField);
        txtTime = (EditText) findViewById(R.id.timeField);

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
        unixTimeStamp = calendar.getTimeInMillis() / 1000;
    }

    public void schedulePost(View v) {
    }

    public void viewPosts(View v) {
        Intent curIntent = new Intent(PostMessages.this, ViewPosts.class);
        startActivity(curIntent);
    }

    /**
     * This function will post the published page status to Facebook Page -- Mama Su's Kitchen
     * @param v
     */
    public void fbPost(View v) {
        //has to be declared as final otherwise line 116 text doesn't work
        final EditText text = (EditText) findViewById(R.id.messagesField);
        final CheckBox scheduleBx = (CheckBox) findViewById(R.id.scheduleCheckBox);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), accountPath, null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        onFbPostComplete(response, text, scheduleBx);
                    }
                }
        ).executeAsync();
    }

    private void onFbPostComplete(GraphResponse response, EditText text, final CheckBox scheduleBx) {
        Bundle bundle = new Bundle();
        System.out.println(AccessToken.getCurrentAccessToken().toString());
        bundle.putString("message", text.getText().toString());

        if (scheduleBx.isChecked()) {
            bundle.putString("posted", "false");
            bundle.putLong("scheduled_post_time", unixTimeStamp);
        }
        JSONObject jsonObject = response.getJSONObject();
        try {
            JSONArray infos = jsonObject.getJSONArray("data");
            JSONObject objectInfo = infos.getJSONObject(0);
            String accessToken = objectInfo.getString("access_token");

            // Getting the access token object for corresponding Facebook Page
            // If not using access token for page instead of account accesstoken
            // Post will become "Visitor comment" instead of official Page Post
            AccessToken accessToken_obj = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(),
                    AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(),
                    AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(),
                    AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());
            // Output the accesstoken
            Log.d("AccessToken", accessToken_obj.toString());
            new GraphRequest(accessToken_obj, getPageFeed, bundle, HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            if (response.getError() == null) {
                                if (scheduleBx.isChecked()) {
                                    Toast.makeText(PostMessages.this, "Time for "
                                            + txtDate.getText().toString() + ", "
                                            + txtTime.getText().toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(PostMessages.this, shareFbPage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(PostMessages.this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                                Log.d(response.getError().getErrorMessage(), errorMessage);
                            }
                        }
                    }).executeAsync();
        } catch (JSONException e) {
            Log.d(e.getMessage(), errorMessage);
        } catch (Exception e) {
            Log.d(e.getMessage(), errorMessage);
        }
    }

    /**
     * This function will store the unpublished posts to the corresponding section and not post to actual Facebook Page
     * @param v
     */
    public void fbSave(View v) {
        final EditText text = (EditText) findViewById(R.id.messagesField);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), accountPath, null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        onFbSaveComplete(response, text);
                    }
                }
        ).executeAsync();
    }

    private void onFbSaveComplete(GraphResponse response, EditText text) {
        Bundle bundle = new Bundle();
        bundle.putString("message", text.getText().toString());
        bundle.putSerializable("unpublished_content_type", PostType.DRAFT);
        bundle.putString("published", "false");
        JSONObject JsonObject = response.getJSONObject();
        try {
            JSONArray infos = JsonObject.getJSONArray("data");
            JSONObject objectInfo = infos.getJSONObject(0);
            String accessToken = objectInfo.getString("access_token");

            // Creating a Facebook Pages ID access token
            AccessToken accessToken_obj = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(),
                    AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(),
                    AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(),
                    AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());
            Log.d("Access Token :", AccessToken.getCurrentAccessToken().getToken());
            new GraphRequest(accessToken_obj, getPageFeed, bundle, HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            if (response.getError() == null)
                                Toast.makeText(PostMessages.this, saveFbPage, Toast.LENGTH_LONG).show();
                            else {
                                Toast.makeText(PostMessages.this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                                Log.d(response.getError().getErrorMessage(), errorMessage);
                            }
                        }
                    }).executeAsync();

        } catch (JSONException j) {
            Log.d(j.getMessage(), errorMessage);
        }
    }

    public enum PostType {SCHEDULED, DRAFT, ADS_POST}

}
