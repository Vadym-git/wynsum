package com.wynsumart.wynsum.view_models;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TargetFragmentVM extends ViewModel {
    private CountDownTimer timer;
    private final MutableLiveData<Long> timeLeft = new MutableLiveData(60L);
    private final MutableLiveData<Boolean> _isTimerGoing = new MutableLiveData(false);
    public LiveData<Boolean> isTimerGoing(){
        return _isTimerGoing;
    }
    public LiveData<Long> getTimeLeft() {
        return timeLeft;
    }

    public void startMeditation() {
        if (_isTimerGoing.getValue()) {
            timer.cancel();
            _isTimerGoing.setValue(false);
        } else {
            timer = initTimer(timeLeft.getValue()*1000);
            timer.start();
            _isTimerGoing.setValue(true);
        }
    }

    private CountDownTimer initTimer(long time){
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setValue(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                _isTimerGoing.setValue(false);
                timeLeft.setValue(60L); // mast me changed !!! get time from the settings
            }
        };
        return timer;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        timer = null;
    }

}