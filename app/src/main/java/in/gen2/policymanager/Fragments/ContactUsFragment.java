package in.gen2.policymanager.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.gen2.policymanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    private Unbinder unbinder;
    private View view;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
       view= inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder=ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.tvWebsiteLink)
    public void onWeblinkClick(){
        String url = "https://www.liccards.co.in/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Contact Us");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }
    @OnClick({R.id.devContact})
    public void onContactDeveloper(){

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"developer@gen2.in"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding LIC cards app support");
        getActivity().startActivity(intent);
    }
    @OnClick(R.id.gen2Weblink)
    public void onGen2WelinkClick(){
        String url = "https://www.gen2.in/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
