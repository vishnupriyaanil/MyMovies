package com.circuithouse.mymovies.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.circuithouse.mymovies.ui.navigation.Screen
import com.circuithouse.mymovies.R

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate(Screen.GENRES.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.select_fav_genres),
                fontSize = 18.sp,
            )
        }
        Button(
            onClick = { navController.navigate(Screen.MOVIES.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.select_fav_movies),
                fontSize = 18.sp,
            )
        }
        Button(
            onClick = { navController.navigate(Screen.MY_SELECTION.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.view_selection),
                fontSize = 18.sp,
            )
        }
    }
}