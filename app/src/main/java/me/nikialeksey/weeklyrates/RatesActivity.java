package me.nikialeksey.weeklyrates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import me.nikialeksey.weeklyrates.api.rest.RatesApi;

public class RatesActivity extends AppCompatActivity {

    @Inject
    RatesApi ratesApi;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rates_activity);

        WeeklyRatesApp.getApplicationComponent().inject(this);

        try {
            ratesApi.rates("2009-01-01", "RUB")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Rates>() {
                        @Override
                        public void accept(final Rates rates) throws Exception {
                            Log.d("asdas", rates.getBase());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(final Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            Log.e("asdas", throwable.getMessage());
                        }
                    });
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            Log.e("asdas", throwable.getMessage());
        }
    }
}
