package haiweisu.facebookpagesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final List<String> PERMISSIONS = Arrays.asList("manage_pages", "publish_actions", "publish_pages", "read_insights");
    protected LoginButton loginButton;
    CallbackManager callbackManager;

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
        serializeAccessToken sAccessToken = new serializeAccessToken();
        sAccessToken.accessToken = AccessToken.getCurrentAccessToken();
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
                Log.e("cancel", "Cancelled log in");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Log in Error", e.toString());
            }
        });
        try {
            if (loginButton.getFragment() != null) {
                LoginManager.getInstance().logInWithPublishPermissions(loginButton.getFragment(), PERMISSIONS);
            } else {
                LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this, PERMISSIONS);
            }
        } catch (Exception e) {
            Log.d("this is exception", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Serialize the Access Token before put into storage
    class serializeAccessToken implements Serializable {
        AccessToken accessToken;
    }


}
