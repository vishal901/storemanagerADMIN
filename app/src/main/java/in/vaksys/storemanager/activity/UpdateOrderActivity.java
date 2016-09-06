package in.vaksys.storemanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.CuopanSpinnerAdapter;
import in.vaksys.storemanager.adapter.SpinnerTextAdapterstatic;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.coupan;
import in.vaksys.storemanager.response.GetCoupanList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 9/6/2016.
 */
public class UpdateOrderActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edt_select_customer_name1)
    EditText edtSelectCustomerName1;
    @Bind(R.id.edt_select_product_name1)
    EditText edtSelectProductName1;
    @Bind(R.id.edt_select_product_id)
    EditText edtSelectProductId;
    @Bind(R.id.rec_set_productlist_data)
    RecyclerView recSetProductlistData;
    @Bind(R.id.sp_coupons_order1)
    Spinner spCouponsOrder1;
    @Bind(R.id.edt_total_orderdetils1)
    EditText edtTotalOrderdetils1;
    @Bind(R.id.sp_payment_order1)
    Spinner spPaymentOrder1;
    @Bind(R.id.btn_edit_orderdetils1)
    Button btnEditOrderdetils1;
    @Bind(R.id.layout_hide_order1)
    LinearLayout layoutHideOrder1;
    @Bind(R.id.rec_get_orderlist)
    RecyclerView recGetOrderlist;
    @Bind(R.id.pb_get_product_all)
    ProgressBar pbGetProductAll;
    @Bind(R.id.reletivie_hide)
    RelativeLayout reletivieHide;
    private ArrayList<String> arrayList;

    private String productid, coupanid, paymentname, paymentid, coupanname, coupanprice;
    private PreferenceHelper preferenceHelper;
    private String apikey, customer_id, branchid, type;
    private MyApplication myApplication;
    private ArrayList<coupan> addcoupanlist;
    int total = 0;
    private int product_sum = 0;
    private int price_sum = 0;
    private int addsum = 0;
    private int sum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);
        ButterKnife.bind(this);

        toolbar.setTitle("Update Order");

        preferenceHelper = new PreferenceHelper(UpdateOrderActivity.this, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");

        myApplication = MyApplication.getInstance();
        myApplication.createDialog(UpdateOrderActivity.this, false);

        init();
    }

    private void init() {

        PaymentSpinner();
        Get_Coupan_List(apikey);
        Clicklistenetbutton();

    }

    private void Clicklistenetbutton() {


    }

    private void PaymentSpinner() {

        arrayList = new ArrayList<>();
        arrayList.add("CASH");
        arrayList.add("CREDIT");
        arrayList.add("PAYPAL");

        SpinnerTextAdapterstatic spinnerTextAdapterstatic = new SpinnerTextAdapterstatic(UpdateOrderActivity.this, arrayList);
        spPaymentOrder1.setAdapter(spinnerTextAdapterstatic);
        spPaymentOrder1.setSelection(0);
        spPaymentOrder1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.spin_text);

                paymentname = textView.getText().toString();
                paymentid = String.valueOf(id);
                ApiClient.showLog("paymentid", paymentid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });
    }

    private void Get_Coupan_List(String apikey) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetCoupanList> getBranchAdminCall = apiInterface.GET_ALL_COUPAN(apikey);

        getBranchAdminCall.enqueue(new Callback<GetCoupanList>() {
            @Override
            public void onResponse(Call<GetCoupanList> call, final Response<GetCoupanList> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {
                    myApplication.hideDialog();
                    List<GetCoupanList.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {

                        addcoupanlist = new ArrayList<>();

                        for (GetCoupanList.DataBean a : as) {

                            coupan data = new coupan();
                            data.setCoupan_id((a.getId()));
                            data.setCoupan_name(a.getCouponName());
                            data.setCoupan_price(a.getPrice());
                            addcoupanlist.add(data);

                        }


                        //    Toast.makeText(Create_Order_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        CuopanSpinnerAdapter spinnerTextAdapterday = new CuopanSpinnerAdapter(UpdateOrderActivity.this, addcoupanlist);
                        // attaching data adapter to spinner
                        spCouponsOrder1.setAdapter(spinnerTextAdapterday);
                        spCouponsOrder1.setSelection(0);

                        spCouponsOrder1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                GetCoupanList.DataBean aa = response.body().getData().get(position);

                                coupanid = aa.getId();

                                coupanname = aa.getCouponName();
                                coupanprice = aa.getPrice();

                                price_sum = Integer.parseInt(aa.getPrice());


                                edt_setValue(price_sum,product_sum);

                                addsum = sum + Integer.parseInt(aa.getPrice());

                                // setvalue(addsum,sum);
                                System.out.println("sum data" + addsum + sum);

                                //    setdata(sum,Integer.parseInt(aa.getPrice()));

                                //   edtTotalOrderdetils1.setText("" + addsum);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                    } else {

                        Toast.makeText(UpdateOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    myApplication.hideDialog();
                    Toast.makeText(UpdateOrderActivity.this, "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetCoupanList> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(UpdateOrderActivity.this, "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void edt_setValue(int price_sum, int product_sum) {

        total = price_sum + product_sum;

        edtTotalOrderdetils1.setText(""+total);
    }

}
