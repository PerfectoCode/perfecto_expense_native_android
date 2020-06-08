package app.perfecto.com.expencemanager.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Karthik V Dhanya on 10-04-2018.
 */

public class CustomViewPager extends ViewPager {

    private boolean swipeActionState;

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        swipeActionState = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(this.swipeActionState){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeActionState) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.swipeActionState = enabled;
    }
}
