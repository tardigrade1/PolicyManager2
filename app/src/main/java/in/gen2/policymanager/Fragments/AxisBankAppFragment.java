package in.gen2.policymanager.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.gen2.policymanager.R;

public class AxisBankAppFragment extends Fragment {

    private View view;
    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Axis Bank App");
        view = inflater.inflate(R.layout.fragment_axis_bank_app, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        unbinder = ButterKnife.bind(this,view);

    }
    @OnClick(R.id.imgDownloadAxisApp)
    public void onDownloadAxis(){
        Intent launchIntent = null;
        try{
            launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.axis.mobile");
        } catch (Exception ignored) {}

        if(launchIntent == null){
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.axis.mobile")));
        } else {
            startActivity(launchIntent);
        }
    }
}
