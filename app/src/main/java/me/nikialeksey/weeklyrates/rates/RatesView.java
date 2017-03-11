package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.nikialeksey.weeklyrates.api.entities.Rate;

interface RatesView extends MvpView {

    void setData(final List<Rate> rates);

}
