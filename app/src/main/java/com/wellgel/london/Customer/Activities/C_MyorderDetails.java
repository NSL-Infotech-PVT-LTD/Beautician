package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.wellgel.london.Customer.Adapters.C_MyOrderDetailAdapter;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.SerializeModelClasses.C_MyOrdersSerial;
import com.wellgel.london.R;
import java.util.ArrayList;
import java.util.List;

public class C_MyorderDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<C_MyOrdersSerial.Orderdetail> list;
    private ArrayList<C_MyOrdersSerial.Address> listAddres;
    private C_MyOrderDetailAdapter adapter;
    private C_CartModel model;
    private TextView totalAmountOrder, textAddress, itemPrice;
    private ImageView c_cart_back;

    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__myorder_details);

        list = (ArrayList<C_MyOrdersSerial.Orderdetail>) getIntent().getSerializableExtra("order_detail_list");
        listAddres = (ArrayList<C_MyOrdersSerial.Address>) getIntent().getSerializableExtra("order_address_list");
        recyclerView = findViewById(R.id.myOrderDetailRec);
        c_cart_back = findViewById(R.id.c_cart_back);
        itemPrice = findViewById(R.id.itemPrice);



        c_cart_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        totalAmountOrder = findViewById(R.id.totalAmountOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        totalAmountOrder.setText(" "+getString(R.string.currency) + grandTotal(list) + ".00");
        itemPrice.setText(" "+getString(R.string.currency) + grandTotal(list) + ".00");
        adapter = new C_MyOrderDetailAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private int grandTotal(List<C_MyOrdersSerial.Orderdetail> items) {

        int totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice += items.get(i).getProductId().getPrice() * items.get(i).getQuantity();
        }

        return totalPrice;
    }
}
