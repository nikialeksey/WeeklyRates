package me.nikialeksey.weeklyrates.rates;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.rates.impl.RatesModelImpl;

@Module
public class RatesModule {

    @Provides
    @Singleton
    RatesModel provideRateModel() {
        return new RatesModelImpl();
    }
}
