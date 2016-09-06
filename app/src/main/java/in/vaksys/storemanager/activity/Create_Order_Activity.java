package in.vaksys.storemanager.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.Add_Product_Adapter;
import in.vaksys.storemanager.adapter.Ailments_Adapter;
import in.vaksys.storemanager.adapter.CuopanSpinnerAdapter;
import in.vaksys.storemanager.adapter.Product_Checkbox_Adapter;
import in.vaksys.storemanager.adapter.SpinnerTextAdapterstatic;
import in.vaksys.storemanager.extra.AdapterCallback;
import in.vaksys.storemanager.extra.AdapterProductCallback;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.DividerItemDecoration;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.EventCustomer;
import in.vaksys.storemanager.model.EventData;
import in.vaksys.storemanager.model.coupan;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.model.productdata;
import in.vaksys.storemanager.model.set_product_data;
import in.vaksys.storemanager.response.GetAllProduct;
import in.vaksys.storemanager.response.GetCoupanList;
import in.vaksys.storemanager.response.GetCustomerList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 9/2/2016.
 */
public class Create_Order_Activity extends AppCompatActivity implements AdapterCallback, AdapterProductCallback {

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<String> arrayList;
    private String productid, coupanid, paymentname, paymentid, coupanname, coupanprice;
    private PreferenceHelper preferenceHelper;
    private String apikey, customer_id, branchid, type;
    private MyApplication myApplication;
    private ArrayList<coupan> addcoupanlist;
    private int addsum = 0;
    private int sum = 0;

    private Dialog dialog;
    private Dialog dialog1;
    private RecyclerView list;
    private RecyclerView listproduct;
    private ProgressBar progressBar;
    private ProgressBar progressBar1;
    private EditText editsearch, edtsearchproduct;
    private Ailments_Adapter listViewAdapter;
    private Product_Checkbox_Adapter listViewProductAdapter;
    private List<customerlist> customer, coupons, payment;
    private Button btnsave;
    private List<product> addproductlist;
    private String setname;
    private ArrayList<productdata> productdatas = new ArrayList<>();
    private Add_Product_Adapter adapter;
    private int changePrice = 0;
    private ArrayList<set_product_data> stringArrayList = new ArrayList<>();

    int total = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);
        ButterKnife.bind(this);
        toolbar.setTitle("Create Order");

        preferenceHelper = new PreferenceHelper(Create_Order_Activity.this, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");

        myApplication = MyApplication.getInstance();
        myApplication.createDialog(Create_Order_Activity.this, false);

        init();
    }

    private void init() {

        PaymentSpinner();

        Get_Coupan_List(apikey);

        Clicklistenetbutton();


    }

    private void Clicklistenetbutton() {

        edtSelectCustomerName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialogWithListview();
            }
        });
        edtSelectProductName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowAlertDialogProduct();
            }
        });

        btnEditOrderdetils1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validateCustomerName()) {
                    return;
                }

                if (!validatetotalprice()) {
                    return;
                }
//                if (!validateBranchAddress()) {
//                    return;
//                }


                preferenceHelper = new PreferenceHelper(Create_Order_Activity.this, "order");
                preferenceHelper.initPref();
                preferenceHelper.SaveStringPref(AppConfig.PREF_USER_ID, customer_id);
                preferenceHelper.SaveStringPref(AppConfig.PREF_COUPAN_ID, coupanid);
                preferenceHelper.SaveStringPref(AppConfig.PREF_PRODUCT_ID, edtSelectProductId.getText().toString().trim());
                preferenceHelper.SaveStringPref(AppConfig.PREF_PAYMENT_NAME, paymentname);
                preferenceHelper.ApplyPref();


                ApiClient.showLog("all data", customer_id + "\n" + coupanid + "\n" + edtSelectProductId.getText().toString().trim() + "\n" + paymentname);


                String total = edtTotalOrderdetils1.getText().toString().trim();
                String productid = edtSelectProductId.getText().toString().trim();

                ///   PlaceOrder_Network_Call(apikey, branchid, customer_id, productid, coupanid, total, paymentname);



