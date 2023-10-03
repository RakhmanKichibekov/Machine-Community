package ru.mirea.kichibekov.cars_;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import ru.mirea.kichibekov.cars_.databinding.ActivityRegistrationBinding;
import ru.mirea.kichibekov.cars_.models.User;

public class Registration extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityRegistrationBinding binding;
    private FirebaseAuth mAuth;
    DatabaseReference users;
    FirebaseDatabase db;
    RelativeLayout root;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
// [START initialize_auth] Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");
        root = binding.rootAllement;

        @SuppressLint("HardwareIds")
        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("Индивидуальный код "+m_androidId);
        binding.textID.setText("ID вашего устройства="+m_androidId);
        binding.signedInButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(binding.emailPasswordFields.getText());
                String password = String.valueOf(binding.passEdit.getText());
                signIn(email, password, v);
            }
        });
        binding.emailPasswordButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(binding.emailPasswordFields.getText());
                String password = String.valueOf(binding.passEdit.getText());
                showRegisterWindow();
                //createAccount(email, password, v);
            }
        });
// [END initialize_auth]
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]
    private void updateUI(FirebaseUser user) {
        if (user != null) {
//            binding.textView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            binding.textViewUID.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//            binding.emailPasswordButtons.setVisibility(View.GONE);
//            binding.passEdit.setVisibility(View.GONE);
//            binding.signedInButtons.setVisibility(View.GONE);

        } else {
            binding.textView.setText(R.string.signed_out);
            binding.textViewUID.setText(null);
            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.passEdit.setVisibility(View.VISIBLE);
            binding.signedInButtons.setVisibility(View.VISIBLE);
        }
    }

    private void createAccount(String email, String password, View v) {
        Log.d(TAG, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }
// [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            goSystem(v);
                        } else {
// If sign in fails, display a message to the user.

                            Log.w(TAG, "createUserWithEmail:failure",

                                    task.getException());
                            Toast.makeText(Registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
// [END create_user_with_email]
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window, null);
        dialog.setView(registerWindow);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText email = registerWindow.findViewById(R.id.emailField);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})final EditText password = registerWindow.findViewById(R.id.passField);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})final EditText name = registerWindow.findViewById(R.id.nameField);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})final EditText phone = registerWindow.findViewById(R.id.phoneField);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})final EditText carNumber = registerWindow.findViewById(R.id.carNumberField);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})final EditText age = registerWindow.findViewById(R.id.ageField);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})final EditText expirience = registerWindow.findViewById(R.id.expirienceField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 6) {
                    Snackbar.make(root, "Введите пароль больше 6 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(carNumber.getText().toString())) {
                    Snackbar.make(root, "Введите номерной знак автомобиля", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(age.getText().toString())) {
                    Snackbar.make(root, "Введите возраст", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(expirience.getText().toString())) {
                    Snackbar.make(root, "Введите стаж вождения", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //Регистрация пользователя
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String age2 = "18";
                                User user = new User(name.getText().toString(), email.getText().toString(),
                                        phone.getText().toString(), carNumber.getText().toString(),
                                        new ArrayList<String>(), Long.parseLong(age.getText().toString()), Long.parseLong(expirience.getText().toString()));
                                users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Пользователь добавлен", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root, "Ошибка регистрации(возможно email уже занят). " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }

    private void signIn(String email, String password, View v) {
        Log.d(TAG, "signIn:" + email);
// [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            goSystem(v);
                        } else {
// If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(Registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
// [START_EXCLUDE]

                        if (!task.isSuccessful()) {

                            binding.textView.setText(R.string.auth_failed);
                        }

// [END_EXCLUDE]

                    }
                });
// [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    public void goSystem(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}