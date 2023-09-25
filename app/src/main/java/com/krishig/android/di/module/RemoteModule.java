package com.krishig.android.di.module;

import android.content.Context;

import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.OkHttpClientHelper;
import com.krishig.android.data.remote.helper.RetrofitHelper;
import com.krishig.android.di.qualifier.isDebug;
import com.krishig.android.di.qualifier.remote.ApiKey;
import com.krishig.android.di.qualifier.remote.BaseUrl;
import com.krishig.android.ui.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


@Module
@InstallIn(SingletonComponent.class)
public class RemoteModule {

    @Provides
    @Singleton
    @isDebug
    boolean provideIsDebug() {
        return AppConstants.AppConfig.IS_DEBUG;
    }

    @Provides
    @Singleton
    @BaseUrl
    String provideBaseUrl() {
        return AppConstants.APIPath.BASE_URL;
    }

    @Provides
    @Singleton
    @ApiKey
    String provideApiKey() {
        return AppConstants.APIPath.BASE_URL;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context, @isDebug boolean isDebug) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance().new Builder(context);
        builder.setShowLog(isDebug);
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@BaseUrl String baseUrl, OkHttpClient okHttpClient) {
        RetrofitHelper.Builder builder = RetrofitHelper.getInstance().new Builder(baseUrl);
        builder.setOkHttpClient(okHttpClient);
        return builder.build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
