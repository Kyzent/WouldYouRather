package sg.edu.rp.pd.wouldyourather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    TextView tvRed, tvBlue, tvBlack;

    FirebaseAuth fbAuth;
    FirebaseFirestore fbFirestore;
    CollectionReference CollectionRef;
    DocumentReference QuestionsRef;

    int questionSize;
    String red;
    String blue;
    int votesRed;
    int votesBlue;
    String creator;
    ArrayList<String> QuestionID = new ArrayList<String>();
    String randomID;
    Random random;
    Boolean textFreezer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        random = new Random();

        fbAuth = FirebaseAuth.getInstance();
        fbFirestore = FirebaseFirestore.getInstance();

        tvRed = findViewById(R.id.tvRed);
        tvBlue = findViewById(R.id.tvBlue);
        tvBlack = findViewById(R.id.tvBlack);

        tvBlack.setEnabled(false);

        tvRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor();
                votesRed += 1;
                QuestionsRef.update("votesRed", votesRed);
                int votesTotal = votesRed + votesBlue;
                double redPercent = 100 * votesRed / votesTotal;
                double bluePercent = (100 * votesBlue) / votesTotal;
                tvRed.setText(redPercent + "%\n"+ votesRed+" Agree\n\n"+red);
                tvBlue.setText(bluePercent + "%\n"+ votesBlue+" Disagree\n\n"+blue);
                tvRed.setEnabled(false);
                tvBlue.setEnabled(false);
                tvBlack.setText(getString(R.string.next));
                tvBlack.setEnabled(true);
            }
        });

        tvBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor();
                votesBlue += 1;
                QuestionsRef.update("votesBlue", votesBlue);
                int votesTotal = votesRed + votesBlue;
                double redPercent = 100 * votesRed / votesTotal;
                double bluePercent = (100 * votesBlue) / votesTotal;
                tvRed.setText(redPercent + "%\n"+ votesRed+" Disagree\n\n"+red);
                tvBlue.setText(bluePercent + "%\n"+ votesBlue+" Agree\n\n"+blue);
                tvRed.setEnabled(false);
                tvBlue.setEnabled(false);
                tvBlack.setText(getString(R.string.next));
                tvBlack.setEnabled(true);
            }
        });

        tvBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });

        CollectionRef = fbFirestore.collection("Questions");
        fbFirestore.collection("Questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        questionSize++;
                        QuestionID.add(document.getId());
                    }
                    Log.d(TAG, "Questions Size:" + questionSize);
                    randomID = QuestionID.get(random.nextInt(questionSize));
                    loadQuestions();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void loadQuestions() {
        QuestionsRef = fbFirestore.collection("Questions").document(randomID);
        QuestionsRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                red = documentSnapshot.getString("red");
                blue = documentSnapshot.getString("blue");
                votesRed = documentSnapshot.getLong("votesRed").intValue();
                votesBlue = documentSnapshot.getLong("votesBlue").intValue();
                creator = documentSnapshot.getString("creator");

                if (textFreezer == false) {
                    tvRed.setText(red);
                    tvBlue.setText(blue);
                    textFreezer = true;
                }
            }
        });
    }

    private void selectColor() {
        QuestionsRef = fbFirestore.collection("Questions").document(randomID);
        QuestionsRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                red = documentSnapshot.getString("red");
                blue = documentSnapshot.getString("blue");
                votesRed = documentSnapshot.getLong("votesRed").intValue();
                votesBlue = documentSnapshot.getLong("votesBlue").intValue();
                creator = documentSnapshot.getString("creator");
            }
        });
    }


    private void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
