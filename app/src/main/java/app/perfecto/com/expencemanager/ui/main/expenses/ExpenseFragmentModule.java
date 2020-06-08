package app.perfecto.com.expencemanager.ui.main.expenses;

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.perfecto.com.expencemanager.ViewModelProviderFactory;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 08-12-2017.
 */

@Module
public class ExpenseFragmentModule {

    @Provides
    ExpenseViewModel providesBlogViewModel(DataManager dataManager,
                                           SchedulerProvider schedulerProvider,
                                           NetworkUtils networkUtils) {
        return new ExpenseViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory blogViewModelProvider(ExpenseViewModel expenseViewModel) {
        return new ViewModelProviderFactory<>(expenseViewModel);
    }

    @Provides
    ExpenseAdapter providesBlogAdapter() {
        return new ExpenseAdapter(new ArrayList<Expense>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(ExpenseFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }
}
