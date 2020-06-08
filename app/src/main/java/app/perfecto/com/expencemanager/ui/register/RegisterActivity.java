package app.perfecto.com.expencemanager.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.google.android.material.textfield.TextInputEditText;


import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.ui.base.BaseActivity;
import app.perfecto.com.expencemanager.ui.login.LoginActivity;
import app.perfecto.com.expencemanager.utils.AppLogger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.aflak.libraries.dialog.FingerprintDialog;


public class RegisterActivity extends BaseActivity<RegisterViewModel>  {
    private RegisterViewModel registerViewModel;
    @Inject
    ViewModelProvider.Factory factory;
    @BindView(R.id.signup_name)
    TextInputEditText teiName;
    @BindView(R.id.signup_email)
    TextInputEditText tvUserName;
    @BindView(R.id.signup_password)
    TextInputEditText tvPassWord;
    @BindView(R.id.signup_confirm_password)
    TextInputEditText tvConfirmPassword;

    @BindView(R.id.signup_currency)
    AppCompatSpinner spinnerCurrency;

    @BindView(R.id.signup_save_btn)
    Button btnSignUp;

    @BindView(R.id.signup_back_btn)
    Button btnBackLogin;

    String currency = null;



    public static Intent getStartIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUnBinder(ButterKnife.bind(this));
        setUp();
        observeAllEvents();
    }

    private void observeAllEvents(){
        registerViewModel.getOpenMainActivityEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                openLoginActivity();
            }
        });
        registerViewModel.getCheckFingerprintEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                checkFingerPrint();
            }
        });
    }




    @Override
    protected void setUp() {
        String[] currencyList = getResources().getStringArray(R.array.currency);

        ArrayAdapter currencyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, currencyList);


        /*ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,currencyList);*/
        spinnerCurrency.setAdapter(currencyAdapter);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.signup_save_btn)
    public void startSignUpProcess(){
        User user = getUserDetailsFromForm();
        if(user != null){
            AppLogger.i("User :",user);
            registerViewModel.insertUser(user);
        }
    }

    @OnClick(R.id.signup_back_btn)
    public void goBack() {
        onBackPressed();
        finish();
    }


    private User getUserDetailsFromForm(){

        String name,userName,password;
        if(isValidName() && isValidUserName() && isValidPassword() && (currency != null)) {
            name = teiName.getText().toString();
            userName = tvUserName.getText().toString();
            password = tvPassWord.getText().toString();
            User user = new User();
            user.setName(name);
            user.setEmail(userName);
            user.setCurrency(currency);
            user.setPassword(password);
            user.setUserID(new Random().nextLong());
            return user;
        }
        return null;



    }

    private boolean isValidName(){
        if(TextUtils.isEmpty(teiName.getText())){
            teiName.setError("Required");
            return false;
        }else if(teiName.getText().length()<5){
            teiName.setError("Minimum length 5");
            return false;
        }
        return true;
    }

    private boolean isValidUserName(){
        if(TextUtils.isEmpty(tvUserName.getText())){
            tvUserName.setError("Required");
            return false;
        }else if(tvUserName.getText().length()<3){
            tvUserName.setError("Invalid email");
            return false;
        }else if(tvUserName.getText().toString().contains(" ")){
            tvUserName.setError("Please remove space");
        }
        return true;
    }

    private boolean isValidPassword(){
        if(TextUtils.isEmpty(tvPassWord.getText())){
            tvPassWord.setError("Required");
            return false;
        }else if(tvPassWord.getText().length()<5){
            tvPassWord.setError("Minimum length 5");
            return false;
        }else if(tvPassWord.getText().toString().contains(" ")){
            tvPassWord.setError("Please remove space");
        }
        if(TextUtils.isEmpty(tvConfirmPassword.getText())){
            tvConfirmPassword.setError("Required");
            return false;
        }else if(tvConfirmPassword.getText().length()<5){
            tvConfirmPassword.setError("Minimum length 5");
            return false;
        }else if(tvConfirmPassword.getText().toString().contains(" ")){
            tvConfirmPassword.setError("Please remove space");
            return false;
        }
        if(!tvConfirmPassword.getText().toString().contentEquals(tvPassWord.getText().toString())){
            tvConfirmPassword.setError("Two passwords are not matching");
            return false;
        }
        return true;
    }





    public void checkFingerPrint(){

        if(FingerprintDialog.isAvailable(this)  && (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)) {
            FingerprintDialog.initialize(this)
                    .title(R.string.biometric_title)
                    .message(R.string.biometric_description)
                    .callback(registerViewModel)
                    .show();
        }else{
            registerViewModel.onAuthenticationCancel();
        }


    }


    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    @Override
    public RegisterViewModel getViewModel() {
        registerViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel.class);
        return registerViewModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
