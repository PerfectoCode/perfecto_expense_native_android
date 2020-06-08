package app.perfecto.com.expencemanager.di.builder;


import app.perfecto.com.expencemanager.ui.expenseDetail.ExpenseDetailActivity;
import app.perfecto.com.expencemanager.ui.expenseDetail.ExpenseDetailModule;
import app.perfecto.com.expencemanager.ui.login.LoginActivity;
import app.perfecto.com.expencemanager.ui.login.LoginActivityModule;
import app.perfecto.com.expencemanager.ui.main.MainActivity;
import app.perfecto.com.expencemanager.ui.main.MainActivityModule;
import app.perfecto.com.expencemanager.ui.main.about.AboutFragmentProvider;
import app.perfecto.com.expencemanager.ui.main.expenses.ExpenseFragmentProvider;
import app.perfecto.com.expencemanager.ui.register.RegisterActivity;
import app.perfecto.com.expencemanager.ui.register.RegisterActivityModule;
import app.perfecto.com.expencemanager.ui.splash.SplashActivity;
import app.perfecto.com.expencemanager.ui.splash.SplashActivityModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Abhijit on 04-12-2017.
 */

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = RegisterActivityModule.class)
    abstract RegisterActivity bindRegisterActivity();

    @ContributesAndroidInjector(modules = ExpenseDetailModule.class)
    abstract ExpenseDetailActivity bindExpenseDetailActivity();

    @ContributesAndroidInjector(modules = {
            MainActivityModule.class,
            ExpenseFragmentProvider.class,
            AboutFragmentProvider.class})
    abstract MainActivity bindMainActivity();


}