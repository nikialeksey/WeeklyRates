package me.nikialeksey.weeklyrates.rates.impl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.WeeklyRatesApp;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;

public class RatesHolder extends RecyclerView.ViewHolder {

    @Inject
    NumericRepresenter numericRepresenter;
    @BindView(R.id.currentValue)
    TextView currentValue;
    @BindView(R.id.currencyCode)
    TextView currencyCode;
    @BindView(R.id.weeklyRates)
    ConstraintLayout weeklyRates;
    @BindView(R.id.ratesChart)
    LineChartView ratesChart;

    private final List<TextView> weeklyRateValues = new ArrayList<>();
    private final List<TextView> dayOfWeekNames = new ArrayList<>();

    RatesHolder(final View view) {
        super(view);
        WeeklyRatesApp.getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);
        initWeeklyRates(weeklyRates);
    }

    void bind(final Rate rate, final List<Rate> weeklyRates) {
        final String representedValue = numericRepresenter.represent(rate.getValue());
        final String representCurrency = rate.getCurrency();

        currentValue.setText(representedValue);
        currencyCode.setText(representCurrency);

        fillDayOfWeekNames(rate.getDate());
        fillDailyRateValues(rate, weeklyRates);
    }

    private void fillDailyRateValues(Rate rate, List<Rate> weeklyRates) {
        DateTime currentDay = new DateTime(rate.getDate());
        final LineSet rateSet = new LineSet();
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (int i = weeklyRateValues.size() - 1, j = weeklyRates.size() - 1; i >= 0; i--) {
            final TextView dailyRateValue = weeklyRateValues.get(i);

            dailyRateValue.setText("-");
            if (j >= 0) {
                final Rate dailyRate = weeklyRates.get(j);
                if (dailyRate.getDate().equals(currentDay.toDate())) {
                    final String dailyRateText = numericRepresenter.representWithTwoDecimalPlaces(
                            dailyRate.getValue()
                    );
                    dailyRateValue.setText(dailyRateText);
                    float floatValue = Float.parseFloat(dailyRate.getValue());
                    rateSet.addPoint("", floatValue);

                    min = Math.min(min, floatValue);
                    max = Math.max(max, floatValue);

                    j--;
                }
            }
            currentDay = currentDay.minusDays(1);
        }

        ratesChart.getData().clear();
        ratesChart.addData(rateSet);
        ratesChart.setXAxis(false);
        float offset = (max - min) / 2f;
        if (offset == 0f) {
            offset = 0.01f;
        }
        float minAxis = Math.max(min - offset, 0f);
        float maxAxis = max + offset;
        ratesChart.setAxisBorderValues(minAxis, maxAxis, (maxAxis - minAxis) / 3);
        ratesChart.show();
    }

    private void fillDayOfWeekNames(Date lastWeekDay) {
        DateTime currentDay = new DateTime(lastWeekDay).minusDays(
                DateTimeConstants.DAYS_PER_WEEK
        ).plusDays(1);
        for (int i = 0; i < dayOfWeekNames.size(); i++) {
            TextView dayOfWeekName = dayOfWeekNames.get(i);
            dayOfWeekName.setText(currentDay.dayOfWeek().getAsShortText());

            currentDay = currentDay.plusDays(1);
        }
    }

    private void initWeeklyRates(final ConstraintLayout weeklyRates) {
        final LayoutInflater inflater = LayoutInflater.from(weeklyRates.getContext());

        final List<View> dailyRates = new ArrayList<>();
        for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
            final View daily = createDailyRate(inflater, weeklyRates);
            dailyRates.add(daily);
            weeklyRates.addView(
                    daily,
                    new ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            );
        }

        final ConstraintSet constraintSet = createConstraintSetForHorizontalChain(
                weeklyRates,
                dailyRates
        );
        constraintSet.applyTo(weeklyRates);
    }

    @NonNull
    private ConstraintSet createConstraintSetForHorizontalChain(
            final ConstraintLayout weeklyRates,
            final List<View> dailyRates
    ) {
        final int[] chainIds = new int[dailyRates.size()];
        for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
            chainIds[i] = dailyRates.get(i).getId();
        }

        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(weeklyRates);
        constraintSet.createHorizontalChain(
                weeklyRates.getId(), ConstraintSet.LEFT,
                weeklyRates.getId(), ConstraintSet.RIGHT,
                chainIds, null, ConstraintSet.CHAIN_SPREAD_INSIDE
        );
        return constraintSet;
    }

    @NonNull
    private View createDailyRate(
            final LayoutInflater inflater,
            final ConstraintLayout weeklyRates
    ) {
        final View daily = inflater.inflate(R.layout.daily_rate_view, weeklyRates, false);
        daily.setId(View.generateViewId());

        final TextView day = daily.findViewById(R.id.day);
        final TextView value = daily.findViewById(R.id.value);
        weeklyRateValues.add(value);
        dayOfWeekNames.add(day);

        return daily;
    }

}
