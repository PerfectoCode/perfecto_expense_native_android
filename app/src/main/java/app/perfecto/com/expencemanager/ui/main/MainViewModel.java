package app.perfecto.com.expencemanager.ui.main;


import android.util.Log;

import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.AppLogger;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainViewModel extends BaseViewModel {

    //EVENT (transmit DATA)
    private final SingleLiveEvent<User> user = new SingleLiveEvent<>();


    //EVENT
    private final SingleLiveEvent<Void> openLoginActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openAboutActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> closeNavigationDrawerEvent = new SingleLiveEvent<>();


    public MainViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }



    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */

    public SingleLiveEvent<User> getUser() {
        return user;
    }

    public SingleLiveEvent<Void> getOpenLoginActivityEvent() {
        return openLoginActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenAboutActivityEvent() {
        return openAboutActivityEvent;
    }

    public SingleLiveEvent<Void> getCloseNavigationDrawerEvent() {
        return closeNavigationDrawerEvent;
    }



    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */
    private void onUserUpdated(User user) {
        this.user.setValue(user);
    }

    private void onOpenLoginActivityEvent() {
        openLoginActivityEvent.call();
    }

    private void onOpenFeedActivityEvent() {
        openAboutActivityEvent.call();
    }

    private void onCloseNavigationDrawerEvent() {
        closeNavigationDrawerEvent.call();
    }


    public void onNavMenuCreated() {
        User user = new User();

                user.setUserID(getDataManager().getCurrentUserId());

                user.setEmail(getDataManager().getCurrentUserEmail());

        getCompositeDisposable().add(
                getDataManager().getUser(getDataManager().getCurrentUserEmail())
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {

                                AppLogger.i("Fetched User :",user);
//                                    insertCurrentUserIntoDb(user);
                                onUserUpdated(user);

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                hideLoading();
                               // showSnackbarMessage(R.string.wrong_credentials);
                            }
                        })
        );



    }

    public void onDrawerOptionAboutClicked() {
        onCloseNavigationDrawerEvent();
        onOpenFeedActivityEvent();
    }


    private void checkFeedAvailableInDb() {
        showLoading();


    }

    private void checkFeedAvailableInOpenSourceDb() {
        showLoading();

    }

    public void onDrawerOptionLogoutClicked() {
        showLoading();

        getDataManager().deleteUser( getDataManager().getCurrentUserId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getDataManager().setCurrentUserLoggedOut();
                        nukeExpensesTable();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        showSnackbarMessage(R.string.there_was_an_error_logout);
                    }
                });

    }

    private void nukeExpensesTable(){
        getDataManager().wipeExpensesData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                        showSnackbarMessage(R.string.logging_you_out);
                        addDefaultUser();

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        //showSnackbarMessage(R.string.there_was_an_error_logout);
                    }
                });
    }

    private void addDefaultUser(){
        getCompositeDisposable().add(
                getDataManager().insertUser(User.getDefaultUser())
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                onOpenLoginActivityEvent();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i(MainViewModel.class.getSimpleName(),throwable.getMessage());
                            }
                        })
        );
    }


}
