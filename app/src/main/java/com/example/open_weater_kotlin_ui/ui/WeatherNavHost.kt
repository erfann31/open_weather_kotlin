package com.example.open_weater_kotlin_uiui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.open_weater_kotlin_uiui.detail.WeatherDetailUi
import com.example.open_weater_kotlin_uiui.detail.viewmodel.WeatherDetailViewModel
import com.example.open_weater_kotlin_uiui.detail.viewmodel.WeatherDetailViewModelImpl
import com.example.open_weater_kotlin_uiui.search.SearchUi
import com.example.open_weater_kotlin_uiui.search.viewmodel.SearchViewModel
import com.example.open_weater_kotlin_uiui.search.viewmodel.SearchViewModelImpl

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NavItem.WeatherDetail.nav_route) {
        composable(NavItem.WeatherDetail.nav_route) {
            val weatherDetailViewModel: WeatherDetailViewModel = hiltViewModel<WeatherDetailViewModelImpl>()

            WeatherDetailUi(
                navController = navController,
                weatherDetailViewModel = weatherDetailViewModel
            )
        }
        composable(NavItem.Search.nav_route) {
            val searchViewModel: SearchViewModel = hiltViewModel<SearchViewModelImpl>()

            SearchUi(
                navController = navController,
                searchViewModel = searchViewModel
            )
        }
    }
}

sealed class NavItem(
    val nav_route: String
) {
    object Search : NavItem(nav_route = "nav_search")

    object WeatherDetail : NavItem(nav_route = "nav_weather_detail")
}
