package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import me.nikialeksey.weeklyrates.api.rest.RatesApi;

public class RatesPresenter extends MvpBasePresenter<RatesView> {
    private RatesApi api;

    public void load(final boolean pullToRefresh) {
//        api
//                .rates("2000-01-01", "RUS")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Rates>() {
//                    @Override
//                    public void accept(final Rates ratesResult) {
//                        if (isViewAttached()) {
//                            getView().setData(ratesResult);
//                            getView().showContent();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(final Throwable throwable) {
//                        if (isViewAttached()) {
//                            getView().showError(throwable, pullToRefresh);
//                        }
//                    }
//                });
    }

}
