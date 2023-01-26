package com.example.damapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.damapp.databinding.ActivityAdminMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;
    AdminQuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizAdapter=new AdminQuizAdapter(this);

        binding.addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = UUID.randomUUID().toString();
                String name = binding.quizName.getText().toString();
                AdminQuizModel quizModel = new AdminQuizModel(id,name,0);
                uploadQuiz(quizModel);
                Toast.makeText(AdminMainActivity.this,"Quiz Added Successfully",Toast.LENGTH_SHORT).show();
                loadQuiz();


            }
        });

        binding.quizRecycler.setAdapter(quizAdapter);
        binding.quizRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadQuiz(){
        quizAdapter.clear();
        FirebaseFirestore.getInstance().collection("quiz").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot ds : dsList){
                            AdminQuizModel quizModel = ds.toObject(AdminQuizModel.class);
                            quizAdapter.addQuiz(quizModel);

                        }
                    }
                });
    }

    private void uploadQuiz(AdminQuizModel quizModel){
        FirebaseFirestore.getInstance()
                .collection("quiz")
                .document(quizModel.getQuizId())
                .set(quizModel);

        binding.quizName.setText("");

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
                            Toast.makeText(AdminMainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            finish();
                        }
                    });
        }else {
            loadQuiz();
        }

    }
}