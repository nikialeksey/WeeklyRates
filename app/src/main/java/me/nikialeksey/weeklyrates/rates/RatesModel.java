package me.nikialeksey.weeklyrates.rates;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;

public interface RatesModel {

    void updateRealmInstance(Realm realm);

    Rate findNearestLessDateWithCurrency(Date date, String currencyCode);

    void save(List<Rate> rates);

    List<Rate> actualRates();

    List<Rate> weeklyRatesByRate(Rate rate);
}
