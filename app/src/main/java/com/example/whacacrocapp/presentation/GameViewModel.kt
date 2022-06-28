package com.example.whacacrocapp.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whacacrocapp.domain.entity.GameResult
import com.example.whacacrocapp.domain.entity.Crocodile
import kotlin.random.Random
import kotlin.random.nextInt

class GameViewModel : ViewModel() {

    private val listCrocodiles: MutableList<Crocodile> by lazy { mutableListOf() }
    private var timer: CountDownTimer? = null
    private var timerCrocodileState: CountDownTimer? = null
    private var countHitsCrocodile = 0

    private var _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private var _countOfHitsCrocodile = MutableLiveData<Int>()
    val countOfHitsCrocodile: LiveData<Int>
        get() = _countOfHitsCrocodile

    private var _crocodileList = MutableLiveData<MutableList<Crocodile>>()
    val crocodileList: LiveData<MutableList<Crocodile>>
        get() = _crocodileList

    private var _isFinish = MutableLiveData<Boolean>()
    val isFinish: LiveData<Boolean>
        get() = _isFinish

    init {
        _countOfHitsCrocodile.value = 0
        createCrocodilesList()
        startGame()
    }

    private fun startGame() {
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            30 * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun startTimerCrocodileState() {
        val randomNumber = Random.nextInt(0 until 9)
        listCrocodiles[randomNumber].IsActive = true
        _crocodileList.value = listCrocodiles

        timerCrocodileState = object : CountDownTimer(
            1 * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                listCrocodiles[randomNumber].IsActive = false
                _crocodileList.value = listCrocodiles
                randomEnabledCrocodile()
            }
        }
        timerCrocodileState?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun checkResult(): Boolean = countHitsCrocodile >= 10

    private fun finishGame() {
        _gameResult.value = GameResult(
            checkResult(),
            countHitsCrocodile
        )
        _isFinish.value = true
    }

    private fun createCrocodilesList() {
        for (i in 0 until 9) {
            val item = Crocodile(
                i,
                false
            )
            listCrocodiles.add(item)
        }
        _crocodileList.value = listCrocodiles
    }

    fun hittingTheCrocodile(crocodile: Crocodile) {
        if (crocodile.IsActive) {
            countHitsCrocodile++
            _countOfHitsCrocodile.value = countHitsCrocodile
            listCrocodiles[crocodile.id].IsActive = false
            _crocodileList.value = listCrocodiles
        }
    }

    fun randomEnabledCrocodile() {
        startTimerCrocodileState()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}