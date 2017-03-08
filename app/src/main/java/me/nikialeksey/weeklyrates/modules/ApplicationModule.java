package me.nikialeksey.weeklyrates.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;

@Module
public class ApplicationModule {
    private WeeklyRatesApp application;

    public ApplicationModule(WeeklyRatesApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    WeeklyRatesApp provideWeeklyRatesApp() {
        return application;
    }
}
