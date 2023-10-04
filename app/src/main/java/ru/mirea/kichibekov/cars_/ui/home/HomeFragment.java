package ru.mirea.kichibekov.cars_.ui.home;


import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.util.Objects;

import ru.mirea.kichibekov.cars_.databinding.FragmentHomeBinding;
import ru.mirea.kichibekov.cars_.models.User;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ImageView imageView;

    TextView textAge;

    TextView textNumberAuto;

    TextView textName;

    TextView textExperience;

    private FirebaseAuth mAuth;
    DatabaseReference users;
    FirebaseDatabase db;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        View root = binding.getRoot();
        String url = "https://firebasestorage.googleapis.com/v0/b/cars-5fe3d.appspot.com/o/images%2Fphoto%201x1.jpg?alt=media&token=95aeb742-b7dd-4091-8927-0d4018fb58dd&_gl=1*1j6ksq1*_ga*MzQ4MjkyNTQyLjE2NzQzMjM2OTQ.*_ga_CW55HF8NVT*MTY5NjQxNjU2OS4xNDkuMS4xNjk2NDE3OTk0LjQ4LjAuMA..";
        imageView = binding.imageViewPushButton;
        Glide.with(getApplicationContext()).load(url).into(imageView);
        textName = binding.textViewName;
        textNumberAuto = binding.textViewNumberAuto;
        textAge = binding.textViewAge;
        textExperience = binding.textViewExperience;
        getDataFromDB();
        return root;
    }

    private void getDataFromDB() {
        ValueEventListener listener = new ValueEventListener() {
            @SuppressLint({"SuspiciousIndentation", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    if (Objects.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), user.getEmail()))
                    {
                        textName.setText("Name: " + user.getName());
                        textNumberAuto.setText("Car number: " + user.getCarNumber());
                        textAge.setText("Age: " + user.getAge());
                        textExperience.setText("Experience: " + user.getExperience());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        users.addValueEventListener(listener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}