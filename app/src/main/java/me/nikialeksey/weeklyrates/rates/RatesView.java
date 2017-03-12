package me.nikialeksey.weeklyrates.rates;

import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.nikialeksey.weeklyrates.rates.api.entities.Rate;

interface RatesView extends MvpView {

    void setData(List<Rate> rates, Multimap<String, Rate> weeklyRates);

}
