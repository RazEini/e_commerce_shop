package com.shop.bagrutproject.adapters;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Order;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderId.setText("מספר הזמנה: " + order.getOrderId());
        holder.totalPrice.setText(String.format(Locale.getDefault(), "סה״כ: ₪%.2f", order.getTotalPrice()));
        holder.date.setText("תאריך: " + order.getFormattedDate());

        String status = order.getStatus().toLowerCase();
        String displayStatus = "";

        switch (status) {
            case "pending":
                displayStatus = "ממתינה לאישור";
                holder.status.setTextColor(Color.parseColor("#FFA500")); // כתום
                break;
            case "processing":
                displayStatus = "בהכנה";
                holder.status.setTextColor(Color.parseColor("#800080")); // סגול
                break;
            case "shipped":
                displayStatus = "נשלחה";
                holder.status.setTextColor(Color.parseColor("#007bff")); // כחול
                break;
            case "delivered":
                displayStatus = "סופקה";
                holder.status.setTextColor(Color.parseColor("#28a745")); // ירוק
                break;
            case "cancelled":
                displayStatus = "בוטלה";
                holder.status.setTextColor(Color.parseColor("#dc3545")); // אדום
                break;
            default:
                displayStatus = "לא ידוע";
                holder.status.setTextColor(Color.BLACK);
                break;
        }
        holder.status.setText("סטטוס: " + displayStatus);

        if (status.equals("shipped")) {
            holder.deliverButton.setVisibility(View.VISIBLE);
            holder.deliverButton.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("אישור קבלת הזמנה")
                        .setMessage("האם אתה בטוח שקיבלת את ההזמנה?")
                        .setPositiveButton("כן", (dialog, which) -> {
                            FirebaseDatabase.getInstance().getReference("orders")
                                    .child(order.getOrderId())
                                    .child("status")
                                    .setValue("delivered")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(v.getContext(), "ההזמנה סומנה כהושלמה!", Toast.LENGTH_SHORT).show();
                                            order.setStatus("delivered");
                                            notifyItemChanged(position);
                                        } else {
                                            Toast.makeText(v.getContext(), "שגיאה בהשלמת ההזמנה", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                        .setNegativeButton("לא", null)
                        .show();
            });
        } else {
            holder.deliverButton.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, totalPrice, status, date;
        Button deliverButton;  // כפתור שינוי הסטטוס

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            deliverButton = itemView.findViewById(R.id.deliverButton);  // כפתור השלמת ההזמנה
        }
    }
}
