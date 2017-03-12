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
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;

public class RatesHolder extends RecyclerView.ViewHolder {

    private final List<TextView> weeklyRateValues;
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
        weeklyRateValues = initWeeklyRates(weeklyRates);
    }

    void bind(final Rate rate, final List<Rate> weeklyRates) {
        final String representedValue = numericRepresenter.represent(rate.getValue());
        final String representCurrency = rate.getCurrency();

        currentValue.setText(representedValue);
        currencyCode.setText(representCurrency);

        for (int i = weeklyRateValues.size() - 1, j = weeklyRates.size() - 1; i >= 0; i--, j--) {
            final TextView dailyRateValue = weeklyRateValues.get(i);

            if (j >= 0) {
                final Rate dailyRate = weeklyRates.get(j);
                dailyRateValue.setText(numericRepresenter.representWithTwoDecimalPlaces(dailyRate.getValue()));
            } else {
                dailyRateValue.setText(null);
            }
        }

    }

    @NonNull
    private List<TextView> initWeeklyRates(final ConstraintLayout weeklyRates) {
        final LayoutInflater inflater = LayoutInflater.from(weeklyRates.getContext());

        DateTime currentDay = new DateTime()
                .minusDays(DateTimeConstants.DAYS_PER_WEEK)
                .plusDays(1);

        final List<TextView> weeklyRateValues = new ArrayList<>();
        final List<View> dailyRates = new ArrayList<>();
        for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
            final View daily = createDailyRate(inflater, weeklyRates, currentDay.dayOfWeek());
            dailyRates.add(daily);
            weeklyRates.addView(daily, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            final TextView value = ButterKnife.findById(daily, R.id.value);
            weeklyRateValues.add(value);

            currentDay = currentDay.plusDays(1);
        }

        final ConstraintSet constraintSet = createConstraintSetForHorizontalChain(weeklyRates, dailyRates);
        constraintSet.applyTo(weeklyRates);

        return weeklyRateValues;
    }

    @NonNull
    private ConstraintSet createConstraintSetForHorizontalChain(final ConstraintLayout weeklyRates, final List<View> dailyRates) {
        final int[] chainIds = new int[dailyRates.size()];
        for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
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

        day.setText(dayOfWeek.getAsShortText());

        return daily;
    }

}
