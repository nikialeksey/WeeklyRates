package me.nikialeksey.weeklyrates.api.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import io.realm.RealmList;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.entities.Rates;

public class RatesDeserializer extends JsonDeserializer<Rates> {

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

        final String base = node.get("base").asText();
        final String date = node.get("date").asText();

        final Rates rates = new Rates();
        rates.setRates(rateList);
        rates.setBase(base);
        rates.setDate(date);

        return rates;
    }
}
