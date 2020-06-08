package app.perfecto.com.expencemanager.ui.expenseDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.ui.base.BaseViewHolder;
import app.perfecto.com.expencemanager.utils.AppLogger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhijit on 08-12-2017.
 */


public class AttachemntsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_EMPTY = 1;



    private List<String> attachments;

    public AttachemntsAdapter(List<String> attachments) {
        this.attachments = attachments;
    }





    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            default:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment_thumbnail, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {

        if (attachments != null && attachments.size() > 0)
            return VIEW_TYPE_NORMAL;
        else return VIEW_TYPE_EMPTY;
    }


    @Override
    public int getItemCount() {

        if (attachments != null && attachments.size() > 0)
            return attachments.size();
        else return 1;
    }

    /*
    * Update the current list to an assigned list
    *
    * This update method is called every time the list item has to be updated.
    * Previous List must be cleared to store new list, to avoid duplicates*/
    public void updateListItems(List<String> attachments) {
        this.attachments.clear();
        this.attachments.addAll(attachments);
        AppLogger.i("Count:"+String.valueOf(attachments.size()));
        notifyDataSetChanged();
    }





    public class ViewHolder extends BaseViewHolder {



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);



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

        }
    }
}

