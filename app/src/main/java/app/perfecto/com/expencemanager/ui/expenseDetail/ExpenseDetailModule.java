package app.perfecto.com.expencemanager.ui.expenseDetail;

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.perfecto.com.expencemanager.ViewModelProviderFactory;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;



@Module
public class ExpenseDetailModule {

    @Provides
    ExpenseDetailViewModel providesOSDetailsViewModel(DataManager dataManager,
                                                      SchedulerProvider schedulerProvider,
                                                      NetworkUtils networkUtils) {
        return new ExpenseDetailViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory osDetailsViewModelProvider(ExpenseDetailViewModel expenseDetailViewModel) {
        return new ViewModelProviderFactory<>(expenseDetailViewModel);
    }

    @Provides
    AttachemntsAdapter providesAttachemntsAdapter() {
        return new AttachemntsAdapter(new ArrayList<String>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(ExpenseDetailActivity activity) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        return linearLayoutManager;
    }
}
