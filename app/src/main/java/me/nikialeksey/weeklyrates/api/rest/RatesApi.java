package me.nikialeksey.weeklyrates.api.rest;

import java.util.List;

import io.reactivex.Single;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RatesApi {

    @GET("/{date}?base=RUB")
    Single<List<Rate>> rates(@Path("date") String date);

}
