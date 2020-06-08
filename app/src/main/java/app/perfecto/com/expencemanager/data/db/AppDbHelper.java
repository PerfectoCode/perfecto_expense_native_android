package app.perfecto.com.expencemanager.data.db;


import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.data.db.model.User;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


@Singleton
public class AppDbHelper implements DbHelper {

    private AppDatabase appDatabase;

    @Inject
    AppDbHelper(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }


    //USER
    @Override
    public Observable<Long> insertUser(final User user) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return appDatabase.userDao().insertUser(user);
            }
        });
    }

    @Override
    public Observable<User> getCurrentUser() {
        return Observable.fromCallable(() -> appDatabase.userDao().getUser());
    }

    @Override
    public Observable<User> getUser(String email) {
        return Observable.fromCallable(() -> appDatabase.userDao().getUser(email));
    }

    @Override
    public Completable wipeUserData() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.userDao().nukeUserTable();
            }
        });
    }

    @Override
    public Completable deleteUser(long userId) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.userDao().deleteUserFromTable(userId);
            }
        });
    }

    @Override
    public Observable<Long> insertExpense(Expense expense) {
        return Observable.fromCallable(() -> appDatabase.expenseDao().insert(expense));
    }

    @Override
    public Observable<List<Long>> insertExpenses(List<Expense> expenses) {
        return Observable.fromCallable(() -> appDatabase.expenseDao().insert(expenses));
    }


    @Override
    public Observable<Integer> updateExpense(Expense expense) {
        return Observable.fromCallable(() -> appDatabase.expenseDao().update(expense));
    }

    @Override
    public Completable deletexpense(Expense expense) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.expenseDao().delete(expense);
            }
        });
    }

    @Override
    public Observable<List<Expense>> getExpensesListObservable() {
        return Observable.fromCallable(() -> appDatabase.expenseDao().getAllExpensesObservable());
    }

    @Override
    public LiveData<List<Expense>> getExpensesList() {
       return appDatabase.expenseDao().getAllExpenses();
    }

    @Override
    public Completable wipeExpensesData() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.expenseDao().nukeExpensesTable();
            }
        });
    }


}
