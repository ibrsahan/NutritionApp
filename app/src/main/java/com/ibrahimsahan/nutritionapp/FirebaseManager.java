package com.ibrahimsahan.nutritionapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseManager {

    private final FirebaseFirestore firebaseFirestore;
    Context context;
    private FirebaseManager firebaseManager;



    public FirebaseManager(Context context) {
        this.context = context;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void saveData(String collection, String key, Object value) {

        HashMap<String, Object> data = new HashMap<>();
        data.put(key,value);

        firebaseFirestore.collection(collection).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Verileriniz başarıyla kaydedildi!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Verileriniz kaydedilirken bir hata oluştu!", Toast.LENGTH_LONG).show();

            }
        });


    }

    public ArrayList<String> getData(String collection,String key) {
        ArrayList<String> data = new ArrayList<>();
        firebaseFirestore.collection(collection).document("6IzBI120vQ23c7ghDSVZ").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        data.add(document.getString(key));

                    }else {

                    }
                }else {
                }
            }
        });




//        firebaseFirestore.collection(collection).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                if(value != null) {
//
//
//                    for (DocumentSnapshot snapshot :  value.getDocuments()) {
//                        Map<String, Object> dataMap = snapshot.getData();
//                        data.add((String) dataMap.get(key));
//
//
//                    }
//                } else {
//                    System.out.println("Verileri getirirken bir hata oluştu");
//                }
//            }
//
//        });
        return  data;
    }
}
