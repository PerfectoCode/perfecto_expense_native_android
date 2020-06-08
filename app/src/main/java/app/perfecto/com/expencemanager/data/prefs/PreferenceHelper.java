package app.perfecto.com.expencemanager.data.prefs;


import app.perfecto.com.expencemanager.data.DataManager;

/**
 * Created by Abhijit on 08-11-2017.
 */

public interface PreferenceHelper {

    void setCurrentUserId(Long id);

    Long getCurrentUserId();


    void setCurrentUserEmail(String email);

    String getCurrentUserEmail();

    void setCurrentUserSelectedCurrency(String SelectedCurrency);

    String getCurrentUserSelectedCurrency();

    void updateUserInfoInPrefs(Long userId,
                               String userEmail,
                               String userSelectedCurrency,
                               DataManager.LoggedInMode mode);

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedOut();

    void setBiometricVerificationStatus(int biometricVerificationStatus);

    int getBiometricVerificationStatus();
}
