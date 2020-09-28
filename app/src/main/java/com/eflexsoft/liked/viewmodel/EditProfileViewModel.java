package com.eflexsoft.liked.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.repository.EditProfileRepository;

public class EditProfileViewModel extends AndroidViewModel {

    EditProfileRepository repository;

    public MutableLiveData<Boolean> isSuccessMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFailureMutableLiveData = new MutableLiveData<>();


    public EditProfileViewModel(@NonNull Application application) {
        super(application);

        repository = new EditProfileRepository(application);

    }

    public void updateProfile(final String names, final String Address, final String gender, final String age, final String about) {

        repository.updateProfile(names, Address, gender, age, about);

    }

    public LiveData<Boolean> isFailureLiveData() {

        isFailureMutableLiveData = repository.isFailureMutableLiveData;

        return isFailureMutableLiveData;

    }

    public LiveData<Boolean> isSuccessLiveData() {

        isSuccessMutableLiveData = repository.isSuccessMutableLiveData;

        return isSuccessMutableLiveData;

    }

}
