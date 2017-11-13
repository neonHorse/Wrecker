package ch.zli.wrecker.Control;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.zli.wrecker.Model.Song;
import ch.zli.wrecker.R;

/**
 * Created by admin on 13.11.2017.
 */

public class SongListAdapter extends BaseAdapter implements Filterable {
    private List<Song> originalData = null;
    private List<Song> filteredData = null;
    private LayoutInflater inflater;
    private ItemFilter itemFilter = new ItemFilter();

    public SongListAdapter(Context context, List<Song> data){
        originalData = data;
        filteredData = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            view = inflater.inflate(R.layout.list_item_song, null);
            holder = new ViewHolder();

            holder.lblSongName = (TextView)view.findViewById(R.id.lblSongName);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        Song s = (Song)getItem(i);

        holder.lblSongName.setText(s.getName());

        return view;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ViewHolder{
        TextView lblSongName;
    }

    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filterString = charSequence.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<Song> list = originalData;

            int count = list.size();
            final ArrayList<Song> nList = new ArrayList<>(count);

            for (int i = 0; i < count; i++){
                Song s = list.get(i);
                String searchable = s.getName();

                if(searchable.toLowerCase().contains(filterString)){
                    nList.add(s);
                }
            }

            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredData = (ArrayList<Song>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}
