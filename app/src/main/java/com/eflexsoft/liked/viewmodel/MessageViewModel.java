package com.eflexsoft.liked.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.Chat;
import com.eflexsoft.liked.model.Like;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.repository.MessageRepository;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    MessageRepository repository;
    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Like>> likeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Chat>> chatMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Chat>> chatMoreMutableLiveData = new MutableLiveData<>();

    public MessageViewModel(@NonNull Application application) {
        super(application);

        repository = new MessageRepository(application);
        repository.getFirstLikes();

    }

//    public LiveData<User> observeUserDetails() {
//
//        userMutableLiveData = repository.userMutableLiveData;
//        return userMutableLiveData;
//
//    }

    public LiveData<List<Like>> observeGetChats() {

        likeMutableLiveData = repository.likeMutableLiveData;
        return likeMutableLiveData;

    }

    public LiveData<List<Chat>> observeGetMessage() {

        chatMutableLiveData = repository.chatMutableLiveData;
        return chatMutableLiveData;

    }

    public LiveData<List<Chat>> observeGetMoreMessage() {

        chatMoreMutableLiveData = repository.chatMoreMutableLiveData;
        return chatMoreMutableLiveData;

    }

//    public void doSendId(String id) {
//        repository.getSearchedUserProfile(id);
//    }
//

    public void sendMessage(String firstId, String secondId, String message, String messageId) {
        repository.sendMessage(firstId, secondId, message, messageId);
    }

    //    public void sendImageCamera(String receiver, String senderId, byte[] bytes, String chatId) {
//        repository.sendImageCamera(receiver, senderId, bytes,chatId);
//    }
//
    public void sendImageGallery(Uri uri, String messageId, String firstId, String secondId) {
        repository.sendImageGallery(uri, messageId, firstId, secondId);
    }

    public void getMessage(String messageId) {
        repository.getMessage(messageId);
    }

    public void getMessageMore(String messageId) {
        repository.getMessageMore(messageId);
    }
//    public void updateIsSeen(String chatId){
//        repository.updateIsSeen(chatId);
//    }
}
