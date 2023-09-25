package com.krishig.android.ui.login.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.krishig.android.data.remote.ApiCallback;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.exception.NetworkException;
import com.krishig.android.data.repository.LocalRepository;
import com.krishig.android.data.repository.RemoteRepository;
import com.krishig.android.model.User;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.RequestBody;
import retrofit2.Response;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private LocalRepository localRepository;
    private RemoteRepository remoteRepository;

    private MutableLiveData<User> userSuccess = new MutableLiveData<>();
    private MutableLiveData<String> userError = new MutableLiveData<>();
    private MutableLiveData<User> userOtpSuccess = new MutableLiveData<>();
    private MutableLiveData<String> userOtpError = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public MutableLiveData<User> userSuccess() {
        return userSuccess;
    }

    public MutableLiveData<String> userError() {
        return userError;
    }

    public MutableLiveData<User> userOtpSuccess() {
        return userOtpSuccess;
    }

    public MutableLiveData<String> userOtpError() {
        return userOtpError;
    }


    public void login(RequestBody body, String accept, String authorisation) {
        remoteRepository.login(body, accept, authorisation).enqueue(new ApiCallback<ApiResponseObject<User>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<User>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            userError.setValue(response.body().getMessage());
                        } else {
                            userSuccess.setValue(response.body().getData());
                        }
                    } else {
                        userError.setValue(response.body().getMessage());
                    }
                } else
                    userError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                userError.setValue(networkException.getDisplayMessage());
            }
        });

    }



}
