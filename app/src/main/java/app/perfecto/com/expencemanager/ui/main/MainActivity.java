package app.perfecto.com.expencemanager.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import app.perfecto.com.expencemanager.BuildConfig;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.ui.base.BaseActivity;
import app.perfecto.com.expencemanager.ui.custom.CustomSwipeToRefresh;
import app.perfecto.com.expencemanager.ui.custom.CustomViewPager;
import app.perfecto.com.expencemanager.ui.expenseDetail.ExpenseDetailActivity;
import app.perfecto.com.expencemanager.ui.login.LoginActivity;
import app.perfecto.com.expencemanager.ui.main.expenses.ExpenseFragment;
import app.perfecto.com.expencemanager.ui.register.RegisterActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;



public class MainActivity extends BaseActivity<MainViewModel>
        implements Interactor.Expense, HasSupportFragmentInjector {



    private Menu menu;

    private MenuItem mMenuItem;

    private TextView tvUserName, tvUserEmail;

    ActionBarDrawerToggle drawerToggle;

    @Inject
    MainViewModel mainViewModel;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    MainPagerAdapter pagerAdapter;


    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_view_pager)
    CustomViewPager viewPager;



    @BindView(R.id.swipe_to_refresh)
    LinearLayout refreshLayout;

    @BindView(R.id.list_add_btn)
    FloatingActionButton fabAddNewButton;




    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setUnBinder(ButterKnife.bind(this));

        setUp();

        observeAllEvents();
    }


    @Override
    public MainViewModel getViewModel() {
        return mainViewModel;
    }


    //INITIAL SET-UP
    @Override
    protected void setUp() {
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setUpNavMenu();
        mainViewModel.onNavMenuCreated();
        setUpViewPager();


    }


    //DRAWER
    public void closeNavigationDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //CALLBACKS FROM FRAGMENTS
    private void refresh() {
        onExpenseListReFetched();


    }



    private void checkRefreshFlag() {

    }


    //NAVIGATION MENU
    private void setUpNavMenu() {
        View headerLayout = navigationView.getHeaderView(0);

        tvUserName = headerLayout.findViewById(R.id.tv_name);
        tvUserEmail = headerLayout.findViewById(R.id.tv_email);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (item.getItemId()) {
                    case R.id.list_logout_menu:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Log Out")
                                .setMessage("Logging out will delete all your expenses. Do you really want to Log Out?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mainViewModel.onDrawerOptionLogoutClicked();


                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                        return true;
                    case R.id.list_about_menu:
                        mainViewModel.onDrawerOptionAboutClicked();
                       // toolbar.setTitle(R.string.about);
                        return true;
                    default:
                        return false;
                }
            }
        });

        updateAppVersion();
    }


    //VIEWPAGER
    private void setUpViewPager() {
        pagerAdapter.setCount(2);

        viewPager.setAdapter(pagerAdapter);

    }


    //OBSERVE EVENTS FROM VIEW-MODEL
    private void observeAllEvents() {
        //EVENT (transmit DATA)
        mainViewModel.getUser().observe(this,
                new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        updateUserViews(user);
                    }
                });


        //EVENT
        mainViewModel.getOpenLoginActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openLoginActivity();
                    }
                });

        mainViewModel.getOpenAboutActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                       startActivity(AboutActivity.getStartIntent(MainActivity.this));
                    }
                });

        mainViewModel.getCloseNavigationDrawerEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        closeNavigationDrawer();
                    }
                });
    }





    //UPDATE VIEWS
    public void resetAllAdapterPositions() {
        viewPager.setCurrentItem(0);
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            WeakReference<Fragment> fragment = pagerAdapter.getRegisteredFragments().get(i);



            fragment = null;
        }
    }



    public void updateUserViews(User user) {
        if (user != null) {
            if (user.getName() != null)
                tvUserName.setText(user.getName());
            if (user.getEmail() != null)
                tvUserEmail.setText(user.getEmail());
        }
    }

    public void updateAppVersion() {
        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        tvAppVersion.setText(version);
    }

    //NAVIGATION
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }




    //MENU ITEMS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        onExpenseListReFetched();
        unlockDrawer();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @OnClick(R.id.list_add_btn)
    public void openExpenseDetailActivity(){
        startActivity(ExpenseDetailActivity.getStartIntent(MainActivity.this));
    }


    @Override
    public void onExpenseListReFetched() {
        WeakReference<Fragment> fragmentWeakReference =
                pagerAdapter.getRegisteredFragments().get(0);

        if (fragmentWeakReference != null)
            ((ExpenseFragment) fragmentWeakReference.get()).onParentCallToFetchList();

        fragmentWeakReference = null;

    }

    @Override
    public void updateSwipeRefreshLayoutOne(boolean isVisible) {
        checkRefreshFlag();
    }

    @Override
    public void displayDeleteButtonInActionBar(boolean isVisible) {
        if(menu!= null) {

            menu.findItem(R.id.list_delete_btn).setVisible(isVisible);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_delete_btn:
                deleteSelectedExpenses();
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }

    private void deleteSelectedExpenses(){
        WeakReference<Fragment> fragmentWeakReference =
                pagerAdapter.getRegisteredFragments().get(0);

        if (fragmentWeakReference != null)
            ((ExpenseFragment) fragmentWeakReference.get()).deleteSelectedExpenses();

        fragmentWeakReference = null;
    }

    private void openAboutActivity(){

    }

    @Override
    public void onBackPressed() {
            if(viewPager.getCurrentItem() != 0){
                viewPager.setCurrentItem(0,false);
                toolbar.setTitle(R.string.expenses);
                return;
            }
            super.onBackPressed();

    }
}
