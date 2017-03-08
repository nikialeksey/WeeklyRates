package me.nikialeksey.weeklyrates.api.entities;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Rates implements RealmModel {

    @PrimaryKey
    private String base;
    @PrimaryKey
    private String date;
    private RealmList<Rate> rates;

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
    }

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
