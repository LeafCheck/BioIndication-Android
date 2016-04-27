package com.example.skoml.bioindication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryFragment extends Fragment {

    private View parentView;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.history, container, false);
        listView = (ListView) parentView.findViewById(R.id.listView);
        initView();
        return parentView;
    }

    private void initView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getCalendarData());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<String> getCalendarData() {
        Format dateFormat = DateFormat.getDateFormat(getActivity());
        CharSequence pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();


        ArrayList<String> calendarList = new ArrayList<String>();

        for (int i = 0; i < 25; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(c.getTime().getTime() - i * 1000 * 60 * 60 * 24));
            calendarList.add(DateFormat.format(pattern, c).toString());
        }
        return calendarList;
    }
}
