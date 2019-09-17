package com.wellgel.london.Provider;

import com.wellgel.london.Customer.SerializeModelClasses.LoginSerialized;
import com.wellgel.london.Customer.SerializeModelClasses.RegistrationSerial;
import com.wellgel.london.Provider.ModelSerialized.ForgotPasswordSerial;
import com.wellgel.london.Provider.ModelSerialized.P_Registration_serial;
import com.wellgel.london.Provider.ModelSerialized.P_SubscripSerial;
import com.wellgel.london.Provider.ModelSerialized.ServiceSerilize;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Provider_APIs {

    @Multipart
    @POST("salon/register")
    Call<P_Registration_serial> registration(@Part MultipartBody.Part file,
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
                                             @Query("country") String country);

    @POST("services")
    Call<ServiceSerilize> services();

    @GET("subscriptions")
    Call<P_SubscripSerial> subscription();

    @POST("reset-password")
    Call<ForgotPasswordSerial> forgotPass(@Query("email") String email);
}
