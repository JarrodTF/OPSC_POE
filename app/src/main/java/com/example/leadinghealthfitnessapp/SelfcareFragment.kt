package com.example.leadinghealthfitnessapp

import android.os.Bundle
import android.os.CountDownTimer
import android.os.health.TimerStat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_selfcare.*
import util.PrefUtil

class SelfcareFragment : Fragment() {

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selfcare, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        but_start.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        but_pause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        but_stop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }

    }

    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
        }
        else if (timerState == TimerState.Paused){

        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireActivity().applicationContext)
        PrefUtil.setSecondsRemaining(secondsRemaining, requireActivity().applicationContext)
        PrefUtil.setTimerState(timerState, requireActivity().applicationContext)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(requireActivity().applicationContext)

        if (timerState == TimerState.Stopped){
            setNewTimerLength()
        }
        else
            setPreviousTimerLength()
        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(requireActivity().applicationContext)
        else
            timerLengthSeconds

        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        setNewTimerLength()

        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireActivity().applicationContext)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object: CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long){
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMin = PrefUtil.getTimerLenght(requireActivity().applicationContext)
        timerLengthSeconds = (lengthInMin * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireActivity().applicationContext)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minsUntilFinished = secondsRemaining / 60
        val secsInMinsUnilFinished = secondsRemaining - minsUntilFinished * 60
        val secsStr = secsInMinsUnilFinished.toString()
        textView_countdown.text = "$minsUntilFinished:${
        if (secsStr.length == 2) secsStr
        else "0" + secsStr}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running ->{
                but_start.isEnabled = false
                but_pause.isEnabled = true
                but_stop.isEnabled = true
            }
            TimerState.Stopped ->{
                but_start.isEnabled = true
                but_pause.isEnabled = false
                but_stop.isEnabled = false
            }
            TimerState.Paused ->{
                but_start.isEnabled = true
                but_pause.isEnabled = false
                but_stop.isEnabled = true
            }
        }
    }
}
