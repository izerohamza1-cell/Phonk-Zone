package com.phonkzone.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Track(
    val title: String,
    val artist: String,
    val category: String
)

private val tracks = listOf(
    Track("Neon Drift", "PHONK ZONE", "Drift Phonk"),
    Track("Night Ride", "PHONK ZONE", "Chill Phonk"),
    Track("Brazil Night", "PHONK ZONE", "Brazilian Phonk"),
    Track("Dark Engine", "PHONK ZONE", "Aggressive Phonk"),
    Track("Retro Bass", "PHONK ZONE", "Classic Phonk")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhonkZoneApp()
        }
    }
}

@Composable
fun PhonkZoneApp() {
    var selectedTrack by remember { mutableStateOf<Track?>(null) }
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Home") }

    val filteredTracks = tracks.filter {
        it.title.contains(searchText, ignoreCase = true) ||
        it.artist.contains(searchText, ignoreCase = true) ||
        it.category.contains(searchText, ignoreCase = true)
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF080808),
            surface = Color(0xFF111111),
            primary = Color(0xFFFF3B30),
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Color(0xFF080808),

            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF111111)
                ) {
                    listOf("Home", "Search", "Favorites").forEach { tab ->

                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = {
                                selectedTab = tab
                            },
                            icon = {
                                Text(
                                    when (tab) {
                                        "Home" -> "⌂"
                                        "Search" -> "⌕"
                                        else -> "♥"
                                    }
                                )
                            },
                            label = {
                                Text(tab)
                            }
                        )
                    }
                }
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp)
            ) {

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "PHONK ZONE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = "ONLY PHONK. NOTHING ELSE.",
                    fontSize = 12.sp,
                    color = Color(0xFFFF3B30),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Search phonk...")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "PHONK CATEGORIES",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    listOf(
                        "Drift",
                        "Brazilian",
                        "Aggressive"
                    ).forEach { category ->

                        AssistChip(
                            onClick = {
                                searchText = category
                            },
                            label = {
                                Text(category)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "TRACKS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(filteredTracks) { track ->

                        TrackCard(
                            track = track,
                            onClick = {
                                selectedTrack = track
                            }
                        )
                    }
                }

                selectedTrack?.let { track ->

                    PlayerBar(
                        track = track,
                        onClose = {
                            selectedTrack = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackCard(
    track: Track,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF151515),
                RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            }
            .padding(14.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    Color(0xFF252525),
                    RoundedCornerShape(12.dp)
                ),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "♫",
                fontSize = 25.sp,
                color = Color(0xFFFF3B30)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = track.title,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = track.artist,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Text(
                text = track.category,
                color = Color(0xFFFF3B30),
                fontSize = 11.sp
            )
        }

        Text(
            text = "▶",
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun PlayerBar(
    track: Track,
    onClose: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1B1B1B),
                RoundedCornerShape(18.dp)
            )
            .padding(12.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = track.title,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = track.artist,
                color = Color.Gray,
                fontSize = 11.sp
            )
        }

        Text(
            text = "▶",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "×",
            fontSize = 22.sp,
            modifier = Modifier
                .clickable {
                    onClose()
                }
                .padding(8.dp)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}
