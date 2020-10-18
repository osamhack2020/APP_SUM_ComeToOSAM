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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.osam2020.sum.Adapter.UserAdapter;
import kr.osam2020.sum.MainActivity;
import kr.osam2020.sum.Model.AssociationMatrix;
import kr.osam2020.sum.Model.IndexExpert;
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
    private List<Users> listUsers;
    private Spinner categorySpinner;
    private RadioGroup indexGroup;
    private RadioButton intimacyRadio, expertRadio, complexRadio;
    private ProgressBar progressBar;
    private LinearLayout initLayout;
    private int category = 0;
    private int index = 1;
    private FirebaseFunctions mFunctions;

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
        listUsers = new ArrayList<>();

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
                    updateList();
                    initLayout.setVisibility(View.GONE);
                } else {
                    listUsers.clear();
                    userAdapter = new UserAdapter(getContext(), listUsers);
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
                    updateList();
                }
            }
        });

        return v;
    }

    private void updateList() {
        if (mUsers.size() == 0 && progressBar.getVisibility()==View.GONE)
            readUsers();
        else if (mUsers.size() != 0)
            updateView();

    }

    private void updateView() {
        String associationVector;
        String associationVectorExpert;

        if (category == 1) {
            associationVector = AssociationMatrix.LANGUAGE;
            associationVectorExpert = AssociationMatrix.LANGUAGE_EXPERT;
        } else if (category == 2) {
            associationVector = AssociationMatrix.COMBAT;
            associationVectorExpert = AssociationMatrix.COMBAT_EXPERT;
        } else if (category == 3) {
            associationVector = AssociationMatrix.COMPUTER;
            associationVectorExpert = AssociationMatrix.COMPUTER_EXPERT;
        } else if (category == 4) {
            associationVector = AssociationMatrix.ADMIN;
            associationVectorExpert = AssociationMatrix.ADMIN_EXPERT;
        } else {
            associationVector = AssociationMatrix.LAW;
            associationVectorExpert = AssociationMatrix.LAW_EXPERT;
        }

        for (Users users : mUsers) {
            users.setAssociationVector(associationVector);
            users.setAssociationVectorExpert(associationVectorExpert);

            users.setIntimacyScore();
            users.setExpertScore();
            users.setMixScore();

            if (index == 1) {
                users.setRepresentationScore(users.getIntimacyScore());
            } else if (index == 2) {
                users.setRepresentationScore(users.getExpertScore());
            } else {
                users.setRepresentationScore(users.getMixScore());
            }
        }

        Collections.sort(mUsers, new Comparator<Users>() {
            @Override
            public int compare(Users o1, Users o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        Collections.sort(mUsers, new Comparator<Users>() {
            @Override
            public int compare(Users o1, Users o2) {
                int result;
                if (o1.getRepresentationScore() > o2.getRepresentationScore())
                    result = -1;
                else if (o1.getRepresentationScore() < o2.getRepresentationScore())
                    result = 1;
                else
                    result = 0;

                return result;
            }
        });

        // For Debugging
        for (int i=0; i<10; i++) {
            Log.d("BSJ", i + "th : " + mUsers.get(i).getId() + " / " + mUsers.get(i).getRepresentationScore());
        }

        progressBar.setVisibility(View.GONE);
        listUsers = new ArrayList<>();
        listUsers.addAll(mUsers.subList(0, 100));
        userAdapter = new UserAdapter(getContext(), listUsers);
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void readUsers() {
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFunctions = FirebaseFunctions.getInstance();
        getRelationalMatrix(firebaseUser.getUid())
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "네트워크 문제가 생겼습니다.", Toast.LENGTH_LONG);
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            final HashMap<String, String> indexIntimacy = new HashMap<>();
                            try {
                                JSONArray relationalMatrix = new JSONArray(task.getResult());
                                // Log.d("BSJ", task.getResult());
                                for (int i=0; i<relationalMatrix.length(); i++) {
                                    JSONObject jsonObject = relationalMatrix.getJSONObject(i);
                                    indexIntimacy.put(jsonObject.getString("uid"), jsonObject.getString("intimacy"));
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "네트워크 문제가 생겼습니다.", Toast.LENGTH_LONG);
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    mUsers.clear();
                                    for (DataSnapshot s : snapshot.getChildren()) {
                                        Users user = s.getValue(Users.class);

                                        assert user != null;
                                        if (!user.getId().equals(firebaseUser.getUid())) {
                                            mUsers.add(user);
                                        }
                                    }

                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("IndexExpert");
                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Map<String, Object> indexExpert = new HashMap<>();

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                IndexExpert tempIndexExpert = ds.getValue(IndexExpert.class);

                                                assert tempIndexExpert != null;
                                                if (!tempIndexExpert.getId().equals(firebaseUser.getUid())) {
                                                    indexExpert.put(tempIndexExpert.getId(), tempIndexExpert);
                                                }
                                            }

                                            // TODO : Matching...
                                            for (Users users : mUsers) {
                                                if (indexIntimacy.get(users.getId()) != null) {
                                                    users.setIndexIntimacy(indexIntimacy.get(users.getId()));
                                                } else {
                                                    Log.d("BSJ", "Not Matched(Intimacy) : " + users.getId());
                                                }
                                                if (indexExpert.get(users.getId()) != null) {
                                                    users.setIndexExpert((IndexExpert) indexExpert.get(users.getId()));
                                                } else {
                                                    Log.d("BSJ", "Not Matched(Expert) : " + users.getId());
                                                }
                                            }

                                            updateView();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }

    private Task<String> getRelationalMatrix(String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        return mFunctions
                .getHttpsCallable("getRelationalMatrix")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
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
