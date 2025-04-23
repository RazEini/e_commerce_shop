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
    private boolean isArchived;

    public AdminOrderAdapter(List<Order> orders) {
        this(orders, false);
    }

    public AdminOrderAdapter(List<Order> orders, boolean isArchived) {
        this.orders = orders;
        this.isArchived = isArchived;
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

        if (!isArchived) {
            // כפתורי ניהול במצב רגיל
            holder.btnProcessing.setVisibility(status.equals("pending") ? View.VISIBLE : View.GONE);
            holder.btnShip.setVisibility(status.equals("processing") ? View.VISIBLE : View.GONE);
            holder.btnCancel.setVisibility(status.equals("pending") || status.equals("processing") ? View.VISIBLE : View.GONE);
            holder.btnRestore.setVisibility(View.GONE);

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

            if (status.equals("delivered")) {
                holder.archiveButton.setVisibility(View.VISIBLE);
                holder.archiveButton.setOnClickListener(v -> {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("העבר לארכיון")
                            .setMessage("האם אתה בטוח שברצונך להעביר את ההזמנה לארכיון?")
                            .setPositiveButton("כן", (dialog, which) -> {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String orderId = order.getOrderId();

                                database.getReference("archivedOrders").child(orderId).setValue(order)
                                        .addOnSuccessListener(aVoid -> {
                                            database.getReference("orders").child(orderId).removeValue()
                                                    .addOnSuccessListener(aVoid1 -> {
                                                        orders.remove(position);
                                                        notifyItemRemoved(position);
                                                        Toast.makeText(v.getContext(), "ההזמנה הועברה לארכיון", Toast.LENGTH_SHORT).show();
                                                    });
                                        });
                            })
                            .setNegativeButton("ביטול", null)
                            .show();
                });
            } else {
                holder.archiveButton.setVisibility(View.GONE);
            }
        } else {
            // במצב ארכיון - הסתרת כפתורי ניהול, והצגת כפתור שחזור
            holder.btnProcessing.setVisibility(View.GONE);
            holder.btnShip.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
            holder.archiveButton.setVisibility(View.GONE);
            holder.btnRestore.setVisibility(View.VISIBLE);

            holder.btnRestore.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("שחזור הזמנה")
                        .setMessage("האם אתה בטוח שברצונך לשחזר את ההזמנה?")
                        .setPositiveButton("כן", (dialog, which) -> {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String orderId = order.getOrderId();

                            database.getReference("orders").child(orderId).setValue(order)
                                    .addOnSuccessListener(aVoid -> {
                                        database.getReference("archivedOrders").child(orderId).removeValue()
                                                .addOnSuccessListener(aVoid1 -> {
                                                    orders.remove(position);
                                                    notifyItemRemoved(position);
                                                    Toast.makeText(v.getContext(), "ההזמנה שוחזרה", Toast.LENGTH_SHORT).show();
                                                });
                                    });
                        })
                        .setNegativeButton("ביטול", null)
                        .show();
            });
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
        Button archiveButton, btnRestore;

        public AdminOrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            status = itemView.findViewById(R.id.status);
            btnProcessing = itemView.findViewById(R.id.btnProcessing);
            btnShip = itemView.findViewById(R.id.btnShip);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            archiveButton = itemView.findViewById(R.id.archiveButton);
            btnRestore = itemView.findViewById(R.id.btnRestore); // ודא שהכפתור הזה קיים ב-XML
        }
    }
}
