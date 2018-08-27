package ir.iot.smartremote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.ArrayList;

/**
 * Created by Dreamz on 27-08-2018.
 */

public class ChannelRecyclerAdapter extends RecyclerView.Adapter<ChannelRecyclerAdapter.ChannelViewHolder> implements View.OnClickListener {
    Context context;
    ArrayList<Channel> channelsList;

    public class ChannelViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView albumArt;
        public RelativeLayout cardParent;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textview_album_title);
            albumArt = (ImageView) itemView.findViewById(R.id.imageview_album_art);
            cardParent = (RelativeLayout) itemView.findViewById(R.id.text_card_background);
        }
    }


    public ChannelRecyclerAdapter(Context context, ArrayList<Channel> channelsList) {
        this.context = context;
        this.channelsList = channelsList;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_item_layout,parent,false);
        itemView.setOnClickListener(this);
        return new ChannelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Channel channel = channelsList.get(position);

        holder.title.setText(channel.getTitle());
        Glide.with(context).load(channel.getImageUrl())
                .listener(GlidePalette.with(channel.getImageUrl())
                        .use(GlidePalette.Profile.VIBRANT)
                        .intoBackground(holder.cardParent, GlidePalette.Swatch.RGB)
                        .crossfade(true)
                )
                .into((holder.albumArt));
    }

    @Override
    public int getItemCount() {
        return channelsList.size();
    }

    @Override
    public void onClick(View v) {

    }
}
