package in.vaksys.storemanager.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import in.vaksys.storemanager.activity.PaymentActivity;
import in.vaksys.storemanager.adapter.Add_Product_Adapter;
import in.vaksys.storemanager.adapter.CuopanSpinnerAdapter;
import in.vaksys.storemanager.adapter.Get_Order_Adapter;
import in.vaksys.storemanager.adapter.ListViewAdapter;
import in.vaksys.storemanager.adapter.ListViewProductAdapter;
import in.vaksys.storemanager.adapter.SpinnerTextAdapterstatic;
import in.vaksys.storemanager.extra.AdapterCallback;
import in.vaksys.storemanager.extra.AdapterProductCallback;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.EventData;
import in.vaksys.storemanager.model.coupan;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.getorederlist;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.model.productdata;
import in.vaksys.storemanager.model.set_product_data;
import in.vaksys.storemanager.response.GetAllProduct;
import in.vaksys.storemanager.response.GetCoupanList;
import in.vaksys.storemanager.response.GetCustomerList;
import in.vaksys.storemanager.response.GetOrederList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderFragment extends Fragment implements AdapterCallback, AdapterProductCallback {


    @Bind(R.id.sp_coupons_order)
    Spinner spCouponsOrder;
    @Bind(R.id.rec_set_productlist_data)
    RecyclerView recSetProductlistData;
    @Bind(R.id.edt_select_customer_name)
    EditText edtSelectCustomerName;
    @Bind(R.id.edt_select_product_name)
    EditText edtSelectProductName;
    @Bind(R.id.btn_edit_orderdetils)
    Button btnEditOrderdetils;
    @Bind(R.id.edt_select_product_id)
    EditText edtSelectProductId;
    @Bind(R.id.edt_total_orderdetils)
    EditText edtTotalOrderdetils;
    @Bind(R.id.sp_payment_order)
    Spinner spPaymentOrder;
    @Bind(R.id.layout_hide_order)
    LinearLayout layoutHideOrder;
    @Bind(R.id.rec_get_orderlist)
    RecyclerView recGetOrderlist;
    @Bind(R.id.pb_get_product_all)
    ProgressBar pbGetProductAll;
    @Bind(R.id.reletivie_hide)
    RelativeLayout reletivieHide;
    private List<customerlist> customer, coupons, payment;
    private String apikey, customer_id, branchid, type;
    PreferenceHelper preferenceHelper;
    private List<product> addproductlist;
    private ArrayList<coupan> addcoupanlist;
    Dialog dialog;
    Dialog dialog1;
    ListView list, listproduct;
    ListViewAdapter listViewAdapter;
    ListViewProductAdapter listViewProductAdapter;
    private EditText editsearch, edtsearchproduct;
    private ProgressBar progressBar;
    private ProgressBar progressBar1;
    private Button btnsave;
    ArrayList<product> productArrayList;
    private Add_Product_Adapter adapter;
    private String setname;
    private Get_Order_Adapter get_order_adapter;


    ArrayList<productdata> productdatas = new ArrayList<>();
    private int sum = 0;
    private int addsum;
    private String productid, coupanid, paymentname, paymentid, coupanname, coupanprice;
    ArrayList<String> arrayList;
    ArrayList<getorederlist> getorederlists = new ArrayList<>();
    private int changePrice = 0;

    ArrayList<set_product_data> stringArrayList = new ArrayList<>();
    private getorederlist getorederlistdata;
    private MyApplication myApplication;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        ButterKnife.bind(this, rootView);

        myApplication = MyApplication.getInstance();
        myApplication.createDialog(getActivity(), false);

        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");

        if (type.equalsIgnoreCase("0")) {
            //admin

            reletivieHide.setVisibility(View.VISIBLE);
            layoutHideOrder.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recGetOrderlist.setLayoutManager(layoutManager);

            Get_OrderList_Network_Call(apikey);
        }

        ApiClient.showLog("key", apikey);
        init();

        return rootView;


    }

    private void Get_OrderList_Network_Call(String apikey) {
        pbGetProductAll.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetOrederList> getOrederListCall = apiInterface.GET_ORDER_LIST(apikey);

        getOrederListCall.enqueue(new Callback<GetOrederList>() {
            @Override
            public void onResponse(Call<GetOrederList> call, Response<GetOrederList> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    //// TODO: 8/16/2016 get all order list api
                    List<GetOrederList.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {
                        pbGetProductAll.setVisibility(View.GONE);
                        for (GetOrederList.DataBean a : as) {

                            getorederlistdata = new getorederlist();
                            getorederlistdata.setUid(a.getOrderId());
                            getorederlistdata.setCustomername(a.getCustomerName());
                            getorederlistdata.setProcut_count(a.getProductCount());
                            getorederlistdata.setCoupan_count(a.getCouponCount());
                            getorederlistdata.setTotal(a.getOrderTotal());
                            getorederlistdata.setDate(a.getOrderDate());

                            getorederlists.add(getorederlistdata);

                        }

                        get_order_adapter = new Get_Order_Adapter(getActivity(), getorederlists);
                        recGetOrderlist.setAdapter(get_order_adapter);

                    } else {
                        pbGetProductAll.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    pbGetProductAll.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOrederList> call, Throwable t) {
                pbGetProductAll.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No InterNet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void init() {

        //  GetCustomerList_Network_Call();
        //  Get_Product_List(apikey);

        arrayList = new ArrayList<>();
        arrayList.add("CASH");
        arrayList.add("CREDIT");
        arrayList.add("PAYPAL");

        SpinnerTextAdapterstatic spinnerTextAdapterstatic = new SpinnerTextAdapterstatic(getActivity(), arrayList);
        spPaymentOrder.setAdapter(spinnerTextAdapterstatic);
        spPaymentOrder.setSelection(0);
        spPaymentOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        Get_Coupan_List(apikey);


        edtSelectCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialogWithListview();
            }
        });
        edtSelectProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowAlertDialogProduct();
            }
        });

        btnEditOrderdetils.setOnClickListener(new View.OnClickListener() {
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


                preferenceHelper = new PreferenceHelper(getActivity(), "order");
                preferenceHelper.initPref();
                preferenceHelper.SaveStringPref(AppConfig.PREF_USER_ID, customer_id);
                preferenceHelper.SaveStringPref(AppConfig.PREF_COUPAN_ID, coupanid);
                preferenceHelper.SaveStringPref(AppConfig.PREF_PRODUCT_ID, edtSelectProductId.getText().toString().trim());
                preferenceHelper.SaveStringPref(AppConfig.PREF_PAYMENT_NAME, paymentname);
                preferenceHelper.ApplyPref();


                ApiClient.showLog("all data", customer_id + "\n" + coupanid + "\n" + edtSelectProductId.getText().toString().trim() + "\n" + paymentname);


                String total = edtTotalOrderdetils.getText().toString().trim();
                String productid = edtSelectProductId.getText().toString().trim();

                ///   PlaceOrder_Network_Call(apikey, branchid, customer_id, productid, coupanid, total, paymentname);


                for (productdata p_data : adapter.getAllData()) {


//                    stringArrayList.add(p_data.getProduct_id());
//                    stringArrayList.add(p_data.getProduct_name());
//                    stringArrayList.add(p_data.getProduct_price());

                    set_product_data set_product = new set_product_data();

                    set_product.setProduct_id(p_data.getProduct_id());
                    set_product.setProduct_name(p_data.getProduct_name());
                    set_product.setProduct_price(p_data.getProduct_price());

                    stringArrayList.add(set_product);
                }


                Intent i = new Intent(getActivity(), PaymentActivity.class);


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


//        customer = new ArrayList<>();
//
//        customer.add("abc");
//        customer.add("xyx");
//        customer.add("rdfdf");
//        customer.add("xydfdx");
//        customer.add("xydfdx");
//        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, customer);
//        spCustomerOrder.setAdapter(arrayAdapter);


//
//        product = new ArrayList<>();
//        product.add("Abacavir Sulfate");
//
//        coupons = new ArrayList<>();
//        coupons.add("Brufen 400");
//
//        payment = new ArrayList<>();
//        payment.add("Cash");
//        payment.add("Credit");
//        payment.add("Paypal");


//        SpinnerTextAdapter spinnerTextAdapterday = new SpinnerTextAdapter(getActivity(), customer);
//        // attaching data adapter to spinner
//        spCustomerOrder.setAdapter(spinnerTextAdapterday);
//
//        SpinnerTextAdapter spinnerTextAdapteryr = new SpinnerTextAdapter(getActivity(), product);
//        // attaching data adapter to spinner
//        spProductOrder.setAdapter(spinnerTextAdapteryr);
//
//        SpinnerTextAdapter spinnerTextAdapteryrsts = new SpinnerTextAdapter(getActivity(), coupons);
//        // attaching data adapter to spinner
//        spCouponsOrder.setAdapter(spinnerTextAdapteryrsts);
//
//        SpinnerTextAdapter spinnerTextAdapteryrs = new SpinnerTextAdapter(getActivity(), payment);
//        // attaching data adapter to spinner
//        spPaymentOrder.setAdapter(spinnerTextAdapteryrs);


    }


    private void ShowAlertDialogProduct() {

        dialog1 = new Dialog(getActivity());
        dialog1.setTitle("Choose Product Name");
        dialog1.setContentView(R.layout.dailog_product_list);

        listproduct = (ListView) dialog1.findViewById(R.id.list_product);
        progressBar1 = (ProgressBar) dialog1.findViewById(R.id.pb_productlist_data);
        btnsave = (Button) dialog1.findViewById(R.id.btn_save);

        progressBar1.setVisibility(View.VISIBLE);

        Get_Product_List(apikey);

        edtsearchproduct = (EditText) dialog1.findViewById(R.id.et_search_product);

        // Capture Text in EditText
        edtsearchproduct.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edtsearchproduct.getText().toString().toLowerCase(Locale.getDefault());
                listViewProductAdapter.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        listproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtSelectProductName.setText(parent.getSelectedItem().toString());
            }
        });

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
                    sum = sum + Integer.parseInt(product.getProduct_price());

                    productid = product.getProduct_id() + ",";


                    edtSelectProductId.append(product.getProduct_id());
                    edtSelectProductId.append(",");

                    setname = product.getProduct_name() + ",";
                    ApiClient.showLog("product id", "" + productid);

                    productdatas.add(productdata);

                }

                //  edtSelectProductName.setText(setname);
                ApiClient.showLog("sum", "" + sum);

                edtTotalOrderdetils.setText("" + sum);


                if (productdatas.size() == 0) {
                    ApiClient.showLog("valuew", "data null");
                } else {
                    ApiClient.showLog("recycle view ", "data");
                    recSetProductlistData.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recSetProductlistData.setLayoutManager(layoutManager);
                    adapter = new Add_Product_Adapter(getActivity(), productdatas);
                    recSetProductlistData.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }


            }
        });
        dialog1.show();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void ShowAlertDialogWithListview() {
        dialog = new Dialog(getActivity());
        dialog.setTitle("Choose Customer Name");
        dialog.setContentView(R.layout.country_list);

        list = (ListView) dialog.findViewById(R.id.list);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_customerlist_data);

        progressBar.setVisibility(View.VISIBLE);

        GetCustomerList_Network_Call();

        editsearch = (EditText) dialog.findViewById(R.id.et_search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                listViewAdapter.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtSelectCustomerName.setText(parent.getSelectedItem().toString());
            }
        });
        dialog.show();
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

                        listViewAdapter = new ListViewAdapter(OrderFragment.this, customer);
                        list.setAdapter(listViewAdapter);


                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCustomerList> call, Throwable t) {
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
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

                        listViewProductAdapter = new ListViewProductAdapter(OrderFragment.this, addproductlist);
                        listproduct.setAdapter(listViewProductAdapter);

                        //    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetAllProduct> call, Throwable t) {
                progressBar1.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Get_Coupan_List(String apikey) {


        myApplication.DialogMessage("Loading...");
        myApplication.showDialog();

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


                        //    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        CuopanSpinnerAdapter spinnerTextAdapterday = new CuopanSpinnerAdapter(getActivity(), addcoupanlist);
                        // attaching data adapter to spinner
                        spCouponsOrder.setAdapter(spinnerTextAdapterday);
                        spCouponsOrder.setSelection(0);

                        spCouponsOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                GetCoupanList.DataBean aa = response.body().getData().get(position);

                                coupanid = aa.getId();

                                coupanname = aa.getCouponName();
                                coupanprice = aa.getPrice();

                                addsum = sum + Integer.parseInt(aa.getPrice());
                                edtTotalOrderdetils.setText("" + addsum);


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                    } else {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    myApplication.hideDialog();
                    Toast.makeText(getActivity(), "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetCoupanList> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onMethodCallback() {
        dialog.dismiss();
        preferenceHelper = new PreferenceHelper(getActivity(), "data");
        edtSelectCustomerName.setText(preferenceHelper.LoadStringPref(AppConfig.PREF_USER_NAME, ""));
        customer_id = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_ID, "");
    }

    @Override
    public void onMethodPoductCallback() {

        dialog1.dismiss();
        preferenceHelper = new PreferenceHelper(getActivity(), "data");
        edtSelectProductName.setText(preferenceHelper.LoadStringPref(AppConfig.PREF_PRODUCT_NAME, ""));
    }

    private boolean validateCustomerName() {
        if (edtSelectCustomerName.getText().toString().trim().isEmpty()) {
            edtSelectCustomerName.setError(getString(R.string.err_msg_customer_name));
            requestFocus(edtSelectCustomerName);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatetotalprice() {
        if (edtTotalOrderdetils.getText().toString().trim().isEmpty()) {
            edtTotalOrderdetils.setError(getString(R.string.err_msg_total));
            requestFocus(edtTotalOrderdetils);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBranchAddress() {
        if (edtSelectProductName.getText().toString().trim().isEmpty()) {
            edtSelectProductName.setError(getString(R.string.err_msg_product));
            requestFocus(edtSelectProductName);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Subscribe
    public void onEvent(EventData messageCar) {
        changePrice = messageCar.getData();
        sum = sum - changePrice + addsum;
        edtTotalOrderdetils.setText(String.valueOf(sum));
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

}
