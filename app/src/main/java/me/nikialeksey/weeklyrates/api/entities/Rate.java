package me.nikialeksey.weeklyrates.api.entities;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Rate implements RealmModel {

    private String currency;
    private String value;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
