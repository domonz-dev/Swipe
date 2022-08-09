package com.domonz.swipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    TextView btnNewProduct;
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize(){

        btnNewProduct = findViewById(R.id.btnNewProduct);

        setFragment(new Product_List_Frag());


        btnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new New_Product_Frag());
                btnNewProduct.setVisibility(View.GONE);
                current = 1;
            }
        });

    }

    protected void setFragment(Fragment fragment) {
        try {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.frag_container, fragment);
            t.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if(current == 1){
            setFragment(new Product_List_Frag());
            current = 0;
            btnNewProduct.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }
}