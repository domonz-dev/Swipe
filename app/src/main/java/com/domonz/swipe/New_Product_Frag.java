package com.domonz.swipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link New_Product_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class New_Product_Frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText in_name, in_price, in_tax;
    RadioGroup type;
    RecyclerView rec_images;
    AdapterProductImages adapterProductImages;
    TextView btnAdd, btnAddImage;
    View view;
    ArrayList<File> files;

    public New_Product_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment New_Product_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static New_Product_Frag newInstance(String param1, String param2) {
        New_Product_Frag fragment = new New_Product_Frag();
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
        view = inflater.inflate(R.layout.fragment_new__product_, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view){

        in_name = view.findViewById(R.id.in_product);
        in_price = view.findViewById(R.id.in_price);
        in_tax = view.findViewById(R.id.in_tax);
        type = view.findViewById(R.id.radioGroup);
        rec_images = view.findViewById(R.id.rec_product_img);
        btnAdd = view.findViewById(R.id.buttonAddProduct);
        btnAddImage = view.findViewById(R.id.buttonSelectImage);
        files = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rec_images.setLayoutManager(layoutManager);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product = in_name.getText().toString();
                int typ_id = type.getCheckedRadioButtonId();
                String price = in_price.getText().toString();
                String tax = in_tax.getText().toString();


                if(product.isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter product name.", Toast.LENGTH_SHORT).show();
                }else if(typ_id == -1){
                    Toast.makeText(view.getContext(), "Please select product type", Toast.LENGTH_SHORT).show();
                }else if(price.isEmpty()){
                    Toast.makeText(view.getContext(), "Please select product price", Toast.LENGTH_SHORT).show();
                }else if(tax.isEmpty()){
                    Toast.makeText(view.getContext(), "Please select product tax", Toast.LENGTH_SHORT).show();
                }else{
                    ProgressDialog pd = new ProgressDialog(view.getContext());
                    pd.setMessage("Uploading Product...");
                    pd.setCancelable(false);
                    pd.show();
                    RadioButton rb = view.findViewById(typ_id);
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    builder.addFormDataPart("product_name", product);
                    builder.addFormDataPart("price", price);
                    builder.addFormDataPart("tax", tax);
                    builder.addFormDataPart("product_type", rb.getText().toString());
                    addProduct(view, builder, pd);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == -1){
            if(data != null) {
                Uri uri = data.getData();
                File f = new File(getImageFilePath(uri));
                files.add(f);
                adapterProductImages = new AdapterProductImages(view.getContext(), files);
                rec_images.setAdapter(adapterProductImages);
            }
        }

    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity)view.getContext(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
            }, 2);
        }else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        if (permsRequestCode == 200) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("TAG", "Permissions: permission granted");

            }else if(ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                Log.d("TAG", "Permissions: permission granted");

            }else{
                Toast.makeText(view.getContext(), "To select an image you have to give the storage access.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public String getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = view.getContext().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range")
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            cursor.close();
            return imagePath;
        }
        return null;
    }

    private void addProduct(View view, MultipartBody.Builder builder, ProgressDialog pd) {

        if(adapterProductImages != null && adapterProductImages.files.size() > 0) {
            ArrayList<File> f = adapterProductImages.files;
            for (int i = 0; i < f.size(); i++) {
                File file = f.get(i);
                builder.addFormDataPart("files[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }

        MultipartBody requestBody = builder.build();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.uploadMultiFile(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                System.out.println(response.body());
                Toast.makeText(view.getContext(), "Upload success", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.d(TAG, "Error " + t.getMessage());
            }
        });


    }
}