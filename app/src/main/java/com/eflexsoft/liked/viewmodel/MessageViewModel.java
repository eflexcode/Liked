package com.eflexsoft.liked.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.Chat;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.repository.MessageRepository;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    MessageRepository repository;
    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Chat> > chatMutableLiveData = new MutableLiveData<>();

    public MessageViewModel(@NonNull Application application) {
        super(application);

        repository = new MessageRepository(application);

    }

    public LiveData<User> observeUserDetails() {

        userMutableLiveData = repository.userMutableLiveData;
        return userMutableLiveData;

    }

    public LiveData<List<Chat>> observeGetChats() {

        chatMutableLiveData = repository.chatMutableLiveData;
        return chatMutableLiveData;

    }

    public void doSendId(String id) {
        repository.getSearchedUserProfile(id);
    }

    public void sendMessage(String receiver, String senderId, String message,String chatId) {
        repository.sendMessage(receiver, senderId, message,chatId);
    }
    public void sendImageCamera(String receiver, String senderId, byte[] bytes) {
        repository.sendImageCamera(receiver, senderId, bytes);
    }

    public void sendImageGallery(String receiver, String senderId, Uri uri) {
        repository.sendImageGallery(receiver, senderId, uri);
    }
    public void getChat(String chatId, String receiverId){
        repository.getChat(chatId,receiverId);
    }

    public void getChatMore(String chatId, String receiverId) {
        repository.getChatMore(chatId,receiverId);
    }
    public void updateIsSeen(String chatId){
        repository.updateIsSeen(chatId);
    }
}
