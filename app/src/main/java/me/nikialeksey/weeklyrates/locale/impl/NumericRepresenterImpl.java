package me.nikialeksey.weeklyrates.locale.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import me.nikialeksey.weeklyrates.locale.NumericRepresenter;

public class NumericRepresenterImpl implements NumericRepresenter {

    private final DecimalFormat twoDecimalPlacesFormatter = new DecimalFormat("0.00");
    private final NumberFormat numberFormatter = NumberFormat.getNumberInstance();

    @Override
    public String represent(final Number number) {
        return numberFormatter.format(number);
    }

    @Override
    public String represent(final String number) {
        return represent(Double.valueOf(number));
    }

    @Override
    public String representWithTwoDecimalPlaces(final String number) {
        return twoDecimalPlacesFormatter.format(Double.valueOf(number));
    }

}
