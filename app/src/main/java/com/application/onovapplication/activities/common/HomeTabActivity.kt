package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.activities.DonationsActivity
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.action_bar_layout.*
import kotlinx.android.synthetic.main.activity_home_tab.*

class HomeTabActivity : BaseAppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    var fragment: Fragment? = null
    var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_tab)
        navigation.setOnNavigationItemSelectedListener(this)

        navigation.selectedItemId = R.id.navigation_home

        role = userPreferences.getRole()

        backArrow.visibility = View.GONE
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        role = userPreferences.getRole()

        if (!role.isNullOrEmpty() && role == "Citizens" || !role.isNullOrEmpty() && role == "citizens") {
            navigation.menu.getItem(3).setIcon(R.drawable.vote_yea)
            navigation.menu.getItem(3).title = getString(R.string.voting)

        } else {
            navigation.menu.getItem(3).setIcon(R.drawable.donate)
            navigation.menu.getItem(3).title = getString(R.string.donations)

        }

        when (menuItem.itemId) {
            R.id.navigation_home -> {
                ab_home.visibility = View.VISIBLE
                fragment = ViewDebatesFragment()
            }

            R.id.navigation_chat -> {
                ab_home.visibility = View.VISIBLE

                fragment = ChatFragment()
            }
            R.id.navigation_debate -> {
                ab_home.visibility = View.GONE
                fragment = CreateDebateFragment()
            }

            R.id.navigation_voting -> {

                if (!role.isNullOrEmpty() && role == "Citizens" || !role.isNullOrEmpty() && role == "citizens") {
                    ab_home.visibility = View.VISIBLE
                    fragment = VotingFragment()
                } else {
                    ab_home.visibility = View.VISIBLE
                    fragment = DonationsFragment()
                }
            }

            R.id.navigation_more -> {
                ab_home.visibility = View.VISIBLE

                fragment = MoreFragment()

            }
        }

        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.homeTabContainer, fragment)
                .commit()
            return true
        }

        return false
    }

}