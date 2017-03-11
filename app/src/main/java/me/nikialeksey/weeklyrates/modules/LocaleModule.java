package me.nikialeksey.weeklyrates.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;
import me.nikialeksey.weeklyrates.locale.impl.NumericRepresenterImpl;

@Module
public class LocaleModule {
    @Provides
    @Singleton
    NumericRepresenter provideNumericRepresenter() {
        return new NumericRepresenterImpl();
    }
}
