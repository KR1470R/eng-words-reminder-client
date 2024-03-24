package com.example.eng_words_reminder_client.fragments.fragmentTenWords

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eng_words_reminder_client.R
import com.example.eng_words_reminder_client.databinding.FragmentTenWordsBinding
import com.example.eng_words_reminder_client.network.NetworkVM
import com.example.eng_words_reminder_client.network.responses.ResponseGetTenWords
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.math.abs

class FragmentTenWords : Fragment() {
    private val binding by lazy { FragmentTenWordsBinding.inflate(layoutInflater) }
    private val viewModel: NetworkVM by activityViewModels()
    private val listOfCurrentWords = mutableListOf<ResponseGetTenWords>()
    private lateinit var currentWord: ResponseGetTenWords
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
        getTenWords()
    }

    private fun setObserver() {
        viewModel.dataTenWords.observe(viewLifecycleOwner) { listOfWords ->
            listOfWords.forEach { listOfCurrentWords.add(it) }
            showWord()
        }
    }

    private fun showWord() {
        with(binding) {
            lifecycleScope.launch {
                if (listOfCurrentWords.size != 0) {
                    currentWord = listOfCurrentWords.random()
                    word.text = currentWord.term
                    val listVariants = setVariantsList(currentWord)
                    variantOne.text = listVariants[0]
                    variantTwo.text = listVariants[1]
                    variantThree.text = listVariants[2]
                }
                showText()
            }
        }
    }

    private fun setVariantsList(currentWord: ResponseGetTenWords): List<String> {
        val secondVariant = viewModel.getDataTenWords?.filterNot { it == currentWord }!!.random()
        val thirdVariant =
            viewModel.getDataTenWords?.filterNot { it == currentWord || it == secondVariant }!!
                .random()
        return listOf(
            currentWord.meanings.random(),
            secondVariant.meanings.random(),
            thirdVariant.meanings.random()
        ).shuffled()
    }


    private fun getTenWords() {
        viewModel.getTenWords()
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
                            swipeHorizontal(diffX)
                        } else {
                            swipeVertical(diffY)
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

    private fun swipeVertical(diffY: Float) {
        lifecycleScope.launch {
            if (diffY > 0) {
                swipeDown()
            } else {
                swipeUp()
            }
        }
    }

    private fun swipeHorizontal(diffX: Float) {
        lifecycleScope.launch {
            if (diffX > 0) {
                swipeRight()
            } else {
                swipeLeft()
            }
        }
    }

    private suspend fun swipeUp() {
        if (currentWord.meanings.contains(binding.variantTwo.text)) {
            listOfCurrentWords.remove(currentWord)
            fadeText(true)
            showWord()
        } else {
            fadeText(false)
            showWord()
        }
    }

    private fun swipeDown() {
        findNavController().popBackStack()
    }

    private suspend fun swipeLeft() {
        lifecycleScope.launch {
            if (currentWord.meanings.contains(binding.variantOne.text)) {
                listOfCurrentWords.remove(currentWord)
                fadeText(true)
                showWord()
            } else {
                fadeText(false)
                showWord()
            }
        }
    }

    private suspend fun swipeRight() {
        if (currentWord.meanings.contains(binding.variantThree.text)) {
            listOfCurrentWords.remove(currentWord)
            fadeText(true)
            showWord()
        } else {
            fadeText(false)
            showWord()
        }
    }

    private suspend fun fadeText(b: Boolean) {
        with(binding) {
            val decreaseValue = BigDecimal.valueOf(0.02)
            while (variantOne.alpha.toDouble() > 0.0) {
                variantOne.alpha =
                    (variantOne.alpha.toBigDecimal() - decreaseValue).toFloat()
                variantTwo.alpha =
                    (variantTwo.alpha.toBigDecimal() - decreaseValue).toFloat()
                variantThree.alpha =
                    (variantThree.alpha.toBigDecimal() - decreaseValue).toFloat()
                word.alpha = (word.alpha.toBigDecimal() - decreaseValue).toFloat()
                delay(10)
            }
            showResult(b, binding.ivResult)
            delay(1000)
        }
    }

    private fun showResult(b: Boolean, ivResult: ImageView) {
        if (b) {
            ivResult.setBackgroundResource(R.drawable.ic_correct)
        } else ivResult.setBackgroundResource(R.drawable.ic_wrong)
        ivResult.visibility = View.VISIBLE
    }

    private suspend fun showText() {
        with(binding) {
            hideResult(binding.ivResult)
            val increaseValue = BigDecimal.valueOf(0.02)
            while (variantOne.alpha.toDouble() < 1.0) {
                variantOne.alpha =
                    (variantOne.alpha.toBigDecimal() + increaseValue).toFloat()
                variantTwo.alpha =
                    (variantTwo.alpha.toBigDecimal() + increaseValue).toFloat()
                variantThree.alpha =
                    (variantThree.alpha.toBigDecimal() + increaseValue).toFloat()
                word.alpha =
                    (word.alpha.toBigDecimal() + increaseValue).toFloat()
                delay(10)
            }
        }
    }

    private fun hideResult(ivResult: ImageView) {
        ivResult.background = null
        ivResult.visibility = View.INVISIBLE
    }
}