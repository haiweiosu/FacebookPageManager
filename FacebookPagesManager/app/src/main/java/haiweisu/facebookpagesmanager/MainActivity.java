package haiweisu.facebookpagesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    protected LoginButton loginButton;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private static final String TAG = "LoginButtonActivity";
    private static final List<String> PERMISSIONS = Arrays.asList("manage_pages,publish_actions,read_insights");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //The Facebook SDK is now auto initialized on Application start.
        // If you are using the Facebook SDK in the main process
        // and don't need a callback on SDK initialization completion
        // you can now remove calls to FacebookSDK.sdkInitialize.
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        // Get Log in Button
        loginButton = (LoginButton) findViewById(R.id.login_Button);
        loginButton.setReadPermissions(PERMISSIONS);
//
        accessToken = AccessToken.getCurrentAccessToken();
        if (AccessToken.getCurrentAccessToken() != null) {
            Intent curIntent = new Intent(this, PostMessages.class);
            startActivity(curIntent);
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent curIntent = new Intent(MainActivity.this, PostMessages.class);
                startActivity(curIntent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        if (loginButton.getFragment() != null) {
            LoginManager.getInstance().logInWithPublishPermissions(loginButton.getFragment(), PERMISSIONS);
        } else {
            try {
                LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this, PERMISSIONS);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
