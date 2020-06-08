package app.perfecto.com.expencemanager.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import app.perfecto.com.expencemanager.data.db.model.User;


@Dao
public interface UserDao {

    @Insert
    Long insertUser(User user);

    @Query("SELECT * FROM user")
    User getUser();

    @Query("SELECT * FROM user WHERE email =:email")
    User getUser(String email);

    @Query("DELETE FROM user")
    void nukeUserTable();

    @Query("DELETE FROM user WHERE user_id=:userId")
    void deleteUserFromTable(long userId);
}
