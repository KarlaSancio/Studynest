package com.example.studynest.ui

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studynest.databinding.FragmentAddMaterialBinding
import com.example.studynest.repository.Material
import com.example.studynest.repository.MaterialRepository
import com.example.studynest.repository.StorageRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.io.File

class AddMaterialFragment : Fragment() {

    private var _binding: FragmentAddMaterialBinding? = null
    private val binding get() = _binding!!

    private val args: AddMaterialFragmentArgs by navArgs()

    private val storageRepository = StorageRepository()
    private val materialRepository = MaterialRepository()

    private var selectedFileUri: Uri? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            binding.tvSelectedFile.text = getOriginalFileName(it) ?: "Arquivo selecionado"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botão Voltar
         binding.btnBack.setOnClickListener {
             findNavController().popBackStack()
         }

        // Botão Selecionar Material
        binding.btnSelectFile.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        // Botão Enviar Material
        binding.btnUpload.setOnClickListener {
            uploadAndSaveMaterial()
        }
    }

    // Função para enviar e salvar o material
    private fun uploadAndSaveMaterial() {
        val baseFileName = binding.edtNomeDocumento.text.toString().trim()
        if (baseFileName.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, insira um nome para o arquivo", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica se um arquivo foi selecionado e adiciona a extensao dele para o supabase salvar corretamente
        selectedFileUri?.let { uri ->
            val fileExtension = getFileExtension(uri)
            if (fileExtension.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Não foi possível determinar o tipo do arquivo.", Toast.LENGTH_SHORT).show()
                return@let
            }
            
            val finalFileNameWithExtension = "$baseFileName.$fileExtension"

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val fileToUpload = getFileFromUri(uri, finalFileNameWithExtension)
                    val fileUrl = storageRepository.uploadFile(fileToUpload, "materials")
                    
                    val material = Material(
                        nomeArquivo = finalFileNameWithExtension,
                        urlArquivo = fileUrl,
                        idDisciplina = args.idDisciplina,
                        dataUpload = Timestamp.now()
                    )

                    // Salva o material no banco firestore
                    materialRepository.addMaterial(material)
                    
                    Toast.makeText(requireContext(), "Material enviado com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Falha no processo: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: Toast.makeText(requireContext(), "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show()
    }

    // Helper para pegar o nome original do arquivo
    private fun getOriginalFileName(uri: Uri): String? {
        return requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }

    // Helper para pegar a extensão do arquivo
    private fun getFileExtension(uri: Uri): String? {
        return context?.contentResolver?.getType(uri)?.let {
            MimeTypeMap.getSingleton().getExtensionFromMimeType(it)
        }
    }

    private fun getFileFromUri(uri: Uri, fileName: String): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, fileName)
        val outputStream = file.outputStream()
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
