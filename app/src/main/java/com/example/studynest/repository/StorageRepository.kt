package com.example.studynest.repository

import com.example.studynest.service.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.io.File

class StorageRepository {

    //Faz o upload de um arquivo para o Supabase Storage e retorna a url do arquivo
    suspend fun uploadFile(file: File, bucketName: String): String {
        val bucket = SupabaseClient.client.storage[bucketName]
        return bucket.upload(file.name, file.readBytes())
    }
}
