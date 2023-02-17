package com.ibrahimsahan.nutritionapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.protobuf.DoubleValue;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BodyMassIndex extends AppCompatActivity {

    EditText heightInput;
    EditText weightInput;
    EditText ageInput;
    RadioGroup radioGroupGender;
    RadioGroup radioGroupActivityLevel;
    TextView bmiValueExplanation;
    TextView bmiExplanation;
    RadioButton checkedGender;
    RadioButton checkedActivityLevel;
    double bmiValue;
    double heightInputDouble;
    double weightInputDouble;
    double ageInputDouble;
    double bmr;
    double neededCalorieDb;
    float neededCalorie;
    private FirebaseFirestore db;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_mass_index);

        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        ageInput = findViewById(R.id.ageInput);
        bmiValueExplanation = findViewById(R.id.bmiValueExplanation);
        bmiExplanation = findViewById(R.id.bmiExplanation);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioGroupActivityLevel = findViewById(R.id.radioGroupActivityLevel);
        radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> checkedGender= findViewById(i));

        radioGroupActivityLevel.setOnCheckedChangeListener((radioGroup, i) -> checkedActivityLevel= findViewById(i));

        db = FirebaseFirestore.getInstance();
        docRef = db.collection("userinformations").document("userinformations");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.d(TAG, "DocumentSnapshot access error");
                    return;
                }
                if (value != null && value.exists()) {
                    bmiValueExplanation.setText(value.getData().get("Body Mass Index").toString());
                    if (Double.parseDouble(value.getData().get("Body Mass Index").toString())<18.5){
                        bmiExplanation.setText("Düşük Kilolusunuz!");
                    } else if(18.5 <= Double.parseDouble(value.getData().get("Body Mass Index").toString()) && Double.parseDouble(value.getData().get("Body Mass Index").toString())<25){
                        bmiExplanation.setText("Normal Kilolusunuz!");
                    } else if(25 <= Double.parseDouble(value.getData().get("Body Mass Index").toString()) && Double.parseDouble(value.getData().get("Body Mass Index").toString())<30){
                        bmiExplanation.setText("Fazla Kilolusunuz!");
                    } else {
                        bmiExplanation.setText("Obezsiniz!");
                    }
                }
            }
        });


    }

    public void bmiCalculator(View view) {


        if (heightInput.getText().toString().matches("") || heightInput.getText().toString().matches("0") ||
                weightInput.getText().toString().matches("") || weightInput.getText().toString().matches("0") ||
                ageInput.getText().toString().matches("") || ageInput.getText().toString().matches("0") ||
                checkedGender == null || checkedActivityLevel == null) {

            Toast.makeText(BodyMassIndex.this, "Lütfen bütün bilgileri eksiksiz ve doğru bir şekilde giriniz!", Toast.LENGTH_LONG).show();
        } else {
            heightInputDouble = Double.parseDouble(heightInput.getText().toString())/100;
            System.out.println(heightInputDouble);
            weightInputDouble = Double.parseDouble(weightInput.getText().toString());
            ageInputDouble = Double.parseDouble(ageInput.getText().toString());

            bmiValue = weightInputDouble / (heightInputDouble*heightInputDouble);

            AlertDialog.Builder alert = new AlertDialog.Builder(BodyMassIndex.this);

            alert.setTitle("Hesaplama Sonucu : ");

            calCalculator();

            if (bmiValue < 18.5) {
                alert.setMessage("Vücut Kitle İndeksi: " + (int) bmiValue + "\nDüşük Kilolusunuz!" + "\nAlmanız Gereken Günlük Kalori Miktarı: " + (int) neededCalorieDb + " Kalori");
            } else if (18.5 <= bmiValue & bmiValue<24.99) {
                alert.setMessage("Vücut Kitle İndeksi: " + (int) bmiValue + "\nNormal Kilolusunuz!" + "\nAlmanız Gereken Günlük Kalori Miktarı: " + (int) neededCalorieDb + " Kalori");

            } else if (25 <= bmiValue & bmiValue<29.99) {
                alert.setMessage("Vücut Kitle İndeksi: " + (int) bmiValue + "\nFazla Kilolusunuz!" + "\nAlmanız Gereken Günlük Kalori Miktarı: " + (int) neededCalorieDb + " Kalori");

            } else {
                alert.setMessage("Vücut Kitle İndeksi: " + (int) bmiValue + "\nObezsiniz!" + "\nAlmanız Gereken Günlük Kalori Miktarı: " + (int) neededCalorieDb + " Kalori");
            }

            alert.setPositiveButton("Tamam", (dialogInterface, i) -> {

            });

            alert.show();




        }


    }

    public void calCalculator() {

        if(checkedGender.getText().toString().equals("Kadın")) {
            bmr = 655.1+ (9.563*weightInputDouble) + (1.85*heightInputDouble*100) - (4.676*ageInputDouble);
        } else if(checkedGender.getText().toString().equals("Erkek")) {
            bmr = 66.47 + (13.75*weightInputDouble) + (5.003*heightInputDouble*100) - (6.755*ageInputDouble);
        }

        if(checkedActivityLevel.getText().toString().equals("Az")) {
            neededCalorieDb = bmr*1.2;
            neededCalorie = (int) neededCalorieDb;
        } else if (checkedActivityLevel.getText().toString().equals("Orta")) {
            neededCalorieDb = bmr*1.55;
            neededCalorie = (int) neededCalorieDb;
        } else {
            neededCalorieDb = bmr*1.725;
            neededCalorie = (int) neededCalorieDb;
        }


        Map<String, Object> user = new HashMap<>();
        user.put("Height", heightInputDouble*100);
        user.put("Weight", weightInputDouble);
        user.put("Age", ageInputDouble);
        user.put("Gender", checkedGender.getText().toString());
        user.put("Activity Level", checkedActivityLevel.getText().toString());
        user.put("Daily Needed Calorie", neededCalorie);
        user.put("Body Mass Index", (int) bmiValue);


        docRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });



        //        db.collection("userinformations")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });



    }
}