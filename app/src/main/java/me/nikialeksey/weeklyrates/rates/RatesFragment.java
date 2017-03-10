package me.nikialeksey.weeklyrates.rates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;

public class RatesFragment extends MvpFragment<RatesView, RatesPresenter> implements RatesView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    RatesApi ratesApi;

    @BindView(R.id.rates)
    RecyclerView ratesView;
    @BindView(R.id.ratesRefresh)
    SwipeRefreshLayout ratesRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rates_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WeeklyRatesApp.getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);

        ratesView.setLayoutManager(new LinearLayoutManager(getContext()));

        ratesRefreshLayout.setOnRefreshListener(this);
    }

    @NonNull
    @Override
    public RatesPresenter createPresenter() {
        return new RatesPresenter(ratesApi);
    }

    @Override
    public void setData(final Rates rates) {
        ratesView.setAdapter(new RatesAdapter(rates.getRates()));
        ratesRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getPresenter().load(true);
    }
}
