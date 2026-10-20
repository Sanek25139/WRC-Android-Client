package com.example.wrc.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.wrc.data.MediaPlaybackState
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.RemoteControllerVM
import com.example.wrc.vms.toMediaTime

@Composable
fun RemoteController(vm: RemoteControllerVM){

    val iconColor = if(isSystemInDarkTheme()) Color.White else Color.Black


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .padding(top = 15.dp)) {


        Row(Modifier.padding(horizontal = 15.dp)) {
            Column {
                AnimatedVisibility(
                    visible = vm.media.mediaState != MediaPlaybackState.Closed,
                    enter = expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top
                    ) + fadeOut()
                ) {
                    Column{
                        val imageSize by animateDpAsState(
                            if (vm.media.imageBitmap != null) 250.dp else 0.dp,
                            label = "imageSize"
                        )

                        val imageAlpha by animateFloatAsState(
                            if (vm.media.imageBitmap != null) 1f else 0f,
                            label = "imageAlpha"
                        )
                        Row(Modifier
                            .padding(vertical = 25.dp)
                            .height(imageSize)
                            .fillMaxWidth()
                            .alpha(imageAlpha)
                            .align(Alignment.CenterHorizontally)) {
                            Box(Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterVertically)) {
                                vm.media.imageBitmap?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
                        Row { Text(vm.media.title) }
                        Row { Text(vm.media.artist) }

                        Row{
                            if(vm.media.canSeek){
                                Box(Modifier
                                    .weight(1f)
                                    .height(60.dp)){
                                    Text(if(vm.media.changedSeek) vm.media.seek.toMediaTime() else vm.media.currentMs.toMediaTime(),Modifier.align(Alignment.TopStart))
                                    Text(vm.media.durationMs.toMediaTime(),Modifier.align(Alignment.TopEnd))
                                    Slider(
                                        value = if(vm.media.changedSeek) vm.media.seek.toFloat() else vm.media.currentMs.toFloat(),
                                        onValueChange = { vm.media.changedSeek = true; vm.media.seek = it.toLong() },
                                        onValueChangeFinished = vm.media::seekChangedFinished,
                                        valueRange =0f..vm.media.durationMs.toFloat(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                    )
                                }
                            }
                        }
                    }

                }
            }

        }
        val barPadding by animateDpAsState(
            if(vm.media.mediaState == MediaPlaybackState.Closed) 75.dp else 0.dp,
            label = "barPadding"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier =  Modifier.padding(top = barPadding)) {

            val skipScale by animateFloatAsState(
                if (vm.media.durationMs != 0L) 1.5f else 0f,
                label = "skipScale"
            )
            val seekOffset by animateDpAsState(
                if (vm.media.durationMs == 0L) 55.dp else 0.dp
            )
            val nextOffset by animateDpAsState(
                if (vm.media.durationMs == 0L) 70 .dp else 0.dp
            )

            IB(
                AppIcons.SkipPrevious ,
                if(vm.media.canPrevious) iconColor else Color.Gray,
                {vm.net.sendTcp(0x04)},
                modifier =  Modifier.offset(x = nextOffset))


            IB(
                AppIcons.FastRewind,
                iconColor,
                vm.media::fastRewindSeek,
                iconScale = 1f,
                buttonScale = skipScale,
                modifier =  Modifier.offset(x = seekOffset)
            )
            when(vm.media.mediaState){
                MediaPlaybackState.Playing -> IB(AppIcons.Pause,iconColor,{vm.net.sendTcp(0x02)}, iconScale = 1.2f)
                MediaPlaybackState.Paused -> IB(AppIcons.Play,iconColor,{vm.net.sendTcp(0x01)},iconScale = 1.2f)
                else -> { IB(AppIcons.PlayPause,iconColor,{vm.net.sendTcp(0x01)},iconScale = 1.2f)}
            }
            IB(
                AppIcons.FastForward,
                iconColor,vm.media::fastForwardSeek,
                iconScale = 1f,
                buttonScale = skipScale,
                modifier =  Modifier.offset(x = -seekOffset)
                )
            IB(
                AppIcons.SkipNext,
                if(vm.media.canNext) iconColor else Color.Gray,{vm.net.sendTcp(0x03)},
                modifier =  Modifier.offset(x = -nextOffset))
        }
        Row(Modifier.padding(horizontal = 15.dp)){


            IconButton(vm.audio::sendSetMute, Modifier.scale(1.1f)) {
                Icon(if(vm.audio.isMute) AppIcons.VolumeOff else AppIcons.VolumeUp,null, tint = iconColor, modifier =  Modifier.scale(1.1f))
            }
            Slider(
                value = vm.audio.volume,
                onValueChange = vm.audio::seekVolume,
                onValueChangeFinished = {vm.audio.seekVolumeChanged = false},
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f)
            )
        }

    }

}
@Composable
fun IB(imageVector: ImageVector, color: Color, onClick: ()-> Unit, modifier: Modifier = Modifier, iconScale: Float = 1.5f, buttonScale: Float = 1.5f,enabled: Boolean = true ){
    IconButton(onClick, modifier.scale(buttonScale), enabled = enabled) {
        Icon(imageVector,null, tint = color, modifier =  Modifier.scale(iconScale))
    }
}