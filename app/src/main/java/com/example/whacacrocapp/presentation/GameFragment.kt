package com.example.whacacrocapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.whacacrocapp.domain.entity.Crocodile
import com.example.whacacrocapp.domain.entity.GameResult
import whacacrocapp.R
import whacacrocapp.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    private val listButton: MutableList<ImageButton> by lazy { mutableListOf() }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    private val gameViewModel by viewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        gameViewModel.randomEnabledCrocodile()

        listButton.add(binding.ibCrocodile1)
        listButton.add(binding.ibCrocodile2)
        listButton.add(binding.ibCrocodile3)
        listButton.add(binding.ibCrocodile4)
        listButton.add(binding.ibCrocodile5)
        listButton.add(binding.ibCrocodile6)
        listButton.add(binding.ibCrocodile7)
        listButton.add(binding.ibCrocodile8)
        listButton.add(binding.ibCrocodile9)
    }

    private fun observeViewModel() {
        with(gameViewModel) {
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            countOfHitsCrocodile.observe(viewLifecycleOwner) {
                binding.tvCountOfHits.text = requireContext().getString(R.string.hits_text, it)
            }
            crocodileList.observe(viewLifecycleOwner) {
                crocodileState(it)
            }
            isFinish.observe(viewLifecycleOwner) { isFinish ->
                if (isFinish) {
                    gameResult.observe(viewLifecycleOwner) { gameResult ->
                        launchGameFinishedFragment(gameResult)
                    }
                }
            }
        }
    }

    private fun crocodileState(list: List<Crocodile>) {

        listButton.forEachIndexed { index, imageButton ->
            imageButton.isEnabled = list[index].IsActive
            imageButton.setOnClickListener {
                gameViewModel.hittingTheCrocodile(list[index])
            }
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val edt = pref.edit()
        val record = pref.getString(SP_KEY, DEFAULT_VALUE_PREF)

        if (record != null) {
            if (record.toInt() < gameResult.countOfHits) {
                edt.putString(SP_KEY, gameResult.countOfHits.toString())
                edt.apply()
            }
        }


        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(
                gameResult
            )
        )
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