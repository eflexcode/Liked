package com.eflexsoft.liked.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.repository.LoadUsersRepository;

import java.util.List;

public class LoadItemsViewModel extends AndroidViewModel {

    public MutableLiveData<List<User>> listMutableLiveData = new MutableLiveData<>();

    LoadUsersRepository repository;

    public LoadItemsViewModel(@NonNull Application application) {
        super(application);
        repository = LoadUsersRepository.getInstance();
    }

    public void firstLoad(Context context, String usersToGetGender) {
        repository.firstLoad(context, usersToGetGender);
    }

    public LiveData<List<User>> getFirstUsersList() {
        listMutableLiveData = repository.listMutableLiveData;
        return listMutableLiveData;
    }

}
