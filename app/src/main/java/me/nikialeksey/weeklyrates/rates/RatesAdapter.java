package me.nikialeksey.weeklyrates.rates;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nikialeksey.weeklyrates.R;
import me.nikialeksey.weeklyrates.api.entities.Rate;

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.RatesHolder> {

    private final List<Rate> rates;

    public RatesAdapter(final List<Rate> rates) {
        this.rates = rates;
    }

    @Override
    public RatesHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View rateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_view, parent, false);
        return new RatesHolder(rateView);
    }

    @Override
    public void onBindViewHolder(final RatesHolder holder, final int position) {
        holder.bind(rates.get(position));
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    class RatesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        RatesHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Rate rate) {
            name.setText(rate.getCurrency());
        }
    }

}
