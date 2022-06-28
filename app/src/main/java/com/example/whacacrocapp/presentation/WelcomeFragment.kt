package com.example.whacacrocapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import whacacrocapp.R
import whacacrocapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val record = pref.getString(SP_KEY, DEFAULT_VALUE_PREF)

        binding.buttonStart.setOnClickListener {
            launchGameFragment()
        }
        binding.tvRecordGame.text = requireContext().getString(R.string.record_text, record)
    }

    private fun launchGameFragment() {
        findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DEFAULT_VALUE_PREF = "0"
        private const val SP_KEY = "record"
    }
}