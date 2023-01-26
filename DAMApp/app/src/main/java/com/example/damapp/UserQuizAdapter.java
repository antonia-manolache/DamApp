package com.example.damapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserQuizAdapter extends RecyclerView.Adapter<UserQuizAdapter.MyViewHolder> {

    private Context context;
    private List<UserQuizModel> quizModelList;

    public UserQuizAdapter(Context context){
        this.context=context;
        quizModelList=new ArrayList<>();
    }

    public void addQuiz(UserQuizModel quizModel){
        quizModelList.add(quizModel);
        notifyDataSetChanged();
    }

    public void removeQuiz(int position){
        quizModelList.remove(position);
        notifyDataSetChanged();
    }

    public void clear(){
        quizModelList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_quiz_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserQuizModel quizModel = quizModelList.get(position);
        holder.bindViews(quizModel);
    }

    @Override
    public int getItemCount() {
        return quizModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView quizName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            quizName=itemView.findViewById(R.id.quizName);

        }

        public void bindViews(UserQuizModel quizModel) {

            quizName.setText(quizModel.getQuizName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserQuestionActivity.class);
                    intent.putExtra("id",quizModel.getQuizId());
                    context.startActivity(intent);

                }
            });


        }
    }
}
