package app.perfecto.com.expencemanager.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.ui.base.BaseActivity;
import app.perfecto.com.expencemanager.ui.main.MainActivity;
import app.perfecto.com.expencemanager.ui.register.RegisterActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.aflak.libraries.dialog.FingerprintDialog;


public class LoginActivity extends BaseActivity<LoginViewModel>  {


    private static final String IS_USER_EXIST = "IS_USER_EXIST";

    private LoginViewModel loginViewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @BindView(R.id.login_email)
    EditText emailET;

    @BindView(R.id.login_password)
    EditText passwordET;

    @BindView(R.id.btn_finger_print)
    AppCompatImageButton btnFingerPrint;

    @BindView(R.id.login_signup_btn)
    MaterialButton btnRegister;

    @BindView(R.id.login_login_btn)
    MaterialButton btnLogin;

    @BindView(R.id.login_biometric_check_box)
    AppCompatCheckBox chkBoxBiometric;

    @BindView(R.id.parent_contstraint_layout)
    LinearLayout parent;
    boolean isUserExist;
    boolean promptBiometric = false;

    private boolean showBiometricAuthenticFirstTime = false;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent getStartIntentForLogIn(Context context) {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(IS_USER_EXIST,true);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUnBinder(ButterKnife.bind(this));
        isUserExist = getIntent().getBooleanExtra(IS_USER_EXIST,false);
        setUp();
        observeOpenMainActivityEvent();
    }

    @OnClick(R.id.login_login_btn)
    void onServerLoginClicked() {
        btnLogin.requestFocus();

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnLogin.getWindowToken(), 0);
            loginViewModel.onServerLoginClicked(emailET.getText().toString(),
                    passwordET.getText().toString());

    }



    @Override
    protected void setUp() {
        chkBoxBiometric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(getApplicationContext(),"Show",Toast.LENGTH_SHORT).show();
                promptBiometric = isChecked;


            }
        });

        if(FingerprintDialog.isAvailable(this) && android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            chkBoxBiometric.setEnabled(true);
        }else{
            chkBoxBiometric.setEnabled(false);
        }
        loginViewModel.checkFingerPrintStored();


    }


    private void observeOpenMainActivityEvent() {
        loginViewModel.getOpenMainActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openMainActivity();
                    }
                });
        loginViewModel.getPromptFingerPrintForLogin().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer aBoolean)
            {
                if(aBoolean == 1){
                    loginViewModel.getUserName();
                }else if(aBoolean == 0){
                    showBiometricAuthenticFirstTime = true;

                }
            }
        });
      loginViewModel.getpromptFingerPrintAfterLogin().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer aBoolean)
            {
                if(aBoolean == 1){
                    promptFingerPrintToRemember();
                }else if(aBoolean == 0){
                    showBiometricAuthenticFirstTime = true;

                }
            }
        });
        loginViewModel.getsucessfullLoginEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if(promptBiometric){
                    promptFingerPrintToRemember();
                }else{
                    openMainActivity();
                }
            }
        });
        loginViewModel.getGetUserNameEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                promptFingerPrint(s);
            }
        });
    }

    public void openMainActivity() {

            startActivity(MainActivity.getStartIntent(this));
            finish();

    }


    @OnClick(R.id.login_signup_btn)
    public void openRegisterActivity() {
        startActivity(RegisterActivity.getStartIntent(this));

    }


    public void promptFingerPrint(String userName) {
        if(FingerprintDialog.isAvailable(this) && android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            FingerprintDialog.initialize(this)
                    .title( getString(R.string.biometric_title_login)+" as "+userName)
                    .message( getString(R.string.biometric_description_login)+" " + userName)
                    .callback(loginViewModel)
                    .show();
        }else{
            openMainActivity();
        }
    }

    public void promptFingerPrintToRemember() {
        if(FingerprintDialog.isAvailable(this) && android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            //Toast.makeText(this,"Show",Toast.LENGTH_SHORT).show();
            FingerprintDialog.initialize(this)
                    .title(R.string.biometric_title_login)
                    .message(R.string.biometric_description)
                    .callback(loginViewModel)
                    .show();
        }
    }


    @Override
    public LoginViewModel getViewModel() {
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        return loginViewModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
