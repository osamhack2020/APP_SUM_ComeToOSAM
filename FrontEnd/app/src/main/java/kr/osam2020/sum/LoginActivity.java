package kr.osam2020.sum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText gunbunEdit, passwordEdit;
    Button loginButton, registerButton;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        // Firebase Auth
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init
        gunbunEdit = findViewById(R.id.gunbunEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        // Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String gunbun_text = gunbunEdit.getText().toString();
                String pass_text = passwordEdit.getText().toString();

                if (TextUtils.isEmpty(gunbun_text) || TextUtils.isEmpty(pass_text)) {
                    Toast.makeText(getApplication(), "양식을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    auth.signInWithEmailAndPassword(gunbun_text + "@" + getString(R.string.email), pass_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UpdateUidThread updateUidThread = new UpdateUidThread();
                                updateUidThread.run();
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    class UpdateUidThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.0.8:8888/addUid");

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("my_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                Intent i = getIntent();
                builder2.add("gunbun", gunbunEdit.getText().toString());

                FormBody formBody = builder2.build();
                builder = builder.post(formBody);

                Request request = builder.build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.code() != 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                }
                            });
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String responseStr = response.body().string();
                                    Log.d("BSJ", responseStr);

                                    if (!responseStr.isEmpty()) {
                                        Toast.makeText(getApplicationContext(),
                                                "등록된 군번이 아닙니다.\n관리자에게 문의하십시오.", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                    else {
                                        Intent i = new Intent(getApplication(), MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                }
                            }
                        });
                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                });
            }
        }
    }
}
