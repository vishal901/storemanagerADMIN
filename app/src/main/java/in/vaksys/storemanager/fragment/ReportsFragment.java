package in.vaksys.storemanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.SpinnerTextAdapter;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class ReportsFragment extends Fragment {
    private ArrayList<String> visitor,datereport;
    @Bind(R.id.sp_visitor_reports)
    Spinner spVisitorReports;
    @Bind(R.id.sp_datereport_reports)
    Spinner spDatereportReports;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);

        ButterKnife.bind(this, rootView);

        init();
        return rootView;


    }

    private void init() {

        visitor = new ArrayList<>();
        visitor.add("Visitor");

        datereport = new ArrayList<>();
        datereport.add("Daily Report");

//        SpinnerTextAdapter spinnerTextAdaptervisitor= new SpinnerTextAdapter(getActivity(), visitor);
//        // attaching data adapter to spinner
//        spVisitorReports.setAdapter(spinnerTextAdaptervisitor);
//
//        SpinnerTextAdapter spinnerTextAdapterdatereport = new SpinnerTextAdapter(getActivity(), datereport);
//        // attaching data adapter to spinner
//        spDatereportReports.setAdapter(spinnerTextAdapterdatereport);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
