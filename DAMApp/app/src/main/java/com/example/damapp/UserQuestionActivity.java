package com.example.damapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.damapp.databinding.ActivityUserQuestionBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserQuestionActivity extends AppCompatActivity {

    ActivityUserQuestionBinding binding;
    String quizId;
    private List<UserQuestionModel>questionModelList;
    private int position = 0;
    private int numberOfQuestions=0;
    private static String answer=null;
    private static int correctAnswers=0;
    private static int incorrectAnswers=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizId=getIntent().getStringExtra("id");

        loadQuestions();

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionModelList!=null && answer!=null){
                    String selectedRadioButton = ((RadioButton) findViewById(binding.radioGroup.getCheckedRadioButtonId())).getText().toString();
                    if(selectedRadioButton.equals(answer)){
                        correctAnswers++;
                        Toast.makeText(UserQuestionActivity.this,"Correct",Toast.LENGTH_SHORT).show();
                    }else{
                        incorrectAnswers++;
                        Toast.makeText(UserQuestionActivity.this,"Incorrect",Toast.LENGTH_SHORT).show();
                    }
                    position++;
                    if(numberOfQuestions<=position){
                        String resultId = UUID.randomUUID().toString();
                        UserResultModel resultModel = new UserResultModel(resultId, FirebaseAuth.getInstance().getUid(),correctAnswers,incorrectAnswers);

                        FirebaseFirestore.getInstance().collection("results").document("resultId").set(resultModel);

                        AlertDialog.Builder builder = new AlertDialog.Builder(UserQuestionActivity.this);
                        builder.setTitle("Result").setMessage("Your Score: " + correctAnswers + "/" + (correctAnswers+incorrectAnswers));
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                finish();
                            }
                        });

                        builder.create().show();
                        return;

                    }
                    showQuestions(position);
                }
            }
        });
    }

    private void loadQuestions(){
        FirebaseFirestore.getInstance().collection("questions").whereEqualTo("quizId",quizId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList  =queryDocumentSnapshots.getDocuments();
                        numberOfQuestions=dsList.size();
                        questionModelList = new ArrayList<>();
                        for(DocumentSnapshot ds : dsList){
                            UserQuestionModel questionModel = ds.toObject(UserQuestionModel.class);
                            questionModelList.add(questionModel);
                        }
                        if(dsList.size()>=0){
                            showQuestions(position);
                        }else{
                            Toast.makeText(UserQuestionActivity.this,"No Qestions found",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                });
    }

    private void showQuestions(int i){
        UserQuestionModel questionModel = questionModelList.get(i);


        answer = questionModel.getCorrectChoice();

        binding.quizQuestion.setText(questionModel.getQuestion());
        binding.option1.setText(questionModel.getChoice1());
        binding.option2.setText(questionModel.getChoice2());
        binding.option3.setText(questionModel.getChoice3());
        binding.option4.setText(questionModel.getChoice4());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(quizId==null){
            Toast.makeText(this,"session expired", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}