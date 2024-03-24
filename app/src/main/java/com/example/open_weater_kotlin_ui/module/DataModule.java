package com.example.open_weater_kotlin_ui.module;

import android.content.Context;

import com.example.open_weater_kotlin_uidata.datasource.WeatherApiDataSource;
import com.example.open_weater_kotlin_uidata.datasource.WeatherLocalDataSource;
import com.example.open_weater_kotlin_uidata.weather.WeatherRepository;
import com.example.open_weater_kotlin_uidata.weather.WeatherRepositoryImpl;
import com.example.open_weater_kotlin_uidata.weather.WeatherUseCase;
import com.example.open_weater_kotlin_uinetwork.WeatherApi;
import com.example.open_weater_kotlin_uiroom.DatabaseFactory;
import com.example.open_weater_kotlin_uiroom.WeatherDao;
import com.example.open_weater_kotlin_uiroom.WeatherDatabase;
import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DataModule {

    @Singleton
    @Provides
    WeatherApiDataSource weatherApiDataSource(
            WeatherApi weatherApi,
            CoroutineDispatchers coroutineDispatchers
    ) {
        return new WeatherApiDataSource(
                weatherApi,
                coroutineDispatchers
        );
    }

    @Singleton
    @Provides
    WeatherRepository weatherRepository(
            WeatherApiDataSource weatherApiDataSource,
            WeatherLocalDataSource weatherLocalDataSource
    ) {
        return new WeatherRepositoryImpl(
                weatherApiDataSource,
                weatherLocalDataSource
        );
    }

    @Singleton
    @Provides
    WeatherUseCase weatherUseCase(
            WeatherRepository weatherRepository
    ) {
        return new WeatherUseCase(
                weatherRepository
        );
    }


    @Singleton
    @Provides
    WeatherDatabase weatherDatabase(
            @ApplicationContext Context context
    ) {
        return DatabaseFactory.Companion.getWeatherDatabase(context);
    }


    @Singleton
    @Provides
    WeatherDao weatherDao(
            WeatherDatabase weatherDatabase
    ) {
        return weatherDatabase.weatherDao();
    }


    @Singleton
    @Provides
    WeatherLocalDataSource weatherLocalDataSource(
            WeatherDao weatherDao,
            CoroutineDispatchers coroutineDispatchers
    ) {
        return new WeatherLocalDataSource(
                weatherDao,
                coroutineDispatchers
        );
    }
}
