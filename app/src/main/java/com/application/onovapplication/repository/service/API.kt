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
        @Part("options") options: RequestBody?,
        @Part("tillDate") tillDate: RequestBody?,
        @Part("tillTime") tillTime: RequestBody?,
        @Part("areaLimit") areaLimit: RequestBody?,
        @Part("isPublic") isPublic: RequestBody?,
        @Part("isMultiple") isMultiple: RequestBody?,
        @Part pollImage: MultipartBody.Part
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
        @Part("countryName") countryName: RequestBody,
        @Part("stateName") stateName: RequestBody,
        @Part("cityName") cityName: RequestBody,
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
        @Part("Value") value: RequestBody,
        @Part("selfUserRef") selfUserRef: RequestBody
    ): Observable<ProfileModel>

    @Multipart
    @POST("editUser")
    fun editProfile(
        @Part("fullName") name: RequestBody,
        @Part("about") about: RequestBody?,
        @Part("politicalParty") politicalParty: RequestBody?,
        @Part("webUrl") webUrl: RequestBody?,
        @Part("countryName") countryName: RequestBody?,
        @Part("stateName") stateName: RequestBody?,
        @Part("cityName") cityName: RequestBody?,
        @Part("userRef") userRef: RequestBody,
        @Part coverPhoto: MultipartBody.Part  ,
        @Part profilePic: MultipartBody.Part
    ): Observable<LoginResponse>

    @Multipart
    @POST("editPetition")
    fun editPetition(
        @Part("feedId") feedId: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("discription") discription: RequestBody?,
        @Part("websitelink") websitelink: RequestBody?,
        @Part("duration") duration: RequestBody?,
        @Part("signtureCount") signtureCount: RequestBody?,
        @Part("areaLimit") areaLimit: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("mediaType") mediaType: RequestBody,
//        @Part coverPhoto: MultipartBody.Part  ,
        @Part petitionMedia: MultipartBody.Part
    ): Observable<LoginResponse>

    @Multipart
    @POST("createPost")
    fun createPost(
        @Part("userRef") userRef: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("fileType") fileType: RequestBody?,
        @Part("areaLimit") areaLimit: RequestBody?,
        @Part mediaFile: MultipartBody.Part
    ): Observable<LoginResponse>

    @Multipart
    @POST("createEvent")
    fun createEvent(
        @Part("userRef") name: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("start_date") start_date: RequestBody?,
        @Part("start_time") start_time: RequestBody?,
        @Part("end_date") end_date: RequestBody?,
        @Part("end_time") end_time: RequestBody?,
        @Part("areaLimit") stateName: RequestBody?,
        @Part cover_image: MultipartBody.Part,
        @Part ent_video: MultipartBody.Part
    ): Observable<LoginResponse>

    @Multipart
    @POST("addPetition")
    fun addPetition(
        @Part("userRef") name: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("discription") discription: RequestBody?,
        @Part("petitionDate") petitionDate: RequestBody?,
        @Part("websitelink") websitelink: RequestBody?,
        @Part("duration") duration: RequestBody?,
        @Part("signtureCount") signtureCount: RequestBody?,
        @Part("mediaType") mediaType: RequestBody?,
        @Part("areaLimit") stateName: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part petitionMedia: MultipartBody.Part
    ): Observable<LoginResponse>


    @Multipart
    @POST("requestDonation")
    fun requestDonation(
        @Part("userRef") name: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("donationGoal") donationGoal: RequestBody?,
        @Part("fileType") fileType: RequestBody?,
        @Part("tagPeopleArr") tagPeopleArr: RequestBody?,
        @Part("areaLimit") areaLimit: RequestBody?,
        @Part imageFile: MultipartBody.Part
    ): Observable<LoginResponse>


    @Multipart
    @POST("CreateNewPassword")
    fun createNewPassword(
        @Part("email") email: RequestBody, @Part("otp") otp: RequestBody, @Part("password") password: RequestBody
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
    fun getChatList(@Part("userRef") userRef: RequestBody
                    ,@Part("keyword") keyword: RequestBody): Observable<ChatModel>

    @Multipart
    @POST("messageSeen")
    fun getChatseen(@Part("fromRef") userRef: RequestBody
                    ,@Part("toRef") keyword: RequestBody): Observable<ChatModel>

    @Multipart
    @POST("peopleList")
    fun getPeople(@Part("userRef") userRef: RequestBody): Observable<PeopleList>

    @Multipart
    @POST("getAllDonation")
    fun getAllDonation(@Part("userRef") userRef: RequestBody): Observable<DonationModel>

    @Multipart
    @POST("getmydonner")
    fun getmydonner(@Part("userRef") userRef: RequestBody,@Part("keyword") keyword: RequestBody): Observable<DonorsResponse>

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
    @POST("chatNotification")
    fun chatNotification(
        @Part("fromRef") fromRef: RequestBody,
        @Part("toRef") toRef: RequestBody,
        @Part("message") message: RequestBody
    ): Observable<SimpleResponse>

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
        @Part("pollingId") pollingId: RequestBody,
        @Part("pollByRef") pollByRef: RequestBody,
        @Part("pollOption") pollOption: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("viewPetition")
    fun viewPetition(
        @Part("past") past: RequestBody
    ): Observable<ViewPetitionResponse>

    @Multipart
    @POST("getSignPetition")
    fun getSignPetition(
        @Part("petitionId") petitionId: RequestBody
    ): Observable<PetitionSignResponse>

    @Multipart
    @POST("getAllDonationRequest")
    fun getAllDonationRequest(
        @Part("userRef") userRef: RequestBody
    ): Observable<DonationRequestsResponse>

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
    @POST("endpoll")
    fun endpoll(
        @Part("userRef") userRef: RequestBody,
        @Part("pollId") pollId: RequestBody
    ): Observable<SearchModel>

    @Multipart
    @POST("getpollresult")
    fun getpollresult(
        @Part("pollId") pollId: RequestBody,
        @Part("userRef") userRef: RequestBody
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
        @Part("userRef") userRef: RequestBody,
        @Part("searchkeyword") searchkeyword: RequestBody
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
        @Part("userRef") userRef: RequestBody,
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
        @Part("areaLimit") areaLimit: RequestBody,
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
        @Part("areaLimit") areaLimit: RequestBody,
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
        @Part("time") time: RequestBody,
        @Part("isPublic") isPublic: RequestBody,
        @Part("debateDuration") debateDuration: RequestBody,
        @Part("areaLimit") areaLimit: RequestBody
    ): Observable<DebateResponse>

    @Multipart
    @POST("debaterVote")
    fun debaterVote(
        @Part("voteFrom") voteFrom: RequestBody,
        @Part("voteTo") voteTo: RequestBody,
        @Part("debateId") debateId: RequestBody
    ): Observable<DebateResponse>

    @Multipart
    @POST("addsocialmedia")
    fun addsocialmedia(
        @Part("userRef") userRef: RequestBody,
        @Part("instagram") instagram: RequestBody,
        @Part("twitter") twitter: RequestBody,
        @Part("facebook") facebook: RequestBody
    ): Observable<DebateResponse>

    @Multipart
    @POST("getsocialaccount")
    fun getsocialaccount(
        @Part("userRef") userRef: RequestBody
    ): Observable<SocialMediaResponse>

    @Multipart
    @POST("donationdetail")
    fun donationdetail(
        @Part("donationId") donationId: RequestBody
    ): Observable<DonationDetailResponse>

    @Multipart
    @POST("getSetting")
    fun getSettings(
        @Part("userRef") userRef: RequestBody
    ): Observable<GetSettingsResponse>

    @Multipart
    @POST("getAllMedia")
    fun getAllMedia(
        @Part("userRef") userRef: RequestBody
    ): Observable<MediaResponse>


    @Multipart
    @POST("getallpoll")
    fun getallpoll(
        @Part("userRef") userRef: RequestBody
    ): Observable<PollListsResponse>


    @Multipart
    @POST("getdebaterjoiner")
    fun getdebaterjoiner(
        @Part("debateId") debateId: RequestBody
    ): Observable<DebateJoinerResponse>

    @Multipart
    @POST("getAllWinner")
    fun getWinners(
        @Part("userRef") userRef: RequestBody,
        @Part("keyword") keyword: RequestBody,
    ): Observable<GetAllWinnersResponse>

    @Multipart
    @POST("deleteMedia")
    fun deleteMedia(
        @Part("id") id: RequestBody,
        @Part("recordType") recordType: RequestBody,
    ): Observable<MediaResponse>

    @GET("getStats/{id}")
    fun getStats(
        @Path("id") userRef: String
    ): Observable<GetStatsResponse>

    @GET("getDebateDetail/{id}")
    fun getDebateDetail(
        @Path("id") id: String
    ): Observable<DebateDetailResponse>

    @GET("getlivedebate")
    fun getlivedebate(): Observable<LiveDebateModel>

    @GET("getgovtstates")
    fun getgovtstates(): Observable<StateResponse>

    @GET("getjudicial")
    fun getjudicial(): Observable<JudicialModel>

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