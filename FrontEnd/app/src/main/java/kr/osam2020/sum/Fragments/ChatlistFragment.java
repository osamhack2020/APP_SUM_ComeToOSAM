package kr.osam2020.sum.Fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.osam2020.sum.Adapter.ChatlistAdapter;
import kr.osam2020.sum.Adapter.UserAdapter;
import kr.osam2020.sum.Model.Chatlist;
import kr.osam2020.sum.Model.Users;
import kr.osam2020.sum.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatlistFragment extends Fragment {
    private ChatlistAdapter chatlistAdapter;
    private List<Chatlist> mChatlists;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<Chatlist> allList;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chatlist, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Drawable divider = ContextCompat.getDrawable(v.getContext(), R.drawable.divider);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(v.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(divider);
        recyclerView.addItemDecoration(dividerItemDecoration);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        allList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allList.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Chatlist temp = s.getValue(Chatlist.class);
                    temp.setId(s.getKey());
                    allList.add(temp);
                }

                getAllChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    private void getAllChatList() {
        mChatlists = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChatlists.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Users users = s.getValue(Users.class);

                    for (Chatlist temp : allList) {
                        if (users.getId().equals(temp.getId())) {
                            Chatlist chatlist = new Chatlist(users.getId(), users.getUsername(), temp.getTimestamp(), users.getImageURL(), temp.getIsRead());
                            mChatlists.add(chatlist);
                        }
                    }
                }
                Collections.sort(mChatlists);
                chatlistAdapter = new ChatlistAdapter(getContext(), mChatlists);
                recyclerView.setAdapter(chatlistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
