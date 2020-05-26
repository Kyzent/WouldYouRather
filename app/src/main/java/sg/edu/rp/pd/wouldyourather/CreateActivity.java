package sg.edu.rp.pd.wouldyourather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class CreateActivity extends AppCompatActivity {
    private static final String TAG = "CreateActivity";

    private static final String KEY_RED = "red";
    private static final String KEY_BLUE = "blue";

    EditText etRed, etBlue;
    Button btnSubmit;

    FirebaseAuth fbAuth;
    FirebaseFirestore fbFirestore;
    CollectionReference CollectionRef;
    DocumentReference DocumentRef, UsersRef;

    String userID;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create);

        fbAuth = FirebaseAuth.getInstance();
        fbFirestore = FirebaseFirestore.getInstance();

        userID = fbAuth.getCurrentUser().getUid();

        CollectionRef = fbFirestore.collection("Questions");
        DocumentRef = fbFirestore.document("Questions/Test");
        UsersRef = fbFirestore.collection("users").document(userID);
        UsersRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username = documentSnapshot.getString("Username");
            }
        });


        etRed = findViewById(R.id.etRed);
        etBlue = findViewById(R.id.etBlue);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String redOption = etRed.getText().toString().trim();
                String blueOption = etBlue.getText().toString().trim();

                if (TextUtils.isEmpty(redOption) && TextUtils.isEmpty(blueOption)) {
                    Toast.makeText(CreateActivity.this, "First Option and Second Option must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(redOption)) {
                    Toast.makeText(CreateActivity.this, "First Option must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(blueOption)) {
                    Toast.makeText(CreateActivity.this, "Second Option must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Question question = new Question(redOption, blueOption, 0, 0, userID);

                CollectionRef.add(question)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                etBlue.getText().clear();
                                etRed.getText().clear();
                                Toast.makeText(CreateActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        });

    }
}
