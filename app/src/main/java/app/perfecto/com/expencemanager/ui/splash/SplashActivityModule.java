package app.perfecto.com.expencemanager.ui.splash;

import androidx.lifecycle.ViewModelProvider;
import app.perfecto.com.expencemanager.ViewModelProviderFactory;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 04-12-2017.
 */

@Module
public class SplashActivityModule {

    @Provides
    SplashViewModel providesSplashViewModel(DataManager dataManager,
                                            SchedulerProvider schedulerProvider,
                                            NetworkUtils networkUtils) {
        return new SplashViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory splashViewModelProvider(SplashViewModel splashViewModel) {
        return new ViewModelProviderFactory<>(splashViewModel);
    }
}
