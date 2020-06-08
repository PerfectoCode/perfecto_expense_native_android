package app.perfecto.com.expencemanager.data.network;


import javax.inject.Inject;
import javax.inject.Singleton;

import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.data.network.model.LoginRequest;
import io.reactivex.Observable;

/**
 * Created by Abhijit on 10-11-2017.
 */

@Singleton
public class AppApiHelper implements ApiHelper{

    private ApiCall apiCall;

    @Inject
    AppApiHelper(ApiCall apiCall) {
        this.apiCall = apiCall;
    }


    @Override
    public Observable<User> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return apiCall.doServerLogin(request);
    }


}
