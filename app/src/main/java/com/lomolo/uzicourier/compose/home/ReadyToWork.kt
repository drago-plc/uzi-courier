package com.lomolo.uzicourier.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lomolo.uzicourier.MainViewModel
import com.lomolo.uzicourier.compose.signin.SessionViewModel
import com.lomolo.uzicourier.model.CourierStatus
import com.lomolo.uzicourier.model.Session
import kotlinx.coroutines.launch

@Composable
internal fun ReadyToWork(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    sessionViewModel: SessionViewModel,
    snackbarHostState: SnackbarHostState,
    session: Session
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (session.courierStatus == CourierStatus.OFFLINE) {
                Text(
                    "Ready to work?",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                Text(
                    "Go offline?",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val s = if (session.courierStatus == CourierStatus.OFFLINE)
                        CourierStatus.ONLINE.toString()
                    else
                        CourierStatus.OFFLINE.toString()

                    mainViewModel.setCourierStatus(s) {
                        sessionViewModel.refreshSession()
                        scope.launch { snackbarHostState.showSnackbar("You are now ${s.lowercase()}!") }
                    }
                },
                contentPadding = PaddingValues(16.dp),
                colors = ButtonColors(
                    containerColor = if (session.courierStatus == CourierStatus.ONLINE) Color(0xff1b7f37) else ButtonDefaults.buttonColors().containerColor,
                    contentColor = ButtonDefaults.buttonColors().contentColor,
                    disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor,
                    disabledContentColor = ButtonDefaults.buttonColors().disabledContentColor
                ),
                modifier = Modifier
                    .padding(16.dp),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = "Go",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}
