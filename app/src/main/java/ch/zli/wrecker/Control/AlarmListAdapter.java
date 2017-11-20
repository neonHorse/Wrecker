package ch.zli.wrecker.Control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.zli.wrecker.Model.Alarm;
import ch.zli.wrecker.R;

/**
 * Created by admin on 07.11.2017.
 */

public class AlarmListAdapter extends BaseAdapter{
    private List<Alarm> data = null;
    private LayoutInflater inflater;

    public AlarmListAdapter(Context context, List<Alarm> data){
       this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            view = inflater.inflate(R.layout.list_item_alarm, null);
            holder = new ViewHolder();

            holder.lblTime = view.findViewById(R.id.lblTime);
            holder.lblDays = view.findViewById(R.id.lblDays);
            holder.switchActive = view.findViewById(R.id.switchActive);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        final Alarm a = (Alarm) getItem(i);

        String time = a.getHour() + ":" + a.getMinute();
        String days = "";

        if(a.getRecurrences().isEmpty()){
            days = "Einmalig";
        }
        else {
            for (Integer day : a.getRecurrences()){
                switch (day){
                    case 1:
                        days += "So,";
                        break;

                    case 2:
                        days += "Mo,";
                        break;

                    case 3:
                        days += "Di,";
                        break;

                    case 4:
                        days += "Mi,";
                        break;

                    case 5:
                        days += "Do,";
                        break;

                    case 6:
                        days += "Fr,";
                        break;

                    case 7:
                        days += "Sa,";
                        break;
                }
            }

            days = days.substring(0, days.lastIndexOf(","));
        }

        holder.lblTime.setText(time);
        holder.lblDays.setText(days);

        holder.switchActive.setChecked(a.isActive());

        holder.switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmHandler.getInstance().setActive(a, b);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView lblTime;
        TextView lblDays;

        Switch switchActive;
    }
}
