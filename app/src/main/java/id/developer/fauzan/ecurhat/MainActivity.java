package id.developer.fauzan.ecurhat;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.developer.fauzan.ecurhat.model.Users;
import id.developer.fauzan.ecurhat.util.Constant;

public class MainActivity extends AppCompatActivity {

    private final int splash_display_length = 3000;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            //splash screen in 3 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentUserLogin(auth);
                }
            },splash_display_length);

        }else {
            //splash screen in 3 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },splash_display_length);
        }
    }

    private void getCurrentUserLogin(FirebaseAuth auth){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                if (user.getStatus().equals(Constant.ROLE_ADMIN)){
                    Intent intent = new Intent(new Intent(MainActivity.this, HomeActivity.class));
                    intent.putExtra("isAdmin", true);
                    startActivity(intent);
                    finish();
                }else if (user.getStatus().equals(Constant.ROLE_USER)){
                    Intent intent = new Intent(new Intent(MainActivity.this, HomeActivity.class));
                    intent.putExtra("isAdmin", false);
                    startActivity(intent);
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
