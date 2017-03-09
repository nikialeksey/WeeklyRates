package me.nikialeksey.weeklyrates.rates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import javax.inject.Inject;

import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;

public class RatesFragment extends MvpFragment<RatesView, RatesPresenter> {

    @Inject
    RatesApi ratesApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rates_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WeeklyRatesApp.getApplicationComponent().inject(this);
    }

    @NonNull
    @Override
    public RatesPresenter createPresenter() {
        return new RatesPresenter(ratesApi);
    }
}
