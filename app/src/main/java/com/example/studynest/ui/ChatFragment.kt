package com.example.studynest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studynest.databinding.FragmentChatBinding
import com.example.studynest.domain.Message
import com.example.studynest.ui.adapter.MessageAdapter
import com.example.studynest.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels()

    private lateinit var adapter: MessageAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disciplinaId = args.idDisciplina
        val uid = auth.currentUser?.uid ?: return

        adapter = MessageAdapter(uid)

        binding.recyclerMessages.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        binding.recyclerMessages.adapter = adapter

        // Listener realtime
        viewModel.startListening(disciplinaId)

        viewModel.messages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.recyclerMessages.scrollToPosition(it.size - 1)
        }

        // Envio
        binding.btnSend.setOnClickListener {
            val text = binding.edtMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                val msg = Message(
                    senderId = uid,
                    senderName = auth.currentUser?.displayName ?: "Usuário",
                    text = text,
                    timestamp = System.currentTimeMillis()
                )
                viewModel.sendMessage(disciplinaId, msg)
                binding.edtMessage.text.clear()
            }
        }
    }
}
