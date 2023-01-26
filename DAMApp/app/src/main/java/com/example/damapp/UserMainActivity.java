package com.example.damapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;


import com.example.damapp.databinding.ActivityUserMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserMainActivity extends AppCompatActivity {
    private UserQuizAdapter quizAdapter;
    ActivityUserMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizAdapter = new UserQuizAdapter(this);

        binding.quizRecycler.setAdapter(quizAdapter);
        binding.quizRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadQuiz(){
        FirebaseFirestore.getInstance().collection("quiz").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        quizAdapter.clear();
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot ds : dsList){
                            UserQuizModel quizModel = ds.toObject(UserQuizModel.class);
                            quizAdapter.addQuiz(quizModel);

                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Setting");
        progressDialog.setMessage("Quiz App");

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            FirebaseAuth.getInstance().signInAnonymously()
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.cancel();
                            loadQuiz();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserMainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            finish();
                        }
                    });
        }else {
            loadQuiz();
        }

    }
}
