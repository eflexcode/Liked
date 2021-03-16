package com.eflexsoft.liked.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.MessageLayoutBinding;
import com.eflexsoft.liked.model.Chat;
import com.eflexsoft.liked.model.Like;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.MessageListViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageListViewHolder> {

    List<Like> likes = new ArrayList<>();
    Context context;
    boolean canLoad = true;

    public MessageAdapter(List<Like> likes, Context context) {
        this.likes = likes;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        MessageLayoutBinding messageLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.message_layout, parent, false);

        return new MessageListViewHolder(messageLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {

        Like like = likes.get(position);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users")
                .document(like.getUserId());

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    User user = value.toObject(User.class);

                    holder.binding.nameMessage.setText(user.getName());

                    if (user.getIsOnline().equals("yes")) {
                        holder.binding.idOnline.setImageResource(R.color.on_line);
                    } else {
                        holder.binding.idOnline.setImageResource(R.color.off_line);
                    }

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.error(R.drawable.no_p);
                    requestOptions.placeholder(R.drawable.no_p);

                    Glide.with(context).load(user.getProfilePictureUrl())
                            .apply(requestOptions)
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;

                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(holder.binding.proPicMessage);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        Query query = FirebaseFirestore.getInstance()
                .collection("Messages").document(like.getMessageId()).collection(like.getMessageId()).orderBy("chatId", Query.Direction.DESCENDING).limit(1);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot
                                        value, @Nullable FirebaseFirestoreException error) {

                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {

                    Chat chat = documentSnapshot.toObject(Chat.class);
                    holder.binding.last.setText(chat.getMessage());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);

                    PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

                    try {
                        if (chat.getDate() != null) {
                            Date date = dateFormat.parse(chat.getDate());
                            holder.binding.dateMessage.setText(prettyTime.format(date));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }

            }
        });





}

    @Override
    public int getItemCount() {
        return likes.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && canLoad) {
                    canLoad = false;
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    String myId = firebaseAuth.getUid();

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    Like like = likes.get(likes.size() - 1);

                    Query myReference = firebaseFirestore.collection("Users")
                            .document(myId).collection("Likes").orderBy("messageId", Query.Direction.DESCENDING).startAfter(like.getMessageId()).limit(20);

                    myReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                Like like = documentSnapshot.toObject(Like.class);
                                if (like != null)
                                likes.add(like);

                            }
                            canLoad = true;
                            notifyDataSetChanged();
                        }
                    });

                }
            }
        });
    }
}