//                if (adapter.getAllData().size() == 0){
//
//                }

                try {

                    for (productdata p_data : adapter.getAllData()) {


                        set_product_data set_product = new set_product_data();

                        set_product.setProduct_id(p_data.getProduct_id());
                        set_product.setProduct_name(p_data.getProduct_name());
                        set_product.setProduct_price(p_data.getProduct_price());

                        stringArrayList.add(set_product);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



                Intent i = new Intent(Create_Order_Activity.this, PaymentActivity.class);


                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("key", stringArrayList);
                bundleObject.putString("branchid", branchid);
                bundleObject.putString("customer_id", customer_id);
                bundleObject.putString("coupanid", coupanid);
                bundleObject.putString("payid", paymentid);
                bundleObject.putString("productid", productid);
                bundleObject.putString("cname", coupanname);
                bundleObject.putString("cprice", coupanprice);
                bundleObject.putString("total", total);
                bundleObject.putString("paymentname", paymentname);

                i.putExtras(bundleObject);
                startActivity(i);


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
                        CuopanSpinnerAdapter spinnerTextAdapterday = new CuopanSpinnerAdapter(Create_Order_Activity.this, addcoupanlist);
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

                                addsum = sum + Integer.parseInt(aa.getPrice());

                                // setvalue(addsum,sum);
                                System.out.println("sum data" + addsum + sum);

                                edtTotalOrderdetils1.setText("" + addsum);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                    } else {

                        Toast.makeText(Create_Order_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    myApplication.hideDialog();
                    Toast.makeText(Create_Order_Activity.this, "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetCoupanList> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(Create_Order_Activity.this, "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setvalue(int addsum, int sum) {


        total = addsum + sum;

        edtTotalOrderdetils1.setText("" + total);
    }

    private void PaymentSpinner() {

        arrayList = new ArrayList<>();
        arrayList.add("CASH");
        arrayList.add("CREDIT");
        arrayList.add("PAYPAL");

        SpinnerTextAdapterstatic spinnerTextAdapterstatic = new SpinnerTextAdapterstatic(Create_Order_Activity.this, arrayList);
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

    public void ShowAlertDialogWithListview() {
        dialog = new Dialog(Create_Order_Activity.this);
        dialog.setTitle("Choose Customer Name");
        dialog.setContentView(R.layout.dialog_customer_list);
        dialog.show();

        list = (RecyclerView) dialog.findViewById(R.id.rec_customer_list1);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_customerlist_data1);

        progressBar.setVisibility(View.VISIBLE);

        GetCustomerList_Network_Call();

        editsearch = (EditText) dialog.findViewById(R.id.et_search1);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                //  listViewAdapter.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence query, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

                query = query.toString().toLowerCase();

                final List<customerlist> filteredList = new ArrayList<>();

                for (int i = 0; i < customer.size(); i++) {

                    final String text = customer.get(i).getCustomername().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(customer.get(i));
                    }
                }

                list.addItemDecoration(new DividerItemDecoration(Create_Order_Activity.this));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Create_Order_Activity.this);
                list.setLayoutManager(layoutManager);
                final Ailments_Adapter adapter = new Ailments_Adapter(Create_Order_Activity.this, filteredList);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void ShowAlertDialogProduct() {

        dialog1 = new Dialog(Create_Order_Activity.this);
        dialog1.setTitle("Choose Product Name");
        dialog1.setContentView(R.layout.dailog_product_list_new);
        dialog1.show();

        listproduct = (RecyclerView) dialog1.findViewById(R.id.rec_product_list1);
        progressBar1 = (ProgressBar) dialog1.findViewById(R.id.pb_productlist_data1);
        btnsave = (Button) dialog1.findViewById(R.id.btn_save1);

        progressBar1.setVisibility(View.VISIBLE);

        Get_Product_List(apikey);

        edtsearchproduct = (EditText) dialog1.findViewById(R.id.et_search_product1);

        // Capture Text in EditText
        edtsearchproduct.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edtsearchproduct.getText().toString().toLowerCase(Locale.getDefault());
                //listViewProductAdapter.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence query, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

                query = query.toString().toLowerCase();

                final List<product> filteredList = new ArrayList<>();

                for (int i = 0; i < addproductlist.size(); i++) {

                    final String text = addproductlist.get(i).getProduct_name().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(addproductlist.get(i));
                    }
                }

                list.addItemDecoration(new DividerItemDecoration(Create_Order_Activity.this));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Create_Order_Activity.this);
                list.setLayoutManager(layoutManager);
                final Product_Checkbox_Adapter adapter = new Product_Checkbox_Adapter(Create_Order_Activity.this, filteredList);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
        });

