package com.example.livetracking.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.livetracking.model.Trip
import com.example.livetracking.navigation.LiveTrackingScreens
import com.example.livetracking.viewmodel.TripListViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TripListScreen(modifier: Modifier,navController: NavController,tripListViewModel: TripListViewModel= hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())



    val tripList by tripListViewModel.tripList.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        tripListViewModel.getTripList()
    })
    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                Text(
                    text = "Company Name",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                )
            }, scrollBehavior = scrollBehavior, actions = {
                IconButton(onClick = {
                    tripListViewModel.getTripList()}) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                }
                TextButton(onClick = { runBlocking {
                    tripListViewModel.logout()
                }
                navController.navigate(LiveTrackingScreens.LOGIN.name)
                }) {
                    Text(text = "Logout")
                }
            }
        )
    }, modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        Column(
            Modifier
                .padding(it)
                .padding(5.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                return@rememberPagerState 2
            }
            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(selected = pagerState.currentPage == 0, onClick = {
                    runBlocking {
                        scope.launch {
                            pagerState.scrollToPage(0)
                        }
                    }
                }) {
                    Text(text = "Live", modifier = Modifier.padding(10.dp))
                }
                Tab(selected = pagerState.currentPage == 1, onClick = {
                    scope.launch {
                        pagerState.scrollToPage(1)
                    }
                }) {
                    Text(text = "Past", modifier = Modifier.padding(10.dp))
                }
            }
            HorizontalPager(state = pagerState) {
                if (it == 0) {
                    LiveTripList(modifier = Modifier,tripList.orEmpty(),navController)
                } else if (
                    it == 1
                ) {

                }
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveTripList(modifier: Modifier, tripList: List<Trip>,navController: NavController) {
    AnimatedVisibility(visible = tripList.isEmpty()) {
        Column(modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    }
    LazyColumn {
        items(tripList) {
            var isOpened by remember {
                mutableStateOf(false)
            }
            Card(
                modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Row(
                    modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = it.name,modifier=modifier.fillMaxWidth(0.5f))
                    Row {
                        Button(onClick = { navController.navigate(LiveTrackingScreens.LIVE_TRACK.name+"/"+it.id) }) {
                            Text(text = "Start")
                        }
                        IconButton(onClick = { isOpened = !isOpened }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }

                }
                AnimatedVisibility(visible = isOpened, modifier = modifier.padding(20.dp)) {
                    Column {
                        Text(text = "Scheduled Time", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        Text(text = formattedDate(it.scheduledTime.orEmpty()), style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = modifier.height(5.dp))
                        Text(text = "Expected Deliver Time", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))


                        Text(text = formattedDate(it.expectedDeliverTime.orEmpty()), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun formattedDate(isoTime: String): String {
    val actual = OffsetDateTime.parse(isoTime, DateTimeFormatter.ISO_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")
    return actual.format(formatter)
}