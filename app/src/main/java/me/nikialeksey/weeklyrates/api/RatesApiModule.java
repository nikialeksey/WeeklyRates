package me.nikialeksey.weeklyrates.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.reflect.TypeToken;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.nikialeksey.weeklyrates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.api.deserializers.RatesDeserializer;
import me.nikialeksey.weeklyrates.api.deserializers.impl.RateDateFormatterImpl;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class RatesApiModule {

    @Provides
    @Singleton
    RateDateFormatter provideDateFormatter() {
        return new RateDateFormatterImpl();
    }

    @Provides
    @Singleton
    ObjectMapper provideRatesObjectMapper(final RateDateFormatter rateDateFormatter) {
        final SimpleModule ratesDeserializerModule = new SimpleModule();
        final TypeToken<List<Rate>> ratesTypeToken = new TypeToken<List<Rate>>() {
        };

        ratesDeserializerModule.addDeserializer(ratesTypeToken.getRawType(), new RatesDeserializer(rateDateFormatter));

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
