package app.perfecto.com.expencemanager.data.network;


import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.data.network.model.LoginRequest;
import io.reactivex.Observable;

/**
 * Created by Abhijit on 10-11-2017.
 */

public interface ApiHelper {

    Observable<User> doServerLoginApiCall(LoginRequest.ServerLoginRequest request);



}
