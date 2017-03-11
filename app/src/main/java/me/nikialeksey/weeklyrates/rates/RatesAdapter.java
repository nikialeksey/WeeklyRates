package me.nikialeksey.weeklyrates.rates;

import java.util.List;

import me.nikialeksey.weeklyrates.api.entities.Rate;

public interface RatesAdapter {
    void changeRates(List<Rate> rates);
}
