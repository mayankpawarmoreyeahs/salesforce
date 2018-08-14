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
import demo.recycle.com.salforceapp.pojo.Taskpojo;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

   public ViewClick viewclick;


 public interface ViewClick
    {

        public void move(int position,String Type);



    }


    public void setViewClick(ViewClick viewClick)

    {
        this.viewclick=viewClick;

    }

    Context mContext;
    LayoutInflater inflater;
    private List<Taskpojo> personNamesList = null;
    private ArrayList<Taskpojo> arraylist;
    String Type;

    public ListViewAdapter(Context context, List<Taskpojo> personNamesList,String Type) {
        mContext = context;
        this.personNamesList = personNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Taskpojo>();
        this.arraylist.addAll(personNamesList);
        this.Type=Type;
    }

    public class ViewHolder {
        TextView name;
        TextView date;
    }

    @Override
    public int getCount() {
        return personNamesList.size();
    }

    @Override
    public Taskpojo getItem(int position) {
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
            view = inflater.inflate(R.layout.listview_event, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(personNamesList.get(position).getTaskName());
        holder.date.setText(personNamesList.get(position).getEventDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewclick.move(position,Type);
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        personNamesList.clear();
        if (charText.length() == 0) {
            personNamesList.addAll(arraylist);
        } else {
            for (Taskpojo wp : arraylist) {
                if (wp.getTaskName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    personNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}