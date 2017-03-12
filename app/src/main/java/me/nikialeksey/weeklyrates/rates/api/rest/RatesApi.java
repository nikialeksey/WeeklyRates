package me.nikialeksey.weeklyrates.rates.api.rest;

import java.util.List;

import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface RatesApi {

    @GET("/{date}?base=RUB")
    Observable<List<Rate>> rates(@Path("date") String date);

}
