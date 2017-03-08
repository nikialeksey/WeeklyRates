package me.nikialeksey.weeklyrates.modules;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Cache provideOkHttpCache(final WeeklyRatesApp application, @Named("cacheSize") final int cacheSize) {
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    X509TrustManager provideTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }

            return (X509TrustManager) trustManagers[0];
        } catch (KeyStoreException | NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    @Provides
    @Singleton
    SSLSocketFactory provideSslSocketFactory(final X509TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException ignored) {
        }
        return null;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(final Cache cache, final SSLSocketFactory sslSocketFactory, final X509TrustManager trustManager) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(final OkHttpClient okHttpClient, @Named("baseUrl") final String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    RatesApi provideRatesApi(final Retrofit retrofit) {
        return retrofit.create(RatesApi.class);
    }
}
