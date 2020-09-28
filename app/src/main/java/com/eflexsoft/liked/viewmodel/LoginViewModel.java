package com.eflexsoft.liked.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.repository.CreateAccountRepository;
import com.eflexsoft.liked.repository.LoginRepository;
import com.google.firebase.auth.AuthCredential;

public class LoginViewModel extends AndroidViewModel {

    LoginRepository repository;

    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(application);
    }

    public void doLogin( final String email, String password) {

        repository.doLogin(email, password);

    }

    public void loginCredential(AuthCredential authCredential) {

        repository.loginCredential(authCredential);

    }

    public LiveData<Boolean> booleanLiveData() {

        booleanMutableLiveData = repository.booleanMutableLiveData;

        return booleanMutableLiveData;

    }

}
