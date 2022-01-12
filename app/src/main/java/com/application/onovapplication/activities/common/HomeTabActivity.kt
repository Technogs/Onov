
package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActionBarLayoutBinding
import com.application.onovapplication.databinding.ActivityForgotPasswordOtpBinding
import com.application.onovapplication.databinding.ActivityHomeTabBinding
import com.application.onovapplication.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeTabActivity : BaseAppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    var fragment: Fragment? = null
    var role: String? = null
    private lateinit var binding: ActivityHomeTabBinding
    private lateinit var incnv: BottomNavigationView
    private lateinit var incBinding: ActionBarLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTabBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
         incBinding =binding.abHome
        incnv  =binding.navigation

        incnv.setOnNavigationItemSelectedListener(this)

        incnv.selectedItemId = R.id.navigation_home

        role = userPreferences.getRole()
        incBinding.backArrow.visibility = View.GONE
        incBinding.searchButton.visibility = View.VISIBLE
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        role = userPreferences.getRole()

        if (!role.isNullOrEmpty() && role == "Citizens" || !role.isNullOrEmpty() && role == "citizens") {
            incnv.menu.getItem(3).setIcon(R.drawable.vote_yea)
            incnv.menu.getItem(3).title = getString(R.string.polling)

        } else {
            incnv.menu.getItem(3).setIcon(R.drawable.donate)
            incnv.menu.getItem(3).title = getString(R.string.donations)

        }

        when (menuItem.itemId) {
            R.id.navigation_home -> {
                //changed
              // binding.abHome.visibility = View.VISIBLE
                incBinding.searchButton.visibility = View.VISIBLE
                fragment = FeedFragment()
            }

            R.id.navigation_chat -> {
                //changed
               // binding.abHome = View.VISIBLE
                incBinding.searchButton.visibility = View.INVISIBLE
                fragment = ChatFragment()
            }

            R.id.navigation_debate -> {
                //changed
               // ab_home.visibility = View.GONE
                incBinding.searchButton.visibility = View.VISIBLE
                fragment = AskForMediaFragment()//CreatePostFragment()
            }

            R.id.navigation_voting -> {
                if (!role.isNullOrEmpty() && role == "Citizens" || !role.isNullOrEmpty() && role == "citizens") {
                    //changed
                    //ab_home.visibility = View.VISIBLE
                    incBinding.searchButton.visibility = View.VISIBLE
                    fragment = PollingFragment() //VotingFragment()
                }
                else {
                   // ab_home.visibility = View.VISIBLE
                  //  ab_home.visibility = View.VISIBLE
                    incBinding.searchButton.visibility = View.VISIBLE
                    fragment = DonationsFragment()
                }
            }

            R.id.navigation_more -> {
                //changed
               // ab_home.visibility = View.VISIBLE
                incBinding.searchButton.visibility = View.VISIBLE
                fragment = MoreFragment()

            }
        }

        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().
            replace(R.id.homeTabContainer, fragment).commit()
            return true
        }
        return false
    }
}