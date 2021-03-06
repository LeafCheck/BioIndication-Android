package com.example.skoml.bioindication;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecometr.app.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);

        String version = "0.0";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;


            Element versionElement = new Element();
            versionElement.setTitle(getString(R.string.app_name) + " " + version);

            View aboutPage = new AboutPage(getActivity())
                    .isRTL(false)
                    .setImage(R.drawable.ic_local_florist_black_48px)
                    .addItem(versionElement)

                    .setDescription("Crowdsourcing-driven bioindication framework\nDeveloped during hackathon \"NASA SpaceApps Challenge\"\nhttps://2016.spaceappschallenge.org/challenges/earth/aircheck/projects/crowdsourcing-driven-bioindication-framework\nOriginally the #Aircheck challenge mentions the tool for public entry of one's symptoms and combining them with open data sources of environmental factors. But we recall the quote of famous character Dr. Greg House - \"Everybody lies\" and decided to collect data from honest organisms - plants. In biology science this method called as \"bioindication\".")

                    .addGroup("Connect with us")
                    .addEmail("s.komlach@gmail.com")
                    .addFacebook("sergej.komlach")
                    .addTwitter("SergejKomlach")
                    .addYoutube("UCywYOdZ_6cmPePWF4RbBS0g")
                    .addPlayStore(getActivity().getPackageName())
                    .addGitHub("LeafCheck")
                    //.addInstagram("medyo80")
                    .create();
            return aboutPage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }


}
