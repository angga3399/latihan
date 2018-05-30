package unikom.skripsi.angga.petugas.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unikom.skripsi.angga.petugas.model.Notification;
import unikom.skripsi.angga.petugas.model.NotificationResponse;

public interface ApiInterface {
    @GET("getNotification.php")
    Call<NotificationResponse> getAllNotifications();

    @GET("getNotification.php")
    Call<Notification> getNotification(@Query("message_id") String message_id);
}
