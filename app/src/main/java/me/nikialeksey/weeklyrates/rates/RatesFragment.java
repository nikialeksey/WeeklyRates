package me.nikialeksey.weeklyrates.rates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.api.rest.RatesApi;
import me.nikialeksey.weeklyrates.rates.impl.RatesAdapterImpl;

public class RatesFragment extends MvpFragment<RatesView, RatesPresenter>
        implements RatesView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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
    @BindView(R.id.action)
    Button action;
    @BindView(R.id.rates)
    RecyclerView ratesView;
    @BindView(R.id.ratesRefresh)
    SwipeRefreshLayout ratesRefreshLayout;

    private Realm realm;
    private RatesAdapter ratesAdapter;
    private Snackbar snackbar;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        WeeklyRatesApp.getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.rates_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();
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
        return new RatesPresenter(
                ratesApi,
                rateDateFormatter,
                ratesModel,
                daysCountForLoadingRates,
                new CompositeDisposable()
        );
    }

    @Override
    public void setData(final List<Rate> rates, final Multimap<String, Rate> weeklyRates) {
        ratesAdapter.changeRates(rates, weeklyRates);
        ratesRefreshLayout.setRefreshing(false);

        final String date = rateDateFormatter.formatLocalDate(
                new LocalDate(rates.get(0).getDate())
        );
        showMessage(R.string.syncingAtDate, date);
    }

    @Override
    public void showErrorLoadingMessage() {
        ratesRefreshLayout.setRefreshing(false);
        showMessage(R.string.errorLoading, R.string.reloadAction);
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

    private void showMessage(final int messageId, final int actionId) {
        if (ratesAdapter.isEmpty()) {
            showMessageContainer();
            message.setText(messageId);
            action.setText(actionId);
            action.setOnClickListener(this);
        } else {
            snackbar = Snackbar.make(ratesView, messageId, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(actionId, this);
            snackbar.show();
        }
    }

    private void showMessage(final int messageId, final Object... args) {
        final String message = getContext().getString(messageId, args);
        snackbar = Snackbar.make(ratesView, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    @Override
    public void onClick(final View v) {
        showLoading();
        getPresenter().load(true);
    }
}
