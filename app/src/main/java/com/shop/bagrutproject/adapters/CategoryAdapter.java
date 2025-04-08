package com.shop.bagrutproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Category;
import com.shop.bagrutproject.screens.ShopActivity;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = categories.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.categoryImage);
        TextView title = convertView.findViewById(R.id.categoryTitle);

        image.setImageResource(category.getImageResId());
        title.setText(category.getName());

        // כאשר לוחצים על הקטגוריה, מעבירים את שם הקטגוריה ל-ShopActivity
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShopActivity.class);
            intent.putExtra("category", category.getName()); // שליחת שם הקטגוריה
            context.startActivity(intent);
        });


        return convertView;
    }
}
