package app.perfecto.com.expencemanager.ui.main;


import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 08-12-2017.
 */

@Module
public class MainActivityModule {

    @Provides
    MainViewModel providesMainViewModel(DataManager dataManager,
                                        SchedulerProvider schedulerProvider,
                                        NetworkUtils networkUtils) {
        return new MainViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    MainPagerAdapter providesMainPagerAdapter(MainActivity activity) {
        return new MainPagerAdapter(activity.getSupportFragmentManager());
    }
}
