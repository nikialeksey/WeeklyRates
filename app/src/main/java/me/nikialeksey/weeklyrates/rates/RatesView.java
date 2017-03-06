package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.entities.Rates;

public interface RatesView extends MvpLceView<List<Rate>> {

    void setData(Rates rates);

}
