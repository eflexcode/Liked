package com.eflexsoft.liked.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.repository.DiscoverRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverViewModel extends AndroidViewModel {

    public MutableLiveData<List<User>> firstLoadMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> nextLoadMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPageAtLastMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> isPageLoadedMutableLiveData = new MutableLiveData<>();


    DiscoverRepository repository;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        repository = new DiscoverRepository(application);
        repository.doFirstLoad();
    }

    public LiveData<List<User>> observerLoadFirst() {

        firstLoadMutableLiveData = repository.firstLoadMutableLiveData;

        return firstLoadMutableLiveData;

    }

    public void doLoadNext(String afterId) {
        repository.doLoadNext(afterId);
    }

    public LiveData<List<User>> observerLoadNext() {

        nextLoadMutableLiveData = repository.nextLoadMutableLiveData;

        return nextLoadMutableLiveData;

    }

    public LiveData<Boolean> observerIsPageAt() {

        return isPageAtLastMutableLiveData;

    }
    public void sendHiMessage(String senderId,String receiverId,String chatId){

        repository.sendHiMessage(senderId, receiverId, chatId);

    }

    public LiveData<Boolean> getIsPageLoadedMutableLiveData() {
        return isPageLoadedMutableLiveData;
    }
}
