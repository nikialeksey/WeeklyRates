package me.nikialeksey.weeklyrates.rates.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;

import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.rates.RatesAdapter;
import me.nikialeksey.weeklyrates.rates.api.entities.Rate;

public class RatesAdapterImpl extends RecyclerView.Adapter<RatesHolder> implements RatesAdapter {

    private final List<Rate> rates = new ArrayList<>();
    private final ListMultimap<String, Rate> weeklyRates = ArrayListMultimap.create();

    @Override
    public void changeRates(final List<Rate> rates, final Multimap<String, Rate> weeklyRates) {
        this.rates.clear();
        this.rates.addAll(rates);

        this.weeklyRates.clear();
        this.weeklyRates.putAll(weeklyRates);

        notifyDataSetChanged();
    }

    @Override
    public RatesHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View rateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_view, parent, false);
        return new RatesHolder(rateView);
    }

    @Override
    public void onBindViewHolder(final RatesHolder holder, final int position) {
        final Rate rate = rates.get(position);
        final List<Rate> weeklyRates = this.weeklyRates.get(rate.getCurrency());
        holder.bind(rate, weeklyRates);
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

}
