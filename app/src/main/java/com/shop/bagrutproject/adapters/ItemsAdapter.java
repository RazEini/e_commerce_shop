package com.shop.bagrutproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.screens.ItemDetailActivity;
import com.shop.bagrutproject.screens.ShopActivity;
import com.shop.bagrutproject.utils.ImageUtil;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    public interface ItemClickListener {
        public void onClick(Item item);
    }

    private List<Item> itemsList;
    private Context context;
    @Nullable
    final ItemClickListener itemClickListener;

    public ItemsAdapter(List<Item> itemsList, Context context, @Nullable final ItemClickListener itemClickListener) {
        this.itemsList = itemsList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemselected, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemsList.get(position);
        holder.bindItem(item);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView previewImageView;
        private TextView previewTextView;
        private TextView previewPriceTextView;
        private Button addToCartButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            previewImageView = itemView.findViewById(R.id.PreviewimageView);
            previewTextView = itemView.findViewById(R.id.PreviewtextView);
            previewPriceTextView = itemView.findViewById(R.id.PreviewPriceTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);

            // הוספת לחיצה על כל מוצר כדי להוביל לדף המידע של המוצר
            itemView.setOnClickListener(v -> {
                Item item = itemsList.get(getAdapterPosition());  // מקבל את המוצר שנלחץ
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("itemId", item.getId()); // שולח את ה-ID של המוצר
                context.startActivity(intent);
            });
        }

        public void bindItem(final Item item) {
            previewImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
            previewTextView.setText(item.getName());
            previewPriceTextView.setText("₪" + item.getPrice());

            addToCartButton.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onClick(item);
            });
        }
    }
}
