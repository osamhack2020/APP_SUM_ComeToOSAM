package kr.osam2020.sum;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.osam2020.sum.Adapter.MessageAdapter;
import kr.osam2020.sum.Fragments.APIService;
import kr.osam2020.sum.Model.Chat;
import kr.osam2020.sum.Model.Users;
import kr.osam2020.sum.Notification.Client;
import kr.osam2020.sum.Notification.Data;
import kr.osam2020.sum.Notification.MyResponse;
import kr.osam2020.sum.Notification.Sender;
import kr.osam2020.sum.Notification.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask storageTask;
    StorageReference storageReference;

    TextView username;
    TextView introduction;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText sendText;
    Button sendButton;
    Button sendImageButton;

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    String userid;
    APIService apiService;
    boolean notify = false;
    boolean isRunning = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        username = findViewById(R.id.usernameText);
        introduction = findViewById(R.id.introductionText);
        imageView = findViewById(R.id.profileImage);

        sendImageButton = findViewById(R.id.sendImageButton);
        sendButton = findViewById(R.id.sentButton);
        sendText = findViewById(R.id.sendText);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        intent = getIntent();
        userid = intent.getStringExtra("userid");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUsername());
                introduction.setText(users.getIntroduction());

                if (users.getImageURL().equals("default")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(users.getImageURL())
                            .circleCrop()
                            .into(imageView);
                }

                readMessage(fuser.getUid(), userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendText.getText().toString();
                notify = true;

                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(), userid, msg, "FALSE");
                } else {
                    Toast.makeText(MessageActivity.this, "메시지를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("chatImages");
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, IMAGE_REQUEST);
            }
        });
    }

    private void sendMessage(String sender, final String receiver, final String message, String isImage) {
        final String chatName;
        if (sender.compareTo(receiver) > 0) {
            chatName = receiver + sender;
        } else {
            chatName = sender + receiver;
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatName);

        SimpleDateFormat formatDate = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");
        Date time = new Date();
        final String finalTime = formatDate.format(time);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("timestamp", finalTime);
        hashMap.put("isImage", isImage);

        reference.push().setValue(hashMap);
        sendText.setText("");

        final DatabaseReference chatRef = FirebaseDatabase.getInstance()
                .getReference("Chatlist").child(fuser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("timestamp").setValue(finalTime);
                    chatRef.child("isRead").setValue("TRUE");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        hashMap = new HashMap<>();
        hashMap.put("timestamp", finalTime);
        hashMap.put("isRead", "TRUE");
        chatRef.updateChildren(hashMap);

        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance()
                .getReference("Chatlist").child(userid).child(fuser.getUid());
        chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef2.child("timestamp").setValue(finalTime);
                    chatRef2.child("isRead").setValue("FALSE");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        hashMap = new HashMap<>();
        hashMap.put("timestamp", finalTime);
        hashMap.put("isRead", "FALSE");
        chatRef2.updateChildren(hashMap);

        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (notify) {
                    sendNotification(receiver, user.getUsername(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String msg) {
        Log.d("BSJ", "Sending Notification");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = databaseReference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("BSJ", "Sending Notification (On DataChange Query...)");
                for (DataSnapshot s : snapshot.getChildren()) {
                    Token token = s.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username+": "+msg,
                            "새로운 메시지가 도착했습니다.", userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Log.d("BSJ", "Failed To Send Notification");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(final String myid, final String userid) {
        final String chatName;
        if (myid.compareTo(userid) > 0) {
            chatName = userid + myid;
        } else {
            chatName = myid + userid;
        }

        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats").child(chatName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();

                if (snapshot.hasChildren()) {
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance()
                            .getReference("Chatlist").child(myid).child(userid);
                    HashMap hashMap = new HashMap<>();
                    Log.d("BSJ", "Read Message...");
                    if (isRunning)
                        hashMap.put("isRead", "TRUE");
                    chatRef.updateChildren(hashMap);
                }

                for (DataSnapshot s : snapshot.getChildren()) {
                    Chat chat = s.getValue(Chat.class);
                    mChat.add(chat);

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadMyImage() {
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            byte[] data;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                int MAX_IMAGE_SIZE = 1024 * 1024;
                int streamLength = MAX_IMAGE_SIZE;
                int compressQuality = 100;
                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                while (streamLength >= MAX_IMAGE_SIZE && compressQuality >= 5) {
                    bmpStream.flush();//to avoid out of memory error
                    bmpStream.reset();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                    compressQuality -= 10;
                    byte[] bmpPicByteArray = bmpStream.toByteArray();
                    streamLength = bmpPicByteArray.length;
                }
                data = bmpStream.toByteArray();
            } catch (Exception e) {
                Log.d("BSJ", e.toString());
                return;
            }

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
                        // Firebase Update
                        sendMessage(fuser.getUid(), userid, mUri, "TRUE");
                    }  else {
                        Toast.makeText(getApplicationContext(), "이미지 업로드에 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            imageUri = data.getData();

            if (storageTask != null && storageTask.isInProgress()) {
                Toast.makeText(this, "이미지 업로드가 진행 중입니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "이미지 전송을 시작합니다.", Toast.LENGTH_LONG).show();
                UploadMyImage();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }
}
