package kr.osam2020.sum.Adapter;

import android.app.Activity;
import android.content.Context;
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

import kr.osam2020.sum.Model.Users;
import kr.osam2020.sum.ProfileDialog;
import kr.osam2020.sum.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers ;

    public UserAdapter(Context context, List<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());
        holder.introduction.setText(users.getIntroduction());

        MultiTransformation multiTransformation =
                new MultiTransformation( new CenterCrop(),
                        new RoundedCorners(100) );
        if (users.getImageURL().equals("default")) {
            //holder.imageView.setImageResource(R.mipmap.ic_launcher);
            Glide.with(context).load(R.drawable.profile)
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(holder.imageView);
        } else {
            Glide.with(context).load(users.getImageURL())
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(holder.imageView);
        }

        final ProfileDialog profileDialog = new ProfileDialog((Activity)context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userid", users.getId());
                context.startActivity(i);*/
                profileDialog.loadingDialog(users);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView introduction;
        public ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.nameText);
            imageView = itemView.findViewById(R.id.profileImage);
            introduction = itemView.findViewById(R.id.introductionText);
        }
    }
}
