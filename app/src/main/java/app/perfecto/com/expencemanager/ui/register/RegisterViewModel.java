package app.perfecto.com.expencemanager.ui.register;




import android.os.Build;

import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.DataManager;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.ui.base.BaseViewModel;
import app.perfecto.com.expencemanager.utils.AppLogger;
import app.perfecto.com.expencemanager.utils.NetworkUtils;
import app.perfecto.com.expencemanager.utils.interactors.SingleLiveEvent;
import app.perfecto.com.expencemanager.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;
import me.aflak.libraries.callback.FingerprintDialogCallback;

/**
 * Created by Abhijit on 07-12-2017.
 */

public class RegisterViewModel extends BaseViewModel implements FingerprintDialogCallback {

    private final SingleLiveEvent<Void> openLoginActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> checkFingerprintEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> userInsertionError = new SingleLiveEvent<>();


    public RegisterViewModel(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }


    /*
     * NAVIGATION
     * GETTERS for observing events from UI thread(i.e Activity)
     * */

    public SingleLiveEvent<Void> getOpenMainActivityEvent() {
        return openLoginActivityEvent;
    }

    public SingleLiveEvent<Void> getCheckFingerprintEvent() {
        return checkFingerprintEvent;
    }

    /*
     * NAVIGATION
     * Commands to update Events, which are observed from UI thread
     * */

    private void onOpenLoginActivityEvent() {
        openLoginActivityEvent.call();
    }

    private void onCheckFingerprintEvent() {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            checkFingerprintEvent.call();
        }else{
            onFingerPrintStored(false);
            onOpenLoginActivityEvent();
        }
    }



    public void insertUser(User user){
        showLoading();

        getCompositeDisposable().add(
                getDataManager().insertUser(user)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                hideLoading();
                                getDataManager().updateUserInfoInPrefs(user.getUserID(),
                                        user.getEmail(),
                                        user.getCurrency(),
                                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_SERVER);
                                getDataManager().setCurrentUserSelectedCurrency(user.getCurrency());
                                getDataManager().setBiometricVerificationStatus(DataManager.BIOMETRIC_STATE.NOT_PROMPTED.getBiometricState());
                                //onCheckFingerprintEvent();
                                onOpenLoginActivityEvent();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AppLogger.i("User throwable:",throwable);
                                showSnackbarMessage(R.string.error_sign_up);
                                hideLoading();
                            }
                        })
        );
    }



    public void onFingerPrintStored(boolean isFingerPrintAuthenticated){

        getDataManager().setBiometricVerificationStatus(isFingerPrintAuthenticated?1:2);
    }

    @Override
    public void onAuthenticationSucceeded() {
        showSnackbarMessage(R.string.fingerprint_success);
        onFingerPrintStored(false);
        onOpenLoginActivityEvent();
    }

    @Override
    public void onAuthenticationCancel() {
        // Logic when user canceled operation
        onFingerPrintStored(false);
        onOpenLoginActivityEvent();
    }


}
