package com.example.eng_words_reminder_client.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.*
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eng_words_reminder_client.databinding.FragmentMainMenuBinding
import com.example.eng_words_reminder_client.network.NetworkVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

class FragmentMainMenu : Fragment() {
    private val binding by lazy { FragmentMainMenuBinding.inflate(layoutInflater) }
    private val viewModelNetWork: NetworkVM by activityViewModels()
    private val sharedPreferences by lazy { requireActivity().getSharedPreferences("main", 0) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        backHandler()
        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startRotationLottieObject()
        setObserver()
        setSwipeListener()
        getOrLoadId()
    }

    private fun startRotationLottieObject() {
        lifecycleScope.launch {
            val key = (0..10000000).random()
            while (true) {
                println(key)
                binding.lottieMenuHelper.rotation += 90f
                binding.lottieMenuHelper.playAnimation()
                delay(4500)
                binding.lottieMenuHelper.cancelAnimation()
            }
        }
    }

    private fun setObserver() {
        observerId()
    }

    private fun observerId() {
        with(viewModelNetWork) {
            id.observe(viewLifecycleOwner) { id ->
                if (sharedPreferences.contains("id")) {
                    authUser()
                } else {
                    sharedPreferences.edit().putString("id", id).apply()
                    registerUser()
                }
            }
        }
    }

    private fun getOrLoadId() {
        if (sharedPreferences.contains("id")) {
            viewModelNetWork.setId(sharedPreferences.getString("id", "").toString())
        } else {
            viewModelNetWork.getId(requireContext())
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
}
