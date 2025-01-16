package com.shop.bagrutproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.utils.ImageUtil;

import java.util.List;

public class ItemsAdapter  extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    /// list of items
    /// @see Item
    private final List<Item> itemList;

    public ItemsAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    /// create a view holder for the adapter
    /// @param parent the parent view group
    /// @param viewType the type of the view
    /// @return the view holder
    /// @see ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// inflate the item_selected_item layout
        /// @see R.layout.item_selected_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemselected, parent, false);
        return new ViewHolder(view);
    }

    /// bind the view holder with the data
    /// @param holder the view holder
    /// @param position the position of the item in the list
    /// @see ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        if (item == null) return;

        holder.itemNameTextView.setText(item.getName());
        holder.itemImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
    }

    /// get the number of items in the list
    /// @return the number of items in the list
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /// View holder for the items adapter
    /// @see RecyclerView.ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemNameTextView;
        public final ImageView itemImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.PreviewtextView);
            itemImageView = itemView.findViewById(R.id.PreviewimageView);
        }
    }
}
