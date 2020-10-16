package kr.osam2020.sum.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import kr.osam2020.sum.MessageActivity;
import kr.osam2020.sum.Model.Chatlist;
import kr.osam2020.sum.R;

public class ChatlistAdapter extends RecyclerView.Adapter<ChatlistAdapter.ViewHolder> {
    private Context context;
    private List<Chatlist> mChatlists ;

    public ChatlistAdapter(Context context, List<Chatlist> mChatlists) {
        this.context = context;
        this.mChatlists = mChatlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatlist_item, parent, false);
        return new ChatlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Chatlist chatlists = mChatlists.get(position);
        holder.username.setText(chatlists.getUsername());
        holder.dateText.setText("마지막 대화 : \n" + chatlists.getTimestamp());
        if (chatlists.getIsRead().equals("FALSE"))
            holder.isRead.setVisibility(View.VISIBLE);
        else
            holder.isRead.setVisibility(View.GONE);

        MultiTransformation multiTransformation =
                new MultiTransformation( new CenterCrop(),
                        new RoundedCorners(100) );
        if (chatlists.getImageURL().equals("default")) {
            //holder.imageView.setImageResource(R.mipmap.ic_launcher);
            Glide.with(context).load(R.drawable.profile)
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(holder.imageView);
        } else {
            Glide.with(context).load(chatlists.getImageURL())
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userid", chatlists.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView isRead;
        public TextView dateText;
        public ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.nameText);
            isRead = itemView.findViewById(R.id.isReadText);
            imageView = itemView.findViewById(R.id.profileImage);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}
