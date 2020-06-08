package app.perfecto.com.expencemanager.utils.interactors;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;



public class SnackbarMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final SnackbarObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                if (s != null)
                    return;

                observer.onNewMessage(s);
            }
        });
    }


    public interface SnackbarObserver {
        /**
         * Called when there is a new message to be shown.
         * @param snackbarMessage The new message, non-null.
         */

        void onNewMessage(@StringRes Integer snackbarMessage);
    }
}