package app.perfecto.com.expencemanager.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import androidx.room.Room;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.AppDataManager;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.AppDatabase;
import app.perfecto.com.expencemanager.data.db.AppDbHelper;
import app.perfecto.com.expencemanager.data.db.DbHelper;
import app.perfecto.com.expencemanager.data.network.ApiCall;
import app.perfecto.com.expencemanager.data.network.ApiHelper;
import app.perfecto.com.expencemanager.data.network.AppApiHelper;
import app.perfecto.com.expencemanager.data.prefs.AppPreferenceHelper;
import app.perfecto.com.expencemanager.data.prefs.PreferenceHelper;
import app.perfecto.com.expencemanager.di.ApplicationContext;
import app.perfecto.com.expencemanager.di.DatabaseInfo;
import app.perfecto.com.expencemanager.di.PreferenceInfo;
import app.perfecto.com.expencemanager.utils.AppConstants;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.AppSchedulerProvider;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;
import io.github.inflationx.calligraphy3.CalligraphyConfig;



@Module
public class AppModule {

    @Provides
    @ApplicationContext
    Context providesContext(Application application) {
        return application;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @DatabaseInfo
    String providesDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer providesDatabaseVersion() {
        return AppConstants.DB_VERSION;
    }

    @Provides
    @PreferenceInfo
    String providesSharedPrefName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager providesDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper providesDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    ApiHelper providesApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    PreferenceHelper providesPreferenceHelper(AppPreferenceHelper appPreferenceHelper) {
        return appPreferenceHelper;
    }

    @Provides
    @Singleton
    ApiCall providesApiCall() {
        return ApiCall.Factory.create();
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase(@ApplicationContext Context context,
                                    @DatabaseInfo String dbName) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                dbName).build();

    }

    @Provides
    @Singleton
    CalligraphyConfig providesCalligraphyConfig() {



        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @Singleton
    NetworkUtils providesNetworkUtils(@ApplicationContext Context context) {
        return new NetworkUtils(context);
    }
}
