package com.example.eng_words_reminder_client.fragments.fragmentOptions

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.eng_words_reminder_client.R
import com.example.eng_words_reminder_client.databinding.FragmentOptionsBinding
import com.example.eng_words_reminder_client.network.NetworkVM
import kotlin.math.abs
import kotlin.math.roundToInt

class FragmentOptions : Fragment() {
    private val binding by lazy { FragmentOptionsBinding.inflate(layoutInflater) }
    private val viewModel: NetworkVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setSwipeListener()

        viewModel.getStatistic()
    }

    private fun setObserver() {
        viewModel.statistic.observe(viewLifecycleOwner) {
            binding.apply {
                val maxWords = it.terms_total.toDouble()
                val maxProgress = 1000.toDouble()
                val learnedWords = it.terms_learned.toDouble()
                progressBarWordsLearned.progress = (learnedWords / maxWords
                        * maxProgress).roundToInt()
                tvStatistic.text = getString(R.string.s_s).format(it.terms_learned, it.terms_total)
            }
        }
    }

    private var startX = 0f
    private var startY = 0f
    private var isSwipe = false

    /**This class implement swipe listener for root.*/
    @SuppressLint("ClickableViewAccessibility")
    private fun setSwipeListener() {
        binding.root.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val endX = event.x
                    val endY = event.y
                    //U don't need that in this project, but u will know how many pixels user swiped.
                    //Will use some functions with that in the Action_UP
                    val diffX = endX - startX
                    val diffY = endY - startY

                    if (!isSwipe) {
                        isSwipe = true
                        if (abs(diffX) > abs(diffY)) {
                            if (diffX > 0) {
                                // swipe right
                                findNavController()
                                    .navigate(
                                        FragmentOptionsDirections
                                            .actionFragmentOptionsToFragmentReset()
                                    )
                            } else {
                                // swipe left
                                findNavController().popBackStack()
                            }
                        } else {
                            if (diffY > 0) {
                                // swipe down
                            } else {
                                // swipe up
                            }
                        }
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isSwipe = false
                    startX = 0f
                    startY = 0f
                }
            }
            return@setOnTouchListener true
        }
    }
}