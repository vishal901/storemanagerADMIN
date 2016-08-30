package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.response.DeleteCustomer;
import in.vaksys.storemanager.response.DeleteProduct;
import in.vaksys.storemanager.response.UpdateProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by vishal on 7/18/2016.
 */
public class Get_Product_Adapter extends RecyclerView.Adapter<Get_Product_Adapter.ViewHolder> {
    private List<product> productdata;
    private Context context;
    private EditText edtproductname, edtproductprice;
    private Dialog dialog;
    private Button btnsaveproduct;
    private ProgressBar progressproduct;
    private String sproductname, sproductprice, branch_id, apikey;
    private PreferenceHelper preferenceHelper;
    private int mypos;
    private ArrayList<product> productArrayList = new ArrayList<>();
    private String id;
    private MyApplication myApplication;

    public Get_Product_Adapter(Context context, List<product> countries) {
        this.productdata = countries;
        this.context = context;

        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);
    }

    @Override
    public Get_Product_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Get_Product_Adapter.ViewHolder viewHolder, int i) {

        final product data = productdata.get(i);

        viewHolder.product_no.setText(data.getProduct_id());
        viewHolder.productname.setText(data.getProduct_name());
        viewHolder.productprice.setText("\u0024" +data.getProduct_price());



        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = data.getProduct_id();
                mypos = viewHolder.getAdapterPosition();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Sure Want to Delete this Product??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ids) {

                                DeleteProduct_Network_Call(id, mypos, apikey);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        viewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiClient.showLog("product id", data.getProduct_id());
                mypos = viewHolder.getAdapterPosition();

                update_Product(data.getProduct_id(),apikey,mypos,data.getProduct_name(),data.getProduct_price());
            }
        });
    }

    private void DeleteProduct_Network_Call(final String id, final int mypos, String apikey) {


        myApplication.DialogMessage("Delete Product...");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //// TODO: 8/29/2016 change delete class
        Call<DeleteProduct> deleteCustomerCall = apiInterface.DELETE_PRODUCT_CALL(id, apikey);

        deleteCustomerCall.enqueue(new Callback<DeleteProduct>() {
            @Override
            public void onResponse(Call<DeleteProduct> call, Response<DeleteProduct> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {


                    if (!response.body().isError()) {

                        myApplication.hideDialog();
                        productdata.remove(mypos);

                        notifyDataSetChanged();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        myApplication.hideDialog();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    myApplication.hideDialog();
                    Toast.makeText(context, "Something Worng Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<DeleteProduct> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void update_Product(final String product_id, final String apikey, final int mypos, String product_name, String product_price) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_update_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        edtproductname = (EditText) dialog.findViewById(R.id.edt_add_product_name);
        edtproductprice = (EditText) dialog.findViewById(R.id.edt_add_product_price);
        btnsaveproduct = (Button) dialog.findViewById(R.id.btn_save_product);

        progressproduct = (ProgressBar) dialog.findViewById(R.id.pb_create_product);

        edtproductname.setText(product_name);
        edtproductprice.setText(product_price);

        btnsaveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateProductName()) {
                    return;
                }
                if (!validateProductPrice()) {
                    return;
                }

                sproductname = edtproductname.getText().toString().trim();
                sproductprice = edtproductprice.getText().toString().trim();

                Add_Update_Product_Network_Call(sproductname, sproductprice, apikey,product_id,mypos);
            }
        });

    }

    private void Add_Update_Product_Network_Call(final String sproductname, final String sproductprice, String apikey, final String product_id, final int mypos) {

        progressproduct.setVisibility(View.VISIBLE);
        btnsaveproduct.setEnabled(false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateProduct> updateProductCall  = apiInterface.UPDATE_PRODUCT_RESPONSE_CALL(sproductname,sproductprice,product_id,apikey);

        updateProductCall.enqueue(new Callback<UpdateProduct>() {
            @Override
            public void onResponse(Call<UpdateProduct> call, Response<UpdateProduct> response) {

                if (response.code() == 200){

                    progressproduct.setVisibility(View.GONE);
                    btnsaveproduct.setEnabled(true);

                    if (!response.body().isError()){

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        productdata.remove(mypos);
                        product productlist = new product();
                        productlist.setProduct_id(product_id);
                        productlist.setProduct_name(sproductname);
                        productlist.setProduct_price(sproductprice);
                        productArrayList.add(productlist);

                        productdata.add(mypos,productlist);
                        notifyDataSetChanged();

                        dialog.dismiss();

                    }else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else {
                    progressproduct.setVisibility(View.GONE);
                    btnsaveproduct.setEnabled(true);

                    Toast.makeText(context, "Something Worng Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProduct> call, Throwable t) {
                progressproduct.setVisibility(View.GONE);
                btnsaveproduct.setEnabled(true);

                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return productdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView product_no, productname, productprice;
        private ImageView img_edit, img_delete;

        public ViewHolder(View view) {
            super(view);

            product_no = (TextView) view.findViewById(R.id.txt_set_product_no);
            productname = (TextView) view.findViewById(R.id.txt_set_product_name);
            productprice = (TextView) view.findViewById(R.id.txt_set_product_price);

            img_edit = (ImageView) view.findViewById(R.id.img_edit_product);
            img_delete = (ImageView) view.findViewById(R.id.img_delete_product);
        }
    }

    private boolean validateProductName() {
        if (edtproductname.getText().toString().trim().isEmpty()) {
            edtproductname.setError(context.getString(R.string.err_msg_product_name));
            requestFocus(edtproductname);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateProductPrice() {
        if (edtproductprice.getText().toString().trim().isEmpty()) {
            edtproductprice.setError(context.getString(R.string.err_msg_product_price));
            requestFocus(edtproductprice);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
