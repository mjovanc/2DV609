package com.example.easyfood.view.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyfood.R;
import com.example.easyfood.view.BaseActivity;

public class NewProductActivity extends BaseActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new);

        nameEditText = findViewById(R.id.name_editText);
        descriptionEditText = findViewById(R.id.description_editText);
        priceEditText = findViewById(R.id.price_editText);
        addButton = findViewById(R.id.add_button);

        setAddButtonListener();

    }

    /**
     * Sets the On Click Listener for Add Button.
     */
    private void setAddButtonListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                Double price = Double.valueOf(priceEditText.getInputType());

                if (name.isEmpty()) {
                    nameEditText.setError("Provide a name first!");
                    nameEditText.requestFocus();
                    return;
                }

                if (description.isEmpty()) {
                    descriptionEditText.setError("Enter Description!");
                    descriptionEditText.requestFocus();
                    return;
                }

                if (price.isNaN()) {
                    descriptionEditText.setError("Enter Price!");
                    descriptionEditText.requestFocus();
                    return;
                }

                addProduct(name, description, price);
            }
        });
    }

    private void addProduct(String name, String description, Double price) {
        // TODO Continue here...  New ViewModel or use ProductViewModel?
    }
}
