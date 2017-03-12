package me.nikialeksey.weeklyrates.rates;

import android.support.annotation.NonNull;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.api.rest.RatesApi;

class RatesPresenter extends MvpBasePresenter<RatesView> {

    private final RatesApi api;
    private final RateDateFormatter rateDateFormatter;
    private final RatesModel ratesModel;
    private final int daysCountForLoadingRates;

    RatesPresenter(final RatesApi api, final RateDateFormatter rateDateFormatter, final RatesModel ratesModel, final int daysCountForLoadingRates) {
        this.api = api;
        this.rateDateFormatter = rateDateFormatter;
        this.ratesModel = ratesModel;
        this.daysCountForLoadingRates = daysCountForLoadingRates;
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
            final Multimap<String, Rate> weeklyRates = getWeeklyRates(actualRates);

            getView().setData(actualRates, weeklyRates);
        }
    }

    private void loadRemote() {
        LocalDate currentDay = new LocalDate().minusDays(daysCountForLoadingRates).plusDays(1);

        final List<Single<List<Rate>>> rateRequests = new ArrayList<>();
        for (int i = 0; i < daysCountForLoadingRates; i++) {
            final String currentDayText = rateDateFormatter.formatLocalDate(currentDay);
            rateRequests.add(api.rates(currentDayText));

            currentDay = currentDay.plusDays(1);
        }

        Single.merge(rateRequests)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Rate>>() {
                    @Override
                    public void accept(final List<Rate> rates) {
                        if (isViewAttached()) {
                            ratesModel.save(rates);

                            loadLocal();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                    }
                });
    }

    @NonNull
    private Multimap<String, Rate> getWeeklyRates(final List<Rate> actualRates) {
        final Multimap<String, Rate> weeklyRates = ArrayListMultimap.create();
        for (final Rate actualRate : actualRates) {
            final List<Rate> weeklyRatesByRate = ratesModel.weeklyRatesByRate(actualRate);
            weeklyRates.putAll(actualRate.getCurrency(), weeklyRatesByRate);
        }
        return weeklyRates;
    }

}
