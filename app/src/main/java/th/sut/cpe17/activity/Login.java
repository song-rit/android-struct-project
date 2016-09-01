package th.sut.cpe17.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import th.sut.cpe17.R;
import th.sut.cpe17.constant.Constant;
import th.sut.cpe17.model.LoginModel;
import th.sut.cpe17.session.SessionManager;
import th.sut.cpe17.util.OkHttpRequest;

/**
 * Created by Song-rit Maleerat on 31/8/2559.
 */

public class Login extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassWord;
    private Button buttonSignIn;
    private ProgressDialog progressDialog;

    private String userName;
    private String passWord;

    private OkHttpRequest httpRequest = new OkHttpRequest();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        infixView();

        // Set Username and Password
        editTextUserName.setText("test");
        editTextPassWord.setText("1234");

        buttonSignIn.setOnClickListener(buttonSignInListener());
    }

    private View.OnClickListener newActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    private View.OnClickListener buttonSignInListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get username and password from EditText
                userName = editTextUserName.getText().toString();
                passWord = editTextPassWord.getText().toString();

                // Declare variable for keep service response
                final StringBuffer responseString = new StringBuffer("");

                // Check null string
                if (userName.length() == 0 || passWord.length() == 0) {
                    Toast.makeText(Login.this, "Please fill in complete data", Toast.LENGTH_SHORT).show();
                } else {
                    progressShow();
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseString.append(httpRequest.HTTPGet(Constant.URL.LOGIN));
                                Gson gson = new Gson();

                                //Map json to login model
                                LoginModel login = gson.fromJson(responseString.toString(), LoginModel.class);

                                // Validate username and password
                                checkLogIn(login);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
            }
        };
    }

    private void checkLogIn(LoginModel login) {

        progressDialog.dismiss();

        // Validate status , username, password
        if (login.getStatus() && login.getUserName().equals(this.userName) && login.getPassWord().equals(this.passWord)) {

            // Call instance login session
            SessionManager session = SessionManager.getInstance();
            if (session.getSharedPreferences() == null) {
                session.setSharedPreferences(Login.this);
            }
            // Setting session data
            session.setSession(login);

            if (session.checkLoginValidate()) {
                toastShowText("Login Success");
                session.startMainActivity();
                finish();
            }
        } else {
           toastShowText("Login Fail !");
        }
    }

    private void progressShow() {
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void infixView() {
        buttonSignIn = (Button) findViewById(R.id.button_sign_in);
        editTextPassWord = (EditText) findViewById(R.id.edit_text_password);
        editTextUserName = (EditText) findViewById(R.id.edit_text_username);
    }

    private void toastShowText(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
