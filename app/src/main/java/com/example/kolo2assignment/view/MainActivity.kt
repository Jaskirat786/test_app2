package com.example.kolo2assignment.view

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import com.example.kolo2assignment.R
import com.example.kolo2assignment.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.isSelected
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
    }
//Bottom navigation for showing different tabs

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.tab1 -> {
                    //showing progress bar
                    binding.bar.setVisibility(View.VISIBLE)
                    val handler = Handler()
                    handler.postDelayed({
                        binding.bar.setVisibility(View.GONE)
                    }, 2000)

                    val fragment1 = FirstTabFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment1, fragment1.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.tab2 -> {
                    //Showing progress bar
                    binding.bar.setVisibility(View.VISIBLE)
                    val handler = Handler()
                    handler.postDelayed({
                        binding.bar.setVisibility(View.GONE)
                    }, 2000)

                    val fragment2 = SecondTabFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment2, fragment2.javaClass.getSimpleName())
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }
            }
            true
        }
}