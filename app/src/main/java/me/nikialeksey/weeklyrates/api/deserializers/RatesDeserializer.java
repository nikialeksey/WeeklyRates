package me.nikialeksey.weeklyrates.api.deserializers;

import android.annotation.SuppressLint;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import io.realm.RealmList;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.entities.Rates;

public class RatesDeserializer extends JsonDeserializer<Rates> {
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Rates deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);

        final RealmList<Rate> rateList = new RealmList<>();

        final Iterator<Map.Entry<String, JsonNode>> rateNodes = node.get("rates").fields();
        while (rateNodes.hasNext()) {
            final Map.Entry<String, JsonNode> rateNode = rateNodes.next();
            final String currency = rateNode.getKey();
            final String value = rateNode.getValue().asText();

            final Rate rate = new Rate();
            rate.setCurrency(currency);
            rate.setValue(value);

            rateList.add(rate);
        }

        final Rates rates = new Rates();
        rates.setRates(rateList);

        final String base = node.get("base").asText();
        rates.setBase(base);

        final String dateRepresentation = node.get("date").asText();
        try {
            final Date date = dateFormatter.parse(dateRepresentation);
            rates.setDate(date);
        } catch (final ParseException ignored) {
        }

        return rates;
    }
}
