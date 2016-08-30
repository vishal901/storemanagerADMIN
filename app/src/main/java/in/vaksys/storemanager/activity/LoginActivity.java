package in.vaksys.storemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.response.login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.textView)
    TextView textView;

    @Bind(R.id.txt_forgot_password_login)
    TextView txtForgotPasswordLogin;
    @Bind(R.id.btn_login_login)
    Button btnLoginLogin;
    @Bind(R.id.edt_user_name_login)
    EditText edtUserNameLogin;
    @Bind(R.id.edt_password_login)
    EditText edtPasswordLogin;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    ApiInterface apiInterface;
    PreferenceHelper preferenceHelper;
    @Bind(R.id.layout_parent)
    LinearLayout layoutParent;


    private Toolbar toolbar;
    private String UserName, Password;
    private String type, user_key, branchid;
    private String flag = "";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        preferenceHelper = new PreferenceHelper(this, "type");
        flag = preferenceHelper.LoadStringPref(AppConfig.PREF_FLAG,"");
        ApiClient.showLog("flag",flag);
        if (flag.equalsIgnoreCase("yes")){

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LOGIN");
        btnLoginLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (!validateFirstName()) {
            return;
        }
        if (!validatepassword()) {
            return;
        }

        UserName = edtUserNameLogin.getText().toString().trim();
        Password = edtPasswordLogin.getText().toString().trim();

        Login_Network_Call(UserName, Password);


    }

    private void Login_Network_Call(final String userName, String password) {

        progressBar.setVisibility(View.VISIBLE);
        btnLoginLogin.setVisibility(View.GONE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<login> call = apiInterface.LOGIN_RESPONSE_CALL(userName, password);
        call.enqueue(new Callback<login>() {
            @Override
            public void onResponse(Call<login> call, Response<login> response) {


                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {




                    progressBar.setVisibility(View.GONE);
                    btnLoginLogin.setVisibility(View.VISIBLE);

                    if (!response.body().isError()) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                        type = response.body().getData().getType();
                        username = response.body().getData().getUsername();
                        user_key = response.body().getData().getCsrfKey();
                        branchid = response.body().getData().getBranch();
                        ApiClient.showLog("key", user_key);
                        ApiClient.showLog("branch id ", branchid);

                        preferenceHelper.initPref();
                        preferenceHelper.SaveStringPref(AppConfig.PREF_USER_TYPE, type);
                        preferenceHelper.SaveStringPref(AppConfig.PREF_USER_KEY, user_key);
                        preferenceHelper.SaveStringPref(AppConfig.PREF_BRANCH_ID, branchid);
                        preferenceHelper.SaveStringPref(AppConfig.PREF_NAME, userName);
                        preferenceHelper.SaveStringPref(AppConfig.PREF_FLAG, "yes");
                        preferenceHelper.ApplyPref();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);


                        finish();


                    } else {

                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {


                    progressBar.setVisibility(View.GONE);
                    btnLoginLogin.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<login> call, Throwable t) {


                ApiClient.showmsg(layoutParent,"No Internet Access");

                progressBar.setVisibility(View.GONE);
                btnLoginLogin.setVisibility(View.VISIBLE);
            }
        });
    }


    private boolean validateFirstName() {
        if (edtUserNameLogin.getText().toString().trim().isEmpty()) {
            edtUserNameLogin.setError(getString(R.string.err_msg_first_name));
            requestFocus(edtUserNameLogin);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatepassword() {
        if (edtPasswordLogin.getText().toString().trim().isEmpty()) {
            edtPasswordLogin.setError(getString(R.string.err_msg_password));
            requestFocus(edtPasswordLogin);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
