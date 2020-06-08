package app.perfecto.com.expencemanager.data;


import app.perfecto.com.expencemanager.data.db.DbHelper;
import app.perfecto.com.expencemanager.data.network.ApiHelper;
import app.perfecto.com.expencemanager.data.prefs.PreferenceHelper;



public interface DataManager extends DbHelper, PreferenceHelper, ApiHelper {

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_LOGGED_GOOGLE(1),
        LOGGED_IN_MODE_LOGGED_FB(2),
        LOGGED_IN_MODE_LOGGED_SERVER(3);

        private final int type;

        LoggedInMode(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }


    }

    enum BIOMETRIC_STATE {
        NOT_PROMPTED(0),
        STORED(1),
        NOT_STORED(2);


        private final int state;

        BIOMETRIC_STATE(int state){
            this.state = state;
        };

        public int getBiometricState(){
            return this.state;
        }
    }


}
