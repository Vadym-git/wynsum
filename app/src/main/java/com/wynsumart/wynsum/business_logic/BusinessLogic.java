package com.wynsumart.wynsum.business_logic;

import android.content.Context;

import com.wynsumart.wynsum.MyApp;
import com.wynsumart.wynsum.interfaces.Publisher;
import com.wynsumart.wynsum.interfaces.Subscriber;
import com.wynsumart.wynsum.models.DBHelper;
import com.wynsumart.wynsum.models.DataBase;
import com.wynsumart.wynsum.models.MeditationTargetContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BusinessLogic implements Publisher {
    private static DBHelper dbHelper;
    private List<Subscriber> subscribers = new ArrayList<>();

    public BusinessLogic(MyApp app) {
        dbHelper = app.getDbHelper();
    }

    public CompletableFuture<List<MeditationTargetContainer>> getAllTargets() {
        return CompletableFuture.supplyAsync(() -> {
            if (dbHelper.getAllTargets().isEmpty()) {
                for (MeditationTargetContainer target : basicTargetsStartList()) {
                    dbHelper.insertTargetToDb(target.id, target.name, target.icon, target.description,
                            target.short_description, target.guide);
                }
            }
            return dbHelper.getAllTargets();
        });
    }

    public void clearTargets() {
        dbHelper.clearTargets();
        notifySubscribers();
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.updateData();
        }
    }

    private List<MeditationTargetContainer> basicTargetsStartList() {
        List<MeditationTargetContainer> targetsList = new ArrayList<>();
        targetsList.add(new MeditationTargetContainer(3, "Relaxation",
                "https://cdn-icons-png.flaticon.com/128/2534/2534929.png", "",
                "Experience deep relaxation and release tension through guided meditations designed to calm the mind and body, promoting a sense of peace and tranquility.",
                "Find a quiet and comfortable space where you won't be disturbed.\n" +
                        "Close your eyes and take a few deep, slow breaths to settle your body and mind.\n" +
                        "Bring your attention to the present moment, letting go of any worries or distractions.\n" +
                        "Engage in relaxation techniques like deep breathing, progressive muscle relaxation, or visualization.\n" +
                        "Allow yourself to let go of tension, experience deep calmness, and promote a state of relaxation."
        ));
        targetsList.add(new MeditationTargetContainer(2, "Stress Relief",
                "https://cdn-icons-png.flaticon.com/128/3476/3476366.png", "",
                "Find relief from the pressures of daily life with targeted meditations that help you unwind, manage stress, and cultivate a greater sense of inner calm and balance.",
                "Sit comfortably and bring your awareness to your breath.\n" +
                        "Notice the sensations of the breath entering and leaving your body.\n" +
                        "As thoughts or distractions arise, gently bring your attention back to your breath.\n" +
                        "Practice observing your thoughts and emotions without judgment, allowing them to come and go.\n" +
                        "Cultivate a sense of calm and resilience in the face of stress, letting go of stressors as you focus on the present moment."
        ));
        targetsList.add(new MeditationTargetContainer(4, "Mindfulness",
                "https://cdn-icons-png.flaticon.com/128/4310/4310381.png", "",
                "Develop your mindfulness practice with guided meditations that enhance your present-moment awareness, allowing you to fully engage with your thoughts, feelings, and surroundings.",
                "Sit comfortably and bring your awareness to your breath.\n" +
                        "Notice the sensations of the breath entering and leaving your body.\n" +
                        "As thoughts or distractions arise, gently bring your attention back to your breath.\n" +
                        "Practice observing your thoughts and emotions without judgment, allowing them to come and go.\n" +
                        "Cultivate a sense of calm and resilience in the face of stress, letting go of stressors as you focus on the present moment."
        ));
        targetsList.add(new MeditationTargetContainer(7, "Focus and Concentration",
                "https://cdn-icons-png.flaticon.com/128/5265/5265809.png", "",
                "Enhance your ability to concentrate and stay focused through guided meditations that improve mental clarity, attention, and productivity.",
                "Sit comfortably and bring your awareness to your breath.\n" +
                        "Notice the sensations of the breath entering and leaving your body.\n" +
                        "As thoughts or distractions arise, gently bring your attention back to your breath.\n" +
                        "Practice observing your thoughts and emotions without judgment, allowing them to come and go.\n" +
                        "Cultivate a sense of calm and resilience in the face of stress, letting go of stressors as you focus on the present moment."
        ));
        targetsList.add(new MeditationTargetContainer(5, "Gratitude and Positivity",
                "https://cdn-icons-png.flaticon.com/128/6585/6585744.png", "",
                "Cultivate gratitude and foster a positive mindset with meditations that encourage you to appreciate the present moment, shift your perspective, and find joy in the simple things in life.",
                "Sit comfortably and bring your awareness to your breath.\n" +
                        "Notice the sensations of the breath entering and leaving your body.\n" +
                        "As thoughts or distractions arise, gently bring your attention back to your breath.\n" +
                        "Practice observing your thoughts and emotions without judgment, allowing them to come and go.\n" +
                        "Cultivate a sense of calm and resilience in the face of stress, letting go of stressors as you focus on the present moment."
        ));
        return targetsList;
    }
}
