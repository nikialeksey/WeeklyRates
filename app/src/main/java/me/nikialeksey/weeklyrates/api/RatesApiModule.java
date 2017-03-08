package me.nikialeksey.weeklyrates.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.api.deserializers.RatesDeserializer;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class RatesApiModule {

    @Provides
    @Singleton
    ObjectMapper provideRatesObjectMapper() {
        final SimpleModule ratesDeserializerModule = new SimpleModule();
        ratesDeserializerModule.addDeserializer(Rates.class, new RatesDeserializer());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(ratesDeserializerModule);
        return mapper;
    }

    @Provides
    @Singleton
    RatesApi provideRatesApi(final Retrofit.Builder retrofitBuilder, final ObjectMapper objectMapper) {
        final Retrofit retrofit = retrofitBuilder
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
        return retrofit.create(RatesApi.class);
    }
}
