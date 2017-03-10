package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.entities.Rates;

public interface RatesView extends MvpView {

    void setData(Rates rates);

}
