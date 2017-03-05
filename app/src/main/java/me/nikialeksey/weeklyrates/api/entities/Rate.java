package me.nikialeksey.weeklyrates.api.entities;

import java.math.BigDecimal;
import java.util.Currency;

public class Rate {
    private Currency currency;
    private BigDecimal value;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
