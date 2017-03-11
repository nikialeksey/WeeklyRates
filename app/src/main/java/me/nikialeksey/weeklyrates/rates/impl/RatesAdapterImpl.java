package me.nikialeksey.weeklyrates.rates.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.api.entities.Rate;
import me.nikialeksey.weeklyrates.locale.NumericRepresenter;
import me.nikialeksey.weeklyrates.rates.RatesAdapter;

public class RatesAdapterImpl extends RecyclerView.Adapter<RatesHolder> implements RatesAdapter {

    private final NumericRepresenter numericRepresenter;
    private final List<Rate> rates = new ArrayList<>();

    public RatesAdapterImpl(final NumericRepresenter numericRepresenter) {
        this.numericRepresenter = numericRepresenter;
    }

    @Override
    public void changeRates(final List<Rate> rates) {
        this.rates.clear();
        this.rates.addAll(rates);
        notifyDataSetChanged();
    }

    @Override
    public RatesHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View rateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_view, parent, false);
        return new RatesHolder(rateView, numericRepresenter);
    }

    @Override
    public void onBindViewHolder(final RatesHolder holder, final int position) {
        holder.bind(rates.get(position));
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

}
