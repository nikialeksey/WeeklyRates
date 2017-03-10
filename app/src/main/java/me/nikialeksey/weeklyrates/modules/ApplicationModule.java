package me.nikialeksey.weeklyrates.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;

@Module
public class ApplicationModule {
    private final WeeklyRatesApp application;

    public ApplicationModule(final WeeklyRatesApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    WeeklyRatesApp provideWeeklyRatesApp() {
        return application;
    }
}
