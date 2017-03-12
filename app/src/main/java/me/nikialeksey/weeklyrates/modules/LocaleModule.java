package me.nikialeksey.weeklyrates.modules;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;
import me.nikialeksey.weeklyrates.locale.impl.NumericRepresenterImpl;

@Module
public class LocaleModule {

    @Provides
    NumericRepresenter provideNumericRepresenter() {
        return new NumericRepresenterImpl();
    }

}
