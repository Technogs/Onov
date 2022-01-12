package com.application.onovapplication.repository.service

import com.application.onovapplication.firebase.model.ChatDataResponse
import com.application.onovapplication.firebase.model.ChatSender
import com.application.onovapplication.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface API {

    @Multipart
    @POST("userLogin")
    fun login(
        @Part("FieldType") fieldType: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("Password") Password: RequestBody?,
        @Part("deviceType") mobileType: RequestBody?,
        @Part("deviceToken") deviceToken: RequestBody?
    ): Observable<LoginResponse>

    @Multipart
    @POST("createpolling")
    fun createpolling(

        @Part("createBy") createBy: RequestBody?,
        @Part("pollTitle") pollTitle: RequestBody?,
      //  @Part("options") options:  ArrayList<String>?,
        //  RequestBody id = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getuserDetails().getId());
        @Part("options[]") options: ArrayList<RequestBody>,
        @Part("tillDate") tillDate: RequestBody?,
        @Part("tillTime") tillTime: RequestBody?
    ): Observable<LoginResponse>

    @Multipart
    @POST("userRegister")
    fun register(
        @Part("fullName") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("password") password: RequestBody,
        @Part("role") role: RequestBody,
        @Part("deviceToken") device_token: RequestBody,
        @Part("countryCode") countryCode: RequestBody,
        @Part("deviceType") mobileType: RequestBody,
        @Part profile_image: MultipartBody.Part,
        @Part cover_photo: MultipartBody.Part,
        @Part("about") about: RequestBody,
        @Part("webUrl") webUrl: RequestBody,
        @Part("supporter") support: RequestBody
        ): Observable<RegisterResponse>


    @Multipart
    @POST("GetUserInfo")
    fun getUserInfo(
        @Part("Key") key: RequestBody,
        @Part("Value") value: RequestBody
    ): Observable<ProfileModel>

    @Multipart
    @POST("editUser")
    fun editProfile(
        @Part("fullName") name: RequestBody,
        @Part("about") about: RequestBody?,
        @Part("webUrl") webUrl: RequestBody?,
        @Part("userRef") userRef: RequestBody,
        @Part coverPhoto: MultipartBody.Part  ,
        @Part profilePic: MultipartBody.Part
    ): Observable<LoginResponse>


    @Multipart
    @POST("CreateNewPassword")
    fun createNewPassword(
        @Part("email") email: RequestBody, @Part("password") password: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("ForgetPassword")
    fun forgetPassword(
        @Part("email") email: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("uploadChatImage")
    fun uploadChatImage(
        @Part chatImg: MultipartBody.Part
    ): Observable<ChatImageResponse>

    @Multipart
    @POST("userOut")
    fun logOut(@Part("userRef") userRef: RequestBody): Observable<RegisterResponse>

    @Multipart
    @POST("getChatList")
    fun getChatList(@Part("userRef") userRef: RequestBody): Observable<ChatModel>

    @Multipart
    @POST("peopleList")
    fun getPeople(@Part("userRef") userRef: RequestBody): Observable<PeopleList>

    @Multipart
    @POST("getAllDonation")
    fun getAllDonation(@Part("userRef") userRef: RequestBody): Observable<DonationModel>

    @Multipart
    @POST("getmydonner")
    fun getmydonner(@Part("userRef") userRef: RequestBody): Observable<DonorsResponse>

    @Multipart
    @POST("getAttendees")
    fun getAttendees(@Part("eventId") eventId: RequestBody): Observable<AttendeesModel>

    @Multipart
    @POST("changePassword")
    fun changePassword(
        @Part("userRef") userRef: RequestBody,
        @Part("newPass") newPassword: RequestBody,
        @Part("oldPass") pldPassword: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("payment")
    fun payment(
        @Part("cardToken") cardToken: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("description") description: RequestBody
    ): Observable<PaymentModel>

    @Multipart
    @POST("polling")
    fun polling(
        @Part("pollingId") userRef: RequestBody,
        @Part("pollByRef") newPassword: RequestBody,
        @Part("pollOption") pldPassword: RequestBody
    ): Observable<RegisterResponse>

//    @Multipart
//    @POST("getComment")
//    fun getComment(
//        @Part("commentTo") commentTo: RequestBody,
//        @Part("conmmentOn") conmmentOn: RequestBody,
//        @Part("parentCommentId") parentCommentId: RequestBody
//    ): Observable<RegisterResponse>

    @Multipart
    @POST("addSignPetition")
    fun signPetition(
        @Part("userRef") userRef: RequestBody,
        @Part("petitionId") petitionId: RequestBody,
       @Part signImage: MultipartBody.Part
    ): Observable<EventModel>

    @Multipart
    @POST("eventPrivacy")
    fun eventPrivacy(
        @Part("userRef") userRef: RequestBody,
        @Part("eventId") eventId: RequestBody,
        @Part("action") action: RequestBody
    ): Observable<EventModel>

    @Multipart
    @POST("buyEvent")
    fun buyEvent(
        @Part("userRef") userRef: RequestBody,
        @Part("eventId") eventId: RequestBody,
        @Part("ticketCount") ticketCount: RequestBody,
        @Part("totalAmt") totalAmt: RequestBody
    ): Observable<EventModel>

    @Multipart
    @POST("searchUser")
    fun searchUser(
        @Part("userRef") userRef: RequestBody,
        @Part("find") find: RequestBody
    ): Observable<SearchModel>

    @Multipart
    @POST("getpollresult")
    fun getpollresult(
        @Part("pollId") pollId: RequestBody,
    ): Observable<PollResultResponse>

    @Multipart
    @POST("follow")
    fun follow(
        @Part("userRef") userRef: RequestBody,
        @Part("type") type: RequestBody,
        @Part("followRef") followRef: RequestBody
    ): Observable<SearchModel>

    @Multipart
    @POST("userVerify")
    fun verify(
        @Part("email") mobile: RequestBody,
        @Part("Code") code: RequestBody
    ): Observable<LoginResponse>


    @Multipart
    @POST("getFeed")
    fun getFeed(
        @Part("userRef") userId: RequestBody
    ): Observable<FeedModel>


    @Multipart
    @POST("getEvent")
    fun getEvent(
        @Part("userRef") userId: RequestBody
    ): Observable<EventModel>




    @Multipart
    @POST("saveSetting")
    fun saveSetting(
        @Part("userRef") userRef: RequestBody,
        @Part("notification") notification: RequestBody,
        @Part("donationVisible") donationVisible: RequestBody
    ): Observable<RegisterResponse>


    @Multipart
    @POST("addDonation")
    fun addDonation(
        @Part("fromRef") fromRef: RequestBody,
        @Part("toRef") toRef: RequestBody,
        @Part("txnId") txnId: RequestBody,
        @Part("amount") amount: RequestBody
    ): Observable<DonationModel>


   @Multipart
    @POST("deleteFeed")
    fun deleteFeed(
       @Part("feedId") feedId: RequestBody
   ): Observable<FeedModel>


    @Multipart
    @POST("like")
    fun likeFeed(
        @Part("userRef") userRef: RequestBody,
        @Part("recordId") recordId: RequestBody,
        @Part("likeTo") likeTo: RequestBody,
        @Part("type") type: RequestBody
    ): Observable<FeedModel>

    @Multipart
    @POST("getgovtdata")
    fun getgovtdata(
        @Part("role") role: RequestBody,
        @Part("state") state: RequestBody
    ): Observable<GovtDataResponse>


    @Multipart
    @POST("searchDebate")
    fun searchDebate(
        @Part("keyword") keyword: RequestBody
    ): Observable<LiveDebateModel>

    @Multipart
    @POST("editFeed")
    fun editFeed(
        @Part("recordId") recordId: RequestBody,
        @Part("recordType") recordType: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fileType") fileType: RequestBody,
        @Part("donationGoal") donationGoal: RequestBody,
        @Part mediaFile: MultipartBody.Part
    ): Observable<FeedModel>


    @Multipart
    @POST("commentlike")
    fun commentlike(
        @Part("userRef") userRef: RequestBody,
        @Part("recordId") recordId: RequestBody,
        @Part("likeTo") likeTo: RequestBody,
        @Part("type") type: RequestBody
    ): Observable<FeedModel>

    @Multipart
    @POST("getComment")
    fun getComment(
        @Part("commentTo") commentTo: RequestBody,
        @Part("conmmentOn") conmmentOn: RequestBody,
        @Part("userRef") userRef: RequestBody,
    @Part("parentCommentId") parentCommentId: RequestBody
    ): Observable<CommentsResponse>


    @Multipart
    @POST("acceptRequest")
    fun acceptRequest(
        @Part("requestId") requestId: RequestBody,
        @Part("isAccept") isAccept: RequestBody
    ): Observable<DebateResponse>


    @Multipart
    @POST("shareFeed")
    fun shareFeed(
        @Part("userRef") userRef: RequestBody,
        @Part("sharedId") sharedId: RequestBody
    ): Observable<ChatModel>

    @Multipart
    @POST("addComment")
    fun addComment(
        @Part("userRef") userRef: RequestBody,
        @Part("commentTo") commentTo: RequestBody,
        @Part("conmmentOn") conmmentOn: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part("parentCommentId") parentCommentId: RequestBody
    ): Observable<FeedModel>

    @Multipart
    @POST("addLaws")
    fun addLaws(
        @Part("userRef") userRef: RequestBody,
        @Part("lawTitle") lawTitle: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fileType") fileType: RequestBody,
        @Part documentFile: MultipartBody.Part
     //   @Part("documentFile") documentFile: RequestBody
    ): Observable<FeedModel>


    @Multipart
    @POST("requestDebate")
    fun requestDebate(
        @Part("userRefTo") userRefTo: RequestBody,
        @Part("userRefFrom") userRefFrom: RequestBody,
        @Part("title") title: RequestBody,
        @Part("message") message: RequestBody,
        @Part("date") date: RequestBody,
        @Part("time") time: RequestBody
    ): Observable<DebateResponse>

    @Multipart
    @POST("debaterVote")
    fun debaterVote(
        @Part("voteFrom") voteFrom: RequestBody,
        @Part("voteTo") voteTo: RequestBody,
        @Part("debateId") debateId: RequestBody
    ): Observable<DebateResponse>

    @Multipart
    @POST("getSetting")
    fun getSettings(
        @Part("userRef") userRef: RequestBody
    ): Observable<GetSettingsResponse>


    @Multipart
    @POST("getdebaterjoiner")
    fun getdebaterjoiner(
        @Part("debateId") debateId: RequestBody
    ): Observable<DebateJoinerResponse>


    @GET("getStats/{id}")
    fun getStats(
        @Path("id") userRef: String
    ): Observable<GetStatsResponse>

    @GET("getDebateDetail/{id}")
    fun getDebateDetail(
        @Path("id") id: String
    ): Observable<DebateDetailResponse>

    @GET("getlivedebate")
    fun getlivedebate(
    ): Observable<LiveDebateModel>

    @GET("getgovtstates")
    fun getgovtstates(
    ): Observable<StateResponse>

    @GET("getjudicial")
    fun getjudicial(
    ): Observable<JudicialModel>

    @GET("getDebateRequest/{id}")
    fun getDebateRequest(
        @Path("id") userRef: String
    ): Observable<DebateResponse>

    @GET("aboutApp/{privacy}")
    fun getAbout(
        @Path("privacy") privacy: String
    ): Observable<PolicyModel>

    @GET("getEvent")
    fun getEvents(): Observable<EventModel>


    @GET("getWinner/{id}")
    fun getWinnings(
        @Path("id") userRef: String
    ): Observable<GetStatsResponse>

    @GET("getAllWinner")
    fun getWinners(
    ): Observable<GetAllWinnersResponse>

    @GET("getDebates")
    fun getDebates(
    ): Observable<GetAllWinnersResponse>

    @GET("getNotification/{id}")
    fun getNotifications(
        @Path("id") userRef: String
    ): Observable<GetNotificationResponse>


    //local firebase notification
    @POST("fcm/send")
    fun sendNotification(
        @Body
        body: ChatSender?
    ): Call<ChatDataResponse>


}