package app.perfecto.com.expencemanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.perfecto.com.expencemanager.BuildConfig;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.ui.expenseDetail.ExpenseDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutActivity extends AppCompatActivity {
    private Unbinder unBinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.versionDetails)
    TextView tvAppVersion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.unBinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        updateAppVersion();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }
    @Override
    protected void onDestroy() {
        if (unBinder != null)
            unBinder.unbind();
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateAppVersion() {
        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        tvAppVersion.setText(version);
    }

    @OnClick(R.id.crash_me_button)
    public void crashMe(){
        throw new RuntimeException("This is a crash");
    }
}
