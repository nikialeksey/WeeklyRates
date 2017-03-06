package me.nikialeksey.weeklyrates.rates;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;
import retrofit2.Response;

public class RatesPresenter extends MvpBasePresenter<RatesView> {
    private RatesApi api;

    private void load(final boolean pullToRefresh) {
        api
                .rates("2000-01-01", "RUS")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Rates>>() {
                    @Override
                    public void accept(final Response<Rates> ratesResult) {
                        if (isViewAttached()) {
                            getView().setData(ratesResult.body());
                            getView().showContent();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        if (isViewAttached()) {
                            getView().showError(throwable, pullToRefresh);
                        }
                    }
                });
    }

}
