package com.shop.bagrutproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Deal;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    private List<Deal> dealsList;

    public DealsAdapter(List<Deal> dealsList) {
        this.dealsList = dealsList;
    }

    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        Deal deal = dealsList.get(position);
        holder.titleTextView.setText(deal.getTitle());
        holder.descriptionTextView.setText(deal.getDescription());
        holder.discountTextView.setText("הנחה: " + deal.getDiscountPercentage() + "%");
        holder.validUntilTextView.setText("תוקף עד: " + deal.getValidUntil());
        holder.typeTextView.setText("סוג: " + deal.getItemType());
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        TextView discountTextView;
        TextView validUntilTextView;
        TextView typeTextView;

        public DealViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dealTitle);
            descriptionTextView = itemView.findViewById(R.id.dealDescription);
            discountTextView = itemView.findViewById(R.id.dealDiscount);
            validUntilTextView = itemView.findViewById(R.id.dealValidUntil);
            typeTextView = itemView.findViewById(R.id.dealType);
        }
    }
}
