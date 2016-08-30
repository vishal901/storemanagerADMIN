package in.vaksys.storemanager.extra;


import in.vaksys.storemanager.response.AddCuopan;
import in.vaksys.storemanager.response.AddProduct;
import in.vaksys.storemanager.response.CreateBranchAdmin;
import in.vaksys.storemanager.response.Create_Order;
import in.vaksys.storemanager.response.DeleteCustomer;
import in.vaksys.storemanager.response.DeleteProduct;
import in.vaksys.storemanager.response.DeleteUser;
import in.vaksys.storemanager.response.FindAs;
import in.vaksys.storemanager.response.GetAllProduct;
import in.vaksys.storemanager.response.GetBranchAdmin;
import in.vaksys.storemanager.response.GetCoupanList;
import in.vaksys.storemanager.response.GetCustomerList;
import in.vaksys.storemanager.response.GetOrederList;
import in.vaksys.storemanager.response.GetOrederListById;
import in.vaksys.storemanager.response.HomeData;
import in.vaksys.storemanager.response.RegisterResponse;
import in.vaksys.storemanager.response.UpdateCoupan;
import in.vaksys.storemanager.response.UpdateCustomer;
import in.vaksys.storemanager.response.UpdateProduct;
import in.vaksys.storemanager.response.UpdateUser;
import in.vaksys.storemanager.response.createuser;
import in.vaksys.storemanager.response.getstoremanagerlist;
import in.vaksys.storemanager.response.login;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by lenovoi3 on 8/8/2016.
 */
public interface ApiInterface {


    @FormUrlEncoded
    @POST(AppConfig.URL_LOGIN)
    Call<login> LOGIN_RESPONSE_CALL(@Field("username") String username,
                                    @Field("password") String password);


    @FormUrlEncoded
    @POST(AppConfig.URL_CREATE_BRANCH)
    Call<CreateBranchAdmin> CREATE_BRANCH_RESPONSE_CALL(@Field("name") String branchname,
                                                        @Field("address") String branchaddress,
                                                        @Header("Authorization") String auuth);


    @GET(AppConfig.URL_GET_BRANCH)
    Call<GetBranchAdmin> GET_BRANCH_ADMIN_CALL(@Header("Authorization") String auuth);

    @GET(AppConfig.URL_GET_USER)
    Call<getstoremanagerlist> GET_USER_ADMIN_CALL(@Header("Authorization") String auuth);


    @GET(AppConfig.URL_GET_CUSTOMER_LIST)
    Call<GetCustomerList> GET_CUSTOME_LIST(@Header("Authorization") String auuth);


       @FormUrlEncoded
    @POST(AppConfig.URL_CREATE_USER)
    Call<createuser> ADD_USER_RESPONSE_CALL(@Field("username") String username,
                                            @Field("password") String password,
                                            @Field("branch") String abc,
                                            @Header("Authorization") String auuth);

    @FormUrlEncoded
    @POST(AppConfig.URL_CREATE_USER)
    Call<UpdateUser> UPDATE_USER_RESPONSE_CALL(@Field("id") String username,
                                               @Field("username") String password,
                                               @Field("branch") String abc,
                                               @Header("Authorization") String auuth);


    @FormUrlEncoded
    @POST(AppConfig.URL_ADD_PRODUCT)
    Call<AddProduct> ADD_PRODUCT_RESPONSE_CALL(@Field("product_name") String username,
                                               @Field("price") String password,
                                               @Field("branch_id") String abc,
                                               @Header("Authorization") String auuth);
      @FormUrlEncoded
    @POST(AppConfig.URL_UPDATE_PRODUCT)
    Call<UpdateProduct> UPDATE_PRODUCT_RESPONSE_CALL(@Field("product_name") String username,
                                                     @Field("price") String password,
                                                     @Field("product_id") String abc,
                                                     @Header("Authorization") String auuth);

    @FormUrlEncoded
    @POST(AppConfig.URL_UPDATE_COUPAN)
    Call<UpdateCoupan> URL_UPDATE_COUPAN(@Field("coupon_name") String coupon_name,
                                         @Field("price") String price,
                                         @Field("id") String id,
                                         @Field("branch_id") String branch_id,
                                         @Header("Authorization") String auuth);



