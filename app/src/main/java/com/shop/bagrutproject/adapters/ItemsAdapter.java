package com.shop.bagrutproject.adapters;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.screens.ItemDetailActivity;
import com.shop.bagrutproject.screens.ShopActivity;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.ImageUtil;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private static final String TAG = "ItemsAdapter";
    private List<Item> originalItemsList;
    private List<Item> filteredItemsList;
    private Context context;
    private DatabaseService databaseService;

    public interface ItemClickListener {
        void onClick(Item item);
    }

    @Nullable
    private final ItemClickListener itemClickListener;

    public ItemsAdapter(List<Item> itemsList, Context context, @Nullable ItemClickListener itemClickListener) {
        this.originalItemsList = itemsList;
        this.filteredItemsList = new ArrayList<>(itemsList);
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.databaseService = DatabaseService.getInstance();
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
        private TextView previewTextView, previewPriceTextView, previewDescriptionTextView, oldPriceTextView;
        private RatingBar previewRatingBar;
        private ImageButton addToCartButton;
        private String itemId;
        private TextView dealtag;

        public ItemViewHolder(View itemView) {
            super(itemView);
            previewImageView = itemView.findViewById(R.id.PreviewimageView);
            previewTextView = itemView.findViewById(R.id.PreviewtextView);
            previewPriceTextView = itemView.findViewById(R.id.PreviewPriceTextView);
            previewDescriptionTextView = itemView.findViewById(R.id.PreviewDescriptionTextView);
            previewRatingBar = itemView.findViewById(R.id.PreviewRatingBar);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            oldPriceTextView = itemView.findViewById(R.id.oldPriceTextView); // הוספת טקסט למחיר ישן עם קו חוצה
            dealtag = itemView.findViewById(R.id.saleTag);

            itemView.setOnClickListener(v -> {
                Item item = filteredItemsList.get(getAdapterPosition());
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("itemId", item.getId());
                context.startActivity(intent);
            });

            // הצגת כפתור הוספה לעגלה רק אם לא מדובר במנהל
            if (SharedPreferencesUtil.isAdmin(context)) {
                addToCartButton.setVisibility(View.GONE);
            } else {
                addToCartButton.setVisibility(View.VISIBLE);
            }
        }

        public void bindItem(final Item item) {
            previewImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
            previewTextView.setText(item.getName());
            previewDescriptionTextView.setText(item.getAboutItem());

            // הצגת המחיר לאחר הנחה
            updatePriceWithDeal(item);

            itemId = item.getId();
            updateAverageRating(previewRatingBar, itemId);

            addToCartButton.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onClick(item);
            });
        }

        private void updatePriceWithDeal(Item item) {
            databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
                @Override
                public void onCompleted(List<Deal> deals) {
                    double finalPrice = item.getPrice();
                    double originalPrice = item.getPrice(); // נשמור את המחיר המקורי
                    boolean hasDiscount = false;

                    for (Deal deal : deals) {
                        if (deal.isValid() && deal.getItemType().equals(item.getType())) {
                            double discount = deal.getDiscountPercentage();
                            finalPrice = item.getPrice() * (1 - discount / 100);
                            hasDiscount = true;
                            break;
                        }
                    }

                    // הצגת המחיר המעודכן
                    previewPriceTextView.setText("₪" + finalPrice);

                    // אם יש הנחה, נציג את המחיר המקורי עם קו חוצה
                    if (hasDiscount) {
                        oldPriceTextView.setVisibility(View.VISIBLE); // הראה את המחיר המקורי
                        SpannableString spannableString = new SpannableString("₪" + originalPrice);

                        // הוסף קו חוצה
                        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // הוסף קו אדום
                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        oldPriceTextView.setText(spannableString); // הצגת המחיר הישן עם קו אדום
                        dealtag.setVisibility(View.VISIBLE);
                    } else {
                        oldPriceTextView.setVisibility(View.GONE); // הסתר אם אין הנחה
                        dealtag.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Failed to fetch deals", e);
                }
            });
        }


        private void updateAverageRating(RatingBar itemAverageRatingBar, String itemId) {
            databaseService.updateAverageRating(itemId, new DatabaseService.DatabaseCallback<Double>() {
                @Override
                public void onCompleted(Double averageRating) {
                    itemAverageRatingBar.setRating(averageRating.floatValue());
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Failed to fetch average rating", e);
                }
            });
        }
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
