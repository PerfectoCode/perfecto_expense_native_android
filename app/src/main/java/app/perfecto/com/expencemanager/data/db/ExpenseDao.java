package app.perfecto.com.expencemanager.data.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import app.perfecto.com.expencemanager.data.db.model.Expense;

@Dao
public interface ExpenseDao {


    @Query("SELECT * FROM expenses")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expenses")
    List<Expense> getAllExpensesObservable();

    @Insert
    Long insert(Expense expense);

    @Insert
    List<Long>  insert(List<Expense> expense);

    @Update
    int update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expenses")
    void nukeExpensesTable();
}
