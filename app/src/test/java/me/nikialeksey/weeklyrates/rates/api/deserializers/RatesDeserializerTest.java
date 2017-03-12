package me.nikialeksey.weeklyrates.rates.api.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.reflect.TypeToken;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import me.nikialeksey.weeklyrates.rates.api.deserializers.impl.RateDateFormatterImpl;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;

import static org.junit.Assert.assertThat;

public class RatesDeserializerTest {

    private final RateDateFormatter rateDateFormatter = new RateDateFormatterImpl();
    private final RatesDeserializer ratesDeserializer = new RatesDeserializer(rateDateFormatter);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeToken<List<Rate>> typeToken = new TypeToken<List<Rate>>() {
    };

    @Before
    public void setUp() {
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(typeToken.getRawType(), ratesDeserializer);
        objectMapper.registerModule(simpleModule);
    }


    @Test
    public void deserializeRates() throws IOException {
        final String json = "{\"date\": \"2001-01-01\", \"rates\": {\"USD\" : 0.12}}";
        final List<Rate> rates = (List<Rate>) objectMapper.readValue(json, typeToken.getRawType());

        final Date expectedDate = rateDateFormatter.parseLocalDate("2001-01-01").toDate();
        final String expectedValue = "0.12";
        final String expectedCurrency = "USD";

        assertThat(rates.size(), IsEqual.equalTo(1));
        final Rate rate = rates.get(0);
        assertThat(rate.getDate(), IsEqual.equalTo(expectedDate));
        assertThat(rate.getValue(), IsEqual.equalTo(expectedValue));
        assertThat(rate.getCurrency(), IsEqual.equalTo(expectedCurrency));
    }

}