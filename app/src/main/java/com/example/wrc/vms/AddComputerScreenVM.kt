package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.wrc.data.ComputerData
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService
import kotlin.text.isDigit

class AddComputerScreenVM : FormViewModel(), IService {

    lateinit var dataService: DataLocalService

    var hostName = state("") { validateRule(String::isNotEmpty,"Не должно быть пустым") }
    var ip = state("") { validateRule(String::isNotEmpty,"Не должно быть пустым") }
    var port = state("") {
        validateRule(String::isNotEmpty,"Не должно быть пустым")
        validateFilter("Только цифры") { it.all(Char::isDigit)}
    }


    override fun inject(provider: DIContext) {
        dataService = provider.get(DataLocalService::class)
    }

    fun save(): Boolean{
        if(!validateAll()){
            return false;
        }

        val newData = ComputerData(hostName.value,ip.value,port.value.toInt())
        dataService.addAndSave(newData)
        return true;
    }


}