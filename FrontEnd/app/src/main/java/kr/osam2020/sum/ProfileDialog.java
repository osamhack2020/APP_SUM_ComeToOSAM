package kr.osam2020.sum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
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

import kr.osam2020.sum.Model.AssociationMatrix;
import kr.osam2020.sum.Model.Users;
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

    public void loadingDialog(Users users) {
        this.uid = users.getId();
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
        progressBar.setVisibility(View.GONE);
        textTag.setVisibility(View.VISIBLE);

        //int px = (int)convertDpToPixel(750 / 400);
        MultiTransformation multiTransformation =
                new MultiTransformation( new CenterCrop(),
                        new RoundedCorners(100) );

        // Glide.with(activity).load(imageURL).into(profileImage);
        if (users.getImageURL().equals("default")) {
            Glide.with(activity).load(R.drawable.profile_2)
                    //.circleCrop()
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(profileImage);
        } else {
            Glide.with(activity).load(users.getImageURL())
                    //.circleCrop()
                    .override(400, 400)
                    .apply(new RequestOptions().bitmapTransform(multiTransformation))
                    .into(profileImage);
        }

        usernameText.setText(users.getUsername());
        introductionText.setText(users.getIntroduction());

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

        String intimacyResult = users.getIntimacyResultVector();
        String expertResult = users.getExpertResultVector();

        textTag.setText("관계도 : \n");
        for (int i=0; i<intimacyResult.length(); i++) {
            if (intimacyResult.charAt(i) == '1') {
                switch (i) {
                    case 0:
                        textTag.append("#보직소속 ");
                        break;
                    case 1:
                        textTag.append("#보직직무 ");
                        break;
                    case 2:
                        textTag.append("#군내교육기관 ");
                        break;
                    case 3:
                        textTag.append("#군내교육기수 ");
                        break;
                    case 4:
                        textTag.append("#민간학교 ");
                        break;
                    case 5:
                        textTag.append("#민간학위 ");
                        break;
                    case 6:
                        textTag.append("#군경력 ");
                        break;
                    case 7:
                        textTag.append("#군병과 ");
                        break;
                    case 8:
                        textTag.append("#개인성향 ");
                        break;
                    case 9:
                        textTag.append("#개인관계 ");
                        break;
                }
            }
        }
        textTag.append("\n전문가 : \n");
        for (int i=0; i<expertResult.length(); i++) {
            if (expertResult.charAt(i) == '1') {
                String tempTag = "";
                int index = i / AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT;
                switch(index) {
                    case 0:
                        tempTag = "#언어";
                        break;
                    case 1:
                        tempTag = "#전투능력";
                        break;
                    case 2:
                        tempTag = "#전산";
                        break;
                    case 3:
                        tempTag = "#행정";
                        break;
                    case 4:
                        tempTag = "#법";
                        break;
                }

                switch(i % AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT) {
                    case 0:
                        tempTag += "보직 ";
                        break;
                    case 1:
                        tempTag += "교육 ";
                        break;
                    case 2:
                        tempTag += "경력 ";
                        break;
                    case 3:
                        tempTag += "실적 ";
                        break;
                }
                textTag.append(tempTag);
            }
        }
        textTag.setMovementMethod(new ScrollingMovementMethod());
    }

    void dismissDialog() {
        alertDialog.dismiss();
    }

}
