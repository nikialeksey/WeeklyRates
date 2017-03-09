package me.nikialeksey.weeklyrates.api.rest;

import io.reactivex.Single;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RatesApi {

    @GET("/{date}?base=RUB")
    Single<Rates> rates(@Path("date") String date);

}
