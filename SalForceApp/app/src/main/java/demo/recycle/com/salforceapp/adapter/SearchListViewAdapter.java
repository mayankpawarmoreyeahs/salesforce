package demo.recycle.com.salforceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.pojo.PersonNames;

public class SearchListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<PersonNames> personNamesList = null;
    private ArrayList<PersonNames> arraylist;

    public SearchListViewAdapter(Context context, List<PersonNames> personNamesList) {
        mContext = context;
        this.personNamesList = personNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PersonNames>();
        this.arraylist.addAll(personNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return personNamesList.size();
    }

    @Override
    public PersonNames getItem(int position) {
        return personNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(personNamesList.get(position).getPersonName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        personNamesList.clear();
        if (charText.length() == 0) {
            personNamesList.addAll(arraylist);
        } else {
            for (PersonNames wp : arraylist) {
                if (wp.getPersonName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    personNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}