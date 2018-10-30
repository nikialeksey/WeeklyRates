package me.nikialeksey.weeklyrates.rates;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.api.rest.RatesApi;

class RatesPresenter extends MvpBasePresenter<RatesView> {

    private final RatesApi api;
    private final RateDateFormatter rateDateFormatter;
    private final RatesModel ratesModel;
    private final int daysCountForLoadingRates;
    private final CompositeDisposable disposables;

    RatesPresenter(
            final RatesApi api,
            final RateDateFormatter rateDateFormatter,
            final RatesModel ratesModel,
            final int daysCountForLoadingRates,
            final CompositeDisposable disposables
    ) {
        this.api = api;
        this.rateDateFormatter = rateDateFormatter;
        this.ratesModel = ratesModel;
        this.daysCountForLoadingRates = daysCountForLoadingRates;
        this.disposables = disposables;
    }

    void load(final boolean pullToRefresh) {
        ifViewAttached(new ViewAction<RatesView>() {
            @Override
            public void run(@NonNull final RatesView view) {
                view.showLoading();
                if (pullToRefresh) {
                    loadRemote();
                } else {
                    loadLocal();
                }
            }
        });
    }

    private void loadLocal() {
        ifViewAttached(new ViewAction<RatesView>() {
            @Override
            public void run(@NonNull final RatesView view) {
                final List<Rate> actualRates = ratesModel.actualRates();
                final Multimap<String, Rate> weeklyRates = getWeeklyRates(actualRates);

                if (actualRates.isEmpty()) {
                    loadRemote();
                } else {
                    view.setData(actualRates, weeklyRates);
                }
            }
        });
    }

    private void loadRemote() {
        LocalDate currentDay = new LocalDate().minusDays(daysCountForLoadingRates).plusDays(1);

        final List<Observable<List<Rate>>> rateRequests = new ArrayList<>();
        for (int i = 0; i < daysCountForLoadingRates; i++) {
            final String currentDayText = rateDateFormatter.formatLocalDate(currentDay);
            rateRequests.add(api.rates(currentDayText));

            currentDay = currentDay.plusDays(1);
        }

        disposables.add(
                Observable.merge(rateRequests)
                        .toList()
                        .map(new Function<List<List<Rate>>, List<Rate>>() {
                            @Override
                            public List<Rate> apply(final List<List<Rate>> ratesLists) {
                                final List<Rate> allRates = new ArrayList<>();
                                for (final List<Rate> rates : ratesLists) {
                                    allRates.addAll(rates);
                                }
                                return allRates;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Rate>>() {
                            @Override
                            public void accept(final List<Rate> rates) {
                                ifViewAttached(new ViewAction<RatesView>() {
                                    @Override
                                    public void run(@NonNull final RatesView view) {
                                        ratesModel.save(rates);
                                        loadLocal();
                                    }
                                });
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) {
                                ifViewAttached(new ViewAction<RatesView>() {
                                    @Override
                                    public void run(@NonNull final RatesView view) {
                                        view.showErrorLoadingMessage();
                                    }
                                });
                            }
                        })
        );
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
