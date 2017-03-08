package me.nikialeksey.weeklyrates.components;

import javax.inject.Singleton;

import dagger.Component;
import me.nikialeksey.weeklyrates.RatesActivity;
import me.nikialeksey.weeklyrates.modules.ApplicationModule;
import me.nikialeksey.weeklyrates.modules.NetworkModule;
import me.nikialeksey.weeklyrates.modules.PropertiesModule;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, PropertiesModule.class})
public interface ApplicationComponent {
    void inject(RatesActivity activity);
}
