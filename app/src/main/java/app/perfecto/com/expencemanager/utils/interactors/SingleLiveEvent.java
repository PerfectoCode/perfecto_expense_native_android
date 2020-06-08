package app.perfecto.com.expencemanager.utils.interactors;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;



import java.util.concurrent.atomic.AtomicBoolean;

import app.perfecto.com.expencemanager.utils.AppLogger;


public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer){


        if (hasActiveObservers()) {
            AppLogger.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        postValue(null);
    }
}
