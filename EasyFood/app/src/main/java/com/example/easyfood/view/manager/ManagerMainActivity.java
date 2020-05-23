package com.example.easyfood.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easyfood.R;
import com.example.easyfood.view.BaseActivity;

public class ManagerMainActivity extends BaseActivity {
    Button menuButton;
    Button ordersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);

        menuButton = findViewById(R.id.menu_button);
        ordersButton = findViewById(R.id.orders_button);

        setLoginButtonListener();
        setOrdersButtonListener();
    }

    /**
     * Sets the OnClickListener for the Menu Button.
     */
    private void setLoginButtonListener() {
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(new Intent(getApplicationContext(), ManagerMenuActivity.class));
            }
        });
    }

    private void setOrdersButtonListener() {
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(new Intent(getApplicationContext(), OrdersActivity.class));
            }
        });
    }
}
