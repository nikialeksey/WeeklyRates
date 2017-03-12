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

import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;
import me.nikialeksey.weeklyrates.rates.impl.RatesAdapterImpl;

public class RatesFragment extends MvpFragment<RatesView, RatesPresenter> implements RatesView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    RatesApi ratesApi;
    @Inject
    RatesModel ratesModel;
    @Inject
    RateDateFormatter rateDateFormatter;

    @BindView(R.id.rates)
    RecyclerView ratesView;
    @BindView(R.id.ratesRefresh)
    SwipeRefreshLayout ratesRefreshLayout;

    private Realm realm;
    private RatesAdapter ratesAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rates_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        WeeklyRatesApp.getApplicationComponent().inject(this);
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();
        ratesModel.updateRealmInstance(realm);

        ratesAdapter = new RatesAdapterImpl();
        ratesView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratesView.setAdapter((RecyclerView.Adapter) ratesAdapter);

        ratesRefreshLayout.setOnRefreshListener(this);

        getPresenter().load(false);
    }

    @NonNull
    @Override
    public RatesPresenter createPresenter() {
        return new RatesPresenter(ratesApi, rateDateFormatter, ratesModel);
    }

    @Override
    public void setData(final List<Rate> rates, final Multimap<String, Rate> weeklyRates) {
        ratesAdapter.changeRates(rates, weeklyRates);
        ratesRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getPresenter().load(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
