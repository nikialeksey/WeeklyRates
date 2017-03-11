package me.nikialeksey.weeklyrates.rates.impl;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;

public class RatesHolder extends RecyclerView.ViewHolder {

    @Inject
    NumericRepresenter numericRepresenter;

    @BindView(R.id.currentValue)
    TextView currentValue;
    @BindView(R.id.currencyCode)
    TextView currencyCode;
    @BindView(R.id.weeklyRates)
    ConstraintLayout weeklyRates;

    RatesHolder(final View view) {
        super(view);
        WeeklyRatesApp.getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);
        initWeeklyRates(weeklyRates);
    }

    private void initWeeklyRates(final ConstraintLayout weeklyRates) {
        final LayoutInflater inflater = LayoutInflater.from(weeklyRates.getContext());

        final List<View> dailyRates = new ArrayList<>();
        DateTime currentDay = new DateTime().minusDays(6);
        for (int i = 0; i < 7; i++) {
            final View dailyRate = createDailyRate(inflater, weeklyRates, currentDay.dayOfWeek());
            dailyRates.add(dailyRate);
            weeklyRates.addView(dailyRate, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            currentDay = currentDay.plusDays(1);
        }

        final ConstraintSet constraintSet = createConstraintSetForHorizontalChain(weeklyRates, dailyRates);
        constraintSet.applyTo(weeklyRates);
    }

    @NonNull
    private ConstraintSet createConstraintSetForHorizontalChain(final ConstraintLayout weeklyRates, final List<View> dailyRates) {
        final int[] chainIds = new int[dailyRates.size()];
        for (int i = 0; i < 7; i++) {
            chainIds[i] = dailyRates.get(i).getId();
        }

        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(weeklyRates);
        constraintSet.createHorizontalChain(
                weeklyRates.getId(), ConstraintSet.LEFT, weeklyRates.getId(), ConstraintSet.RIGHT,
                chainIds, null, ConstraintSet.CHAIN_SPREAD_INSIDE);
        return constraintSet;
    }

    @NonNull
    private View createDailyRate(final LayoutInflater inflater, final ConstraintLayout weeklyRates, final DateTime.Property dayOfWeek) {
        final View daily = inflater.inflate(R.layout.daily_rate_view, weeklyRates, false);
        daily.setId(View.generateViewId());

        final TextView day = ButterKnife.findById(daily, R.id.day);
        final TextView value = ButterKnife.findById(daily, R.id.value);

        day.setText(dayOfWeek.getAsShortText());
        value.setText("523.23");

        return daily;
    }

    void bind(final Rate rate) {
        final String representedValue = numericRepresenter.represent(rate.getValue());
        final String representCurrency = rate.getCurrency();

        currentValue.setText(representedValue);
        currencyCode.setText(representCurrency);
    }
}
