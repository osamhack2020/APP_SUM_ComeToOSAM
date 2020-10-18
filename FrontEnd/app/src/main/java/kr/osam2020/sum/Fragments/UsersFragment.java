package kr.osam2020.sum.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
    private RadioButton intimacyRadio, expertRadio, complexRadio;
    private ProgressBar progressBar;
    private LinearLayout initLayout;
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

        initLayout = v.findViewById(R.id.initLayout);
        if (category == 0)
            initLayout.setVisibility(View.VISIBLE);
        else
            initLayout.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progressBarWaiting);
        progressBar.setVisibility(View.GONE);
        categorySpinner = v.findViewById(R.id.categorySpinner);
        intimacyRadio = v.findViewById(R.id.radioButton);
        expertRadio = v.findViewById(R.id.radioButton2);
        complexRadio = v.findViewById(R.id.radioButton3);
        indexGroup = v.findViewById(R.id.indexRadio);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (category == position)
                    return;
                category = position;

                if (category > 0) {
                    // TODO
                    readUsers();
                    initLayout.setVisibility(View.GONE);
                } else {
                    mUsers.clear();
                    userAdapter = new UserAdapter(getContext(), mUsers);
                    recyclerView.setAdapter(userAdapter);
                    initLayout.setVisibility(View.VISIBLE);
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
                int tempIndex = 1;
                intimacyRadio.setTextColor(Color.parseColor("#000000"));
                expertRadio.setTextColor(Color.parseColor("#000000"));
                complexRadio.setTextColor(Color.parseColor("#000000"));

                switch (checkedId) {
                    case R.id.radioButton:
                        tempIndex = 1;
                        intimacyRadio.setTextColor(Color.parseColor("#045e3d"));
                        break;
                    case R.id.radioButton2:
                        tempIndex = 2;
                        expertRadio.setTextColor(Color.parseColor("#8f004a"));
                        break;
                    case R.id.radioButton3:
                        tempIndex = 3;
                        complexRadio.setTextColor(Color.parseColor("#003091"));
                        break;
                }

                if (index == tempIndex)
                    return;
                index = tempIndex;

                if (category > 0) {
                    // TODO
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
                progressBar.setVisibility(View.VISIBLE);
                mUsers.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Users user = s.getValue(Users.class);

                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
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

    @Override
    public void onResume() {
        super.onResume();

        if (userAdapter != null) {
            userAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(userAdapter);
        }
    }
}
