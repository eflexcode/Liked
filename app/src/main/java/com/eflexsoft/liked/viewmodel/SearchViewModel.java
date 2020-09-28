package com.eflexsoft.liked.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.repository.ProfileRepository;
import com.eflexsoft.liked.repository.SearchRepository;

public class SearchViewModel extends AndroidViewModel {

    SearchRepository repository;
    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();


    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new SearchRepository(application);

    }

    public LiveData<User> observeUserDetails(){

        userMutableLiveData = repository.userMutableLiveData;
        return userMutableLiveData;

    }

    public void doSendId(String  id){
        repository.getSearchedUserProfile(id);
    }

    public void sendHiMessage(String senderId,String receiverId,String chatId){
        repository.sendHiMessage(senderId, receiverId, chatId);
    }

//    public LiveData<Boolean> booleanLiveData() {
//
//        booleanMutableLiveData = repository.booleanMutableLiveData;
//
//        return booleanMutableLiveData;
//
//    }

}