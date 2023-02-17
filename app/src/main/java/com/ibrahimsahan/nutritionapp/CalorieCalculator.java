package com.ibrahimsahan.nutritionapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalorieCalculator extends AppCompatActivity {

    TextView textViewSoapPiece;
    TextView textViewMainCoursePiece;
    TextView textViewDessertPiece;
    TextView textViewDrinkPiece;
    TextView textViewBreadPiece;
    TextView textViewNeededCalorie;
    TextView textViewSoapCal;
    TextView textViewMainCourseCal;
    TextView textViewDessertCal;
    TextView textViewDrinkCal;
    TextView textViewFinalCalories;
    Spinner spinnerSoap;
    Spinner spinnerMainCourse;
    Spinner spinnerDessert;
    Spinner spinnerDrink;
    PlusMinusButtonHelper plusMinusButtonHelper = new PlusMinusButtonHelper();
    private FirebaseFirestore db;
    private PieChart pieChart;
    DocumentReference docRef;
    DocumentReference docRef1;
    DocumentReference docRefCal;
    int selectedSoap;
    int selectedMainCourse;
    int selectedDessert;
    int selectedDrink;
    int takenCalories = 0;
    String today;
    String dailyCalorie;
    float pieChartX;
    float pieChartY;
    int currentCalorie;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_calculator);

        getCurrentTime();

        db = FirebaseFirestore.getInstance();
        docRef = db.collection("userinformations").document("userinformations");
        docRef1 = db.collection("FoodCalorieInformation").document("FoodCalorieInformation");
        docRefCal = db.collection("TakenCalories").document(today);




        textViewSoapPiece = findViewById(R.id.textViewTarhanaPiece);
        textViewSoapCal = findViewById(R.id.textViewSoapCal);
        textViewMainCoursePiece = findViewById(R.id.textViewChickenSautePiece);
        textViewMainCourseCal = findViewById(R.id.textViewMainCourseCal);
        textViewDessertPiece = findViewById(R.id.textViewRingDessertPiece);
        textViewDessertCal = findViewById(R.id.textViewDessertCal);
        textViewDrinkCal = findViewById(R.id.textViewDrinkCal);
        textViewDrinkPiece = findViewById(R.id.textViewRicePiece);
        textViewBreadPiece = findViewById(R.id.textViewBreadPiece);
        textViewNeededCalorie = findViewById(R.id.textViewNeededCalorie);
        textViewFinalCalories = findViewById(R.id.textViewFinalCalories);
        spinnerSoap = findViewById(R.id.spinnerSoap);
        spinnerMainCourse = findViewById(R.id.spinnerMainCourse);
        spinnerDessert = findViewById(R.id.spinnerDessert);
        spinnerDrink = findViewById(R.id.spinnerDrink);
        pieChart = findViewById(R.id.calorie_calculator_Piechart);

        setSpinner();


        getFirestoreData();

        setupPieChart();
        loadPieChartData();


    }

    private void getFirestoreData() {
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.d(TAG, "DocumentSnapshot access error");
                    return;
                }
                if (value != null && value.exists()) {
                    dailyCalorie = (value.getData().get("Daily Needed Calorie").toString());
                    textViewNeededCalorie.setText(dailyCalorie + " Kalori");


                }
            }
        });

        docRefCal.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    System.out.println("DocumentSnapshot access error");
                    return;
                }
                if (value != null && value.exists() && today.equals(docRefCal.getId())) {
                    currentCalorie = Integer.parseInt(value.getData().get("Taken Calories").toString());
                    textViewFinalCalories.setText(value.getData().get("Taken Calories").toString() + "/" + dailyCalorie);
                } else {
                    textViewFinalCalories.setText("0/" + dailyCalorie);
                }
            }
        });
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapterSoap = ArrayAdapter.createFromResource(this,
                R.array.SoapOptions, android.R.layout.simple_spinner_item);
        adapterSoap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSoap.setAdapter(adapterSoap);

        ArrayAdapter<CharSequence> adapterMainCourse = ArrayAdapter.createFromResource(this,
                R.array.MainCourseOptions, android.R.layout.simple_spinner_item);
        adapterSoap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMainCourse.setAdapter(adapterMainCourse);

        ArrayAdapter<CharSequence> adapterDessert = ArrayAdapter.createFromResource(this,
                R.array.DessertOptions, android.R.layout.simple_spinner_item);
        adapterSoap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDessert.setAdapter(adapterDessert);

        ArrayAdapter<CharSequence> adapterDrink = ArrayAdapter.createFromResource(this,
                R.array.DrinkOptions, android.R.layout.simple_spinner_item);
        adapterDrink.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrink.setAdapter(adapterDrink);

        spinnerSoap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docRef1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.d(TAG, "DocumentSnapshot access error");
                            return;
                        }
                        if (value != null && value.exists()) {
                            textViewSoapCal.setText(value.getData().get(parent.getItemAtPosition(position).toString()) + " Kalori");
                            selectedSoap = Integer.parseInt(value.getData().get(parent.getItemAtPosition(position).toString()).toString());
                        }
                    }
                });
                ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textViewSoapCal.setText("");
            }
        });

        spinnerMainCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docRef1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.d(TAG, "DocumentSnapshot access error");
                            return;
                        }
                        if (value != null && value.exists()) {
                            textViewMainCourseCal.setText(value.getData().get(parent.getItemAtPosition(position).toString()) + " Kalori");
                            selectedMainCourse = Integer.parseInt(value.getData().get(parent.getItemAtPosition(position).toString()).toString());
                        }
                    }
                });
                ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDessert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docRef1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.d(TAG, "DocumentSnapshot access error");
                            return;
                        }
                        if (value != null && value.exists()) {
                            textViewDessertCal.setText(value.getData().get(parent.getItemAtPosition(position).toString()) + " Kalori");
                            selectedDessert = Integer.parseInt(value.getData().get(parent.getItemAtPosition(position).toString()).toString());
                        }
                    }
                });
                ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDrink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docRef1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.d(TAG, "DocumentSnapshot access error");
                            return;
                        }
                        if (value != null && value.exists()) {
                            textViewDrinkCal.setText(value.getData().get(parent.getItemAtPosition(position).toString()) + " Kalori");
                            selectedDrink = Integer.parseInt(value.getData().get(parent.getItemAtPosition(position).toString()).toString());
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        today = day + "-" + (month + 1) + "-" + year;
    }

    @SuppressLint("SetTextI18n")
    public void piecePlusSoap(View view) {

        textViewSoapPiece.setText("" + plusMinusButtonHelper.plusPieceSoap());

    }

    @SuppressLint("SetTextI18n")
    public void pieceMinusSoap(View view) {

        textViewSoapPiece.setText("" + plusMinusButtonHelper.minusPieceSoap());

    }

    @SuppressLint("SetTextI18n")
    public void piecePlusMainCourse(View view) {
        textViewMainCoursePiece.setText("" + plusMinusButtonHelper.plusPieceMainCourse());
    }

    @SuppressLint("SetTextI18n")
    public void pieceMinusMainCourse(View view) {
        textViewMainCoursePiece.setText("" + plusMinusButtonHelper.minusPieceMainCourse());
    }

    @SuppressLint("SetTextI18n")
    public void piecePlusDessert(View view) {
        textViewDessertPiece.setText("" + plusMinusButtonHelper.plusPieceDessert());
    }


    @SuppressLint("SetTextI18n")
    public void pieceMinusDessert(View view) {
        textViewDessertPiece.setText("" + plusMinusButtonHelper.minusPieceDessert());
    }

    @SuppressLint("SetTextI18n")
    public void piecePlusDrink(View view) {
        textViewDrinkPiece.setText("" + plusMinusButtonHelper.plusPieceDrink());
    }

    @SuppressLint("SetTextI18n")
    public void pieceMinusDrink(View view) {
        textViewDrinkPiece.setText("" + plusMinusButtonHelper.minusPieceDrink());
    }

    @SuppressLint("SetTextI18n")
    public void piecePlusBread(View view) {
        textViewBreadPiece.setText("" + plusMinusButtonHelper.plusPieceBread());
    }

    @SuppressLint("SetTextI18n")
    public void pieceMinusBread(View view) {
        textViewBreadPiece.setText("" + plusMinusButtonHelper.minusPieceBread());
    }

    @SuppressLint("SetTextI18n")
    public void calorieCalculator(View view) {

        if (plusMinusButtonHelper.pieceSoap == 0
                && plusMinusButtonHelper.pieceMainCourse == 0
                && plusMinusButtonHelper.pieceDessert == 0
                && plusMinusButtonHelper.pieceBread == 0
                && plusMinusButtonHelper.pieceDrink == 0) {
            Toast.makeText(CalorieCalculator.this, "Lütfen adet giriniz!", Toast.LENGTH_LONG).show();
        } else {
            takenCalories = currentCalorie + selectedSoap * plusMinusButtonHelper.pieceSoap +
                    selectedMainCourse * plusMinusButtonHelper.pieceMainCourse +
                    selectedDessert * plusMinusButtonHelper.pieceDessert +
                    77 * plusMinusButtonHelper.pieceBread + selectedDrink * plusMinusButtonHelper.pieceDrink;

            Map<String, Object> takenCaloriesArray = new HashMap<>();
            takenCaloriesArray.put("Taken Calories", takenCalories);

            docRefCal.set(takenCaloriesArray)
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

            plusMinusButtonHelper = new PlusMinusButtonHelper();
            clearTextViews();

        }

    }

    public void clearTextViews() {
        textViewSoapPiece.setText("");
        textViewDessertPiece.setText("");
        textViewBreadPiece.setText("");
        textViewDrinkPiece.setText("");
        textViewMainCoursePiece.setText("");
    }

    private void loadPieChartData() {

        docRefCal.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    System.out.println("DocumentSnapshot access error");
                    return;
                }


                if (value != null && value.exists() && today.equals(docRefCal.getId())) {
                    if (Float.parseFloat(dailyCalorie) <= (float) currentCalorie) {
                        pieChartX = Float.parseFloat(String.valueOf(currentCalorie));
                        pieChartY = 0;

                    }
                    else {
                        pieChartX = Float.parseFloat(String.valueOf(currentCalorie));
                        pieChartY = Float.parseFloat(dailyCalorie) - pieChartX;
                    }

                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(pieChartX, "Aldığınız Kalori"));
                    entries.add(new PieEntry(pieChartY, "Almanız Gereken Kalori"));

                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int color : ColorTemplate.MATERIAL_COLORS) {
                        colors.add(color);
                    }

                    for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                        colors.add(color);
                    }
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(colors);

                    PieData data = new PieData(dataSet);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter(pieChart));
                    data.setValueTextSize(12f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setData(data);
                    pieChart.invalidate();
                    pieChart.animateY(1400, Easing.EaseInOutQuad);
                }
            }
        });


    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }



}