package me.nikialeksey.weeklyrates.rates.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;

class RatesHolder extends RecyclerView.ViewHolder {

    private final NumericRepresenter numericRepresenter;

    @BindView(R.id.currentValue)
    TextView currentValue;
    @BindView(R.id.currencyCode)
    TextView currencyCode;

    RatesHolder(final View view, final NumericRepresenter numericRepresenter) {
        super(view);
        this.numericRepresenter = numericRepresenter;
        ButterKnife.bind(this, view);
    }

    void bind(final Rate rate) {
        final String representedValue = numericRepresenter.represent(rate.getValue());
        final String representCurrency = rate.getCurrency();

        currentValue.setText(representedValue);
        currencyCode.setText(representCurrency);
    }
}
