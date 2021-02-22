package com.eflexsoft.liked.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.eflexsoft.liked.LoginEmailActivity;
import com.eflexsoft.liked.LoginNumActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.SingInAsLayoutBinding;
import com.eflexsoft.liked.signup.NameActivity;
import com.eflexsoft.liked.viewmodel.LoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SingInFragmentBottomSheet extends BottomSheetDialogFragment {


    SingInAsLayoutBinding binding;

    String action;

    LoginViewModel viewModel;

    public SingInFragmentBottomSheet(String action) {
        this.action = action;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.sing_in_as_layout, container, false);

//        Toast.makeText(getContext(), action, Toast.LENGTH_LONG).show();

        View view = binding.getRoot();

        viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        binding.oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if (action.equals("number")) {
                    startActivity(new Intent(getContext(), LoginNumActivity.class));
                } else if (action.equals("google")) {
                    viewModel.googleMutableLiveData.setValue(true);
                } else if (action.equals("email")) {

                    startActivity(new Intent(getContext(), LoginEmailActivity.class));

                } else {
                    viewModel.fbMutableLiveData.setValue(true);
                }
            }
        });

        binding.newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if (action.equals("number")) {
                    startActivity(new Intent(getContext(), NameActivity.class).putExtra("isNum",true));
                } else if (action.equals("google")) {
                    viewModel.googleMutableLiveData.setValue(true);
                } else if (action.equals("email")) {

                    startActivity(new Intent(getContext(), NameActivity.class));

                } else {
                    viewModel.fbMutableLiveData.setValue(true);
                }
            }
        });

        return view;
    }
}
