package me.nikialeksey.weeklyrates.api.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Rates implements RealmModel {

    private String base;
    private Date date;
    private RealmList<Rate> rates;

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public RealmList<Rate> getRates() {
        return rates;
    }

    public void setRates(final RealmList<Rate> rates) {
        this.rates = rates;
    }
}
