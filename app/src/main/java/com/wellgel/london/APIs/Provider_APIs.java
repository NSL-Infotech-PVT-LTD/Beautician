package com.wellgel.london.APIs;

import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateProfileSerial;
import com.wellgel.london.Customer.SerializeModelClasses.LoginSerialized;
import com.wellgel.london.Provider.ModelSerialized.P_AppointmentListSerial;
import com.wellgel.london.Provider.ModelSerialized.P_Registration_serial;
import com.wellgel.london.Provider.ModelSerialized.P_SubscripSerial;
import com.wellgel.london.Provider.ModelSerialized.P_UpdateProfileSeialize;
import com.wellgel.london.Provider.ModelSerialized.RescheduleAppointment;
import com.wellgel.london.Provider.ModelSerialized.ServiceSerilize;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Provider_APIs {

    @Multipart
    @POST("salon/register")
    Call<P_Registration_serial> registration(@Part MultipartBody.Part file,
                                             @Part MultipartBody.Part file1,
                                             @Part MultipartBody.Part file2,
                                             @Part MultipartBody.Part file3,
                                             @Query("name") String name,
                                             @Query("email") String email,
                                             @Query("password") String password,
                                             @Query("phone") String phone,
                                             @Query("latitude") String latitude,
                                             @Query("longitude") String longitude,
                                             @Query("device_type") String device_type,
                                             @Query("device_token") String device_token,
                                             @Query("address") String address,
                                             @Query("service_ids") String service_ids,
                                             @Query("token") String token,
                                             @Query("subscription_id") String subscription_id,
                                             @Query("business_hour_start") String business_hour_start,
                                             @Query("business_hour_end") String business_hour_end,
                                             @Query("country") String country);

    @POST("salon/login")
    Call<LoginSerialized> login(@Query("email") String email,
                                @Query("password") String password,
                                @Query("device_type") String device_type,
                                @Query("device_token") String device_token);

    @Multipart
    @POST("salon/update")
    Call<P_UpdateProfileSeialize> updateProfilProvider(@Header("Authorization") String Auth,
                                                       @Part MultipartBody.Part file,
                                                       @PartMap() Map<String, RequestBody> partMap);

    @POST("services")
    Call<ServiceSerilize> services();

    @GET("subscriptions")
    Call<P_SubscripSerial> subscription();

    @GET("appointments/salon/list")
    Call<P_AppointmentListSerial> appointmentListProvider(@Header("Content-Type") String content,
                                                          @Header("Authorization") String Auth);


    @POST("appointment/salon/update")
    Call<RescheduleAppointment> appointmentUpddateBySalon(@Header("Content-Type") String content,
                                                          @Header("Authorization") String Auth,
                                                          @Query("appointments_id") String appointments_id,
                                                          @Query("available_datetime") String available_datetime,
                                                          @Query("status") String status,
                                                          @Query("comments") String comments,
                                                          @Query("price") String price);
}
