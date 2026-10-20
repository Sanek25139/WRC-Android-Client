package com.example.wrc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wrc.data.ComputerData
import com.example.wrc.data.RawCommand
import com.example.wrc.services.ConnectionState
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.EventBus
import com.example.wrc.services.ServerManager
import com.example.wrc.services.di.DIContext
import com.example.wrc.ui.components.ConnectionLostDialog
import com.example.wrc.ui.screens.AddComputerScreen
import com.example.wrc.ui.screens.EditComputerScreen
import com.example.wrc.ui.screens.MainScreen
import com.example.wrc.ui.screens.RemoteScreen
import com.example.wrc.ui.theme.WRCTheme
import com.example.wrc.vms.AddComputerScreenVM
import com.example.wrc.vms.EditComputerScreenVM
import com.example.wrc.vms.MainScreenVM
import com.example.wrc.vms.RemoteScreenVM
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.InetSocketAddress

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        ContextCompat.startForegroundService(
            applicationContext,
            Intent(applicationContext, WRCService::class.java)
        )


        lifecycleScope.launch {

            val service = WRCService.instance.filterNotNull().first()

            val eventBus = EventBus {

            }

            val di = DIContext {
                single { MainScreenVM() }
                single { AddComputerScreenVM() }
                single { EditComputerScreenVM() }
                single { RemoteScreenVM() }
                single { applicationContext }
                single { service.serverManager }
                single { eventBus }

                single { DataLocalService {
                    fileData<ComputerData>("computers")
                }}
                setContent {
                    WRCTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            App(innerPadding,di);
                        }
                    }
                }
            // здесь сервис уже есть
        }




        }

        super.onCreate(savedInstanceState)


    }
}


@Composable
fun App(padding: PaddingValues, di: DIContext){
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        val service = WRCService.instance.value
            ?: return@LaunchedEffect

        val serverManager = service.serverManager
        val endpoint = serverManager.activeEndPoint
            ?: return@LaunchedEffect

        if (serverManager.connectionState != ConnectionState.Connected)
            return@LaunchedEffect

        val computer = di
            .get<DataLocalService>()
            .get<ComputerData>()
            .firstOrNull {
                it.ip == endpoint.address.hostAddress &&
                        it.port == endpoint.port
            }
            ?: return@LaunchedEffect

        navController.navigate("remote/${computer.id}") {
            popUpTo("main") {
                inclusive = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        fadeComposable("main",500) {

            val vm = di.get(MainScreenVM::class)
            LaunchedEffect(Unit) {
                vm.load()
            }
            MainScreen(vm, navController)
        }
        fadeComposable("add",500) { AddComputerScreen(di.get(AddComputerScreenVM::class),navController) }
        composable("edit/{id}") { backStackEntry ->
            val vm = di.get<EditComputerScreenVM>().also { it.load(backStackEntry.arguments?.getString("id")!!)  }
            EditComputerScreen(vm,navController)
        }
        composable("remote/{id}") { backStackEntry ->

            val vm = di.get<RemoteScreenVM>()
            val serverManager = di.get<ServerManager>()
            val dataLocalService = di.get<DataLocalService>()

            val computerData = dataLocalService.get<ComputerData>().first {it.id == backStackEntry.arguments?.getString("id")!!}

            var isLoading: Boolean by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                vm.load(computerData)
                val address = InetSocketAddress(computerData.ip, computerData.port)
                if(serverManager.connectionState == ConnectionState.Connected
                    && serverManager.activeEndPoint == address
                    && serverManager.checkConnection()){

                    vm.requestAllData()
                    isLoading = false;
                    return@LaunchedEffect
                }

                vm.connect()
                isLoading = false;
            }



            if(!isLoading){
                when (serverManager.connectionState) {
                    ConnectionState.Connecting -> {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    ConnectionState.Connected -> {
                        RemoteScreen(vm,navController)
                    }

                    ConnectionState.Error -> {
                        ConnectionLostDialog(
                            onReconnect = { vm.reconnect() },
                            onBack = { navController.navigate("main") }
                        )
                    }
                    ConnectionState.Disconnected -> {
                        navController.navigate("main")
                    }

                }
            }


        }


    }

}

fun NavGraphBuilder.fadeComposable(
    route: String,
    durationMillis: Int = 300,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            fadeIn(tween(durationMillis))
        },
        exitTransition = {
            fadeOut(tween(durationMillis))
        },
        content = content
    )
}
fun NavGraphBuilder.slideComposable(
    route: String,
    direction: AnimatedContentTransitionScope.SlideDirection,
    durationMillis: Int = 300,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                direction,
                tween(durationMillis)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                direction,
                tween(durationMillis)
            )
        },
        content = content
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun GreetingPreview() {
    WRCTheme {
        Greeting("Android")
    }
}