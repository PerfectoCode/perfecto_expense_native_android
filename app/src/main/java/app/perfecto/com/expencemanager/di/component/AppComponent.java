package app.perfecto.com.expencemanager.di.component;

import android.app.Application;

import javax.inject.Singleton;

import app.perfecto.com.expencemanager.ExpenceManagerApp;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.di.builder.ActivityBuilderModule;
import app.perfecto.com.expencemanager.di.module.AppModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;


@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class,
        ActivityBuilderModule.class})
public interface AppComponent extends AndroidInjector<ExpenceManagerApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(ExpenceManagerApp mvvmApp);

    DataManager getDataManager();


}
