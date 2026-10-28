package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wrc.data.ComputerData
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService

class AddComputerScreenVM : FormViewModel(), IService {

    lateinit var dataService: DataLocalService

    var hostName = state("") { validateRule(String::isNotEmpty,"Не должно быть пустым") }
    var ip = state("") {
        validateRule(String::isNotEmpty, "Не должно быть пустым")
        validateRule("Введите корректный IPv4-адрес") { isValidIpv4(it) }
    }
    var port = state("") {
        validateRule(String::isNotEmpty,"Не должно быть пустым")
        validateFilter("Только цифры") { it.all(Char::isDigit)}
        validateRule("Порт должен быть от 1 до 65535") {
            it.toIntOrNull()?.let { portValue -> portValue in 1..65535 } == true
        }
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

private fun isValidIpv4(value: String): Boolean {
    val parts = value.split('.')
    return parts.size == 4 && parts.all { part ->
        part.isNotEmpty() && part.all(Char::isDigit) &&
                part.toIntOrNull()?.let { octet -> octet in 0..255 } == true
    }
}
