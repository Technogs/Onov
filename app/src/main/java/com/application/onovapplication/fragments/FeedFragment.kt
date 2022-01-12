package com.application.onovapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.activities.CommentActivity
import com.application.onovapplication.activities.UsersActivity
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.ViewDebatesAdapter
import com.application.onovapplication.databinding.FragmentFeedsBinding
import com.application.onovapplication.gpay.CheckoutActivity
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.LikeDataClass
import com.application.onovapplication.viewModels.DonationViewModel
import com.application.onovapplication.viewModels.HomeViewModel
import com.tranxit.stripepayment.activity.add_card.StripeAddCardActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FeedFragment : BaseFragment(), ViewDebatesAdapter.OnClickItem,
    ViewDebatesAdapter.OnClickShare {
    private var param1: String? = null
    private var param2: String? = null
    private var pos: Int? = null
    val LAUNCH_SECOND_ACTIVITY = 1
    var result: String = ""
    var damount: String = ""
    var paymentDesc: String = ""
    private val STRIPE_PAYMENT_REQUEST_CODE = 100

    var likeDataClass: LikeDataClass? = null
    lateinit var binding: FragmentFeedsBinding


    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private val dntnViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }

    var debatesAdapter: ViewDebatesAdapter? = null

    var feedData: MutableList<FeedsData>? = null
    var feedsObj: FeedsData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRole.text =
            "Role: ".plus((activity as BaseAppCompatActivity).userPreferences.getRole())
        //       (activity as HomeTabActivity).showDialog()
        homeViewModel.getFeed(requireContext(), userPreferences.getUserREf())
        showDialog()
        observeViewModel()


