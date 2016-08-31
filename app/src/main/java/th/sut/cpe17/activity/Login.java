package th.sut.cpe17.activity;

import android.app.ProgressDialog;
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
import th.sut.cpe17.util.OkHttpRequest;

public class Login extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassWord;
    private Button buttonSignIn;
    private ProgressDialog progressDialog;

    private String userName;
    private String passWord;

    OkHttpRequest httpRequest = new OkHttpRequest();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        infixView();

        editTextUserName.setText("test");
        editTextPassWord.setText("1234");

        buttonSignIn.setOnClickListener(buttonSignInListener());
    }


    private View.OnClickListener buttonSignInListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editTextUserName.getText().toString();
                passWord = editTextPassWord.getText().toString();
                final StringBuffer responseString = new StringBuffer("");

                if (userName.length() == 0 || passWord.length() == 0) {
                    Toast.makeText(Login.this, "Please fill in complete data", Toast.LENGTH_SHORT).show();
                } else {
                    progressShow();
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseString.append(httpRequest.HTTPGet(Constant.URL_LOGIN));
                                Gson gson = new Gson();
                                LoginModel login = gson.fromJson(responseString.toString(), LoginModel.class);

                                checkLogIn(login.getStatus(), login.getUserName(), login.getPassWord());

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

    private void checkLogIn(Boolean status, String userName, String passWord) {
        progressDialog.dismiss();
        if (status && userName.equals(this.userName) && passWord.equals(this.passWord)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Login.this, "Login Fail", Toast.LENGTH_SHORT).show();
                }
            });
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
}
