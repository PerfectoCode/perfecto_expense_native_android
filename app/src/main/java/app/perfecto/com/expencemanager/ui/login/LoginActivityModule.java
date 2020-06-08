package app.perfecto.com.expencemanager.ui.login;

import androidx.lifecycle.ViewModelProvider;
import app.perfecto.com.expencemanager.ViewModelProviderFactory;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;




@Module
public class LoginActivityModule {

    @Provides
    LoginViewModel providesLoginViewModel(DataManager dataManager,
                                          SchedulerProvider schedulerProvider,
                                          NetworkUtils networkUtils) {
        return new LoginViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory loginViewModelProvider(LoginViewModel loginViewModel) {
        return new ViewModelProviderFactory<>(loginViewModel);
    }
}
