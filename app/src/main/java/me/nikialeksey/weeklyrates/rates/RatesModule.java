package me.nikialeksey.weeklyrates.rates;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;
import me.nikialeksey.weeklyrates.rates.impl.RatesAdapterImpl;

@Module
public class RatesModule {
    @Provides
    RatesAdapter provideRatesAdapter(final NumericRepresenter numericRepresenter) {
        return new RatesAdapterImpl(numericRepresenter);
    }
}
