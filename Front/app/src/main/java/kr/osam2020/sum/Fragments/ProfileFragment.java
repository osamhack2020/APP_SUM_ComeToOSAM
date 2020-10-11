package kr.osam2020.sum.Fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import kr.osam2020.sum.Model.Users;
import kr.osam2020.sum.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText usernameEdit;
    EditText introductionEdit;
    Button modifyButton;
    ImageView profileImage;
    ProgressBar progressBar;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask storageTask;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = v.findViewById(R.id.profileImage);
        usernameEdit = v.findViewById(R.id.usernameEdit);
        introductionEdit = v.findViewById(R.id.introductionEdit);
        modifyButton = v.findViewById(R.id.modificationButton);
        progressBar = v.findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);

        storageReference = FirebaseStorage.getInstance().getReference("profiles");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded())
                    return;

                Users users = snapshot.getValue(Users.class);
                usernameEdit.setText(users.getUsername());
                introductionEdit.setText(users.getIntroduction());

                if (users.getImageURL().equals("default")) {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(users.getImageURL())
                            .circleCrop()
                            .into(profileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, IMAGE_REQUEST);
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String introduction = introductionEdit.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(getContext(), "이름을 입력해주세요.", Toast.LENGTH_LONG);
                }

                String userid = firebaseUser.getUid();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("introduction", introduction);

                databaseReference.updateChildren(hashMap);
            }
        });

        return v;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadMyImage() {
        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            byte[] data;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                int MAX_IMAGE_SIZE = 1024 * 1024;
                int streamLength = MAX_IMAGE_SIZE;
                int compressQuality = 100;
                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 5) {
                    bmpStream.flush();//to avoid out of memory error
                    bmpStream.reset();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                    compressQuality -= 5;
                    if (bmpStream.size() <= 950 * 950)
                        break;
                }
                data = bmpStream.toByteArray();
            } catch (Exception e) {
                Log.d("BSJ", e.toString());
                return;
            }
            //storageTask = fileReference.putFile(imageUri);
            storageTask = fileReference.putBytes(data);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

                        HashMap<String, Object> map = new HashMap<>();

                        map.put("imageURL", mUri);
                        databaseReference.updateChildren(map);

                        progressBar.setVisibility(View.GONE);
                    }  else {
                        Toast.makeText(getContext(), "이미지 업로드에 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(getContext(), "이미지를 선택해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK &&
        data != null && data.getData() != null) {
            imageUri = data.getData();

            if (storageTask != null && storageTask.isInProgress()) {
                Toast.makeText(getContext(), "이미지 업로드가 진행 중입니다.", Toast.LENGTH_LONG).show();
            } else {
                UploadMyImage();
            }
        }
    }
}
