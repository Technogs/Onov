package com.application.onovapplication.repository.service

import com.application.onovapplication.model.*

import com.application.onovapplication.repository.ApiManager
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DataManager {

    private val apiManager = ApiManager.instance

    fun register(
        name: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        password: RequestBody,
        role: RequestBody,
        device_token: RequestBody,
        phoneCountryCode: RequestBody,
        mobileType: RequestBody,
        image: MultipartBody.Part,
        coverPhoto: MultipartBody.Part,
        about: RequestBody,
        webUrl: RequestBody,
        support: RequestBody
        ): Observable<RegisterResponse> {
        return apiManager.authService.register(
            name,
            email,
            phone,
            password,
            role,
            device_token,
            phoneCountryCode,
            mobileType,
            image,
            coverPhoto,
            about,
            webUrl,
            support

        )
    }


    fun login(
        email: RequestBody,
        password: RequestBody,
        fieldType: RequestBody,
        osType: RequestBody,
        deviceToken: RequestBody
    ): Observable<LoginResponse> {

        return apiManager.authService.login(fieldType, email , password, osType, deviceToken)
    }
    fun createpolling(
        createBy: RequestBody,
        pollTitle: RequestBody,
        options: ArrayList<RequestBody>,
        tillDate: RequestBody,
        tillTime: RequestBody
    ): Observable<LoginResponse> {

        return apiManager.authService.createpolling(createBy, pollTitle , options, tillDate, tillTime)
    }


    fun getProfile(
        key: RequestBody, value: RequestBody
    ): Observable<ProfileModel> {
        return apiManager.authService.getUserInfo(key, value)
    }
    fun searchUsr(
        find: RequestBody, userRef: RequestBody
    ): Observable<SearchModel> {
        return apiManager.authService.searchUser(userRef, find)
    }
 fun followUsr(
        userRef: RequestBody,  type: RequestBody, flRef: RequestBody
    ): Observable<SearchModel> {
        return apiManager.authService.follow(userRef,type, flRef)
    }

    fun getpollresult(
        pollId: RequestBody
    ): Observable<PollResultResponse> {
        return apiManager.authService.getpollresult(pollId)
    }


    fun editProfile(
        name: RequestBody,
        about: RequestBody,
        webUrl: RequestBody,
        userRef: RequestBody,
        coverPhoto: MultipartBody.Part,
        photo: MultipartBody.Part
    ): Observable<LoginResponse> {

        return apiManager.authService.editProfile(
            name, about, webUrl, userRef,coverPhoto, photo
        )
    }


    fun signPetition(
        userRef: RequestBody,
        petitionId: RequestBody,
        signImage: MultipartBody.Part
    ): Observable<EventModel> {

        return apiManager.authService.signPetition(
            userRef, petitionId, signImage
        )
    }

    fun uploadChatImage(
        chatImg: MultipartBody.Part
    ): Observable<ChatImageResponse> {
        return apiManager.authService.uploadChatImage(chatImg)
    }


    fun saveSettings(
        donationVisible: RequestBody, notification: RequestBody, userRef: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.saveSetting(userRef, notification, donationVisible)
    }

    fun deleteFeed(
        feedId: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.deleteFeed( feedId)
    }

    fun addDonation(
        fromRef: RequestBody, toRef: RequestBody,txnId: RequestBody, amount: RequestBody
    ): Observable<DonationModel> {
        return apiManager.authService.addDonation(fromRef,toRef,txnId,amount)
    }

    fun likeFeed(
        userRef: RequestBody, likedId: RequestBody,likeTo: RequestBody, type: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.likeFeed(userRef,likedId,likeTo,type)
    }

    fun getgovtdata(
        role: RequestBody, state: RequestBody
    ): Observable<GovtDataResponse> {
        return apiManager.authService.getgovtdata(role,state)
    }

    fun searchDebate(
        keyword: RequestBody
    ): Observable<LiveDebateModel> {
        return apiManager.authService.searchDebate(keyword)
    }


 fun commentlike(
        userRef: RequestBody, recordId: RequestBody,likeTo: RequestBody, type: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.commentlike(userRef,recordId,likeTo,type)
    }

    fun addComment(
        userRef: RequestBody, commentTo: RequestBody, conmmentOn: RequestBody, comment: RequestBody, parentCommentId: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.addComment(userRef,commentTo,conmmentOn,comment,parentCommentId)
    }
    fun editFeed(
        recordId: RequestBody, recordType: RequestBody, title: RequestBody, description: RequestBody,fileType: RequestBody,donationGoal: RequestBody, mediaFile: MultipartBody.Part
    ): Observable<FeedModel> {
        return apiManager.authService.editFeed(recordId,recordType,title,description,fileType,donationGoal,mediaFile)
    }


    fun addLaws(
        userRef: RequestBody, lawTitle: RequestBody, description: RequestBody, fileType: RequestBody, documentFile: MultipartBody.Part
    ): Observable<FeedModel> {
        return apiManager.authService.addLaws(userRef,lawTitle,description,fileType,documentFile)
    }

    fun requestDebate(
        userRefTo: RequestBody, userRefFrom: RequestBody,title: RequestBody,message: RequestBody, date: RequestBody, time: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.requestDebate(userRefTo,userRefFrom,title,message,date,time)
    }

    fun debaterVote(
        voteFrom: RequestBody, voteTo: RequestBody, debateId: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.debaterVote(voteFrom,voteTo,debateId)
    }

    fun getComment(
        commentTo: RequestBody,  conmmentOn: RequestBody,userRef: RequestBody,  parentCommentId: RequestBody
    ): Observable<CommentsResponse> {
        return apiManager.authService.getComment(commentTo,conmmentOn,userRef,parentCommentId)
    }

    fun acceptRequest(
        requestId: RequestBody,  isAccept: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.acceptRequest(requestId,isAccept)
    }


  fun shareFeed(
      userRef: RequestBody,  sharedId: RequestBody
    ): Observable<ChatModel> {
        return apiManager.authService.shareFeed(userRef,sharedId)
    }

    fun getSettings(
        userRef: RequestBody
    ): Observable<GetSettingsResponse> {
        return apiManager.authService.getSettings(userRef)
    }

    fun getdebaterjoiner(
        debateId: RequestBody
    ): Observable<DebateJoinerResponse> {
        return apiManager.authService.getdebaterjoiner(debateId)
    }


    fun getFeed(
        userId: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.getFeed(userId)
    }


   fun getEvent(
        userId: RequestBody
    ): Observable<EventModel> {
        return apiManager.authService.getEvent(userId)
    }

    fun getStats(
        userRef: String
    ): Observable<GetStatsResponse> {
        return apiManager.authService.getStats(userRef)
    }

    fun getDebateDetail(
        id: String
    ): Observable<DebateDetailResponse> {
        return apiManager.authService.getDebateDetail(id)
    }

    fun getDebateRequest(
        userRef: String
    ): Observable<DebateResponse> {
        return apiManager.authService.getDebateRequest(userRef)
    }

    fun getAbout(
        privacy: String
    ): Observable<PolicyModel> {
        return apiManager.authService.getAbout(privacy)
    }


    fun getlivedebate(): Observable<LiveDebateModel> {
        return apiManager.authService.getlivedebate()
    }

    fun getgovtstates(): Observable<StateResponse> {
        return apiManager.authService.getgovtstates()
    }

    fun getjudicial(): Observable<JudicialModel> {
        return apiManager.authService.getjudicial()
    }


    fun getWinnings(
        userRef: String
    ): Observable<GetStatsResponse> {
        return apiManager.authService.getWinnings(userRef)
    }


    fun getAllWinners(
    ): Observable<GetAllWinnersResponse> {
        return apiManager.authService.getWinners()
    }

    fun getDebates(
    ): Observable<GetAllWinnersResponse> {
        return apiManager.authService.getDebates()
    }


    fun getNotifications(
        userRef: String
    ): Observable<GetNotificationResponse> {
        return apiManager.authService.getNotifications(userRef)
    }

    fun forgetPassword(
        email: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.forgetPassword(email)
    }


    fun createNewPassword(
        email: RequestBody, password: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.createNewPassword(email, password)
    }

    fun logout(
        userRef: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.logOut(userRef)
    }


    fun getAllDonation(
        userRef: RequestBody
    ): Observable<DonationModel> {
        return apiManager.authService.getAllDonation(userRef)
    }

    fun getmydonner(
        userRef: RequestBody
    ): Observable<DonorsResponse> {
        return apiManager.authService.getmydonner(userRef)
    }

    fun getPeople(
        userRef: RequestBody
    ): Observable<PeopleList> {
        return apiManager.authService.getPeople(userRef)
    }


    fun getChatList(
        userRef: RequestBody
    ): Observable<ChatModel> {
        return apiManager.authService.getChatList(userRef)
    }



    fun changePassword(
        userRef: RequestBody, oldPassword: RequestBody, newPassword: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.

        changePassword(userRef, newPassword, oldPassword)
    }

    fun polling(
        pollingId: RequestBody, pollByRef: RequestBody, pollOption: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.polling(pollingId, pollByRef, pollOption)
    }
    fun paymentStripe(
        cardToken: RequestBody, amount: RequestBody, description: RequestBody
    ): Observable<PaymentModel> {
        return apiManager.authService.payment(cardToken, amount, description)
    }
    fun buyEvent(
        userRef: RequestBody, eventId: RequestBody, ticketCount: RequestBody, totalAmt: RequestBody
    ): Observable<EventModel> {
        return apiManager.authService.
        buyEvent(userRef, ticketCount, eventId,totalAmt)
    }

    fun getAttendees(
       eventId: RequestBody
    ): Observable<AttendeesModel> {
        return apiManager.authService.
        getAttendees(eventId)
    }
    fun eventPrivacy(
        userRef: RequestBody, eventId: RequestBody, action: RequestBody
    ): Observable<EventModel> {
        return apiManager.authService.
        eventPrivacy(userRef, eventId, action)
    }

    fun verifyOtp(email: RequestBody, otp: RequestBody): Observable<LoginResponse> {

        return apiManager.authService.verify(email , otp)

    }

}