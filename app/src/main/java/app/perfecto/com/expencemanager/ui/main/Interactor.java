package app.perfecto.com.expencemanager.ui.main;



public interface Interactor {

    interface Expense {
        void onExpenseListReFetched();

        void updateSwipeRefreshLayoutOne(boolean isVisible);

        void displayDeleteButtonInActionBar(boolean isVisible);
    }
}
