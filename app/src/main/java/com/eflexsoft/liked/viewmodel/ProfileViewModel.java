package com.eflexsoft.liked.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.repository.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel {

    ProfileRepository repository;
    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        repository.getUserProfileDetails();
    }

    public LiveData<User> observeUserDetails(){

        userMutableLiveData = repository.userMutableLiveData;
        return userMutableLiveData;

    }


    public LiveData<Boolean> booleanLiveData() {

        booleanMutableLiveData = repository.booleanMutableLiveData;

        return booleanMutableLiveData;

    }

    public void uploadImageByte(byte[] bytes) {
        repository.uploadImageByte(bytes);
    }

    public void uploadImageUri(Uri uri) {
        repository.uploadImageUri(uri);
    }
    public void setIsOnline(String status){
        repository.setIsOnline(status);
    }
}