package me.nikialeksey.weeklyrates.rates.api.rest;

import java.util.List;

import io.reactivex.Observable;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RatesApi {

    @GET("/{date}")
    Observable<List<Rate>> rates(@Path("date") String date, @Query("base") String base);

}
