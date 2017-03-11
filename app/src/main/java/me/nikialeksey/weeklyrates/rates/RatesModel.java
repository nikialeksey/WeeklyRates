package me.nikialeksey.weeklyrates.rates;

import org.joda.time.LocalDate;

import io.realm.Realm;
import me.nikialeksey.weeklyrates.api.entities.Rate;

public interface RatesModel {
    Rate findNearestLess(Realm realm, LocalDate date, String currencyCode);
}
