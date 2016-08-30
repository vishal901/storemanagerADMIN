package in.vaksys.storemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.Get_Product_Detail_Adapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.response.GetOrederListById;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 8/17/2016.
 */
public class OrderList_Detail_Activity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.txt_orderid_detail)
    TextView txtOrderidDetail;
    @Bind(R.id.txt_name_detail)
    TextView txtNameDetail;
    @Bind(R.id.txt_address_detail)
    TextView txtAddressDetail;
    @Bind(R.id.txt_orderdate_detail)
    TextView txtOrderdateDetail;
    @Bind(R.id.txt_coupan_title_detail)
    TextView txtCoupanTitleDetail;
    @Bind(R.id.txt_coupan_price_detail)
    TextView txtCoupanPriceDetail;
    @Bind(R.id.rec_get_product_detail)
    RecyclerView recGetProductDetail;
    @Bind(R.id.txt_total_detail)
    TextView txtTotalDetail;
    private String id;
    private PreferenceHelper preferenceHelper;
    private String Apikey;
    private ArrayList<product> productArrayList = new ArrayList<>();
    private Get_Product_Detail_Adapter get_product_detail_adapter;
    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Order Details");
        myApplication = MyApplication.getInstance();
        myApplication.createDialog(OrderList_Detail_Activity.this,false);
        preferenceHelper = new PreferenceHelper(OrderList_Detail_Activity.this, "type");
        Apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");

        Intent i = getIntent();
        id = i.getStringExtra("id");

        ApiClient.showLog("id-->", "" + id);

        Order_Detail_Network_call(id, Apikey);

    }

    private void Order_Detail_Network_call(String id, String apikey) {

        myApplication.DialogMessage("Loading...");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetOrederListById> getOrederListCall = apiInterface.GET_ORDER_LIST_ID(id, apikey);

        getOrederListCall.enqueue(new Callback<GetOrederListById>() {
            @Override
            public void onResponse(Call<GetOrederListById> call, Response<GetOrederListById> response) {

                if (response.code() == 200) {
                    myApplication.hideDialog();
                    List<GetOrederListById.DataBean.OrderBean> order = response.body().getData().getOrder();
                    List<GetOrederListById.DataBean.CouponBean> coupan = response.body().getData().getCoupon();
                    List<GetOrederListById.DataBean.ProductsBean> product = response.body().getData().getProducts();

                    if (!response.body().isError()) {

                        for (GetOrederListById.DataBean.OrderBean aa : order) {

                            txtOrderidDetail.setText("Order Id:-" + "#" + aa.getId());
                            txtNameDetail.setText(aa.getCustomerName());
                            txtAddressDetail.setText(aa.getAddress());
                            txtOrderdateDetail.setText(aa.getOrderDate());
                            txtTotalDetail.setText(aa.getTotal());
                        }

                        for (GetOrederListById.DataBean.CouponBean bb : coupan) {

                            txtCoupanTitleDetail.setText(bb.getCouponName());
                            txtCoupanPriceDetail.setText(bb.getPrice());
                        }

                        for (GetOrederListById.DataBean.ProductsBean cc : product) {

                            product product1 = new product();
                            product1.setProduct_name(cc.getProductName());
                            product1.setProduct_price(cc.getPrice());

                            productArrayList.add(product1);


                        }


                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderList_Detail_Activity.this);
                        recGetProductDetail.setLayoutManager(layoutManager);
                        get_product_detail_adapter = new Get_Product_Detail_Adapter(OrderList_Detail_Activity.this, productArrayList);
                        recGetProductDetail.setAdapter(get_product_detail_adapter);



                    } else {
                        Toast.makeText(OrderList_Detail_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    myApplication.hideDialog();
                    Toast.makeText(OrderList_Detail_Activity.this, "Somting Worng Data", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GetOrederListById> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(OrderList_Detail_Activity.this, "No Internet Access", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
