package com.example.kadai09_pi12a_23

import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleManager.applySavedLocale(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0
            )
            findViewById<BottomNavigationView>(R.id.bottom_nav)
                ?.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        
        // Save current selected item before re-inflating menu
        val currentSelectedId = bottomNav.selectedItemId
        
        // Clear and re-inflate menu to ensure proper localization
        bottomNav.menu.clear()
        bottomNav.inflateMenu(R.menu.bottom_nav_menu)
        
        // Setup badges for navigation items (example: show badge on knowledge tab)
        setupNavigationBadges(bottomNav)
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
            bottomNav.selectedItemId = R.id.nav_home
        } else {
            // Restore previously selected item after menu re-inflation
            if (currentSelectedId != -1) {
                bottomNav.selectedItemId = currentSelectedId
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_knowledge -> KnowledgeFragment()
                R.id.nav_rank -> RankFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> return@setOnItemSelectedListener false
            }
            
            // Apply bounce animation to the selected icon
            animateNavItemSelection(bottomNav, item.itemId)
            
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
            true
        }
    }
    
    private fun setupNavigationBadges(bottomNav: BottomNavigationView) {
        // Example: Add badge to knowledge tab
        // You can customize this based on your app's logic
        val knowledgeBadge = bottomNav.getOrCreateBadge(R.id.nav_knowledge)
        knowledgeBadge.isVisible = false // Set to true when you have new content
        
        // Example: Add badge to profile tab for notifications
        val profileBadge = bottomNav.getOrCreateBadge(R.id.nav_profile)
        profileBadge.isVisible = false // Set to true when you have notifications
    }
    
    private fun animateNavItemSelection(bottomNav: BottomNavigationView, itemId: Int) {
        // Find the view for the selected item and apply bounce animation
        val menuView = bottomNav.getChildAt(0) as? android.view.ViewGroup
        menuView?.let { menu ->
            for (i in 0 until menu.childCount) {
                val item = menu.getChildAt(i)
                if (bottomNav.menu.getItem(i).itemId == itemId) {
                    // Apply bounce animation
                    item.animate()
                        .scaleX(1.15f)
                        .scaleY(1.15f)
                        .setDuration(150)
                        .setInterpolator(AnticipateOvershootInterpolator())
                        .withEndAction {
                            item.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(150)
                                .setInterpolator(AnticipateOvershootInterpolator())
                                .start()
                        }
                        .start()
                    break
                }
            }
        }
    }
}