    @FormUrlEncoded
    @POST(AppConfig.URL_ADD_COUPON)
    Call<AddCuopan> ADD_COUPAN_RESPONSE_CALL(@Field("coupon_name") String username,
                                             @Field("price") String password,
                                             @Field("branch_id") String abc,
                                             @Header("Authorization") String auuth);
    @FormUrlEncoded
    @POST(AppConfig.URL_DELETE_CUSTOMER)
    Call<DeleteCustomer> DELETE_CUSTOMER_CALL(@Field("id") String id,@Header("Authorization") String auuth);

    @FormUrlEncoded
    @POST(AppConfig.URL_DELETE_USER)
    Call<DeleteUser> DELETE_USER_CALL(@Field("id") String id, @Header("Authorization") String auuth);


    @FormUrlEncoded
    @POST(AppConfig.URL_DELETE_COUPAN)
    Call<DeleteCustomer> DELETE_COUPAN_CALL(@Field("id") String id,@Header("Authorization") String auuth);


    @FormUrlEncoded
    @POST(AppConfig.URL_DELETE_PRODUCT)
    Call<DeleteProduct> DELETE_PRODUCT_CALL(@Field("id") String id, @Header("Authorization") String auuth);


    @FormUrlEncoded
    @POST(AppConfig.URL_EDIT_CUSTOMER)
    Call<UpdateCustomer> UPDATE_CUSTOMER_CALL(@Field("customer_id") String customer_id,
                                              @Field("first_name") String first_name,
                                              @Field("gender") String Gender,
                                              @Field("address") String Address,
                                              @Field("mobile") String mobile,
                                              @Field("email") String email,
                                              @Field("branch_id") String branch_id,
                                              @Header("Authorization") String auuth,
                                              @Field("last_name") String last_name,
                                              @Field("date_of_birth") String date_of_birth,
                                              @Field("landline") String landline,
                                              @Field("how_did_you_find_us") String how_did_you_find_us, @Field("ailments") String ailments, @Field("pain_picture") String pain_picture);


    @GET(AppConfig.URL_GET_PRODUCT_LIST)
    Call<GetAllProduct> GET_ALL_PRODUCT(@Header("Authorization") String auuth);

    @GET(AppConfig.URL_GET_COUPAN_LIST)
    Call<GetCoupanList> GET_ALL_COUPAN(@Header("Authorization") String auuth);

    @GET(AppConfig.URL_FINDUS)
    Call<FindAs> findus();

    @GET(AppConfig.URL_DATA)
    Call<HomeData> alldata(@Header("Authorization") String auuth);


    @FormUrlEncoded
    @POST(AppConfig.URL_ADD_ORDER)
    Call<Create_Order> ADD_ORDER_RESPONSE_CALL(@Field("branch_id") String branch_id,
                                               @Field("customer_id") String customer_id,
                                               @Field("item_ids") String item_ids,
                                               @Field("coupon_id") String coupon_id,
                                               @Field("total") String total,
                                               @Field("payment_method") String payment_method,
                                               @Header("Authorization") String auut);

    @FormUrlEncoded
    @POST(AppConfig.URL_REGISTER)
    Call<RegisterResponse> REGISTER_RESPONSE_CALL(@Field("branch_id") String branch_id,
                                                  @Field("first_name") String first_name,
                                                  @Field("gender") String Gender,
                                                  @Field("address") String Address,
                                                  @Field("mobile") String mobile,
                                                  @Field("email") String email);


    @GET(AppConfig.URL_GET_ORDER_LIST)
    Call<GetOrederList> GET_ORDER_LIST(@Header("Authorization") String auuth);
    @FormUrlEncoded
    @POST(AppConfig.URL_GET_ORDER_LIST_ID)
    Call<GetOrederListById> GET_ORDER_LIST_ID(@Field("order_id") String order_id, @Header("Authorization") String auuth);

}
