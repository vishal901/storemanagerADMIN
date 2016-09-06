package in.vaksys.storemanager.activity;

/**
 * Created by lenovoi3 on 7/27/2016.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.fragment.BranchFragment;
import in.vaksys.storemanager.fragment.CoupanFragment;
import in.vaksys.storemanager.fragment.CustomerFragment;
import in.vaksys.storemanager.fragment.FragmentDrawer;
import in.vaksys.storemanager.fragment.HomeFragment;
import in.vaksys.storemanager.fragment.OrderFragmentNew;
import in.vaksys.storemanager.fragment.UserFragment;
import in.vaksys.storemanager.fragment.OrderFragment;
import in.vaksys.storemanager.fragment.ProductsFragment;
import in.vaksys.storemanager.fragment.ReportsFragment;
import in.vaksys.storemanager.model.EventData;
import in.vaksys.storemanager.model.EventMsg;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    PreferenceHelper preferenceHelper;
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        bus.register(this);
        preferenceHelper = new PreferenceHelper(MainActivity.this,"type");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }


    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.home);
                break;
            case 1:
                fragment = new ProductsFragment();
                title = getString(R.string.products);
                break;

            case 2:
                fragment = new CustomerFragment();
                title = getString(R.string.customer);
                break;
            case 3:
                fragment = new CoupanFragment();
                title = getString(R.string.coupan);
                break;
            case 4:
                fragment = new OrderFragmentNew();
                title = getString(R.string.order);
                break;

            case 5:
//                fragment = new ReportsFragment();
//                title = getString(R.string.reports);
                break;

            case 6:
                fragment = new UserFragment();
                title = getString(R.string.user);
                break;

            case 7:
                fragment = new BranchFragment();
                title = getString(R.string.branch);
                break;

            case 8:

                preferenceHelper.initPref();
                preferenceHelper.SaveStringPref(AppConfig.PREF_FLAG,"");
                preferenceHelper.ApplyPref();

                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    @Subscribe
    public void onEvent(EventMsg eventMsg){

        ApiClient.showLog("calling...","Callllllling");
    }

    @Override
    protected void onDestroy() {
        // Unregister
        bus.unregister(this);
        super.onDestroy();
    }


}
