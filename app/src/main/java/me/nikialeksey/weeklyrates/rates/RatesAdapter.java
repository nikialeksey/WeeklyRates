package me.nikialeksey.weeklyrates.rates;

import com.google.common.collect.Multimap;

import java.util.List;

import me.nikialeksey.weeklyrates.api.entities.Rate;

public interface RatesAdapter {
    void changeRates(List<Rate> rates, final Multimap<String, Rate> weeklyRates);
}
