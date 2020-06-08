package app.perfecto.com.expencemanager.data.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.data.db.model.User;
import io.reactivex.Completable;
import io.reactivex.Observable;


public interface DbHelper {

    //User
    Observable<Long> insertUser(User user);

    Observable<User> getCurrentUser();

    Observable<User> getUser(String email);

    Completable wipeUserData();

    Completable deleteUser(long userId);

    Observable<Long> insertExpense(Expense expense);

    Observable<List<Long>> insertExpenses(List<Expense> expenses);

    Observable<Integer> updateExpense(Expense expense);

    Completable deletexpense(Expense expense);

    Observable<List<Expense>> getExpensesListObservable();

    LiveData<List<Expense>> getExpensesList();

    Completable wipeExpensesData();





}
