package me.nikialeksey.weeklyrates.api.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import me.nikialeksey.weeklyrates.api.entities.Rate;

public class RatesDeserializer extends JsonDeserializer<List<Rate>> {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public List<Rate> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);

        final String dateText = node.get("date").asText();
        final LocalDate date = dateTimeFormatter.parseLocalDate(dateText);

        final RealmList<Rate> rates = new RealmList<>();

        final Iterator<Map.Entry<String, JsonNode>> rateNodes = node.get("rates").fields();
        while (rateNodes.hasNext()) {
            final Map.Entry<String, JsonNode> rateNode = rateNodes.next();
            final String currency = rateNode.getKey();
            final String value = rateNode.getValue().asText();

            final Rate rate = new Rate();
            rate.setCurrency(currency);
            rate.setValue(value);
            rate.setDate(date.toDate());
            rate.setId(dateText + currency);

            rates.add(rate);
        }

        return rates;
    }
}
