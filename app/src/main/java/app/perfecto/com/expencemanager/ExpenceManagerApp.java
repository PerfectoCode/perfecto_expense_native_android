package app.perfecto.com.expencemanager;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import javax.inject.Inject;

import app.perfecto.com.expencemanager.di.component.AppComponent;
import app.perfecto.com.expencemanager.di.component.DaggerAppComponent;
import app.perfecto.com.expencemanager.utils.AppLogger;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class ExpenceManagerApp extends Application implements HasActivityInjector, HasFragmentInjector, HasSupportFragmentInjector {

    private AppComponent appComponent;


    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;



    @Override
    public void onCreate() {
        super.onCreate();

        //Instantiate AppComponent
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);

        AppLogger.init();


        /*
         * Init Facebook SDK*/


        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(mCalligraphyConfig))
                .build());


    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }



    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return null;
    }

    @Override
    public AndroidInjector<androidx.fragment.app.Fragment> supportFragmentInjector() {
        return null;
    }
}
