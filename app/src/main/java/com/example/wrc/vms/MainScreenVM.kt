package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrc.data.ComputerData
import com.example.wrc.data.DiscoveredHost
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.EventToken
import com.example.wrc.services.ServerManager
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainScreenVM : ViewModel(), IService {
    var computers: List<ComputerData> by mutableStateOf(listOf<ComputerData>())
    lateinit var dataService: DataLocalService
    private lateinit var serverManager: ServerManager
    private var discoveryToken: EventToken? = null

    override fun inject(provider: DIContext) {
        dataService = provider.get(DataLocalService::class)
        serverManager = provider.get(ServerManager::class)
        //discoveryToken = serverManager.subscribeToDiscoveredHosts(::onHostDiscovered)
    }

    fun searchForHosts() = viewModelScope.launch {
        serverManager.startHostDiscovery(durationMillis = 30_000L)
        delay(30_000)
        serverManager.stopHostDiscovery()
    }



    fun load(){
        computers = dataService.get<ComputerData>().toList()
    }

    fun delete(uuid: String){
        dataService.deleteAndSava<ComputerData>(uuid)
        load();
    }

    override fun onCleared() {
        discoveryToken?.unsubscribe()
        super.onCleared()
    }
}
