package in.vaksys.storemanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.CircularProgressBar;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.response.HomeData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class HomeFragment extends Fragment {
    CircularProgressBar c3;

    PreferenceHelper preferenceHelper;
    @Bind(R.id.txt_no_coupan)
    TextView txtNoCoupan;
    @Bind(R.id.txt_no_product)
    TextView txtNoProduct;
    private String Apikey;
    private MyApplication myApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_home, container, false);

        ButterKnife.bind(this, rootView);

        myApplication = MyApplication.getInstance();
        myApplication.createDialog(getActivity(), false);

        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        Apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        ApiClient.showLog("api key", Apikey);

        c3 = (CircularProgressBar) rootView.findViewById(R.id.circularprogressbar1);


        getdata(Apikey);


        return rootView;


    }

    private void getdata(String apikey) {


        myApplication.DialogMessage("Loading...");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<HomeData> homeDataCall = apiInterface.alldata(apikey);

        homeDataCall.enqueue(new Callback<HomeData>() {
            @Override
            public void onResponse(Call<HomeData> call, Response<HomeData> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {

                    myApplication.hideDialog();

                    if (!response.body().isError()) {


                        c3.setProgress(Integer.parseInt(response.body().getData().getNoSells()));
                        c3.setTitle((response.body().getData().getNoSells()));
                        txtNoCoupan.setText(response.body().getData().getNoCoupon());
                        txtNoProduct.setText(response.body().getData().getNoProduct());


                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    myApplication.hideDialog();
                    Toast.makeText(getActivity(), "Something Wrong Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HomeData> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
