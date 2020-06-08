package app.perfecto.com.expencemanager.ui.expenseDetail;


import android.text.TextUtils;

import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Abhijit on 10-12-2017.
 */

public class ExpenseDetailViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> returnToMainActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openInBrowserEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Long> newExpensesAdded = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> expenseUpdated = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> getPreferredCurrency = new SingleLiveEvent<>();




    public ExpenseDetailViewModel(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }



    public SingleLiveEvent<Void> getReturnToMainActivityEvent() {
        return returnToMainActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenInBrowserEvent() {
        return openInBrowserEvent;
    }

    public SingleLiveEvent<Long> getNewExpensesAdded() {
        return newExpensesAdded;
    }

    public SingleLiveEvent<Integer> getExpenseUpdated() {
        return expenseUpdated;
    }

    public SingleLiveEvent<String> getPreffferedCurrencyChanged() {
        return getPreferredCurrency;
    }

    public void onOSDetailsDisplayedError() {
        returnToMainActivityEvent.call();
    }

    public void onOpenSourceFABClicked() {
        openInBrowserEvent.call();
    }


    public void addNewExpense(Expense expense){
        if(TextUtils.isEmpty(expense.getExpenseHead())){
            showSnackbarMessage(R.string.non_empty_head);
            return;
        }
        if(TextUtils.isEmpty(expense.getDate())){
            showSnackbarMessage(R.string.non_empty_date);
            return;
        }
        if(TextUtils.isEmpty(expense.getExpenseCategory())){
            showSnackbarMessage(R.string.non_empty_category);
            return;
        }
        if(!(expense.getAmount()>0.0)){
            showSnackbarMessage(R.string.non_empty_amount);
            return;
        }
        if((expense.getAmount()>10000.0)){
            showSnackbarMessage(R.string.max_expense);
            return;
        }
        if((expense.getCurrency().equalsIgnoreCase("select"))){
            showSnackbarMessage(R.string.select_currency);
            return;
        }
        getCompositeDisposable().add(
                getDataManager().insertExpense(expense).subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        newExpensesAdded.setValue(aLong);

                    }
                })
        );


    }

    public void getDefaultCurrency(){

               String prefereedCurrency = getDataManager().getCurrentUserSelectedCurrency();
               getPreferredCurrency.setValue(prefereedCurrency);

    }

    public void updateExpense(Expense expense){
        if(TextUtils.isEmpty(expense.getExpenseHead())){
            showSnackbarMessage(R.string.non_empty_head);
            return;
        }
        if(TextUtils.isEmpty(expense.getDate())){
            showSnackbarMessage(R.string.non_empty_date);
            return;
        }
        if(TextUtils.isEmpty(expense.getExpenseCategory())){
            showSnackbarMessage(R.string.non_empty_category);
            return;
        }
        if(!(expense.getAmount()>0.0)){
            showSnackbarMessage(R.string.non_empty_amount);
            return;
        }
        getCompositeDisposable().add(
                getDataManager().updateExpense(expense).subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer aLong) throws Exception {
                                expenseUpdated.setValue(aLong);

                            }
                        })
        );
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

                        showSnackbarMessage(R.string.delete_success);
                        returnToMainActivityEvent.call();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        ;
    }
}
