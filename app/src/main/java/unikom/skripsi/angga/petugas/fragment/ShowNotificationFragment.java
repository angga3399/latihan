package unikom.skripsi.angga.petugas.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.petugas.R;
import unikom.skripsi.angga.petugas.helper.Config;
import unikom.skripsi.angga.petugas.model.Notification;
import unikom.skripsi.angga.petugas.rest.ApiClient;
import unikom.skripsi.angga.petugas.rest.ApiInterface;

public class ShowNotificationFragment extends Fragment {
    FragmentActivity context;
    ProgressBar progress;
    TextView txtProgress, title, time, message;
    ImageView img;

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

        View layout = inflater.inflate(R.layout.fragment_show_notification, parent, false);
        img = layout.findViewById(R.id.showNotificationImage);
        title = layout.findViewById(R.id.showNotificationTitle);
        time = layout.findViewById(R.id.showNotificationTime);
        message = layout.findViewById(R.id.showNotificationMessage);

        img.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        return layout;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String id = Objects.requireNonNull(getArguments()).getString("id");
        Call<Notification> call = apiService.getNotification(id);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, final Response<Notification> response) {
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);

                title.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                message.setVisibility(View.VISIBLE);

                title.setText(response.body().getTitle());
                time.setText(response.body().getTimestamp());
                message.setText(response.body().getMessage());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReferenceFromUrl(Config.FIREBASE_URL);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    storageRef.child("foto-notifikasi/" + response.body().getImage_name()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            img.setVisibility(View.VISIBLE);
                            Picasso.get().load(uri).into(img);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(context, "Gagal mendapatkan foto.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    mAuth.signInAnonymously().addOnSuccessListener(context, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            storageRef.child("foto-notifikasi/" + response.body().getImage_name()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    img.setVisibility(View.VISIBLE);
                                    Picasso.get().load(uri).into(img);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(context, "Gagal mendapatkan foto.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(context, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(context, "Gagal melakukan autentikasi firebase.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                progress.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
                Toast.makeText(context, "Koneksi terputus.", Toast.LENGTH_LONG).show();
                FragmentManager fm = getFragmentManager();
                Objects.requireNonNull(fm).popBackStack();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
