package app.perfecto.com.expencemanager.ui.splash;


import java.util.Timer;
import java.util.TimerTask;

import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.AppLogger;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

/**
 * Created by Abhijit on 04-12-2017.
 */

public class SplashViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> openLoginActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openMainActivityEvent = new SingleLiveEvent<>();

    public SplashViewModel(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);

        startActivityWithDelay();
    }


    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */

    public SingleLiveEvent<Void> getOpenLoginActivityEvent() {
        return openLoginActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenMainActivityEvent() {
        return openMainActivityEvent;
    }


    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onOpenLoginActivityEvent() {
        openLoginActivityEvent.call();
    }

    private void onOpenMainActivityEvent() {
        openMainActivityEvent.call();
    }



    /*
    * APP LOGIC, BUSINESS LOGIC & USE CASES
    * */

    private void startActivityWithDelay() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                decideNextActivity();
            }
        }, 2000);
    }

    private void decideNextActivity() {

        getCompositeDisposable().add(
                getDataManager().getUser(User.getDefaultUser().getEmail())
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {

                                AppLogger.i("Fetched User :",user);
                                getDataManager().getCurrentUser();
                                if (getDataManager().getCurrentUserLoggedInMode() ==
                                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType())
                                    onOpenLoginActivityEvent();
                                else onOpenMainActivityEvent();


                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                addDefaultUser();
                            }
                        })
        );

    }
    private void addDefaultUser(){
        getCompositeDisposable().add(
                getDataManager().insertUser(User.getDefaultUser())
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                getDataManager().getCurrentUser();
                                if (getDataManager().getCurrentUserLoggedInMode() ==
                                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType())
                                    onOpenLoginActivityEvent();
                                else onOpenMainActivityEvent();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        })
        );
    }
}
