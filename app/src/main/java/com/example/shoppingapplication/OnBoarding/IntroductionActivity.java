package com.example.shoppingapplication.OnBoarding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shoppingapplication.OnBoarding.OnBoardingFragment1;
import com.example.shoppingapplication.OnBoarding.OnBoardingFragment2;
import com.example.shoppingapplication.OnBoarding.OnBoardingFragment3;
import com.example.shoppingapplication.R;

public class IntroductionActivity extends AppCompatActivity {

    ImageView splashImg,logo;
    LottieAnimationView lottieAnimationView;
    private static final int NUM_PAGES = 3;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introduction);






    }

    @Override
    protected void onStart() {
        super.onStart();
        logo = findViewById(R.id.logo);
        splashImg = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);

        viewPager= findViewById(R.id.pager);
        ObjectAnimator animator = ObjectAnimator.ofFloat(splashImg, "translationY", -3000);
        animator.setStartDelay(4000);
        animator.setDuration(1000);
        animator.start();
//        splashImg.animate().translationY(-3000).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(-2500).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1600).setDuration(700).setStartDelay(3000);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2:
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}