package com.shop.bagrutproject.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.screens.ItemDetailActivity;
import com.shop.bagrutproject.utils.ImageUtil;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private final List<Item> itemList;

    public ItemsAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemselected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        if (item == null) return;

        holder.itemNameTextView.setText(item.getName());
        holder.itemPriceTextView.setText("₪" + item.getPrice());

        // בודקים אם יש תמונה ב-Firebase (בסיס64) ואם לא, מציגים תמונת ברירת מחדל
        if (item.getPic() != null && !item.getPic().isEmpty()) {
            holder.itemImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
        } else {
            // נטען תמונת ברירת מחדל אם אין תמונה
            holder.itemImageView.setImageResource(R.drawable.ic_launcher_foreground); // ניתן להחליף ב-placeholder שלך
        }

        // הוספת לחיצה על המוצר
        holder.itemView.setOnClickListener(v -> {
            // הוספת אינטרנט לעמוד פרטי המוצר
            Intent intent = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
            intent.putExtra("itemId", item.getId()); // שליחה של ID המוצר לעמוד פרטי המוצר
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemNameTextView;
        public final TextView itemPriceTextView;
        public final ImageView itemImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.PreviewtextView);
            itemPriceTextView = itemView.findViewById(R.id.PreviewPriceTextView);
            itemImageView = itemView.findViewById(R.id.PreviewimageView);
        }
    }
}
