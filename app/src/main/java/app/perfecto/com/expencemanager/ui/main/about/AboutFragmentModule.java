package app.perfecto.com.expencemanager.ui.main.about;

import androidx.lifecycle.ViewModelProvider;
import app.perfecto.com.expencemanager.ViewModelProviderFactory;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;



@Module
public class AboutFragmentModule {

    @Provides
    AboutViewModel providesAboutViewModel(DataManager dataManager,
                                         SchedulerProvider schedulerProvider,
                                         NetworkUtils networkUtils) {
        return new AboutViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory aboutViewModelProvider(AboutViewModel aboutViewModel) {
        return new ViewModelProviderFactory<>(aboutViewModel);
    }



}
