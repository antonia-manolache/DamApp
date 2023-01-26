package com.example.damapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.damapp.databinding.ActivityAdminQuestionBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AdminQuestionActivity extends AppCompatActivity {

    ActivityAdminQuestionBinding binding;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizId=getIntent().getStringExtra("id");

        binding.uploadQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String questionId = UUID.randomUUID().toString();
                String question = binding.question.getText().toString();
                String choice1 = binding.choice1.getText().toString();
                String choice2 = binding.choice2.getText().toString();
                String choice3 = binding.choice3.getText().toString();
                String choice4 = binding.choice4.getText().toString();
                String answer="";

                String selected = ((RadioButton)findViewById(binding.radioGroup.getCheckedRadioButtonId())).getText().toString();

                switch(selected){
                    case"1":
                        answer=choice1;
                        break;
                    case"2":
                        answer=choice2;
                        break;
                    case"3":
                        answer=choice3;
                        break;
                    case"4":
                        answer=choice4;
                        break;

                }

                AdminQuestionModel questionModel = new AdminQuestionModel(questionId,quizId,question,choice1,choice2,choice3,choice4,answer);

                FirebaseFirestore.getInstance().collection("questions").document(questionId).set(questionModel);

                Toast.makeText(AdminQuestionActivity.this,"Question Added Successfully",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminQuestionActivity.this, AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}