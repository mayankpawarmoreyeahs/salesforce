package demo.recycle.com.salforceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.pojo.contactpojo;

/**
 * Created by user on 9/27/2017.
 */
public class SearchCustomContactAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<contactpojo> originalList;
    private List<contactpojo> suggestions = new ArrayList<>();
    private Filter filter = new SearchCustomContactAdapter.CustomFilter();

    public SearchCustomContactAdapter(Context context, List<contactpojo> originalList) {
        this.mContext = context;
        this.originalList = originalList;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public SearchCustomContactAdapter getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchCustomContactAdapter.ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_list_row, viewGroup, false);
            holder = new SearchCustomContactAdapter.ViewHolder();
            holder.autoText = ((TextView) view.findViewById(R.id.textview));
            holder.Id = (TextView) view.findViewById(R.id.textviewId);
            view.setTag(holder);
        } else {
            holder = (SearchCustomContactAdapter.ViewHolder) view.getTag();
        }
        holder.autoText.setText(suggestions.get(i).getName());
        holder.Id.setText(suggestions.get(i).getId());
        return view;

    }

    private static class ViewHolder {
        TextView autoText;
        TextView Id;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();
            // Check if the Original List and Constraint aren't null.
            if (originalList != null && constraint != null) {
                for (int i = 0; i < originalList.size(); i++) {
                    // Compare item in original list if it contains constraints.
                    if (originalList.get(i).getName().toLowerCase().contains(constraint)) {
                        // If TRUE add item in Suggestions.
                        suggestions.add(originalList.get(i));
                    }
                }
            }
            // Create new Filter Results and return this to publishResults;
            FilterResults results = new FilterResults();
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
