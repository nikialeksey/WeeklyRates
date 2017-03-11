package me.nikialeksey.weeklyrates.components;

import javax.inject.Singleton;

import dagger.Component;
import me.nikialeksey.weeklyrates.MainActivity;
import me.nikialeksey.weeklyrates.api.RatesApiModule;
import me.nikialeksey.weeklyrates.modules.ApplicationModule;
import me.nikialeksey.weeklyrates.modules.LocaleModule;
import me.nikialeksey.weeklyrates.modules.NetworkModule;
import me.nikialeksey.weeklyrates.modules.PropertiesModule;
import me.nikialeksey.weeklyrates.rates.RatesFragment;
import me.nikialeksey.weeklyrates.rates.RatesModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        PropertiesModule.class,
        RatesApiModule.class,
        LocaleModule.class,
        RatesModule.class
})
public interface ApplicationComponent {

    void inject(MainActivity activity);

    void inject(RatesFragment fragment);
}
