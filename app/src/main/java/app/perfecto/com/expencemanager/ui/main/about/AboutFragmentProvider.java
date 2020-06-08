package app.perfecto.com.expencemanager.ui.main.about;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Abhijit on 09-12-2017.
 */

@Module
public abstract class AboutFragmentProvider {

    @ContributesAndroidInjector(modules = AboutFragmentModule.class)
    abstract AboutFragment providesAboutFragmentFactory();
}
