package app.perfecto.com.expencemanager.ui.register;

import androidx.lifecycle.ViewModelProvider;
import app.perfecto.com.expencemanager.ViewModelProviderFactory;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;


@Module
public class RegisterActivityModule {

    @Provides
    RegisterViewModel providesLoginViewModel(DataManager dataManager,
                                             SchedulerProvider schedulerProvider,
                                             NetworkUtils networkUtils) {
        return new RegisterViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory loginViewModelProvider(RegisterViewModel registerViewModel) {
        return new ViewModelProviderFactory<>(registerViewModel);
    }
}
