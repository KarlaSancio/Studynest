package com.example.studynest.repository

import com.example.studynest.service.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.io.File

class StorageRepository {
    suspend fun uploadFile(file: File, bucketName: String): String {
        val bucket = SupabaseClient.client.storage[bucketName]
        
        //Faz o upload. A variável 'path' retorna "materials/arquivo.pdf"
        val path = bucket.upload(file.name, file.readBytes())
        
        //Constrói a URL completa com o caminho base + 'path'.
        return "${SupabaseClient.SUPABASE_URL}/storage/v1/object/public/$path"
    }
}
