package com.shop.bagrutproject.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Order;

import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {
    private List<Order> orders;

    public AdminOrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_order, parent, false);
        return new AdminOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Order order = orders.get(position);
        String status = order.getStatus().toLowerCase();
        String displayStatus;

        holder.orderId.setText("מספר הזמנה: " + order.getOrderId());
        holder.totalPrice.setText(String.format(Locale.getDefault(), "סה״כ: ₪%.2f", order.getTotalPrice()));

        switch (status) {
            case "pending":
                displayStatus = "ממתינה לאישור";
                holder.status.setTextColor(Color.parseColor("#FFA500"));
                break;
            case "processing":
                displayStatus = "בהכנה";
                holder.status.setTextColor(Color.parseColor("#800080"));
                break;
            case "shipped":
                displayStatus = "נשלחה";
                holder.status.setTextColor(Color.parseColor("#007bff"));
                break;
            case "delivered":
                displayStatus = "סופקה";
                holder.status.setTextColor(Color.parseColor("#28a745"));
                break;
            case "cancelled":
                displayStatus = "בוטלה";
                holder.status.setTextColor(Color.parseColor("#dc3545"));
                break;
            default:
                displayStatus = "לא ידוע";
                holder.status.setTextColor(Color.BLACK);
                break;
        }
        holder.status.setText("סטטוס: " + displayStatus);

        // תצוגת כפתורים בהתאם לסטטוס
        holder.btnProcessing.setVisibility(status.equals("pending") ? View.VISIBLE : View.GONE);
        holder.btnShip.setVisibility(status.equals("processing") ? View.VISIBLE : View.GONE);
        holder.btnCancel.setVisibility(
                status.equals("pending") || status.equals("processing") ? View.VISIBLE : View.GONE
        );

        holder.btnProcessing.setOnClickListener(v -> {
            order.setStatus("processing");
            updateOrderStatus(order, holder);
        });

        holder.btnShip.setOnClickListener(v -> {
            order.setStatus("shipped");
            updateOrderStatus(order, holder);
        });

        holder.btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("אישור ביטול הזמנה")
                    .setMessage("האם אתה בטוח שברצונך לבטל את ההזמנה?\nלא תוכל לשחזר פעולה זו.")
                    .setPositiveButton("בטל הזמנה", (dialog, which) -> {
                        order.setStatus("cancelled");
                        updateOrderStatus(order, holder);
                    })
                    .setNegativeButton("חזור", null)
                    .show();
        });

        // הצגת כפתור מחיקה רק אם ההזמנה סופקה
        if (status.equals("delivered")) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            // בתוך ה-OnClickListener של כפתור המחיקה
            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("אישור מחיקה")
                        .setMessage("האם אתה בטוח שברצונך למחוק את ההזמנה הזו?")
                        .setPositiveButton("מחק", (dialog, which) -> {
                            // ביצוע המחיקה ב-Firebase
                            FirebaseDatabase.getInstance().getReference("orders")
                                    .child(order.getOrderId())
                                    .removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // הצגת הודעה שההזמנה נמחקה
                                            Toast.makeText(holder.itemView.getContext(), "ההזמנה נמחקה", Toast.LENGTH_SHORT).show();

                                            // מחיקת ההזמנה מהמנוע המקומי של ה-Recyclerview
                                            int adapterPosition = holder.getAdapterPosition();
                                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                                // מחיקת הפריט מהרשימה המקומית
                                                orders.remove(adapterPosition);
                                                notifyItemRemoved(adapterPosition);
                                            }
                                        } else {
                                            Toast.makeText(holder.itemView.getContext(), "שגיאה במחיקה", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                        .setNegativeButton("בטל", null)
                        .show();
            });

        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    private void updateOrderStatus(Order order, AdminOrderViewHolder holder) {
        FirebaseDatabase.getInstance().getReference("orders")
                .child(order.getOrderId())
                .setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "הסטטוס עודכן ל-" + order.getStatus(), Toast.LENGTH_SHORT).show();
                        notifyItemChanged(holder.getAdapterPosition());
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "שגיאה בעדכון הסטטוס", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, totalPrice, status;
        Button btnProcessing, btnShip, btnCancel;
        Button btnDelete;

        public AdminOrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            status = itemView.findViewById(R.id.status);
            btnProcessing = itemView.findViewById(R.id.btnProcessing);
            btnShip = itemView.findViewById(R.id.btnShip);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
