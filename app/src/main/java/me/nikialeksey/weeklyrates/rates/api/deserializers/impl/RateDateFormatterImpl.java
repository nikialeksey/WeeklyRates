package me.nikialeksey.weeklyrates.rates.api.deserializers.impl;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import me.nikialeksey.weeklyrates.rates.api.deserializers.RateDateFormatter;

public class RateDateFormatterImpl implements RateDateFormatter {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public LocalDate parseLocalDate(final String dateText) {
        return dateTimeFormatter.parseLocalDate(dateText);
    }

    @Override
    public String formatLocalDate(final LocalDate date) {
        return dateTimeFormatter.print(date);
    }

}
