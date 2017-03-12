package me.nikialeksey.weeklyrates.rates.api.deserializers;

import org.joda.time.LocalDate;

public interface RateDateFormatter {

    LocalDate parseLocalDate(final String dateText);

    String formatLocalDate(final LocalDate date);
}
