package kr.osam2020.sum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileDialog {
    Activity activity;
    AlertDialog alertDialog;
    String my_uid;
    String uid;
    TextView textTag;
    ProgressBar progressBar;

    public ProfileDialog(Activity activity) {
        this.activity = activity;
    }

    public void loadingDialog(String imageURL, String username, String introduction, final String uid) {
        this.uid = uid;
        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.dialog_profile, null);
        builder.setView(v);
        builder.setCancelable(true);

        ImageView profileImage = v.findViewById(R.id.imageProfile);
        TextView usernameText = v.findViewById(R.id.textUsername);
        TextView introductionText = v.findViewById(R.id.textIntroduction);

        textTag = v.findViewById(R.id.textTag);
        progressBar = v.findViewById(R.id.progressBarWaiting);
        textTag.setVisibility(View.INVISIBLE);

        //int px = (int)convertDpToPixel(750 / 400);
        MultiTransformation multiTransformation =
                new MultiTransformation( new CenterCrop(),
                        new RoundedCorners(100) );

        // Glide.with(activity).load(imageURL).into(profileImage);
        if (imageURL.equals("default")) {
            Glide.with(activity).load(R.drawable.profile_2)
                    //.circleCrop()
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(profileImage);
        } else {
            Glide.with(activity).load(imageURL)
                    //.circleCrop()
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(profileImage);
        }

        usernameText.setText(username);
        introductionText.setText(introduction);

        Button talkButton = v.findViewById(R.id.buttonTalk);
        Button closeButton = v.findViewById(R.id.buttonClose);
        talkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MessageActivity.class);
                i.putExtra("userid", uid);
                activity.startActivity(i);
                dismissDialog();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ReadThread readThread = new ReadThread();
        readThread.run();
    }

    void dismissDialog() {
        alertDialog.dismiss();
    }

    class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.0.8:8888/profile");

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("my_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                builder2.add("uid", uid);

                FormBody formBody = builder2.build();
                builder = builder.post(formBody);

                Request request = builder.build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textTag.setText("네트워크에 문제가 생겼습니다.");
                                textTag.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.code() != 200) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textTag.setText("네트워크에 문제가 생겼습니다.");
                                    textTag.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    textTag.setText(response.body().string());
                                } catch (IOException e) {
                                    textTag.setText("네트워크에 문제가 생겼습니다.");
                                }
                                textTag.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            } catch (Exception e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textTag.setText("네트워크에 문제가 생겼습니다.");
                        textTag.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    /*
    // DP -> PX
    public float convertDpToPixel(float dp){
        Resources resources = activity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
     */
}
