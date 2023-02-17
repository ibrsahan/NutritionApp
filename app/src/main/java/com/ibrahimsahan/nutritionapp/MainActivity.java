package com.ibrahimsahan.nutritionapp;

import static com.ibrahimsahan.nutritionapp.R.id.idRLBottomSheet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    String data1;
    RelativeLayout bottomSheetRL;
    BottomSheetDialog bottomSheetTeachersDialog;
    Button button6;

    private FirebaseFirestore firebaseStore;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheetRL = findViewById(idRLBottomSheet);
        firebaseStore = FirebaseFirestore.getInstance();
        button6 = findViewById(R.id.button6);
        bottomSheetTeachersDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);


        displayBottomSheet();


//
//        firebaseManager = new FirebaseManager(MainActivity.this);
//
//        firebaseManager.saveData("Test","testKey","testValue");
//        firebaseManager.saveData("Test","testKeyInt",1);
//
//            firebaseManager.getData("Test","testKey");



    }


    public void BmiScreen(View view) {

        Intent intent = new Intent(MainActivity.this, BodyMassIndex.class);
        startActivity(intent);


    }

    public void CalorieScreen(View view) {

        Intent intent = new Intent(MainActivity.this, CalorieCalculator.class);
        startActivity(intent);

    }

    private void displayBottomSheet() {



        View layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottom_sheet_layout, bottomSheetRL);

        bottomSheetTeachersDialog.setContentView(layout);

        bottomSheetTeachersDialog.setCancelable(false);

        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);

        bottomSheetTeachersDialog.show();

        ImageView imageIV = layout.findViewById(R.id.idIVimage);
        TextView textOneTV = layout.findViewById(R.id.idTVtext);
        TextView textTwoTV = layout.findViewById(R.id.idTVtextTwo);

        DocumentReference documentReference = firebaseStore.collection("BottomSheetDialog").document("EzCavUTfiv5mGzPDUZli");

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null && value.exists()) {
                    textOneTV.setText(value.getData().get("textOne").toString());
                    Picasso.get().load(value.getData().get("Image").toString()).into(imageIV);
                    textTwoTV.setText(value.getData().get("textTwo").toString());
                }
            }
        });
    }

    public void contunie(View view) {

        bottomSheetTeachersDialog.cancel();

    }






}