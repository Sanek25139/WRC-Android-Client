package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.wrc.data.ComputerData
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService


class MainScreenVM : ViewModel(), IService {
    var computers: List<ComputerData> by mutableStateOf(listOf<ComputerData>())
    lateinit var dataService: DataLocalService

    override fun inject(provider: DIContext) {
        dataService = provider.get(DataLocalService::class)
    }

    fun load(){
        computers = dataService.get<ComputerData>().toList()
    }

    fun delete(uuid: String){
        dataService.deleteAndSava<ComputerData>(uuid)
        load();
    }
}