package com.example.skoml.bioindication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.ecometr.app.R;

//http://developer.android.com/intl/ru/guide/topics/ui/settings.html
public class SettingsFragment extends PreferenceFragment {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
       final SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        final CheckBoxPreference ruler = (CheckBoxPreference) this.getPreferenceManager().findPreference("useRuler");
        ruler.setChecked(pref.getBoolean("useRuler", false));
        ruler.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                pref.edit().putBoolean("useRuler", (Boolean)newValue).commit();

                return true;
            }
        });



        //-----------------------------------------------------


        final CheckBoxPreference Roberts = (CheckBoxPreference) this.getPreferenceManager().findPreference("Roberts");
        final CheckBoxPreference Prewitt = (CheckBoxPreference) this.getPreferenceManager().findPreference("Prewitt");
        final CheckBoxPreference Sobel = (CheckBoxPreference) this.getPreferenceManager().findPreference("Sobel");
        final CheckBoxPreference Scharr = (CheckBoxPreference) this.getPreferenceManager().findPreference("Scharr");

        Roberts.setChecked(pref.getInt("edge_operator", 1) == 0);
        Prewitt.setChecked(pref.getInt("edge_operator", 1) == 1);
        Sobel.setChecked(pref.getInt("edge_operator", 1) == 2);
        Scharr.setChecked(pref.getInt("edge_operator", 1) == 3);

        Preference.OnPreferenceChangeListener operator = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean allfalse = false;

                if(preference.getKey().equals("Roberts"))
                    allfalse =  !Prewitt.isChecked() && !Sobel.isChecked() && !Scharr.isChecked() && !((Boolean) newValue);

                else
                if(preference.getKey().equals("Prewitt"))
                    allfalse =  !Roberts.isChecked() && !Sobel.isChecked() && !Scharr.isChecked()&& !((Boolean) newValue);

                else
                if(preference.getKey().equals("Sobel"))
                    allfalse =  !Roberts.isChecked() && !Prewitt.isChecked() && !Scharr.isChecked() && !((Boolean) newValue);
                else
                if(preference.getKey().equals("Scharr"))
                    allfalse =  !Roberts.isChecked() && !Prewitt.isChecked() && !Sobel.isChecked() && !((Boolean) newValue);


                if(allfalse)
                    return false;

                int value = 1;
                if(preference.getKey().equals("Roberts"))
                    value = 0;

                else
                if(preference.getKey().equals("Prewitt"))
                    value = 1;

                else
                if(preference.getKey().equals("Sobel"))
                    value = 2;

                else
                if(preference.getKey().equals("Scharr"))
                    value = 3;

                pref.edit().putInt("edge_operator", value).commit();

                Roberts.setChecked(pref.getInt("edge_operator", 1) == 0);
                Prewitt.setChecked(pref.getInt("edge_operator", 1) == 1);
                Sobel.setChecked(pref.getInt("edge_operator", 1) == 2);
                Scharr.setChecked(pref.getInt("edge_operator", 1) == 3);

                return true;
            }
        };
        Roberts.setOnPreferenceChangeListener(operator);
        Prewitt.setOnPreferenceChangeListener(operator);
        Sobel.setOnPreferenceChangeListener(operator);
        Scharr.setOnPreferenceChangeListener(operator);



        //-------------------------------------------------------

        final CheckBoxPreference colorEdges = (CheckBoxPreference) this.getPreferenceManager().findPreference("colorEdges");
        final CheckBoxPreference grayEdges = (CheckBoxPreference) this.getPreferenceManager().findPreference("grayEdges");
        final CheckBoxPreference substractEdges = (CheckBoxPreference) this.getPreferenceManager().findPreference("substractEdges");

        colorEdges.setChecked(pref.getInt("edge_filter", 1) == 0);
        grayEdges.setChecked(pref.getInt("edge_filter", 1) == 1);
        substractEdges.setChecked(pref.getInt("edge_filter", 1) == 2);

        Preference.OnPreferenceChangeListener filters = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean allfalse = false;

                if(preference.getKey().equals("colorEdges"))
                    allfalse =  !grayEdges.isChecked() && !substractEdges.isChecked() && !((Boolean) newValue);

                else
                if(preference.getKey().equals("grayEdges"))
                    allfalse =  !colorEdges.isChecked() && !substractEdges.isChecked()&& !((Boolean) newValue);

                else
                if(preference.getKey().equals("substractEdges"))
                    allfalse =  !colorEdges.isChecked() && !grayEdges.isChecked() && !((Boolean) newValue);

                if(allfalse)
                    return false;

                int value = 1;
                if(preference.getKey().equals("colorEdges"))
                    value = 0;

                else
                if(preference.getKey().equals("grayEdges"))
                    value = 1;

                else
                if(preference.getKey().equals("substractEdges"))
                    value = 2;

                pref.edit().putInt("edge_filter", value).commit();

                colorEdges.setChecked(pref.getInt("edge_filter", 1) == 0);
                grayEdges.setChecked(pref.getInt("edge_filter", 1) == 1);
                substractEdges.setChecked(pref.getInt("edge_filter", 1) == 2);

                return true;
            }
        };
        colorEdges.setOnPreferenceChangeListener(filters);
        grayEdges.setOnPreferenceChangeListener(filters);
        substractEdges.setOnPreferenceChangeListener(filters);

    }


}
