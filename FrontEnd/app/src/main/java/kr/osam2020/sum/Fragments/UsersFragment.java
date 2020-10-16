package kr.osam2020.sum.Fragments;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.osam2020.sum.Adapter.UserAdapter;
import kr.osam2020.sum.Model.Users;
import kr.osam2020.sum.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mUsers;
    private Spinner categorySpinner;
    private RadioGroup indexGroup;
    private ProgressBar progressBar;
    private int category = 0;
    private int index = 1;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Drawable divider = ContextCompat.getDrawable(v.getContext(), R.drawable.divider);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(v.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(divider);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));

        mUsers = new ArrayList<>();

        // readUsers();

        progressBar = v.findViewById(R.id.progressBarWaiting);
        progressBar.setVisibility(View.GONE);
        categorySpinner = v.findViewById(R.id.categorySpinner);
        indexGroup = v.findViewById(R.id.indexRadio);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO
                progressBar.setVisibility(View.VISIBLE);
                category = position;

                if (category == 0)
                    readUsers();
                else {
                    ReadThread readThread = new ReadThread();
                    readThread.run();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        indexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO
                progressBar.setVisibility(View.VISIBLE);

                switch (checkedId) {
                    case R.id.radioButton:
                        index = 1;
                    case R.id.radioButton2:
                        index = 2;
                    case R.id.radioButton3:
                        index = 3;
                }

                if (category == 0)
                    readUsers();
                else {
                    ReadThread readThread = new ReadThread();
                    readThread.run();
                }
            }
        });

        return v;
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Users user = s.getValue(Users.class);

                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers);
                    recyclerView.setAdapter(userAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void readUsers(final ArrayList<String> uids) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for (String uid : uids) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Users user = s.getValue(Users.class);

                        assert user != null;
                        if (!user.getId().equals(firebaseUser.getUid()) && user.getId().equals(uid)) {
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.0.8:8888/find");

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("my_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                builder2.add("category", "" + category);
                builder2.add("index", "" + index);

                FormBody formBody = builder2.build();
                builder = builder.post(formBody);

                Request request = builder.build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.code() != 200) {
                            ((Activity)getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            return;
                        }
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray jsonArray = new JSONArray(response.body().string());
                                    Log.d("BSJ", jsonArray.toString());
                                    ArrayList<String> uids = new ArrayList<>();
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        uids.add(jsonObject.getString("uid"));
                                    }
                                    readUsers(uids);
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            } catch (Exception e) {
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "네트워크에 문제가 생겼습니다.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}
