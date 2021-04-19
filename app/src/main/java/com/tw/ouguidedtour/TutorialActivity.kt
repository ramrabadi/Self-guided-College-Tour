package com.tw.ouguidedtour

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import java.util.*

class TutorialActivity : AppCompatActivity() {
    private var fragmentManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        fragmentManager = supportFragmentManager
        val paperOnboardingFragment =
            PaperOnboardingFragment.newInstance(dataForOnBoarding)
        val fragmentTransaction =
            fragmentManager!!.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, paperOnboardingFragment)
        fragmentTransaction.commit()
    }

    private val dataForOnBoarding: ArrayList<PaperOnboardingPage>
        private get() {
            val screen_one = PaperOnboardingPage(
                "Scan a QR Code!",
                "To read about a stop.",
                Color.parseColor("#5873da"),
                R.drawable.qr_code_tut,
                R.drawable.qr_code
            )
            val screen_two = PaperOnboardingPage(
                "View the Map!",
                "To see your location in the Stocker Center.",
                Color.parseColor("#29bf5e"),
                R.drawable.map_menu,
                R.drawable.map_menu
            )
            val screen_three = PaperOnboardingPage(
                "View the Floor Plan!",
                "To see which floor you're on.",
                Color.parseColor("#e34847"),
                R.drawable.blueprint,
                R.drawable.blueprint
            )
            val screen_four = PaperOnboardingPage(
                "View your current stop!",
                "To see which stop you're at.",
                Color.parseColor("#99b725"),
                R.drawable.flag,
                R.drawable.flag
            )
            val elements =
                ArrayList<PaperOnboardingPage>()
            elements.add(screen_one)
            elements.add(screen_two)
            elements.add(screen_three)
            elements.add(screen_four)
            return elements
        }
}