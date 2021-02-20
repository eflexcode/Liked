package com.eflexsoft.liked.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.LoginActivity;
import com.eflexsoft.liked.repository.LoginRepository;
import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;

public class LoginViewModel extends AndroidViewModel {

    LoginRepository repository;

    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> googleMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> fbMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> accessUserLocationMutableLiveData = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(application);
    }

    public void doLogin( final String email, String password) {

        repository.doLogin(email, password);

    }

    public void loginCredential(AuthCredential authCredential, int age, String gender, LoginActivity loginActivity) {

        repository.loginCredential(authCredential,age,gender,loginActivity);

    }

    public LiveData<Boolean> booleanLiveData() {

        booleanMutableLiveData = repository.booleanMutableLiveData;

        return booleanMutableLiveData;

    }
    public void loginCredentialPhone(AuthCredential authCredential) {
        repository.loginCredentialPhone(authCredential);
    }
    public LiveData<Boolean> getGoogleMutableLiveData() {
        return googleMutableLiveData;
    }

    public LiveData<Boolean> getFbMutableLiveData() {
        return fbMutableLiveData;
    }

    public void doSignInWithFb(AccessToken token, AuthCredential authCredential,int age,String gender,LoginActivity loginActivity) {
        repository.doSignInWithFb(token,authCredential,age,gender,loginActivity);
    }

    public LiveData<Boolean> getAccessUserLocationMutableLiveData() {
        return accessUserLocationMutableLiveData;
    }
    public void updateMyLocation(double longitude,double latitude){
        repository.updateMyLocation(longitude, latitude);
    }
}
