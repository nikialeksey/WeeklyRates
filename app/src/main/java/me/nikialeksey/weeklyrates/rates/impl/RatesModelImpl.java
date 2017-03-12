package me.nikialeksey.weeklyrates.rates.impl;

import com.google.common.collect.Lists;

import org.joda.time.DateTimeConstants;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.RatesModel;

public class RatesModelImpl implements RatesModel {

    private Realm realm;

    @Override
    public void updateRealmInstance(final Realm realm) {
        this.realm = realm;
    }

    @Override
    public Rate findNearestLessDateWithCurrency(final Date date, final String currencyCode) {
        return realm.where(Rate.class)
                .equalTo("currency", currencyCode)
                .lessThanOrEqualTo("date", date)
                .findAllSorted("date", Sort.DESCENDING)
                .first();
    }

    @Override
    public void save(final List<Rate> rates) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                realm.copyToRealmOrUpdate(rates);
            }
        });
    }

    @Override
    public List<Rate> actualRates() {
        final Date maximumDate = realm.where(Rate.class).maximumDate("date");
        if (maximumDate == null) return Collections.emptyList();

        return realm.where(Rate.class).equalTo("date", maximumDate).findAll();
    }

    @Override
    public List<Rate> weeklyRatesByRate(final Rate rate) {
        final List<Rate> weeklyRates = realm.where(Rate.class)
                .equalTo("currency", rate.getCurrency())
                .findAllSorted("date", Sort.DESCENDING);

        return Lists.reverse(weeklyRates.subList(0, Math.min(weeklyRates.size(), DateTimeConstants.DAYS_PER_WEEK)));
    }

}
