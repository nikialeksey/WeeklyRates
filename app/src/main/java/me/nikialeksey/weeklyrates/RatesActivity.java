package me.nikialeksey.weeklyrates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;

import java.util.List;

import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.rates.RatesPresenter;
import me.nikialeksey.weeklyrates.rates.RatesView;

public class RatesActivity extends MvpLceActivity<RecyclerView, List<Rate>, RatesView, RatesPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rates_activity);
    }

    @NonNull
    @Override
    public RatesPresenter createPresenter() {
        return new RatesPresenter();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void setData(List<Rate> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