//        listproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                edtSelectProductName1.setText(parent.getSelectedItem().toString());
//            }
//        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.hide();


                for (product product : listViewProductAdapter.getAllData()) {

                    // ApiClient.showLog("kjhujlbju", product.getProduct_name());

                    productdata productdata = new productdata();
                    productdata.setProduct_name(product.getProduct_name());
                    productdata.setProduct_id(product.getProduct_id());
                    productdata.setProduct_price(product.getProduct_price());
                    sum = sum + addsum + Integer.parseInt(product.getProduct_price());

                    productid = product.getProduct_id() + ",";


                    edtSelectProductId.append(product.getProduct_id());
                    edtSelectProductId.append(",");

                    setname = product.getProduct_name() + ",";
                    ApiClient.showLog("product id", "" + productid);

                    productdatas.add(productdata);

                }

                //  edtSelectProductName.setText(setname);
                ApiClient.showLog("sum", "" + sum);

                //  setvalue(sum, sum);
                edtTotalOrderdetils1.setText("" + sum);


                if (productdatas.size() == 0) {
                    ApiClient.showLog("valuew", "data null");
                } else {
                    ApiClient.showLog("recycle view ", "data");
                    recSetProductlistData.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Create_Order_Activity.this);
                    recSetProductlistData.setLayoutManager(layoutManager);
                    adapter = new Add_Product_Adapter(Create_Order_Activity.this, productdatas);
                    recSetProductlistData.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }


            }
        });


    }


    private void GetCustomerList_Network_Call() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetCustomerList> getCustomerListCall = apiInterface.GET_CUSTOME_LIST(apikey);

        getCustomerListCall.enqueue(new Callback<GetCustomerList>() {

            @Override
            public void onResponse(Call<GetCustomerList> call, Response<GetCustomerList> response) {
                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    List<GetCustomerList.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {
                        progressBar.setVisibility(View.GONE);
                        customer = new ArrayList<customerlist>();
                        for (GetCustomerList.DataBean a : as) {

                            ApiClient.showLog("data", a.getFirstName());

                            customerlist data = new customerlist();
                            data.setCustomer_id(a.getId());
                            data.setCustomername((a.getFirstName()));
                            data.setCustomeraddress(a.getAddress());
                            data.setCustomergender(a.getGender());
                            data.setCustomercontactno(a.getMobile());
                            customer.add(data);

                        }

                        //// TODO: 9/2/2016 change listview replace recycleview
                        list.addItemDecoration(new DividerItemDecoration(Create_Order_Activity.this));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Create_Order_Activity.this);
                        list.setLayoutManager(layoutManager);
                        listViewAdapter = new Ailments_Adapter(Create_Order_Activity.this, customer);
                        list.setAdapter(listViewAdapter);


                        Toast.makeText(Create_Order_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Create_Order_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Create_Order_Activity.this, "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCustomerList> call, Throwable t) {
                Toast.makeText(Create_Order_Activity.this, "No Internet Accesss", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void Get_Product_List(String apikey) {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetAllProduct> getBranchAdminCall = apiInterface.GET_ALL_PRODUCT(apikey);

        getBranchAdminCall.enqueue(new Callback<GetAllProduct>() {
            @Override
            public void onResponse(Call<GetAllProduct> call, Response<GetAllProduct> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    List<GetAllProduct.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {
                        progressBar1.setVisibility(View.GONE);
                        addproductlist = new ArrayList<>();

                        for (GetAllProduct.DataBean a : as) {

                            ApiClient.showLog("dayaty", a.getProductName());

                            product data = new product();
                            data.setProduct_id((a.getId()));
                            data.setProduct_name(a.getProductName());
                            data.setProduct_price(a.getPrice());
                            addproductlist.add(data);
//                            addproductlist.add(a.getProductName());

                        }

                        listproduct.addItemDecoration(new DividerItemDecoration(Create_Order_Activity.this));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Create_Order_Activity.this);
                        listproduct.setLayoutManager(layoutManager);
                        listViewProductAdapter = new Product_Checkbox_Adapter(Create_Order_Activity.this, addproductlist);
                        listproduct.setAdapter(listViewProductAdapter);

                        //    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(Create_Order_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(Create_Order_Activity.this, "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetAllProduct> call, Throwable t) {
                progressBar1.setVisibility(View.GONE);
                Toast.makeText(Create_Order_Activity.this, "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Subscribe
    public void onEvent(EventData messageCar) {
        changePrice = messageCar.getData();
        sum = sum - changePrice;
        edtTotalOrderdetils1.setText(String.valueOf(sum));
    }

    @Subscribe
    public void onEvent(EventCustomer eventCustomer) {
        dialog.dismiss();
        edtSelectCustomerName1.setText(eventCustomer.getName());
        customer_id = eventCustomer.getId();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private boolean validateCustomerName() {
        if (edtSelectCustomerName1.getText().toString().trim().isEmpty()) {
            edtSelectCustomerName1.setError(getString(R.string.err_msg_customer_name));
            requestFocus(edtSelectCustomerName1);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatetotalprice() {
        if (edtTotalOrderdetils1.getText().toString().trim().isEmpty()) {
            edtTotalOrderdetils1.setError(getString(R.string.err_msg_total));
            requestFocus(edtTotalOrderdetils1);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBranchAddress() {
        if (edtSelectProductName1.getText().toString().trim().isEmpty()) {
            edtSelectProductName1.setError(getString(R.string.err_msg_product));
            requestFocus(edtSelectProductName1);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onMethodCallback() {
        dialog.dismiss();
        preferenceHelper = new PreferenceHelper(Create_Order_Activity.this, "data");
        edtSelectCustomerName1.setText(preferenceHelper.LoadStringPref(AppConfig.PREF_USER_NAME, ""));
        customer_id = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_ID, "");
    }

    @Override
    public void onMethodPoductCallback() {

        dialog1.dismiss();
        preferenceHelper = new PreferenceHelper(Create_Order_Activity.this, "data");
        edtSelectProductName1.setText(preferenceHelper.LoadStringPref(AppConfig.PREF_PRODUCT_NAME, ""));
    }
}
