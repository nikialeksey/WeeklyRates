package me.nikialeksey.weeklyrates.rates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.api.rest.RatesApi;
import me.nikialeksey.weeklyrates.rates.impl.RatesAdapterImpl;

public class RatesFragment extends MvpFragment<RatesView, RatesPresenter> implements RatesView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    RatesApi ratesApi;
    @Inject
    RatesModel ratesModel;
    @Inject
    RateDateFormatter rateDateFormatter;
    @Inject
    @Named("daysCountForLoadingRates")
    int daysCountForLoadingRates;

    @BindView(R.id.messageContainer)
    View messageContainer;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.rates)
    RecyclerView ratesView;
    @BindView(R.id.ratesRefresh)
    SwipeRefreshLayout ratesRefreshLayout;

    private Realm realm;
    private RatesAdapter ratesAdapter;
    private Snackbar snackbar;

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
        // TODO нужно вынести в отдельное поведение и сделать обновление рилма в базовом классе, например
        ratesModel.updateRealmInstance(realm);

        snackbar = Snackbar.make(ratesView, "", Snackbar.LENGTH_INDEFINITE);

        ratesAdapter = new RatesAdapterImpl();
        ratesView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratesView.setAdapter((RecyclerView.Adapter) ratesAdapter);

        ratesRefreshLayout.setOnRefreshListener(this);

        getPresenter().load(false);
    }

    @NonNull
    @Override
    public RatesPresenter createPresenter() {
        return new RatesPresenter(ratesApi, rateDateFormatter, ratesModel, daysCountForLoadingRates);
    }

    @Override
    public void setData(final List<Rate> rates, final Multimap<String, Rate> weeklyRates) {
        ratesAdapter.changeRates(rates, weeklyRates);
        ratesRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorLoadingMessage() {
        showMessage(R.string.errorLoading);
    }

    @Override
    public void showLoading() {
        showRatesView();

        ratesRefreshLayout.setRefreshing(true);
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

    private void showMessageContainer() {
        snackbar.dismiss();

        ratesRefreshLayout.setVisibility(View.GONE);
        messageContainer.setVisibility(View.VISIBLE);
    }

    private void showRatesView() {
        snackbar.dismiss();

        ratesRefreshLayout.setVisibility(View.VISIBLE);
        messageContainer.setVisibility(View.GONE);
    }

    private void showMessage(final int errorLoading) {
        showMessageContainer();

        message.setText(errorLoading);
    }
}
