package unikom.skripsi.angga.petugas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.fragment.ShowNotificationFragment;
import unikom.skripsi.angga.petugas.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private List<Notification> notifications;
    private int rowLayout;

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout listNotification;
        TextView title;
        TextView message;
        TextView timestamp;
        NotificationViewHolder(View v) {
            super(v);
            listNotification = v.findViewById(R.id.notification_layout);
            title = v.findViewById(R.id.viewTitle);
            message = v.findViewById(R.id.viewMessage);
            timestamp = v.findViewById(R.id.viewTimestamp);
        }
    }

    public NotificationAdapter(List<Notification> notifications, int rowLayout, Context context) {
        this.notifications = notifications;
        this.rowLayout = rowLayout;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.title.setText(notifications.get(position).getTitle());
        holder.timestamp.setText(notifications.get(position).getTimestamp());
        holder.message.setText(notifications.get(position).getMessage());
        holder.listNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ShowNotificationFragment fragmentShow = new ShowNotificationFragment();
                Bundle args = new Bundle();
                args.putString("id", notifications.get(position).getMessage_id());
                fragmentShow.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentShow).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
