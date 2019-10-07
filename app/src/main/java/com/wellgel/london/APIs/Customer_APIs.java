package com.wellgel.london.APIs;

import com.wellgel.london.Customer.SerializeModelClasses.C_AppointmentList;
import com.wellgel.london.Customer.SerializeModelClasses.C_CartDetailSerialize;
import com.wellgel.london.Customer.SerializeModelClasses.C_CartSerailized;
import com.wellgel.london.Customer.SerializeModelClasses.C_CreateAddressSerialize;
import com.wellgel.london.Customer.SerializeModelClasses.C_CreateAppointment;
import com.wellgel.london.Customer.SerializeModelClasses.C_DeleteAddresSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_GetAddressListSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_MyOrdersSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_OrderSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_PlaceOrderSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_ProductsSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_SalonListSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateAdressSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateProfileSerial;
import com.wellgel.london.Customer.SerializeModelClasses.EcomAppoDetailSerial;
import com.wellgel.london.Customer.SerializeModelClasses.LoginSerialized;
import com.wellgel.london.Customer.SerializeModelClasses.ProductDetailSerial;
import com.wellgel.london.Customer.SerializeModelClasses.RegistrationSerial;
import com.wellgel.london.Provider.ModelSerialized.ForgotPasswordSerial;

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
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Customer_APIs {

    @Multipart
    @POST("customer/register")
    Call<RegistrationSerial> registration(@Part MultipartBody.Part file,
                                          @Query("name") String name,
                                          @Query("email") String email,
                                          @Query("password") String password,
                                          @Query("phone") String phone,
                                          @Query("latitude") String latitude,
                                          @Query("longitude") String longitude,
                                          @Query("device_type") String device_type,
                                          @Query("device_token") String device_token,
                                          @Query("address") String address,
                                          @Query("country") String country);

    @POST("customer/login")
    Call<LoginSerialized> login(@Query("email") String email,
                                @Query("password") String password,
                                @Query("device_type") String device_type,
                                @Query("device_token") String device_token);

    @POST("reset-password")
    Call<ForgotPasswordSerial> forgotPass(@Query("email") String email);

    @POST("products")
    Call<C_ProductsSerial> poducts(@Header("Content-Type") String content,
                                   @Header("Authorization") String Auth,
                                   @Query("order_by") String order_by,
                                   @Query("search") String search,
                                   @Query("limit") String limit,
                                   @Query("page") String page);

    @POST("product/details")
    Call<ProductDetailSerial> poductDetail(@Header("Content-Type") String content,
                                           @Header("Authorization") String Auth,
                                           @Query("id") String product_id);

    @POST("cart")
    Call<C_CartSerailized> cart(@Header("Content-Type") String content,
                                @Header("Authorization") String Auth,
                                @Query("product_id") String product_id,
                                @Query("quantity") String quantity,
                                @Query("action") String action);

    @POST("user/address/store")
    Call<C_CreateAddressSerialize> createAddress(@Header("Content-Type") String content,
                                                 @Header("Authorization") String Auth,
                                                 @Query("name") String name,
                                                 @Query("street_address") String street_address,
                                                 @Query("city") String city,
                                                 @Query("county") String county,
                                                 @Query("zip") String zip,
                                                 @Query("country") String country);

    @GET("cart/details")
    Call<C_CartDetailSerialize> cartDetail(@Header("Content-Type") String content,
                                           @Header("Authorization") String Auth);

    @GET("user/address/list")
    Call<C_GetAddressListSerial> getAddressList(@Header("Content-Type") String content,
                                                @Header("Authorization") String Auth);

    @POST("user/address/delete")
    Call<C_DeleteAddresSerial> deleteAddress(@Header("Content-Type") String content,
                                             @Header("Authorization") String Auth,
                                             @Query("id") String id);

    @POST("user/address/update")
    Call<C_UpdateAdressSerial> updateAddress(@Header("Content-Type") String content,
                                             @Header("Authorization") String Auth,
                                             @Query("id") String id,
                                             @Query("name") String name,
                                             @Query("street_address") String street_address,
                                             @Query("city") String city,
                                             @Query("county") String county,
                                             @Query("zip") String zip,
                                             @Query("country") String country);

    @POST("order")
    Call<C_OrderSerial> orderRequest(@Header("Content-Type") String content,
                                     @Header("Authorization") String Auth,
                                     @Query("address_id") String address_id);

    @POST("order/update")
    Call<C_PlaceOrderSerial> placeOrder(@Header("Content-Type") String content,
                                        @Header("Authorization") String Auth,
                                        @Query("order_id") String order_id,
                                        @Query("token") String token);

    @GET("orders")
    Call<C_MyOrdersSerial> myOrders(@Header("Content-Type") String content,
                                    @Header("Authorization") String Auth);

    @GET("appointments/customer/list")
    Call<C_AppointmentList> appointmentList(@Header("Content-Type") String content,
                                            @Header("Authorization") String Auth);


    @Multipart
    @POST("customer/update")
    Call<C_UpdateProfileSerial> updateProfil(@Header("Authorization") String Auth,
                                             @Part MultipartBody.Part file,
                                             @PartMap() Map<String, RequestBody> partMap);

    @POST("salon/list")
    Call<C_SalonListSerial> salonList(@Header("Content-Type") String content,
                                      @Header("Authorization") String Auth,
                                      @Query("radius") String radius,
                                      @Query("search") String sea,
                                      @Query("order_by") String order_by);

    @POST("appointments/store")
    Call<C_CreateAppointment> appointmentsCreate(@Header("Content-Type") String content,
                                                 @Header("Authorization") String Auth,
                                                 @Query("skin_color") String skin_color,
                                                 @Query("nail_polish_color") String nail_polish_color,
                                                 @Query("nail_shape") String nail_shape,
                                                 @Query("salon_user_id") String salon_user_id,
                                                 @Query("requested_datetime") String requested_datetime,
                                                 @Query("comments") String comments);

    @GET("appointment/detail/"+"{appo_id}")
    Call<EcomAppoDetailSerial> appointmentDetail(@Header("Content-Type") String content,
                                                 @Header("Authorization") String Auth,
                                                 @Path("appo_id") int id);

}
