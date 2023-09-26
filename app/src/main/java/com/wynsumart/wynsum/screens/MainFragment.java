package com.wynsumart.wynsum.screens;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.wynsumart.wynsum.MainActivity;

import com.wynsumart.wynsum.R;
import com.wynsumart.wynsum.adapters.TargetsListAdapter;
import com.wynsumart.wynsum.databinding.MainFragmentLayoutBinding;
import com.wynsumart.wynsum.models.MeditationTargetContainer;
import com.wynsumart.wynsum.view_models.MainFragmentVM;


public class MainFragment extends Fragment {
    private MainFragmentLayoutBinding binding;
    MainFragmentVM mainFragmentVM;
    private TargetsListAdapter targetsListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MainFragmentLayoutBinding.inflate(inflater, container, false);
        targetsListAdapter = new TargetsListAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeditationTargetContainer target = (MeditationTargetContainer) v.getTag();
                if (v.getId() == R.id.startTargetSessionButton){
                    ((MainActivity) getActivity()).openTarget(target.id);
                }else {
                    binding.targetTitle.setText(target.name);
                    binding.targetTitleDescription.setText(target.short_description);
                }
            }
        });
        binding.targetsRecycleList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.targetsRecycleList.setAdapter(targetsListAdapter);
//        binding.clearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainFragmentVM.clearTargets();
//            }
//        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // define the live data
        mainFragmentVM = new ViewModelProvider(requireActivity(),
                ViewModelProvider.Factory.from(MainFragmentVM.initializer)
        ).get(MainFragmentVM.class);

        mainFragmentVM.getLiveData().observe(getViewLifecycleOwner(), data -> {
            targetsListAdapter.targets(data);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // start make status bar as default
//        WindowCompat.setDecorFitsSystemWindows(requireActivity().getWindow(), true);
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // --
    }

    public static Bitmap createBitmapWithText(String text, Activity activity) {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);


        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();

        // Create a Bitmap with the specified width and height
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Create a Canvas to draw on the Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Create a Paint object for drawing text
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        paint.setTextSize(440); // Set text size
        paint.setAntiAlias(true); // Enable anti-aliasing for smoother text

        // Calculate the position to center the text
        float textWidth = paint.measureText(text);
        float x = (width - textWidth) / 2;
        float y = height / 2;
        // ===========
        Paint background = new Paint();
        background.setColor(Color.BLACK);
        // ===========
        Paint transparentPaint = new Paint();
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        // ====
        canvas.drawRect(0, 0, width, height, background);
        canvas.drawRect(50, 50, 300, 300, transparentPaint);

        // Draw the text on the Bitmap
        canvas.drawText(text, x, y, paint);

        // Return the Bitmap with the text drawn on it
        return bitmap;
    }


}
