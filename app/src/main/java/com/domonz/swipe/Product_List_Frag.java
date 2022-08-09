package com.domonz.swipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Product_List_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product_List_Frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rec_products;
    ArrayList<ProductModel> products;
    AdapterProducts adapterProducts;

    public Product_List_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product_List_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Product_List_Frag newInstance(String param1, String param2) {
        Product_List_Frag fragment = new Product_List_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product__list_, container, false);

        initialize(view);

        return view;
    }

    private void initialize(View view){

        rec_products = view.findViewById(R.id.rec_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rec_products.setLayoutManager(layoutManager);

        getProducts(view);
    }

    private void getProducts(View view){
        products = new ArrayList<>();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Object> call = apiService.getProducts();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                System.out.println(response.body());
                String js = new Gson().toJson(response.body());
                try {
                    JSONArray array = new JSONArray(js);
                    for(int i = 0; i< array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        ProductModel pm = new Gson().fromJson(obj.toString(), ProductModel.class);
                        products.add(pm);
                        adapterProducts = new AdapterProducts(view.getContext(), products);
                        rec_products.setAdapter(adapterProducts);

                    }

                    Log.e("TAG", ""+ products.size() );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.toString() );
            }
        });

    }
}