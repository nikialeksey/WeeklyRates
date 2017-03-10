package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;

public class RatesPresenter extends MvpBasePresenter<RatesView> {

    private final RatesApi api;

    public RatesPresenter(final RatesApi api) {
        this.api = api;
    }

    public void load(final boolean pullToRefresh) {
        api
                .rates("2009-01-01")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Rates>() {
                    @Override
                    public void accept(final Rates ratesResult) {
                        if (isViewAttached()) {
                            getView().setData(ratesResult);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        if (isViewAttached()) {
                        }
                    }
                });
    }

}
