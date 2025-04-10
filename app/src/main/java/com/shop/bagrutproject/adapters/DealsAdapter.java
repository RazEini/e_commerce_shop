package com.shop.bagrutproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shop.bagrutproject.R;
import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    private List<String> deals;

    public DealsAdapter(List<String> deals) {
        this.deals = deals;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        holder.txtDeal.setText(deals.get(position));
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView txtDeal;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeal = itemView.findViewById(R.id.txtDeal);
        }
    }
}
