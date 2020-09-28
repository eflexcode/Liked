package com.eflexsoft.liked.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.repository.CreateAccountRepository;
import com.google.firebase.auth.AuthCredential;

public class CreateAccountViewModel extends AndroidViewModel {

    CreateAccountRepository repository;

    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();


    public CreateAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new CreateAccountRepository(application);
    }

    public void createAccountEmailPassword(final String names, final String Address, final String gender, final String age, final String about, final String email, String password) {

        repository.createAccountEmailPassword(names, Address, gender, age, about, email, password);

    }

    public void createAccountCredential(AuthCredential authCredential, final String names, final String Address, final String gender, final String age, final String about) {

        repository.createAccountCredential(authCredential, names, Address, gender, age, about);

    }

    public LiveData<Boolean> booleanLiveData() {

        booleanMutableLiveData = repository.booleanMutableLiveData;

        return booleanMutableLiveData;

    }

}
