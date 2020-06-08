package app.perfecto.com.expencemanager.data.db.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Abhijit on 11-11-2017.
 */

@Entity(tableName = "user")
public class User {

    /*NOTE:
    @ColumnInfo(name = "id")
    This is a form of mapping.

    If API response has key as "id", no need to have @ColumnInfo annotation.

     If API response has key anything apart from "id" (example "ID", "Identifier", "Id")in its body,
     then @ColumnInfo annotation has to be given for mapping and parsing correctly.

    * */


    public User(){

    }

    @Ignore

    private String statusCode;

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @Expose @SerializedName("user_id")
    private long userID;



    @ColumnInfo(name = "email")
    @Expose @SerializedName("email")
    private String email;

    @ColumnInfo(name = "access_token")
    @Expose @SerializedName("access_token")
    private String accessToken;

    @Ignore
    private String message;

    @ColumnInfo(name = "name")
    @Expose @SerializedName("name")
    private  String name;

    @ColumnInfo(name = "currency")
    @Expose @SerializedName("currency")
    private String currency;

    @ColumnInfo(name = "password")
    @Expose @SerializedName("password")
    private String password;




    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static User getDefaultUser(){
        User user = new User();
        user.setEmail("test@perfecto.com");
        user.setName("Test");
        user.setPassword("test123");
        return user;
    }
}
