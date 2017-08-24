package haiweisu.facebookpagesmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
        // Initialize Access Token

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
//            Intent curIntent = new Intent(this, PostMessage.class);
            Intent curIntent = new Intent("hahah");
            startActivity(curIntent);
        }

        if (loginButton.getFragment() != null) {
            Log.i("here", "I'm here");
            LoginManager.getInstance().logInWithPublishPermissions(loginButton.getFragment(), PERMISSIONS);
        } else {
            LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this, PERMISSIONS);
        }


//        // Callback registration
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
