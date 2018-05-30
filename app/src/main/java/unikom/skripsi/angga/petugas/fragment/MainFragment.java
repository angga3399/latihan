package unikom.skripsi.angga.petugas.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.adapter.NotificationAdapter;
import unikom.skripsi.angga.petugas.model.Notification;
import unikom.skripsi.angga.petugas.model.NotificationResponse;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;

public class MainFragment extends Fragment {
    FragmentActivity context;
    ProgressBar progress;
    TextView txtProgress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.context = (FragmentActivity) context;
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        progress = context.findViewById(R.id.loadingProgress);
        txtProgress = context.findViewById(R.id.loadingText);
        progress.setVisibility(View.VISIBLE);
        txtProgress.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final RecyclerView recyclerView = context.findViewById(R.id.notification_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NotificationResponse> call = apiService.getAllNotifications();
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                List<Notification> notifications = response.body().getResults();
                recyclerView.setAdapter(new NotificationAdapter(notifications, R.layout.list_notification, context));
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Toast.makeText(context, "Gagal mengambil data dari server.", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
