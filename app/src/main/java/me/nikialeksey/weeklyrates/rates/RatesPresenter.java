package me.nikialeksey.weeklyrates.rates;

import android.support.annotation.NonNull;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.api.rest.RatesApi;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

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
        if (isViewAttached()) {
            getView().showLoading();

            if (pullToRefresh) {
                loadRemote();
            } else {
                loadLocal();
            }
        }
    }

    private void loadLocal() {
        if (isViewAttached()) {
            final List<Rate> actualRates = ratesModel.actualRates();
            final Multimap<String, Rate> weeklyRates = getWeeklyRates(actualRates);

            if (actualRates.isEmpty()) {
                loadRemote();
            } else {
                getView().setData(actualRates, weeklyRates);
            }
        }
    }

    private void loadRemote() {
        LocalDate currentDay = new LocalDate().minusDays(daysCountForLoadingRates).plusDays(1);

        final List<Observable<List<Rate>>> rateRequests = new ArrayList<>();
        for (int i = 0; i < daysCountForLoadingRates; i++) {
            final String currentDayText = rateDateFormatter.formatLocalDate(currentDay);
            rateRequests.add(api.rates(currentDayText, "RUB"));

            currentDay = currentDay.plusDays(1);
        }

        Observable.merge(rateRequests)
                .toList()
                .map(new Func1<List<List<Rate>>, List<Rate>>() {
                    @Override
                    public List<Rate> call(final List<List<Rate>> ratesLists) {
                        final List<Rate> allRates = new ArrayList<>();
                        for (final List<Rate> rates : ratesLists) {
                            for (final Rate rate : rates) {
                                allRates.add(rate);
                            }
                        }
                        return allRates;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Rate>>() {
                    @Override
                    public void call(final List<Rate> rates) {
                        if (isViewAttached()) {
                            ratesModel.save(rates);

                            loadLocal();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(final Throwable throwable) {
                        if (isViewAttached()) {
                            getView().showErrorLoadingMessage();
                        }
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
