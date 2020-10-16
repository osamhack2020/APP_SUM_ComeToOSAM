package kr.osam2020.sum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class PhotoViewActivity extends AppCompatActivity {
    PhotoView photoView;
    Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        photoView = findViewById(R.id.photoView);
        closeButton = findViewById(R.id.closeButton);
        /*
        SharedPreferences prefs = this.getSharedPreferences("PICVIEW", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String temp = prefs.getString("PICVIEW", "");
        byte[] byteArray = Base64.decode(temp, 0);
        editor.clear();
        editor.commit();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
         */
        Intent i = getIntent();
        Glide.with(this).load(i.getStringExtra("imageURL")).into(photoView);

        closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }
}
