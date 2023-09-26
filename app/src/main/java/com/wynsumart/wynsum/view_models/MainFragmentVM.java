package com.wynsumart.wynsum.view_models;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;
import android.util.Log;


import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.wynsumart.wynsum.MyApp;
import com.wynsumart.wynsum.business_logic.BusinessLogic;
import com.wynsumart.wynsum.interfaces.Subscriber;
import com.wynsumart.wynsum.models.DBHelper;
import com.wynsumart.wynsum.models.DataBase;
import com.wynsumart.wynsum.models.MeditationTargetContainer;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentVM extends ViewModel implements Subscriber {
    BusinessLogic businessLogic;
    private final MutableLiveData<List<MeditationTargetContainer>> _liveData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<MeditationTargetContainer>> getLiveData() {
        return _liveData;
    }

    public MainFragmentVM(@NonNull Application app) {
        businessLogic = new BusinessLogic((MyApp) app);
        businessLogic.addSubscriber(this);
        getTargetsData();
    }

    public static final ViewModelInitializer<MainFragmentVM> initializer = new ViewModelInitializer<>(
            MainFragmentVM.class,
            creationExtras -> {
                Application app = (Application) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainFragmentVM(app);
            }
    );

    private void getTargetsData() {
        businessLogic.getAllTargets().thenAcceptAsync(_liveData::postValue);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        businessLogic.removeSubscriber(this);
    }

    public void clearTargets(){
        businessLogic.clearTargets();
    }

    @Override
    public void updateData() {
        getTargetsData();
    }
}
