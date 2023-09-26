package com.wynsumart.wynsum;
// Wynsum (Old English) - "Pleasant, agreeable, gracious"

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.wynsumart.wynsum.databinding.ActivityMainLayoutBinding;
import com.wynsumart.wynsum.interfaces.Navigator;
import com.wynsumart.wynsum.screens.MainFragment;
import com.wynsumart.wynsum.screens.TargetFragment;

public class MainActivity extends AppCompatActivity implements Navigator {
    private ActivityMainLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainLayoutBinding.inflate(getLayoutInflater());
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportFragmentManager()
                .beginTransaction()
                        .add(binding.mainFragmentsContainer.getId(), new MainFragment())
                                .commit();
        setContentView(binding.getRoot());
    }

    @Override
    public void openTarget(int targetId) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(binding.mainFragmentsContainer.getId(), TargetFragment.newInstance(targetId))
                .commit();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}