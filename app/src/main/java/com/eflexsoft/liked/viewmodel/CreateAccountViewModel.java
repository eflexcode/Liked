package com.eflexsoft.liked.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.repository.CreateAccountRepository;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public class CreateAccountViewModel extends AndroidViewModel {

    CreateAccountRepository repository;

    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> cameraMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> galleryMutableLiveData = new MutableLiveData<>();

    public CreateAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new CreateAccountRepository(application);
    }

    public void createAccountEmailPassword(final String names, final String gender, final String age, final String email, String password) {

        repository.createAccountEmailPassword(names, gender, age, email, password);

    }

    public void createAccountCredential(AuthCredential authCredential, final String names, final String gender, final String age) {

        repository.createAccountCredential(authCredential, names, gender, age);

    }

    public LiveData<Boolean> booleanLiveData() {

        booleanMutableLiveData = repository.booleanMutableLiveData;

        return booleanMutableLiveData;

    }

    public LiveData<String> getCameraMutableLiveData() {
        return cameraMutableLiveData;
    }

    public LiveData<String> getGalleryMutableLiveData() {
        return galleryMutableLiveData;
    }

    public void publishProfile(Uri profileUrl, String about, List<Uri> displayImages, double log, double lat) {
        repository.publishProfile(profileUrl, about, displayImages, log, lat);
    }
}
