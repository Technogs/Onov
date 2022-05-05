package com.application.onovapplication.activities.common

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.ViewDebatesAdapter
import com.application.onovapplication.databinding.ActionBarLayoutBinding
import com.application.onovapplication.databinding.ActivityHomeTabBinding
import com.application.onovapplication.fragments.*
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.SocialData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.HomeViewModel
import com.application.onovapplication.viewModels.SettingsViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class HomeTabActivity : BaseAppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    var fragment: Fragment? = null
    var role: String? = null
    var end: String? = null
    var uid: String? = null
    var waitTimer: CountDownTimer? = null
    lateinit var dialog: Dialog
    private val settingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
    private lateinit var binding: ActivityHomeTabBinding
    private lateinit var incnv: BottomNavigationView
    private lateinit var incBinding: ActionBarLayoutBinding
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTabBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        incBinding = binding?.abHome
        incnv = binding.navigation
        dialog = Dialog(this)
        incBinding.usrName.visibility = View.VISIBLE
        incBinding.count.visibility = View.VISIBLE
       incBinding.count2.visibility = View.VISIBLE

        if (intent.getStringExtra("debate") != null) {
            end = intent.getStringExtra("debate")
            uid = intent.getStringExtra("user")
            settingsViewModel.getsocialaccount(this, uid.toString())
        }



        observeViewModel()
        incBinding.usrName.text = userPreferences.getuserDetails()?.fullName?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        incnv.setOnNavigationItemSelectedListener(this)
        incnv.selectedItemId = R.id.navigation_home

        role = userPreferences.getRole()
        incBinding.backArrow.visibility = View.GONE
        incBinding.searchButton.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        homeViewModel.getFeed(this, userPreferences.getUserREf())
        observeViewModel2()
    }

    private fun showSocialDialog(data: SocialData) {
        //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.social_account_dialog)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val instaBtn = dialog.findViewById(R.id.instaValue) as TextView
        val twitterBtn = dialog.findViewById(R.id.twitterValue) as TextView
        val fbBtn = dialog.findViewById(R.id.fbValue) as TextView
        val name = dialog.findViewById(R.id.userName) as TextView
        val image = dialog.findViewById(R.id.ivChatProfile) as CircleImageView
        instaBtn.text = data.instagram
        twitterBtn.text = data.twitter
        fbBtn.text = data.facebook
        name.text = data.fullName
        Glide.with(this).load(BaseUrl.photoUrl + data.profilePic).into(image)
        val yesBtn = dialog.findViewById(R.id.cancel) as TextView
        yesBtn.setOnClickListener {
            if (waitTimer != null) {
                waitTimer!!.cancel()
                waitTimer = null
            }
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun observeViewModel2() {
        homeViewModel.successful.observe(this, Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {
                    if (homeViewModel.status == "success") {
                        if (!homeViewModel.getFeedResponse.NotificationCount.equals("0")){
                            incBinding.count.text=homeViewModel.getFeedResponse.NotificationCount

                        }

                    }
                }
            }
        })
    }


    private fun observeViewModel() {
        settingsViewModel.getSocialAccountSuccess.observe(this, Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {

                    if (settingsViewModel.status == "success") {

                        if (settingsViewModel.socialMediaResponse.socialData != null) {
                            waitTimer = object : CountDownTimer(20000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {

                                    showSocialDialog(settingsViewModel.socialMediaResponse.socialData)
                                }

                                override fun onFinish() {
                                    dialog.dismiss()
                                }
                            }
                            waitTimer!!.start()
                        } else {
                            setError("Winner's social accounts not available")
                        }

                    } else {
                        setError(settingsViewModel.message)
                    }

                } else {
                    setError(settingsViewModel.message)

                }
            }
        })


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        role = userPreferences.getRole()

        if (!role.isNullOrEmpty() && role == "Citizens" || !role.isNullOrEmpty() && role == "citizens") {
            incnv.menu.getItem(3).setIcon(R.drawable.vote_yea)
            incnv.menu.getItem(3).title = getString(R.string.polling)

        } else {
//            incnv.menu.getItem(3).setIcon(R.drawable.donate)
            incnv.menu.getItem(3).setIcon(R.drawable.vote_yea)
            incnv.menu.getItem(3).title = getString(R.string.polling)

        }

        when (menuItem.itemId) {
            R.id.navigation_home -> {
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
                } else {
                    // ab_home.visibility = View.VISIBLE
                    incBinding.searchButton.visibility = View.VISIBLE
                    fragment = PollingFragment()
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

    override fun onRestart() {
        super.onRestart()
        refreshActivity()
    }

    fun refreshActivity() {
        val i = Intent(this, HomeTabActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)

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