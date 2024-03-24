package com.example.open_weater_kotlin_ui.module;

import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchers;
import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchersImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class CoroutinesModule {

    @Singleton
    @Provides
    CoroutineDispatchers coroutinesDispatchers() {
        return CoroutineDispatchersImpl.INSTANCE;
    }
}
