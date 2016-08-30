package in.vaksys.storemanager.extra;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by lenovoi3 on 8/15/2016.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private ProgressDialog pDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    // common in volley singleton and analytics
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void createDialog(Activity activity, boolean cancelable) {
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(cancelable);
    }

    public void DialogMessage(String message) {
        pDialog.setMessage(message);
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        //// TODO: 23-05-2016  errorr solve

        //show dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

    }

    public static void show(LinearLayout linearLayout, String msg) {

        Snackbar snack = Snackbar.make(linearLayout, msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }


}
