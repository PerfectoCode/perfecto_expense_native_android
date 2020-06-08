package app.perfecto.com.expencemanager.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;



public class CustomSwipeToRefresh extends SwipeRefreshLayout {

    private int mTouchSlop;
    private float mPrevX;

    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


}
