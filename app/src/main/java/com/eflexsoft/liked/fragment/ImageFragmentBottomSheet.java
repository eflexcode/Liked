package com.eflexsoft.liked.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.eflexsoft.liked.LoginEmailActivity;
import com.eflexsoft.liked.LoginNumActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ChoseUploadPicBinding;
import com.eflexsoft.liked.databinding.SingInAsLayoutBinding;
import com.eflexsoft.liked.signup.NameActivity;
import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.eflexsoft.liked.viewmodel.LoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImageFragmentBottomSheet extends BottomSheetDialogFragment {


    ChoseUploadPicBinding binding;

    String action;

    CreateAccountViewModel viewModel;

    public ImageFragmentBottomSheet(String action) {
        this.action = action;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.chose_upload_pic, container, false);

//        Toast.makeText(getContext(), action, Toast.LENGTH_LONG).show();

        View view = binding.getRoot();

        viewModel = new ViewModelProvider(getActivity()).get(CreateAccountViewModel.class);

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                viewModel.galleryMutableLiveData.setValue(action);

//                if (action.equals("number")) {
////                    startActivity(new Intent(getContext(), LoginNumActivity.class));
//                } else if (action.equals("google")) {
////                    viewModel.googleMutableLiveData.setValue(true);
//                } else if (action.equals("email")) {
//
////                    startActivity(new Intent(getContext(), LoginEmailActivity.class));
//
//                } else {
////                    viewModel.fbMutableLiveData.setValue(true);
//                }
            }
        });

        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                viewModel.cameraMutableLiveData.setValue(action);

//                if (action.equals("number")) {
////                    startActivity(new Intent(getContext(), NameActivity.class).putExtra("isNum", true));
//                } else if (action.equals("google")) {
////                    viewModel.googleMutableLiveData.setValue(true);
//                } else if (action.equals("email")) {
//
////                    startActivity(new Intent(getContext(), NameActivity.class));
//
//                } else {
////                    viewModel.fbMutableLiveData.setValue(true);
//                }
            }
        });

        return view;
    }
}
