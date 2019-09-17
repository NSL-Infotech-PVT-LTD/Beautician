package com.wellgel.london.UtilClasses;

import android.content.Context;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.wellgel.london.Customer.DropDownListner;
import com.wellgel.london.R;

import java.util.ArrayList;


import static com.wellgel.london.Customer.Adapters.C_CartAdapter.C_product_holder.removeProduct;

public class DropDown extends AppCompatTextView implements View.OnClickListener {

    private ArrayList<String> options = new ArrayList<>();
    private int pos;
    private String spinnerValue;
    private IQuantitySelected itemListener;

    private DropDownListner dropDownListner;

    public DropDown(Context context) {
        super(context);
        initView();
    }

    public DropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.setOnClickListener(this);
    }

    private PopupWindow popupWindowsort(Context context) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setWidth(this.getWidth());
//        ArrayAdapter<String> s_adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_selected, options);
//        s_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_item,
                options);
        // the drop down list is a list view
        ListView listViewSort = new ListView(context);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);
        String spinnerVal = this.getText().toString();

        // set on item selected
        listViewSort.setOnItemClickListener((parent, view, position, id) -> {


//            if (spinnerVal.equalsIgnoreCase(options.get(position))) {
//                removeProduct.setText("Remove");
//            } else {
//                removeProduct.setText("Update");
//            }
            if (!options.get(position).equalsIgnoreCase("More")) {

                this.setText(options.get(position));
//                        if (!value.isEmpty()) {
//                            spinner.setPrompt(value);
            }

            itemListener.onQuantitySelected(position, pos, options.get(position), this);

            popupWindow.dismiss();
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    public void setItemListener(IQuantitySelected itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            PopupWindow window = popupWindowsort(v.getContext());
            window.showAsDropDown(v, 0, 0);
        }
    }

    public void setOptions(ArrayList<String> options, int pos) {
        this.options = options;
        this.pos = pos;
    }

    public void setOnEventListener(DropDownListner listener) {
        dropDownListner = listener;
    }


    public interface IQuantitySelected {
        public void onQuantitySelected(int drodownPos, int pos, String value, DropDown dropDown);
    }

}
