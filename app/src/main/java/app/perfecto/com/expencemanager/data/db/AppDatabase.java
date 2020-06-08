package app.perfecto.com.expencemanager.data.db;

import android.content.Context;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.data.db.model.User;

@Database(entities = {User.class,Expense.class}, version = 1, exportSchema = false)
@Singleton
public abstract class AppDatabase extends RoomDatabase {


    public abstract UserDao userDao();

    public abstract ExpenseDao expenseDao();

}
