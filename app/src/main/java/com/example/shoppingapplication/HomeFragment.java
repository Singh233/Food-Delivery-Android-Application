package com.example.shoppingapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shoppingapplication.Adapter.CategoryAdapter;
import com.example.shoppingapplication.Model.Category;
import com.example.shoppingapplication.Model.Slider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String mParam1;
    private String mParam2;


    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private   List<Category> categoryList;
    private FirebaseFirestore firebaseFirestore;

    ////Banner Slider

    private ViewPager bannerSliderViewPager;
    private List<Slider> sliderList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;

    //////Banner Slider

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home2, container, false);
        categoryRecyclerView = view.findViewById(R.id.category_recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryList = new ArrayList<>();




        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES")
                .orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryList.add(new Category(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }


                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return view;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Item" + v + "clicked", Toast.LENGTH_SHORT).show();
    }
}