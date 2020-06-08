package app.perfecto.com.expencemanager.ui.main.expenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.db.model.Expense;
import app.perfecto.com.expencemanager.ui.base.BaseViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhijit on 08-12-2017.
 */


public class ExpenseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_EMPTY = 1;
    private boolean isCheckBoxShowing = false;


    private Callback callback;
    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    void removeCallback() {
        callback = null;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense_view, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {

        if (expenseList != null && expenseList.size() > 0)
            return VIEW_TYPE_NORMAL;
        else return VIEW_TYPE_EMPTY;
    }


    @Override
    public int getItemCount() {

        if (expenseList != null && expenseList.size() > 0)
            return expenseList.size();
        else return 1;
    }



    /*
    * Update the current list to an assigned list
    *
    * This update method is called every time the list item has to be updated.
    * Previous List must be cleared to store new list, to avoid duplicates*/
    public void updateListItems(List<Expense> expenseList) {
        this.expenseList.clear();
        this.setCheckBoxShowing(false);
        this.expenseList.addAll(expenseList);
        notifyDataSetChanged();
    }


    public interface Callback {

        void onExpenseEmptyRetryClicked();

        void onExpenseItemClicked(Expense expense);

        void onExpenseItemSelected(long expenseCount);
    }


    public Expense getItem(int position){
        if(getItemCount()>0){
            return expenseList.get(position);
        }else
            return null;
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_view_btn)
        ImageView ivCover;

        @BindView(R.id.tv_exp_amount)
        TextView tvExpAmount;

        @BindView(R.id.tv_exp_currency)
        TextView tvCurrency;

        @BindView(R.id.tv_exp_date)
        TextView tvExpenseDate;

        @BindView(R.id.tv_exp_name)
        TextView tvExpenseName;

        @BindView(R.id.cb_select_box)
        CheckBox cbIsSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            ivCover.setImageDrawable(null);
            tvExpAmount.setText("");
            tvCurrency.setText("");
            tvExpenseDate.setText("");
            tvExpenseName.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            final Expense expense = expenseList.get(position);
            tvCurrency.setText(expense.getCurrency());
            tvExpAmount.setText(String.format("%.2f",expense.getAmount()));
            tvExpenseDate.setText(String.valueOf(expense.getDate()));
            tvExpenseName.setText(expense.getExpenseHead());
            if(isCheckBoxShowing()){
                cbIsSelected.setVisibility(View.VISIBLE);
                cbIsSelected.setChecked(expense.isSelected());
            }else{
                cbIsSelected.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expense!= null && callback!= null){
                        callback.onExpenseItemClicked(expense);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    expense.setSelected(true);
                    if(!isCheckBoxShowing){
                        setCheckBoxShowing(true);
                        callback.onExpenseItemSelected(getSelectedExpenses().size());
                        notifyDataSetChanged();
                    }
                    return true;
                }
            });

            cbIsSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    expense.setSelected(isChecked);
                    callback.onExpenseItemSelected(getSelectedExpenses().size());
                }
            });

        }


    }


    public class EmptyViewHolder extends BaseViewHolder {

        @BindView(R.id.btn_retry)
        Button retryButton;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        @OnClick(R.id.btn_retry)
        void onRetryClicked() {
            if (callback != null)
                callback.onExpenseEmptyRetryClicked();
        }
    }

    public boolean isCheckBoxShowing() {
        return isCheckBoxShowing;
    }

    public void setCheckBoxShowing(boolean checkBoxShowing) {
        isCheckBoxShowing = checkBoxShowing;
    }

    public List<Expense> getSelectedExpenses(){
        List<Expense> selectedList = new ArrayList<Expense>();
        for (Expense expense:expenseList
             ) {
            if(expense.isSelected()){
                selectedList.add(expense);
            }
        }

        return selectedList;
    }
}

