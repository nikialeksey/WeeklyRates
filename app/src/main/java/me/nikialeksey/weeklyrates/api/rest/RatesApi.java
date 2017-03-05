package me.nikialeksey.weeklyrates.api.rest;

import io.reactivex.Single;
import me.nikialeksey.weeklyrates.api.entities.Rates;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;

public interface RatesApi {

    @GET("/{date}?base={base}")
    Single<Result<Rates>> rates(String date, String base);

}
