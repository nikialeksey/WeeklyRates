package me.nikialeksey.weeklyrates.locale.impl;

import java.text.NumberFormat;
import java.util.Locale;

import me.nikialeksey.weeklyrates.locale.NumericRepresenter;

public class NumericRepresenterImpl implements NumericRepresenter {

    @Override
    public String represent(final Number number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    @Override
    public String represent(final String number) {
        return represent(Double.valueOf(number));
    }
}
