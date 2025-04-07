package com.shop.bagrutproject.adapters;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.screens.ItemDetailActivity;
import com.shop.bagrutproject.screens.ShopActivity;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.ImageUtil;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private DatabaseService databaseService;


    public interface ItemClickListener {
        void onClick(Item item);
    }

    private static final String TAG = "ItemsAdapter";
    private List<Item> originalItemsList;
    private List<Item> filteredItemsList;
    private Context context;


    @Nullable
    private final ItemClickListener itemClickListener;

    public ItemsAdapter(List<Item> itemsList, Context context, @Nullable final ItemClickListener itemClickListener) {
        this.originalItemsList = itemsList;
        this.filteredItemsList = new ArrayList<>(itemsList);
        this.context = context;
        this.itemClickListener = itemClickListener;
        filter("");
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemselected, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = filteredItemsList.get(position);
        holder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return filteredItemsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView previewImageView;
        private TextView previewTextView;
        private TextView previewPriceTextView;
        private TextView previewDescriptionTextView; // תיאור המוצר
        private RatingBar previewRatingBar; // דירוג המוצר
        private ImageButton addToCartButton;
        private String itemId;

        public ItemViewHolder(View itemView) {
            super(itemView);
            previewImageView = itemView.findViewById(R.id.PreviewimageView);
            previewTextView = itemView.findViewById(R.id.PreviewtextView);
            previewPriceTextView = itemView.findViewById(R.id.PreviewPriceTextView);
            previewDescriptionTextView = itemView.findViewById(R.id.PreviewDescriptionTextView); // תיאור המוצר
            previewRatingBar = itemView.findViewById(R.id.PreviewRatingBar); // דירוג המוצר
            addToCartButton = itemView.findViewById(R.id.addToCartButton);


            itemView.setOnClickListener(v -> {
                Item item = filteredItemsList.get(getAdapterPosition());
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("itemId", item.getId());
                context.startActivity(intent);
            });

            if (SharedPreferencesUtil.isAdmin(context)) {
                addToCartButton.setVisibility(View.GONE);
            } else {
                addToCartButton.setVisibility(View.VISIBLE);
            }
        }

        public void bindItem(final Item item) {
            previewImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
            previewTextView.setText(item.getName());
            previewPriceTextView.setText("₪" + item.getPrice());
            previewDescriptionTextView.setText(item.getAboutItem());
            itemId = item.getId();
            updateAverageRating(previewRatingBar,itemId);

            addToCartButton.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onClick(item);
            });
        }
    }

    private void updateAverageRating(RatingBar itemAverageRatingBar, String itemId) {
        databaseService = DatabaseService.getInstance();

        databaseService.updateAverageRating(itemId, new DatabaseService.DatabaseCallback<Double>() {
            @Override
            public void onCompleted(Double averageRating) {
                itemAverageRatingBar.setRating(averageRating.floatValue());
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }



    public void filter(String query) {
        filteredItemsList.clear();
        if (query.isEmpty()) {
            filteredItemsList.addAll(originalItemsList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            try {
                double queryPrice = Double.parseDouble(query);

                for (Item item : originalItemsList) {
                    if (item.getPrice() == queryPrice) {
                        filteredItemsList.add(item);
                    }
                }
            } catch (NumberFormatException e) {
                for (Item item : originalItemsList) {
                    if (item.getName().toLowerCase().contains(lowerCaseQuery) ||
                            item.getCompany().toLowerCase().contains(lowerCaseQuery) ||
                            item.getType().toLowerCase().contains(lowerCaseQuery) ||
                            item.getColor().toLowerCase().contains(lowerCaseQuery)) {
                        filteredItemsList.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
