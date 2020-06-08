package app.perfecto.com.expencemanager.ui.main.expenses;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Abhijit on 09-12-2017.
 */

@Module
public abstract class ExpenseFragmentProvider {

    @ContributesAndroidInjector(modules = ExpenseFragmentModule.class)
    abstract ExpenseFragment providesBlogFragmentFactory();
}
