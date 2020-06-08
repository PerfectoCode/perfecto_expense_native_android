package app.perfecto.com.expencemanager.ui.main.expenses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.ui.base.BaseFragment;
import app.perfecto.com.expencemanager.ui.expenseDetail.ExpenseDetailActivity;
import app.perfecto.com.expencemanager.ui.main.Interactor;
import app.perfecto.com.expencemanager.utils.AppLogger;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseFragment extends BaseFragment<ExpenseViewModel> {


    private Interactor.Expense callback;
    private ExpenseViewModel expenseViewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    ExpenseAdapter expenseAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;


    @BindView(R.id.rv_expenses)
    RecyclerView rvExpenses;

    private Paint p = new Paint();


    public static ExpenseFragment newInstance() {
        Bundle args = new Bundle();
        ExpenseFragment fragment = new ExpenseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        setUnBinder(ButterKnife.bind(this, view));

        expenseAdapter.setCallback(expenseViewModel);

        observeAllEvents();

        return view;
    }

    @Override
    public ExpenseViewModel getViewModel() {
        expenseViewModel = ViewModelProviders.of(this, factory).get(ExpenseViewModel.class);
        return expenseViewModel;
    }

    @Override
    protected void setUp(View view) {
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvExpenses.setLayoutManager(linearLayoutManager);
        rvExpenses.setItemAnimator(new DefaultItemAnimator());
        rvExpenses.setAdapter(expenseAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext().getApplicationContext(), DividerItemDecoration.VERTICAL);
        rvExpenses.addItemDecoration(decoration);

        expenseViewModel.fetchExpenseList();
        initSwipe();

    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Expense expense = expenseAdapter.getItem(position);

                if (direction == ItemTouchHelper.LEFT){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete Expense")
                            .setMessage("Do you really want to delete?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    expenseViewModel.deleteExpense(expense);


                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                    expenseAdapter.notifyDataSetChanged();

                } else {

                    openExpenseDetailActivityForEdit(expense);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        c.clipRect(background);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mode_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        c.clipRect(background);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_forever);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvExpenses);
    }



    private void observeAllEvents() {
        expenseViewModel.getExpenseList().observe(this,
                new Observer<List<Expense>>() {
                    @Override
                    public void onChanged(@Nullable List<Expense> expenseList) {
                        AppLogger.i("expenses:",expenseList);
                        updateExpensesAdapter(expenseList);
                    }
                });

        expenseViewModel.getOpenExpenseDetailActivity().observe(this,
                new Observer<Expense>() {
                    @Override
                    public void onChanged(@Nullable Expense expense) {
                        openExpenseDetailActivity(expense);
                    }
                });

        expenseViewModel.getPullToRefreshEvent().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isVisible) {
                        if (callback != null)
                            callback.updateSwipeRefreshLayoutOne(isVisible);

                    }
                });

        expenseViewModel.getExpenseListReFetched().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        onExpenseListReFetched();
                    }
                });

        expenseViewModel.getShowDeleteButtonEvent().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aVoid) {
                        callback.displayDeleteButtonInActionBar(aVoid);
                    }
                });
        expenseViewModel.getBatchDeleteFinishedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                expenseAdapter.setCheckBoxShowing(false);
                expenseViewModel.fetchExpenseList();
            }
        });
    }


    private void updateExpensesAdapter(List<Expense> expenseList) {
        expenseAdapter.setCheckBoxShowing(false);
        expenseAdapter.updateListItems(expenseList);
    }

    //This method call comes in from MainActivity
    public void setListScrollTop() {
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    //This method call comes in from MainActivity
    public void onParentCallToFetchList() {
        if(expenseAdapter!= null){
            expenseAdapter.setCheckBoxShowing(false);
        }
        expenseViewModel.fetchExpenseList();
    }


    public void onExpenseListReFetched() {
        if (callback != null)
            callback.onExpenseListReFetched();
        expenseAdapter.notifyDataSetChanged();


    }


    public void openExpenseDetailActivity(Expense expense) {
        Intent intent = ExpenseDetailActivity.getStartIntent(getContext());
        intent.putExtra(ExpenseDetailActivity.KEY_PARCELABLE_EXPENSE, expense);
        startActivity(intent);
    }

    public void openExpenseDetailActivityForEdit(Expense expense) {
        Intent intent = ExpenseDetailActivity.getStartIntent(getContext());
        intent.putExtra(ExpenseDetailActivity.KEY_PARCELABLE_EXPENSE, expense);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ExpenseDetailActivity.KEY_PARCELABLE_EDIT_MODE, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

         callback= (Interactor.Expense) context;

    }

    @Override
    public void onDetach() {
        callback = null;
        expenseAdapter.removeCallback();
        super.onDetach();
    }

    @Override
    public void onResume() {
        if(expenseAdapter!= null){
            expenseAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    public void deleteSelectedExpenses(){
        expenseViewModel.deleteExpenses(expenseAdapter.getSelectedExpenses());
    }

    public boolean isInMultiSelectMode(){
        return expenseAdapter.isCheckBoxShowing();
    }


}
