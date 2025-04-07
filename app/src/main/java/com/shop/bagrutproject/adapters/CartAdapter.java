package com.shop.bagrutproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.utils.ImageUtil;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    public interface OnCartClick {
        void onItemCheckedChanged(int position, boolean isChecked);
    }

    private Context context;
    private List<Item> cartItems;
    @Nullable
    private OnCartClick onCartClick;

    public CartAdapter(Context context, List<Item> cartItems, @Nullable OnCartClick onCartClick) {
        this.context = context;
        this.cartItems = cartItems;
        this.onCartClick = onCartClick;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        }

        final Item item = cartItems.get(position);

        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemPrice = convertView.findViewById(R.id.itemPrice);
        ImageView itemImage = convertView.findViewById(R.id.itemImage);
        CheckBox deleteCheckBox = convertView.findViewById(R.id.deleteCheckBox);

        itemName.setText(item.getName());
        itemPrice.setText("₪" + item.getPrice());

        if (item.getPic() != null && !item.getPic().isEmpty()) {
            itemImage.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
        } else {
            itemImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // יש לוודא של־CheckBox אין מאזין פעיל (כדי למנוע התנגשות עם חזרה)
        deleteCheckBox.setOnCheckedChangeListener(null);

        // מניחים שה־CheckBox יפעל בהתאם למצב הנוכחי
        deleteCheckBox.setChecked(false);  // או true, תלוי במצב של הפריט

        deleteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // מבצעים את הפעולה כאשר ה־CheckBox משתנה
            if (onCartClick != null) {
                onCartClick.onItemCheckedChanged(position, isChecked);
            }
        });

        return convertView;
    }

    public void setItems(List<Item> items) {
        this.cartItems.clear();
        this.cartItems.addAll(items);
        this.notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.cartItems.remove(position);
        this.notifyDataSetChanged();
    }
}
