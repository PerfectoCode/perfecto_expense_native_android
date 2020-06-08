package app.perfecto.com.expencemanager.ui.main.expenses;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class ExpenseViewModel extends BaseViewModel implements ExpenseAdapter.Callback {

    //EVENT (transmit DATA)
    private LiveData<List<Expense>> expenseList = new MutableLiveData<>();
    private final SingleLiveEvent<Expense> openExpenseDetailActivity = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> pullToRefreshEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> showDeleteButtonEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> batchDeleteFinished = new SingleLiveEvent<>();

    //EVENT
    private final SingleLiveEvent<Void> expenseListReFetched = new SingleLiveEvent<>();


    public ExpenseViewModel(DataManager dataManager,
                            SchedulerProvider schedulerProvider,
                            NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);

        expenseList = getDataManager().getExpensesList();
    }


    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */
    public LiveData<List<Expense>> getExpenseList() {
        return expenseList;
    }

    public SingleLiveEvent<Expense> getOpenExpenseDetailActivity() {
        return openExpenseDetailActivity;
    }

    public SingleLiveEvent<Boolean> getPullToRefreshEvent() {
        return pullToRefreshEvent;
    }

    public SingleLiveEvent<Void> getExpenseListReFetched() {
        return expenseListReFetched;
    }

    public SingleLiveEvent<Boolean> getShowDeleteButtonEvent() {
        return showDeleteButtonEvent;
    }

    public SingleLiveEvent<Void> getBatchDeleteFinishedEvent() {
        return batchDeleteFinished;
    }


    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onOpenExpenseDetailActivity(Expense expense) {
        openExpenseDetailActivity.setValue(expense);
    }

    private void onPullToRefreshEvent(boolean status) {
        pullToRefreshEvent.setValue(status);
    }

    private void onExpenseListReFetched() {
        expenseListReFetched.call();
    }





    public void fetchExpenseList() {
        onPullToRefreshEvent(true);
        showDeleteButtonEvent.setValue(false);
        expenseList= getDataManager().getExpensesList();
        onPullToRefreshEvent(false);
    }


    public void deleteExpense(Expense expense){

          getDataManager().deletexpense(expense)
          .subscribeOn(getSchedulerProvider().io())
          .observeOn(getSchedulerProvider().ui())
          .subscribe(new CompletableObserver() {
              @Override
              public void onSubscribe(Disposable d) {

              }

              @Override
              public void onComplete() {
                  fetchExpenseList();
                  showSnackbarMessage(R.string.delete_success);
              }

              @Override
              public void onError(Throwable e) {

              }
          })
        ;
    }

    public void deleteExpenses(List<Expense> expenseList){
        if(expenseList.size()>0){
            getDataManager().deletexpense(expenseList.get(0))
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            expenseList.remove(0);
                            deleteExpenses(expenseList);

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }else{

            getBatchDeleteFinishedEvent().call();
            showSnackbarMessage(R.string.delete_success);
        }


    }




    @Override
    public void onExpenseEmptyRetryClicked() {
        fetchExpenseList();
        onExpenseListReFetched();
    }

    @Override
    public void onExpenseItemClicked(Expense expense) {
        onOpenExpenseDetailActivity(expense);
    }

    @Override
    public void onExpenseItemSelected(long expensesCount) {
        if(expensesCount>0){
            showDeleteButtonEvent.setValue(true);
        }else{
            showDeleteButtonEvent.setValue(false);
        }

    }
}
