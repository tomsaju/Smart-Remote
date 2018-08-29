package ir.iot.smartremote.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.ArrayList;
import java.util.List;

import ir.iot.smartremote.Channel;
import ir.iot.smartremote.R;

/**
 * Created by tom.saju on 8/29/2018.
 */

public class AllChannelAdapter extends BaseAdapter implements Filterable {
    ArrayList<Channel> channelList;
    Context context;
    allChannelListClickListener mListener;
    private ArrayList<Channel> filteredData;
    private ItemFilterByName mFilter = new ItemFilterByName();
    public AllChannelAdapter(ArrayList<Channel> channelList, Context context, allChannelListClickListener mListener) {
        this.channelList = channelList;
        this.context = context;
        this.mListener = mListener;
        this.filteredData = channelList;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredData.get(position).getIndex();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView logo;
        TextView title;
        RelativeLayout parentLayout;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channle_list_item_listview, parent, false);
        }
        logo = (ImageView) convertView.findViewById(R.id.channel_logo);
        title = (TextView) convertView.findViewById(R.id.channel_title);
        parentLayout = (RelativeLayout) convertView.findViewById(R.id.parent_list_item);

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCLicked(filteredData.get(position));
            }
        });
        title.setText(filteredData.get(position).getTitle());

        Glide.with(context).load(filteredData.get(position).getImageUrl()).into(logo);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface allChannelListClickListener{
        void onCLicked(Channel channel);
    }

    private class ItemFilterByName extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if(constraint.toString().equalsIgnoreCase("All channels")){
                FilterResults results = new FilterResults();
                results.values = channelList;
                results.count = channelList.size();

                return results;
            }

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<Channel> list = channelList;

                int count = list.size();
                final ArrayList<Channel> nlist = new ArrayList<Channel>(count);

                String title;
                String category;
                String language;
                for (int i = 0; i < count; i++) {
                    title = list.get(i).getTitle();
                    category = list.get(i).getCategory();
                    language = list.get(i).getLanguage();
                    if (title.toLowerCase().contains(filterString) || category.toLowerCase().contains(filterString) || language.toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {



                if (results.count > 0) {
                    filteredData = (ArrayList<Channel>) results.values;
                } else {
                    filteredData = new ArrayList<>();
                }

            notifyDataSetChanged();
        }

    }

}

