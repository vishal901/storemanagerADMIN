package in.vaksys.storemanager.adapter;

/**
 * Created by dell980 on 6/18/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    //List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private String[] title;
    private int[] image;
    PreferenceHelper preferenceHelper;
    private String user_type;

    public NavigationDrawerAdapter(Context context, String[] title) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        //this.data = data;
        this.title = title;
        preferenceHelper = new PreferenceHelper(context, "type");
        this.image = image;
    }

    /*public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        user_type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");
        ApiClient.showLog("type", user_type);

        holder.title.setText(title[position]);


        if (user_type.equalsIgnoreCase("1")) {

            if (position == 6) {

                holder.title.setVisibility(View.GONE);
                holder.viewId.setVisibility(View.GONE);
            }

            if (position == 7) {

                holder.title.setVisibility(View.GONE);
                holder.viewId.setVisibility(View.GONE);
            }

        }
//        holder.image.setBackgroundResource(image[position]);

//        if (position == 0) {
//            holder.viewId.setVisibility(View.GONE);
//        }
//
//        if (position == 2) {
//            holder.viewId.setVisibility(View.GONE);
//        }
//
//        if (position == 1) {
//            holder.ll.setPadding(50, 0, 0, 0);
//            holder.title.setTextSize(12);
//        }
//
//        if (position == 3) {
//            holder.ll.setPadding(50, 0, 0, 0);
//            holder.title.setTextSize(12);
//        }

    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        View ll, viewId;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titleNav);
            image = (ImageView) itemView.findViewById(R.id.imageNav);
            ll = itemView.findViewById(R.id.ll);
            viewId = itemView.findViewById(R.id.viewId);
        }
    }
}