package com.example.studynest.service

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    internal const val SUPABASE_URL = "https://ervpzlkfmpcxugbwepyg.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVydnB6bGtmbXBjeHVnYndlcHlnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzA1NzUyNDMsImV4cCI6MjA4NjE1MTI0M30.2EE3itqr9cC2s2ugiRYvoqtjTRS3eXMt3rCDUD00GbM"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Storage)
    }
}
