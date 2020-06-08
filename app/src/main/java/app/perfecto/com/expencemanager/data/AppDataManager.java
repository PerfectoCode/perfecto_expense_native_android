package app.perfecto.com.expencemanager.data;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import app.perfecto.com.expencemanager.data.db.DbHelper;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.data.network.ApiHelper;
import app.perfecto.com.expencemanager.data.network.model.LoginRequest;
import app.perfecto.com.expencemanager.data.prefs.PreferenceHelper;
import app.perfecto.com.expencemanager.di.ApplicationContext;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Abhijit on 08-11-2017.
 */

@Singleton
public class AppDataManager implements DataManager {


    private final Context context;
    private final DbHelper dbHelper;
    private final PreferenceHelper preferenceHelper;
    private final ApiHelper apiHelper;

    @Inject
    AppDataManager(@ApplicationContext Context context,
                   DbHelper dbHelper,
                   PreferenceHelper preferenceHelper,
                   ApiHelper apiHelper) {

        this.context = context;
        this.dbHelper = dbHelper;
        this.preferenceHelper = preferenceHelper;
        this.apiHelper = apiHelper;
    }


    //DB:USER
    @Override
    public Observable<Long> insertUser(User user) {
        return dbHelper.insertUser(user);
    }

    @Override
    public Observable<User> getCurrentUser() {
        return dbHelper.getCurrentUser();
    }

    @Override
    public Observable<User> getUser(String email) {
        return dbHelper.getUser(email);
    }

    @Override
    public Completable wipeUserData() {
        return dbHelper.wipeUserData();
    }

    @Override
    public Completable deleteUser(long userId) {
        return dbHelper.deleteUser(userId);
    }

    @Override
    public Observable<Long> insertExpense(Expense expense) {
        return dbHelper.insertExpense(expense);
    }

    @Override
    public Observable<List<Long>> insertExpenses(List<Expense> expenses) {
        return dbHelper.insertExpenses(expenses);
    }

    @Override
    public Observable<Integer> updateExpense(Expense expense) {
        return dbHelper.updateExpense(expense);
    }

    @Override
    public Completable deletexpense(Expense expense) {
        return dbHelper.deletexpense(expense);
    }

    @Override
    public Observable<List<Expense>> getExpensesListObservable() {
        return dbHelper.getExpensesListObservable();
    }

    @Override
    public LiveData<List<Expense>> getExpensesList() {
        return dbHelper.getExpensesList();
    }

    @Override
    public Completable wipeExpensesData() {
        return dbHelper.wipeExpensesData();
    }


    //API CALL:USER
    @Override
    public Observable<User> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return apiHelper.doServerLoginApiCall(request);
    }


    //SHARED PREF:USER
    @Override
    public void setCurrentUserId(Long id) {
        preferenceHelper.setCurrentUserId(id);
    }

    @Override
    public Long getCurrentUserId() {
        return preferenceHelper.getCurrentUserId();
    }



    @Override
    public void setCurrentUserEmail(String email) {
        preferenceHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserEmail() {
        return preferenceHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserSelectedCurrency(String SelectedCurrency) {
        preferenceHelper.setCurrentUserSelectedCurrency(SelectedCurrency);
    }

    @Override
    public String getCurrentUserSelectedCurrency() {
        return preferenceHelper.getCurrentUserSelectedCurrency();
    }

    @Override
    public void updateUserInfoInPrefs(Long userId, String userEmail,String currency, LoggedInMode mode) {
        preferenceHelper.updateUserInfoInPrefs(userId, userEmail,currency, mode);
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        preferenceHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return preferenceHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedOut() {
        preferenceHelper.setCurrentUserLoggedOut();
    }

    @Override
    public void setBiometricVerificationStatus(int isFingerPrintStored) {
        preferenceHelper.setBiometricVerificationStatus(isFingerPrintStored);
    }

    @Override
    public int getBiometricVerificationStatus() {
        return preferenceHelper.getBiometricVerificationStatus();
    }


}
