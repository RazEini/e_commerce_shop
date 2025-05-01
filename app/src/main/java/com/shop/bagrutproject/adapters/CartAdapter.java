package com.shop.bagrutproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.ImageUtil;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    public interface OnCartClick {
        void onItemCheckedChanged(int position, boolean isChecked);
    }

    private Context context;
    private List<Item> cartItems;
    private boolean showCheckbox;
    @Nullable
    private OnCartClick onCartClick;
    private DatabaseService databaseService;

    public CartAdapter(Context context, List<Item> cartItems, @Nullable OnCartClick onCartClick, boolean showCheckbox) {
        this.context = context;
        this.cartItems = cartItems;
        this.onCartClick = onCartClick;
        this.showCheckbox = showCheckbox;
        this.databaseService = DatabaseService.getInstance();
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

        // שליטה על הנראות של CheckBox
        if (showCheckbox) {
            deleteCheckBox.setVisibility(View.VISIBLE);
        } else {
            deleteCheckBox.setVisibility(View.GONE);
        }

        // ניתוק מאזין קודם
        deleteCheckBox.setOnCheckedChangeListener(null);

        // הגדרה מחדש
        deleteCheckBox.setChecked(false);  // או שתעדכן לפי המצב האמיתי אם צריך

        deleteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onCartClick != null) {
                onCartClick.onItemCheckedChanged(position, isChecked);
            }
        });

        // עדכון המחיר עם הנחה אם יש
        updatePriceWithDeal(item, itemPrice);

        return convertView;
    }

    private void updatePriceWithDeal(Item item, TextView itemPriceTextView) {
        databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> deals) {
                double finalPrice = item.getPrice();
                double originalPrice = item.getPrice(); // נשמור את המחיר המקורי
                boolean hasDiscount = false;

                // חיפוש אחר מבצע תקף
                for (Deal deal : deals) {
                    if (deal.isValid() && deal.getItemType().equals(item.getType())) {
                        double discount = deal.getDiscountPercentage();
                        finalPrice = item.getPrice() * (1 - discount / 100);
                        hasDiscount = true;
                        break;
                    }
                }

                // הצגת המחיר לאחר הנחה
                itemPriceTextView.setText("₪" + finalPrice);

                // אם יש הנחה, נציג את המחיר המקורי עם קו חוצה
                TextView oldPriceTextView = (TextView) itemPriceTextView.getRootView().findViewById(R.id.oldPriceTextView);
                if (oldPriceTextView != null) {  // לוודא שה-TextView קיים
                    if (hasDiscount) {
                        oldPriceTextView.setVisibility(View.VISIBLE); // הראה את המחיר המקורי
                        SpannableString spannableString = new SpannableString("₪" + originalPrice);

                        // הוסף קו חוצה
                        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // הוסף קו אדום
                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        oldPriceTextView.setText(spannableString); // הצגת המחיר הישן עם קו אדום
                    } else {
                        oldPriceTextView.setVisibility(View.GONE); // הסתר אם אין הנחה
                    }
                }
            }

            @Override
            public void onFailed(Exception e) {
                // Handle error
            }
        });
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
