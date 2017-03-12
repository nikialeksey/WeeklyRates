package me.nikialeksey.weeklyrates.locale.impl;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertThat;

public class NumericRepresenterImplTest {

    private NumericRepresenterImpl numericRepresenter;

    @Test
    public void representStringWithEnLocale() {
        Locale.setDefault(new Locale("en", "US"));
        final String number = "123.2346";

        numericRepresenter = new NumericRepresenterImpl();
        assertThat(numericRepresenter.represent(number), IsEqual.equalTo("123.235"));
    }

    @Test
    public void representStringWithRuLocale() {
        Locale.setDefault(new Locale("ru", "RU"));
        final String number = "123.2346";

        numericRepresenter = new NumericRepresenterImpl();
        assertThat(numericRepresenter.represent(number), IsEqual.equalTo("123,235"));
    }

    @Test
    public void representStringWithEnLocaleWithTwoDecimalPlaces() {
        Locale.setDefault(new Locale("en", "US"));
        final String number = "123.2346";

        numericRepresenter = new NumericRepresenterImpl();
        assertThat(numericRepresenter.representWithTwoDecimalPlaces(number), IsEqual.equalTo("123.23"));
    }

    @Test
    public void representStringWithRuLocaleWithTwoDecimalPlaces() {
        Locale.setDefault(new Locale("ru", "RU"));
        final String number = "123.2346";

        numericRepresenter = new NumericRepresenterImpl();
        assertThat(numericRepresenter.representWithTwoDecimalPlaces(number), IsEqual.equalTo("123,23"));
    }

}