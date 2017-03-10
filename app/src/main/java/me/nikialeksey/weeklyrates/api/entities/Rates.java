package me.nikialeksey.weeklyrates.api.entities;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Rates implements RealmModel {

    @PrimaryKey
    private String date;
    private RealmList<Rate> rates;

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public RealmList<Rate> getRates() {
        return rates;
    }

    public void setRates(final RealmList<Rate> rates) {
        this.rates = rates;
    }
}
