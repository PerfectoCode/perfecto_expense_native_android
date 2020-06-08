package app.perfecto.com.expencemanager.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.ui.base.BaseActivity;
import app.perfecto.com.expencemanager.ui.login.LoginActivity;
import app.perfecto.com.expencemanager.ui.register.RegisterActivity;
import butterknife.ButterKnife;



public class SplashActivity extends BaseActivity<SplashViewModel> {

    @Inject
    ViewModelProvider.Factory factory;

    private SplashViewModel splashViewModel;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        setUnBinder(ButterKnife.bind(this));

        setUp();

        observeOpenLoginActivityEvent();

        observeOpenMainActivityEvent();
    }

    @Override
    public SplashViewModel getViewModel() {
        splashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel.class);
        return splashViewModel;
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    private void observeOpenMainActivityEvent() {
        splashViewModel.getOpenMainActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openLoginActivity();
                    }
                });
    }

    private void observeOpenLoginActivityEvent() {
        splashViewModel.getOpenLoginActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openRegisterActivity();
                        //openLoginActivity();
                    }
                });
    }

    private void openRegisterActivity() {
        startActivity(LoginActivity.getStartIntent(SplashActivity.this));
        finish();
    }

    private void openLoginActivity() {
        startActivity(LoginActivity.getStartIntentForLogIn(SplashActivity.this));
        finish();
    }
}
