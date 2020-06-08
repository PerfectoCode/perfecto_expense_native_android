package app.perfecto.com.expencemanager.utils.interactors;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;



public class ToastMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final ToastObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                if (s != null)
                    return;

                observer.onNewMessage(s);
            }
        });
    }


    public interface ToastObserver {
        /**
         * Called when there is a new message to be shown.
         * @param toastMessage The new message, non-null.
         */

        void onNewMessage(@StringRes Integer toastMessage);
    }
}