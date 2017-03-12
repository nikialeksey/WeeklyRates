package me.nikialeksey.weeklyrates.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.BuildConfig;

@Module
public class PropertiesModule {

    @Provides
    @Named("baseUrl")
    String provideBaseUrl() {
        return BuildConfig.BASE_URL;
    }

    @Provides
    @Named("cacheSize")
    int provideCacheSize() {
        return 10 * 1024 * 1024; // 10 MiB
    }

    @Provides
    @Named("daysCountForLoadingRates")
    int provideDaysCountForLoadingRates() {
        return BuildConfig.DAYS_COUNT_FOR_LOADING_RATES;
    }

}
