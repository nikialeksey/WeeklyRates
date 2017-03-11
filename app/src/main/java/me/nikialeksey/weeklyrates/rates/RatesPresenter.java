package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;

class RatesPresenter extends MvpBasePresenter<RatesView> {

    private final RatesApi api;

    RatesPresenter(final RatesApi api) {
        this.api = api;
    }

    void load(final boolean pullToRefresh) {
        api
                .rates("2009-01-01")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Rate>>() {
                    @Override
                    public void accept(final List<Rate> rates) {
                        if (isViewAttached()) {
                            getView().setData(rates);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                    }
                });
    }

}
