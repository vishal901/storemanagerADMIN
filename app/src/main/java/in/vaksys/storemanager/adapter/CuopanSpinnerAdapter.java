package in.vaksys.storemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.model.branch;
import in.vaksys.storemanager.model.coupan;


/**
 * Created by dell980 on 6/27/2016.
 */
public class CuopanSpinnerAdapter extends BaseAdapter {

    private Context context;
    private static LayoutInflater inflater;
    private List<coupan> results;

    public CuopanSpinnerAdapter(Context context, ArrayList<coupan> results) {
        this.context = context;
        this.results = results;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        ItemHolder mItemHolder;

        results.get(position);

        rootView = convertView;

        if (convertView == null) {
            rootView = inflater.inflate(R.layout.text_spinner_cupan, null);
            mItemHolder = new ItemHolder();
            mItemHolder.textOne = (TextView) rootView.findViewById(R.id.spin_text_cupan);
            mItemHolder.idtext = (TextView) rootView.findViewById(R.id.rowid);
            rootView.setTag(mItemHolder);
        } else {
            mItemHolder = (ItemHolder) rootView.getTag();
        }

        coupan setbranch = results.get(position);

        mItemHolder.textOne.setText(setbranch.getCoupan_name());
//        mItemHolder.idtext.setText(String.valueOf(addContact.getContactId()));

        return rootView;
    }

    public class ItemHolder {
        TextView textOne, idtext;
    }
}
