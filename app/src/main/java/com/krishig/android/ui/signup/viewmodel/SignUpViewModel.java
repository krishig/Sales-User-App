package com.krishig.android.ui.signup.viewmodel;

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
public class SignUpViewModel extends ViewModel {

    private LocalRepository localRepository;
    private RemoteRepository remoteRepository;

    private MutableLiveData<User> userSuccess = new MutableLiveData<>();
    private MutableLiveData<String> userError = new MutableLiveData<>();


    @Inject
    public SignUpViewModel(LocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public MutableLiveData<User> userSuccess() {
        return userSuccess;
    }

    public MutableLiveData<String> userError() {
        return userError;
    }


    public void signUp(RequestBody jsonObject, String accept, String authorisation) {
        remoteRepository.signUp(jsonObject, accept, authorisation).enqueue(new ApiCallback<User>() {
            @Override
            public void onSuccess(Response<User> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        userSuccess.setValue(response.body());
                    } else {
                        userError.setValue(response.message());
                    }
                } else {
                    userError.setValue(null);
                }
            }

            @Override
            public void onFailure(NetworkException networkException) {
                userError.setValue(networkException.getDisplayMessage());
            }
        });

    }


/*    private MutableLiveData<User> userOtpSuccess = new MutableLiveData<>();
    private MutableLiveData<String> userOtpError = new MutableLiveData<>();

    public MutableLiveData<User> userOtpSuccess() {
        return userOtpSuccess;
    }

    public MutableLiveData<String> userOtpError() {
        return userOtpError;
    }


    public void pinCode(String pinCode) {
        remoteRepository.pinCode(pinCode).enqueue(new ApiCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(Response<ArrayList<User>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        userOtpSuccess.setValue(response.body().get(0));
                    } else {
                        userOtpError.setValue(response.message());
                    }
                } else {
                    userOtpError.setValue(null);
                }
            }

            @Override
            public void onFailure(NetworkException networkException) {
                userOtpError.setValue(networkException.getDisplayMessage());
            }
        });

    }*/


}