//        debatesAdapter = feedData?.let { ViewDebatesAdapter(requireContext(), it) }
//
//        rv_view_debates.adapter = debatesAdapter


    }

    private fun observeViewModel() {
        homeViewModel.successful.observe(requireActivity(), Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {

                    if (homeViewModel.status == "success") {

                        if (homeViewModel.getFeedResponse.data.isNullOrEmpty()) {
                            binding.noDebateData.visibility = View.VISIBLE
                        } else {
                            feedData = homeViewModel.getFeedResponse.data as MutableList<FeedsData>
                            debatesAdapter =
                                ViewDebatesAdapter(requireActivity(), feedData!!, this, this)
                            binding.rvViewDebates.adapter = debatesAdapter

                        }

                    } else {
                        (activity as HomeTabActivity).setError(homeViewModel.message)
                    }

                } else {
                    (activity as HomeTabActivity).setError(homeViewModel.message)
                }
            }
        })
        homeViewModel.successfulLike.observe(requireActivity(), Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {
                    if (homeViewModel.status == "success") {
                        if (homeViewModel.message == "Disliked successfully") {
                            feedsObj?.dislike = true
                            feedsObj?.like = false
                            if ((feedsObj?.likeCount)!!.toInt() > 0 && (feedsObj?.Liked == "1"))
                                feedsObj?.likeCount =
                                    ((feedsObj?.likeCount)!!.toInt() - 1).toString()
                            feedsObj?.dislikeCount =
                                ((feedsObj?.dislikeCount)!!.toInt() + 1).toString()
                            feedsObj?.Liked = "0"
                            feedsObj?.Disliked = "1"

                            binding.rvViewDebates.adapter!!.notifyDataSetChanged()
                        } else if (homeViewModel.message == "Liked successfully") {
                            feedsObj?.like = true
                            feedsObj?.dislike = false
                            feedsObj?.likeCount = ((feedsObj?.likeCount)!!.toInt() + 1).toString()
                            if ((feedsObj?.dislikeCount)!!.toInt() > 0 && (feedsObj?.Disliked == "1"))
                                feedsObj?.dislikeCount =
                                    ((feedsObj?.dislikeCount)!!.toInt() - 1).toString()
                            feedsObj?.Liked = "1"
                            feedsObj?.Disliked = "0"

                            binding.rvViewDebates.adapter!!.notifyDataSetChanged()

                        }

                    } else {
                        (activity as HomeTabActivity).setError(homeViewModel.message)
                    }

                } else {
                    (activity as HomeTabActivity).setError(homeViewModel.message)
                }
            }
        })
        homeViewModel.successfulFeedDelete.observe(requireActivity(), androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (homeViewModel.status == "success") {
//                    setError(profileViewModel.message)
                    if (feedData?.isNotEmpty() == true)
                        feedData!!.remove(feedData!![pos!!])
                    debatesAdapter?.notifyDataSetChanged()

                } else {
                    setError(homeViewModel.message)

                }
            } else {
                setError(homeViewModel.message)
            }
        })
        dntnViewModel.successfulAddDonation.observe(requireActivity(), Observer { successful ->
            (activity as HomeTabActivity).dismissDialog()
            dismissDialog()
            if (successful != null) {
                if (successful) {

                    if (dntnViewModel.status == "success") {

                        Toast.makeText(requireActivity(), dntnViewModel.message, Toast.LENGTH_SHORT)
                            .show()


                    } else {
                        (activity as HomeTabActivity).setError(dntnViewModel.message)
                    }

                } else {
                    (activity as HomeTabActivity).setError(dntnViewModel.message)
                }
            }
        })
        dntnViewModel.successfulPayment.observe(requireActivity(), Observer { successful ->
            (activity as HomeTabActivity).dismissDialog()
            dismissDialog()
            if (successful != null) {
                if (successful) {

                    if (dntnViewModel.status == "success") {
                        showDialog()
                        dntnViewModel.addDonations(
                            requireActivity(),
                            userPreferences.getUserREf(),
                            result,
                            dntnViewModel.paymentModel.transaction_id,
                            damount
                        )

//                        Toast.makeText(requireActivity(),dntnViewModel.message , Toast.LENGTH_SHORT).show()


                    } else {
                        (activity as HomeTabActivity).setError(dntnViewModel.message)
                    }

                } else {
                    (activity as HomeTabActivity).setError(dntnViewModel.message)
                }
            }
        })
    }

    override fun onPause() {

        Log.e("FEEDFRAGMENT", "onPause")

//        debatesAdapter?.releaseExoplayer()

        super.onPause()
        debatesAdapter?.releaseExoplayer()
        debatesAdapter?.exoPlayer?.playWhenReady = false
        debatesAdapter?.exoPlayer?.stop()//.playWhenReady=false
        debatesAdapter?.exoPlayer?.release()
        debatesAdapter?.exoPlayer = null
    }

    override fun onclickdn(feeditem: FeedsData, amount: String, method: String) {
        //  showDialog()
        damount = amount
        result = feeditem.userRef.toString()
        paymentDesc = feeditem.title.toString()
        if (method == "stripe") {
            val intent = Intent(requireActivity(), StripeAddCardActivity::class.java)
            intent.putExtra(
                "stripe_token",
                "pk_test_51K7DgkCqEZCeL0aJqIG6IlKvuxg1HAY0OBDIR8mlzJ1hEPw94X0WSjkloVavG1V0mc6Xa4sFGR0D7XZOxhNdMYJf00TBWfzH0M"
            )
            startActivityForResult(intent, STRIPE_PAYMENT_REQUEST_CODE)
        } else if (method == "gpay") {
            val i = Intent(requireActivity(), CheckoutActivity::class.java)
            i.putExtra("amount",amount)
            startActivity(i)
//        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
        }
//        val i =  Intent(requireActivity(), PeopleActivity::class.java)
//        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
//            if (resultCode == AppCompatActivity.RESULT_OK) {
//                result= data?.getStringExtra("result").toString()
//
//                 dntnViewModel.addDonations(requireActivity(),userPreferences.getUserREf(),result,damount)
//
//            }
//            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
//                // Write your code if there's no result
//                Log.e("result","no result found")
//            }
//        }

        if (data != null) if (requestCode == STRIPE_PAYMENT_REQUEST_CODE) {
            showDialog()
            Log.d("_DonActivityResult", "onActivityResult: " + data.getStringExtra("stripetoken"))
            val token = data.getStringExtra("stripetoken")
            dntnViewModel.paymentStripe(requireActivity(), token.toString(), damount, paymentDesc)
//            dntnViewModel.addDonations(requireActivity(),userPreferences.getUserREf(),result,damount)

        }

    }

    override fun onclickShr(toRef: String, feeditem: FeedsData) {
        val intent = Intent(context, UsersActivity::class.java)
        intent.putExtra("feeds", feeditem)
        startActivity(intent)
    }

    override fun onclickCmnt(feeditem: FeedsData) {
        val intent = Intent(context, CommentActivity::class.java)
        intent.putExtra("feeds", feeditem)
        startActivity(intent)
    }

    override fun onclicklike(type: String, feeditem: FeedsData) {
        showDialog()
        feedsObj = feeditem
        //  feeditem.likeCount = feeditem.likeCount + 1

        if (feeditem.recordType == "post")
            homeViewModel.likeFeed(
                requireActivity(), userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "post", type
            )
        else if (feeditem.recordType == "petition")
            homeViewModel.likeFeed(
                requireActivity(), userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "petition", type
            )
        else if (feeditem.recordType == "donationRequest")
            homeViewModel.likeFeed(
                requireActivity(), userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "donationRequest", type
            )
        else if (feeditem.recordType == "law")
            homeViewModel.likeFeed(
                requireActivity(), userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "law", type
            )
    }

    override fun onDeletePost(feeditem: FeedsData, position: Int) {
        pos = position
        homeViewModel.deleteFeed(requireActivity(), feeditem.id.toString())

    }


}