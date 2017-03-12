package me.nikialeksey.weeklyrates.rates;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.api.rest.RatesApi;

class RatesPresenter extends MvpBasePresenter<RatesView> {

    private final RatesApi api;
    private final RateDateFormatter rateDateFormatter;
    private final RatesModel ratesModel;

    RatesPresenter(final RatesApi api, final RateDateFormatter rateDateFormatter, final RatesModel ratesModel) {
        this.api = api;
        this.rateDateFormatter = rateDateFormatter;
        this.ratesModel = ratesModel;
    }

    void load(final boolean pullToRefresh) {
        if (pullToRefresh) {
            loadRemote();
        } else {
            loadLocal();
        }
    }

    private void loadLocal() {
        if (isViewAttached()) {
            final List<Rate> actualRates = ratesModel.actualRates();
            final Multimap<String, Rate> weeklyRates = ArrayListMultimap.create();
            for (final Rate actualRate : actualRates) {
                final List<Rate> weeklyRatesByRate = ratesModel.weeklyRatesByRate(actualRate);
                weeklyRates.putAll(actualRate.getCurrency(), weeklyRatesByRate);
            }

            getView().setData(actualRates, weeklyRates);
        }
    }

    private void loadRemote() {
        final LocalDate today = new LocalDate();
        final String todayText = rateDateFormatter.formatLocalDate(today);

        api
                .rates(todayText)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Rate>>() {
                    @Override
                    public void accept(final List<Rate> rates) {
                        if (isViewAttached()) {
                            ratesModel.save(rates);

                            getView().setData(rates, ArrayListMultimap.<String, Rate>create());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                    }
                });
    }

}
