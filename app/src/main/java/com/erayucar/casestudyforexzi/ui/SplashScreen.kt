package com.erayucar.casestudyforexzi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.erayucar.casestudyforexzi.R
import com.erayucar.casestudyforexzi.ui.theme.NavyBlue
import com.erayucar.casestudyforexzi.ui.theme.White80
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onCompleted: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(2000)
        onCompleted()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            tint = White80,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.size(80.dp))
        CircularProgressIndicator(color = White80)
    }
}