package com.wellgel.london.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wellgel.london.Customer.Adapters.EcomNotificationAdapter;
import com.wellgel.london.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcomNotificationAct extends AppCompatActivity {


    private RecyclerView notificationRecycler;
    private EcomNoti_Model model;
    private EcomNotificationAdapter adapter;
    private List<EcomNoti_Model> list = new ArrayList<>();
    private String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecom_notification);

        notificationRecycler = findViewById(R.id.notificationRecycler);

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//         Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("letest", "Jugraj");
        user.put("born", 1815);

//// Add a new document with a generated ID
//        db.collection("users")
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

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                model = new EcomNoti_Model();
                                model.setName(document.getData().get("first") + "");
//                                model.setName(document.getData().get("first")+"");
                                list.add(model);
                            }

                            adapter = new EcomNotificationAdapter(EcomNotificationAct.this, list, new EcomNotificationAdapter.onItemClick() {
                                @Override
                                public void onItemClick(String id, String value, String quantity) {

                                }
                            });

                            notificationRecycler.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}
