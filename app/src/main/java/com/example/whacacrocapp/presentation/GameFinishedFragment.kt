package com.example.whacacrocapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import whacacrocapp.R
import whacacrocapp.databinding.FragmentGameFinishedBinding


class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private val args by navArgs<GameFinishedFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val record = pref.getString(SP_KEY, DEFAULT_VALUE_PREF)
        with(binding) {
            if (args.gameResult.winner) {
                tvResult.text = RESULT_VICTORY
                ivLogo.setImageResource(R.drawable.ic_smile)
            } else {
                tvResult.text = RESULT_FAILED
                ivLogo.setImageResource(R.drawable.ic_sad)
            }
            tvResultHits.text =
                requireContext().getString(R.string.hits_text, args.gameResult.countOfHits)
            binding.tvRecordGame.text = requireContext().getString(R.string.record_text, record)
            buttonAgain.setOnClickListener { launchWelcomeFragment() }
        }
    }

    private fun launchWelcomeFragment() {
        findNavController().navigate(R.id.action_gameFinishedFragment_to_welcomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DEFAULT_VALUE_PREF = "0"
        private const val SP_KEY = "record"
        private const val RESULT_VICTORY = "VICTORY"
        private const val RESULT_FAILED = "FAILED"
    }
}