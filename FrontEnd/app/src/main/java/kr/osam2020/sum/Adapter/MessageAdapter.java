package kr.osam2020.sum.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

import kr.osam2020.sum.Model.Chat;
import kr.osam2020.sum.PhotoViewActivity;
import kr.osam2020.sum.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Chat> mChat ;

    final static int MSG_TYPE_LEFT = 0;
    final static int MSG_TYPE_RIGHT = 1;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> mChat) {
        this.context = context;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);

        if (chat.getIsImage().equals("FALSE")) {
            holder.showMessage.setText(chat.getMessage());
        }
        else {
            final ImageView chatImage = holder.chatImage;
            holder.showMessage.setVisibility(View.GONE);
            chatImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(chat.getMessage())
                    .centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            chatImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /*
                                    BitmapDrawable drawable = (BitmapDrawable) chatImage.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                     */
                                    Intent i = new Intent(context, PhotoViewActivity.class);
                                    i.putExtra("imageURL", chat.getMessage());
                                    /*
                                    SharedPreferences prefs = context.getSharedPreferences("PICVIEW", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    editor.putString("PICVIEW", encodedImage);
                                    editor.commit();
                                    */
                                    context.startActivity(i);
                                }
                            });
                            return false;
                        }
                    })
                    .into(chatImage);
        }
        holder.dateText.setText(chat.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public TextView dateText;
        public ImageView chatImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.showMessage);
            dateText = itemView.findViewById(R.id.dateText);
            chatImage = itemView.findViewById(R.id.chatImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
