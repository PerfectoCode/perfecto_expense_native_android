package app.perfecto.com.expencemanager.ui.main.about;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import app.perfecto.com.expencemanager.R;
import app.perfecto.com.expencemanager.ui.base.BaseFragment;
import butterknife.ButterKnife;


public class AboutFragment extends BaseFragment<AboutViewModel> {


    private AboutViewModel aboutViewModel;

    @Inject
    ViewModelProvider.Factory factory;


    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.about_fragment, container, false);

        setUnBinder(ButterKnife.bind(this, view));


        observeAllEvents();

        return view;
    }

    @Override
    public AboutViewModel getViewModel() {
        aboutViewModel = ViewModelProviders.of(this, factory).get(AboutViewModel.class);
        return aboutViewModel;
    }

    @Override
    protected void setUp(View view) {


    }


    private void observeAllEvents() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {


        super.onDetach();
    }
}
