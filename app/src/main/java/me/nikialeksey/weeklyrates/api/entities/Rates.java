package me.nikialeksey.weeklyrates.api.entities;

import java.util.Date;
import java.util.List;

public class Rates {
    private String base;
    private Date date;
    private List<Rate> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
