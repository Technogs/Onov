package com.application.onovapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.EventsActivity
import com.application.onovapplication.activities.citizens.DebateRequestsActivity
import com.application.onovapplication.activities.common.*
import com.application.onovapplication.activities.lpa.CreateAnnouncementActivity
import com.application.onovapplication.activities.DonationsActivity
import com.application.onovapplication.activities.politicians.CreateLawActivity
import com.application.onovapplication.adapters.MoreScreenAdapter
import com.application.onovapplication.model.MoreScreenData
import com.application.onovapplication.viewModels.LogoutViewModel
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : Fragment(), MoreScreenAdapter.MoreItemListener {

    private val logoutViewModel by lazy {
        ViewModelProvider(this).get(LogoutViewModel::class.java)
    }

    val dataList: ArrayList<MoreScreenData> = ArrayList()
    private var moreScreenAdapter: MoreScreenAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()


        when ((activity as HomeTabActivity).userPreferences.getRole()) {
            "Politicians" -> {
                setPoliticiansList()
            }

            "Entertainers" -> {
                setEntertainersList()

            }
            "Organizations" -> {
                setNGOList()
            }

            "LPA" -> {
                setLPAList()
            }

            else -> {
                setCitizensList()
            }
        }


        moreScreenAdapter = MoreScreenAdapter(requireContext(), dataList, this)
        
        rv_more.adapter = moreScreenAdapter

    }

    private fun setPoliticiansList() {

        dataList.add(
            MoreScreenData(
                "Stats",
                R.drawable.stats
            )
        )
        dataList.add(
            MoreScreenData(
                "Debate Search",
                R.drawable.total_wining
            )
        )

        dataList.add(
            MoreScreenData(
                "All Winners",
                R.drawable.all_winners

            )
        )

        dataList.add(
            MoreScreenData(
                "Laws",
                R.drawable.laws

            )
        )

        dataList.add(
            MoreScreenData(
                "Events",
                R.drawable.create_event
            )
        )

        dataList.add(
            MoreScreenData(
                "Profile",
                R.drawable.profile
            )
        )


        dataList.add(
            MoreScreenData(
                "Petitions",
                R.drawable.create_event
            )
        )


        dataList.add(
            MoreScreenData(
                "Debate Requests",
                R.drawable.debate_requests
            )
        )

        dataList.add(
            MoreScreenData(
                "My Gov",
                R.drawable.my_govts
            )
        )

        dataList.add(
            MoreScreenData(
                "About App",
                R.drawable.about_app
            )
        )

        dataList.add(
            MoreScreenData(
                "Setting",
                R.drawable.settings
            )
        )
        dataList.add(
            MoreScreenData(
                "Privacy Policy",
                R.drawable.privacy_policy
            )
        )

        dataList.add(
            MoreScreenData(
                "Terms and Conditions",
                R.drawable.terms_condtions
            )
        )

        dataList.add(
            MoreScreenData(
                "Logout",
                R.drawable.logout
            )
        )
    }


    private fun setCitizensList() {
        dataList.add(
            MoreScreenData(
                "Stats",
                R.drawable.stats
            )
        )
        dataList.add(
            MoreScreenData(
                "Debate Search",
                R.drawable.total_wining
            )
        )

        dataList.add(
            MoreScreenData(
                "All Winners",
                R.drawable.all_winners

            )
        )

        dataList.add(
            MoreScreenData(
                "Profile",
                R.drawable.profile
            )
        )
        dataList.add(
            MoreScreenData(
                "Events",
                R.drawable.create_event
            )
        )
        dataList.add(
            MoreScreenData(
                "Debate Requests",
                R.drawable.debate_requests
            )
        )
        dataList.add(
            MoreScreenData(
                "Petitions",
                R.drawable.petition
            )
        )

        dataList.add(
            MoreScreenData(
                "Donation",
                R.drawable.donations
            )
        )


        dataList.add(
            MoreScreenData(
                "My Gov",
                R.drawable.my_govts
            )
        )

        dataList.add(
            MoreScreenData(
                "About App",
                R.drawable.about_app
            )
        )

        dataList.add(
            MoreScreenData(
                "Setting",
                R.drawable.settings
            )
        )
        dataList.add(
            MoreScreenData(
                "Privacy Policy",
                R.drawable.privacy_policy
            )
        )

        dataList.add(
            MoreScreenData(
                "Terms and Conditions",
                R.drawable.terms_condtions
            )
        )

        dataList.add(
            MoreScreenData(
                "Logout",
                R.drawable.logout
            )
        )
    }

    private fun setNGOList() {
        dataList.add(
            MoreScreenData(
                "Stats",
                R.drawable.stats

            )
        )
        dataList.add(
            MoreScreenData(
                "Laws",
                R.drawable.laws

            )
        )

        dataList.add(
            MoreScreenData(
                "Events",
                R.drawable.create_event
            )
        )

        dataList.add(
            MoreScreenData(
                "Profile",
                R.drawable.profile
            )
        )

        dataList.add(
            MoreScreenData(
                "About App",
                R.drawable.about_app
            )
        )

        dataList.add(
            MoreScreenData(
                "Setting",
                R.drawable.settings
            )
        )
        dataList.add(
            MoreScreenData(
                "Privacy Policy",
                R.drawable.privacy_policy
            )
        )

        dataList.add(
            MoreScreenData(
                "Terms and Conditions",
                R.drawable.terms_condtions
            )
        )

        dataList.add(
            MoreScreenData(
                "Logout",
                R.drawable.logout
            )
        )

    }


    private fun setEntertainersList() {
        dataList.add(
            MoreScreenData(
                "Stats",
                R.drawable.stats

            )
        )

        dataList.add(
            MoreScreenData(
                "Laws",
                R.drawable.laws

            )
        )

        dataList.add(
            MoreScreenData(
                "Events",
                R.drawable.create_event
            )
        )

        dataList.add(
            MoreScreenData(
                "Profile",
                R.drawable.profile
            )
        )

        dataList.add(
            MoreScreenData(
                "About App",
                R.drawable.about_app
            )
        )

        dataList.add(
            MoreScreenData(
                "Setting",
                R.drawable.settings
            )
        )


        dataList.add(
            MoreScreenData(
                "My Govt's",
                R.drawable.my_govts
            )
        )

        dataList.add(
            MoreScreenData(
                "Privacy Policy",
                R.drawable.privacy_policy
            )
        )

        dataList.add(
            MoreScreenData(
                "Terms and Conditions",
                R.drawable.terms_condtions
            )
        )

        dataList.add(
            MoreScreenData(
                "Logout",
                R.drawable.logout
            )
        )

    }
    private fun setLPAList() {
        dataList.add(
            MoreScreenData(
                "Make Announcement",
                R.drawable.stats

            )
        )


        dataList.add(
            MoreScreenData(
                "Events",
                R.drawable.create_event
            )
        )

        dataList.add(
            MoreScreenData(
                "Profile",
                R.drawable.profile
            )
        )

        dataList.add(
            MoreScreenData(
                "About App",
                R.drawable.about_app
            )
        )

        dataList.add(
            MoreScreenData(
                "Setting",
                R.drawable.settings
            )
        )


        dataList.add(
            MoreScreenData(
                "My Govt's",
                R.drawable.my_govts
            )
        )

        dataList.add(
            MoreScreenData(
                "Privacy Policy",
                R.drawable.privacy_policy
            )
        )

        dataList.add(
            MoreScreenData(
                "Terms and Conditions",
                R.drawable.terms_condtions
            )
        )

        dataList.add(
            MoreScreenData(
                "Logout",
                R.drawable.logout
            )
        )

    }


    override fun onClick(position: Int) {

        when ((activity as HomeTabActivity).userPreferences.getRole()) {
            "Politicians" -> {
                politiciansClick(position)
            }

            "Entertainers" -> {
                entertainersClick(position)
            }

            "Organizations" -> {
                NGOClick(position)
            }
            "LPA" -> {
                LPAclick(position)
            }
            else -> {
                citizensClicks(position)
            }
        }
    }

    private fun citizensClicks(position: Int) {
        when (position) {
            0 -> {
                val intent = Intent(requireContext(), StatsActivity::class.java)
                startActivity(intent)
            }

            1 -> {
                val intent = Intent(requireContext(), SearchDebateActivity::class.java)
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(requireContext(), AllWinnersActivity::class.java)
                startActivity(intent)
            }

            3 -> {
                val intent = Intent(requireContext(), ProfileActivity2::class.java)
                intent.putExtra("type", "user")
                startActivity(intent)
            }

            4 -> {
                val intent = Intent(requireContext(), EventsActivity ::class.java)
                startActivity(intent)
            }

            5 -> {
                val intent = Intent(requireContext(), DebateRequestsActivity::class.java)
                startActivity(intent)
            }

            6 -> {
                val intent = Intent(requireContext(), StartPetition::class.java)
                startActivity(intent)
            }

            7 -> {
                val intent = Intent(requireContext(), DonationsActivity::class.java)
                startActivity(intent)
            }

            8 -> {
                //my govts
            }

            9 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "about")
                startActivity(intent)
            }

            10 -> {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }

            11 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "privacy")
                startActivity(intent)
            }

            12 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "conditions")
                startActivity(intent)
            }

            13 -> {
                showPopUp()

            }

        }
    }

    private fun politiciansClick(position: Int) {
        when (position) {
            0 -> {
                val intent = Intent(requireContext(), StatsActivity::class.java)
                startActivity(intent)
            }

            1 -> {
                val intent = Intent(requireContext(), SearchDebateActivity::class.java)
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(requireContext(), AllWinnersActivity::class.java)
                startActivity(intent)
            }

            3 -> {
                val intent = Intent(requireContext(), CreateLawActivity::class.java)
                startActivity(intent)
            }

            4 -> {
                val intent = Intent(requireContext(), EventsActivity::class.java)
                startActivity(intent)
            }

            5 -> {
                val intent = Intent(requireContext(), ProfileActivity2::class.java)
                intent.putExtra("type", "user")
                startActivity(intent)
            }

            6 -> {
                val intent = Intent(requireContext(), StartPetition::class.java)
                startActivity(intent)
            }

            7 -> {
                val intent = Intent(requireContext(), DebateRequestsActivity::class.java)
                startActivity(intent)
            }

            8 -> {
                //my govts
            }

            9 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "about")
                startActivity(intent)
            }

            10 -> {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }

            11 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "privacy")
                startActivity(intent)
            }

            12 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "conditions")
                startActivity(intent)
            }

            13 -> {
                showPopUp()

            }

        }
    }

    private fun NGOClick(position: Int) {
        when (position) {
            0 -> {
                val intent = Intent(requireContext(), StatsActivity::class.java)
                startActivity(intent)
            }

            1 -> {
                val intent = Intent(requireContext(), CreateLawActivity::class.java)
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(requireContext(), EventsActivity::class.java)
                startActivity(intent)
            }

            3 -> {
                val intent = Intent(requireContext(), ProfileActivity2::class.java)
                intent.putExtra("type", "user")
                startActivity(intent)
            }


            4 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "about")
                startActivity(intent)
            }

            5 -> {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }

            6 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "privacy")
                startActivity(intent)
            }

            7 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "conditions")
                startActivity(intent)
            }

            8 -> {
                showPopUp()

            }

        }
    }

    private fun entertainersClick(position: Int) {
        when (position) {
            0 -> {
                val intent = Intent(requireContext(), StatsActivity::class.java)
                startActivity(intent)
            }

            1 -> {
                val intent = Intent(requireContext(), CreateLawActivity::class.java)
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(requireContext(), EventsActivity::class.java)
                startActivity(intent)
            }

            3 -> {
                val intent = Intent(requireContext(), ProfileActivity2::class.java)
                intent.putExtra("type", "user")
                startActivity(intent)
            }


            4 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "about")
                startActivity(intent)
            }

            5 -> {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }

            6 -> {
                //pending
            }

            7 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "privacy")
                startActivity(intent)
            }

            8 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "conditions")
                startActivity(intent)
            }

            9 -> {
                showPopUp()

            }

        }
    }

    private fun LPAclick(position: Int) {
        when (position) {
            0 -> {
                val intent = Intent(requireContext(), CreateAnnouncementActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(requireContext(), EventsActivity::class.java)
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(requireContext(), ProfileActivity2::class.java)
                intent.putExtra("type", "user")
                startActivity(intent)
            }


            3 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "about")
                startActivity(intent)
            }

            4 -> {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }

            5 -> {
                //pending
            }

            6 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "privacy")
                startActivity(intent)
            }

            7 -> {
                val intent = Intent(requireContext(), AboutUsActivity::class.java)
                intent.putExtra("type", "conditions")
                startActivity(intent)
            }

            8 -> {
                showPopUp()

            }

        }
    }


    private fun showPopUp() {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Alert!")
        builder.setMessage("Are you sure you want to log out?")
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Yes"
        ) { dialog, which ->

            if (isAdded) {

                logoutViewModel.logout(
                    requireContext(),
                    (activity!! as HomeTabActivity).userPreferences.getUserREf()
                )

                (activity as HomeTabActivity).showDialog()

            }

        }
        builder.setNegativeButton("Cancel")
        { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }


    private fun observeViewModel() {

        logoutViewModel.successful.observe(activity!!, Observer {
            if (!isAdded) {
                return@Observer
            }

            (activity as HomeTabActivity).dismissDialog()

            if (it) {
                if (logoutViewModel.status == "success") {
                    (activity as HomeTabActivity).dismissDialog()
                    (activity as HomeTabActivity).userPreferences.clearPrefs()

                    val intent =
                        Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()

                } else {
                    (activity as HomeTabActivity).setError(logoutViewModel.message)
                }
            } else {
                (activity as HomeTabActivity).setError(logoutViewModel.message)
            }
        })
    }
}