package app.perfecto.com.expencemanager.ui.login;




import android.util.Log;

import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.AppLogger;
import app.perfecto.com.expencemanager.utils.CommonUtils;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.aflak.libraries.callback.FingerprintDialogCallback;



public class LoginViewModel extends BaseViewModel implements FingerprintDialogCallback {

    private final SingleLiveEvent<Void> openMainActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openGoogleSignInActivityEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Integer> promptFingerPrintForLogin = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> promptFingerPrintAfterLogin = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> sucessfullLogin = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> getUserNameEvent = new SingleLiveEvent<String>();




    public LoginViewModel(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }


    /*
     * NAVIGATION
     * GETTERS for observing events from UI thread(i.e Activity)
     * */

    public SingleLiveEvent<Void> getOpenMainActivityEvent() {
        return openMainActivityEvent;
    }

    public SingleLiveEvent<Integer> getPromptFingerPrintForLogin() {
        return promptFingerPrintForLogin;
    }

    public SingleLiveEvent<Integer> getpromptFingerPrintAfterLogin() {
        return promptFingerPrintAfterLogin;
    }

    public SingleLiveEvent<Void> getOpenGoogleSignInActivityEvent() {
        return openGoogleSignInActivityEvent;
    }

    public SingleLiveEvent<Void> getsucessfullLoginEvent() {
        return sucessfullLogin;
    }

    /*
     * NAVIGATION
     * Commands to update Events, which are observed from UI thread
     * */

    private void onOpenMainActivityEvent() {
        openMainActivityEvent.call();
    }

    private void onOpenGoogleSignInActivityEvent() {
        openGoogleSignInActivityEvent.call();
    }

    public SingleLiveEvent<String> getGetUserNameEvent() {
        return getUserNameEvent;
    }

    public void onServerLoginClicked(String email, String password) {
            if (email == null || email.isEmpty()) {
                showSnackbarMessage(R.string.wrong_credentials);
                showToastMessage(R.string.wrong_credentials);
                return;
            }
            if (!CommonUtils.isEmailValid(email)) {
                showSnackbarMessage(R.string.wrong_credentials);
                showToastMessage(R.string.wrong_credentials);
                return;
            }

            if (password == null || password.isEmpty()) {
                showSnackbarMessage(R.string.wrong_credentials);
                showToastMessage(R.string.wrong_credentials);
                return;
            }
            checkUserWithSharedPref(email,  password);
        }

        private void checkPassword(User user,String password){
            if(user.getPassword().contentEquals(password)){
                onSucessfullValidation(user);
            }else {
                showSnackbarMessage(R.string.wrong_credentials);
                showToastMessage(R.string.wrong_credentials);
                hideLoading();
            }


        }


    public void checkFingerPrintStored(){
        if(getDataManager().getBiometricVerificationStatus() == DataManager.BIOMETRIC_STATE.STORED.getBiometricState()) {
            promptFingerPrintForLogin.setValue(1);
        }
    }

    @Override
    public void onAuthenticationSucceeded() {
        if(getDataManager().getBiometricVerificationStatus() == DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState()){
            getDataManager().setBiometricVerificationStatus(DataManager.BIOMETRIC_STATE.STORED.getBiometricState());
        }
        onOpenMainActivityEvent();
    }

    @Override
    public void onAuthenticationCancel() {
        if(getDataManager().getBiometricVerificationStatus() == DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState()){
            getDataManager().setBiometricVerificationStatus(DataManager.BIOMETRIC_STATE.NOT_STORED.getBiometricState());
            onOpenMainActivityEvent();
        }
    }


    private void checkUserWithSharedPref(String email, String password){
        Log.d("Check shared pref","Email:"+email+", Password"+password);
        getCompositeDisposable().add(
                getDataManager().getUser(email)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {

                                AppLogger.i("Fetched User :",user);
//                                    insertCurrentUserIntoDb(user);
                                checkPassword(user,password);

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                hideLoading();
                                showSnackbarMessage(R.string.wrong_credentials);
                                showToastMessage(R.string.wrong_credentials);
                            }
                        })
        );


    }

    private void onSucessfullValidation(User user){
        Log.d("LoginViewModel","Current User Email :"+user.getEmail());
        Log.d("LoginViewModel","Shared-pref User Email :"+getDataManager().getCurrentUserEmail());
        if(getDataManager().getCurrentUserEmail() == null){
            getDataManager().updateUserInfoInPrefs(user.getUserID(),
                    user.getEmail(),
                    user.getCurrency(),
                    DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_SERVER);
            getDataManager().setBiometricVerificationStatus(DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState());
            sucessfullLogin.call();
        }else if(!getDataManager().getCurrentUserEmail().equalsIgnoreCase(user.getEmail())){
            getDataManager().wipeExpensesData()
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            getDataManager().updateUserInfoInPrefs(user.getUserID(),
                                    user.getEmail(),
                                    user.getCurrency(),
                                    DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_SERVER);
                            getDataManager().setBiometricVerificationStatus(DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState());

                            sucessfullLogin.call();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

        }else{
//            if(getDataManager().getBiometricVerificationStatus()==DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState()){
//                promptFingerPrintAfterLogin.setValue(1);
//            }else{
//                getDataManager().updateUserInfoInPrefs(user.getUserID(),
//                        user.getEmail(),
//                        user.getCurrency(),
//                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_SERVER);
//                getDataManager().setBiometricVerificationStatus(DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState());
//                onOpenMainActivityEvent();
//            }
            sucessfullLogin.call();

        }
        hideLoading();

    }

    public void getUserName (){
        getDataManager().getUser(getDataManager().getCurrentUserEmail())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        getUserNameEvent.setValue(user.getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
