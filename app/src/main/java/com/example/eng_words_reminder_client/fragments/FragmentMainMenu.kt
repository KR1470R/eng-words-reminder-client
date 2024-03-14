package com.example.eng_words_reminder_client.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.eng_words_reminder_client.databinding.FragmentMainMenuBinding
import kotlin.math.abs

class FragmentMainMenu : Fragment() {
    private val binding by lazy { FragmentMainMenuBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backHandler()
        setSwipeListener()
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
                                        FragmentMainMenuDirections
                                            .actionFragmentMainMenuToFragmentOptions()
                                    )
                            } else {
                                // swipe left
                            }
                        } else {
                            if (diffY > 0) {
                                // swipe down
                                requireActivity().finish()
                            } else {
                                // swipe up
                                findNavController()
                                    .navigate(
                                        FragmentMainMenuDirections
                                            .actionFragmentMainMenuToFragmentTenWords()
                                    )
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

    /**Block native button back.
     * Just comment this method and delete from onViewCreated for activate that.*/
    private fun backHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do nothing, button blocked.
                }
            }
        )
    }
}
