package app.perfecto.com.expencemanager.ui.main.about;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class AboutViewModel extends BaseViewModel{

    //EVENT (transmit DATA)
    private LiveData<List<Expense>> expenseList = new MutableLiveData<>();


    //EVENT
    private final SingleLiveEvent<Void> expenseListReFetched = new SingleLiveEvent<>();


    public AboutViewModel(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);

        expenseList = getDataManager().getExpensesList();
    }














}
