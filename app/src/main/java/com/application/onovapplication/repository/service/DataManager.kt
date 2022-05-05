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
        countryName: RequestBody,
        stateName: RequestBody,
        cityName: RequestBody,
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
            countryName,
            stateName,
            cityName,
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

        return apiManager.authService.login(fieldType, email, password, osType, deviceToken)
    }

    fun createpolling(
        createBy: RequestBody,
        pollTitle: RequestBody,
        options: RequestBody,
        tillDate: RequestBody,
        tillTime: RequestBody,
        areaLimit: RequestBody,
        isPublic: RequestBody,
        isMultiple: RequestBody,
        pollImage: MultipartBody.Part
    ): Observable<LoginResponse> {

        return apiManager.authService.createpolling(
            createBy,
            pollTitle,
            options,
            tillDate,
            tillTime,
            areaLimit,
            isPublic,
            isMultiple,
            pollImage
        )
    }


    fun getProfile(
        key: RequestBody, value: RequestBody, selfUserRef: RequestBody
    ): Observable<ProfileModel> {
        return apiManager.authService.getUserInfo(key, value, selfUserRef)
    }

    fun searchUsr(
        find: RequestBody, userRef: RequestBody
    ): Observable<SearchModel> {
        return apiManager.authService.searchUser(userRef, find)
    }

    fun endpoll(
        userRef: RequestBody, pollId: RequestBody
    ): Observable<SearchModel> {
        return apiManager.authService.endpoll(userRef, pollId)
    }

    fun followUsr(
        userRef: RequestBody, type: RequestBody, flRef: RequestBody
    ): Observable<SearchModel> {
        return apiManager.authService.follow(userRef, type, flRef)
    }

    fun getpollresult(
        pollId: RequestBody,
        userRef: RequestBody
    ): Observable<PollResultResponse> {
        return apiManager.authService.getpollresult(pollId,userRef)
    }


    fun editProfile(
        name: RequestBody,
        about: RequestBody,
        supporter: RequestBody,
        webUrl: RequestBody,
        countryName: RequestBody,
        stateName: RequestBody,
        cityName: RequestBody,
        userRef: RequestBody,
        coverPhoto: MultipartBody.Part,
        photo: MultipartBody.Part
    ): Observable<LoginResponse> {

        return apiManager.authService.editProfile(
            name,
            about,
            supporter,
            webUrl,
            countryName,
            stateName,
            cityName,
            userRef,
            coverPhoto,
            photo
        )
    }

    fun editPetition(
        feedId: RequestBody,
        title: RequestBody,
        discription: RequestBody,
        websitelink: RequestBody,
        duration: RequestBody,
        signtureCount: RequestBody,
        areaLimit: RequestBody,
        location: RequestBody,
        mediaType: RequestBody,
        petitionMedia: MultipartBody.Part
    ): Observable<LoginResponse> {

        return apiManager.authService.editPetition(
            feedId,
            title,
            discription,
            websitelink,
            duration,
            signtureCount,
            areaLimit,
            location,
            mediaType,
            petitionMedia
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
        return apiManager.authService.deleteFeed(feedId)
    }

    fun addDonation(
        fromRef: RequestBody, toRef: RequestBody, txnId: RequestBody, amount: RequestBody
    ): Observable<DonationModel> {
        return apiManager.authService.addDonation(fromRef, toRef, txnId, amount)
    }

    fun likeFeed(
        userRef: RequestBody, likedId: RequestBody, likeTo: RequestBody, type: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.likeFeed(userRef, likedId, likeTo, type)
    }

    fun getgovtdata(
        role: RequestBody, state: RequestBody
    ): Observable<GovtDataResponse> {
        return apiManager.authService.getgovtdata(role, state)
    }

    fun searchDebate(
        userRef: RequestBody,
        keyword: RequestBody
    ): Observable<LiveDebateModel> {
        return apiManager.authService.searchDebate(userRef,keyword)
    }


    fun commentlike(
        userRef: RequestBody, recordId: RequestBody, likeTo: RequestBody, type: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.commentlike(userRef, recordId, likeTo, type)
    }

    fun addComment(
        userRef: RequestBody,
        commentTo: RequestBody,
        conmmentOn: RequestBody,
        comment: RequestBody,
        parentCommentId: RequestBody
    ): Observable<FeedModel> {
        return apiManager.authService.addComment(
            userRef,
            commentTo,
            conmmentOn,
            comment,
            parentCommentId
        )
    }

    fun editFeed(
        recordId: RequestBody,
        recordType: RequestBody,
        title: RequestBody,
        description: RequestBody,
        fileType: RequestBody,
        donationGoal: RequestBody,
        areaLimit: RequestBody,
        mediaFile: MultipartBody.Part
    ): Observable<FeedModel> {
        return apiManager.authService.editFeed(
            recordId,
            recordType,
            title,
            description,
            fileType,
            donationGoal,
            areaLimit,
            mediaFile
        )
    }


    fun addLaws(
        userRef: RequestBody,
        lawTitle: RequestBody,
        description: RequestBody,
        areaLimit: RequestBody,
        fileType: RequestBody,
        documentFile: MultipartBody.Part
    ): Observable<FeedModel> {
        return apiManager.authService.addLaws(
            userRef,
            lawTitle,
            description,
            areaLimit,
            fileType,
            documentFile
        )
    }

    fun requestDebate(
        userRefTo: RequestBody,
        userRefFrom: RequestBody,
        title: RequestBody,
        message: RequestBody,
        date: RequestBody,
        time: RequestBody,
        isPublic: RequestBody,
        debateDuration: RequestBody,
        areaLimit: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.requestDebate(
            userRefTo,
            userRefFrom,
            title,
            message,
            date,
            time,
            isPublic,
            debateDuration,
            areaLimit
        )
    }

    fun debaterVote(
        voteFrom: RequestBody, voteTo: RequestBody, debateId: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.debaterVote(voteFrom, voteTo, debateId)
    }

    fun addsocialmedia(
        userRef: RequestBody, instagram: RequestBody, twitter: RequestBody, facebook: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.addsocialmedia(userRef, instagram, twitter, facebook)
    }


    fun getsocialaccount(userRef: RequestBody): Observable<SocialMediaResponse> {
        return apiManager.authService.getsocialaccount(userRef)
    }
  fun donationdetail(donationId: RequestBody): Observable<DonationDetailResponse> {
        return apiManager.authService.donationdetail(donationId)
    }

    fun getComment(
        commentTo: RequestBody,
        conmmentOn: RequestBody,
        userRef: RequestBody,
        parentCommentId: RequestBody
    ): Observable<CommentsResponse> {
        return apiManager.authService.getComment(commentTo, conmmentOn, userRef, parentCommentId)
    }

    fun acceptRequest(
        requestId: RequestBody, isAccept: RequestBody
    ): Observable<DebateResponse> {
        return apiManager.authService.acceptRequest(requestId, isAccept)
    }


    fun shareFeed(
        userRef: RequestBody, sharedId: RequestBody
    ): Observable<ChatModel> {
        return apiManager.authService.shareFeed(userRef, sharedId)
    }

    fun getSettings(
        userRef: RequestBody
    ): Observable<GetSettingsResponse> {
        return apiManager.authService.getSettings(userRef)
    }

    fun getAllMedia(
        userRef: RequestBody
    ): Observable<MediaResponse> {
        return apiManager.authService.getAllMedia(userRef)
    }

    fun getallpoll(
        userRef: RequestBody
    ): Observable<PollListsResponse> {
        return apiManager.authService.getallpoll(userRef)
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
        userRef: RequestBody,
        searchkeyword: RequestBody
    ): Observable<EventModel> {
        return apiManager.authService.getEvent(userRef,searchkeyword)
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
        userRef: RequestBody,
        keyword: RequestBody
    ): Observable<GetAllWinnersResponse> {
        return apiManager.authService.getWinners(userRef,keyword)
    }

    fun deleteMedia(
        id: RequestBody,
        recordType: RequestBody
    ): Observable<MediaResponse> {
        return apiManager.authService.deleteMedia(id,recordType)
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
        email: RequestBody,otp: RequestBody, password: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.createNewPassword(email,otp, password)
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
        userRef: RequestBody,  keyword: RequestBody
    ): Observable<DonorsResponse> {
        return apiManager.authService.getmydonner(userRef,keyword)
    }

    fun getPeople(
        userRef: RequestBody
    ): Observable<PeopleList> {
        return apiManager.authService.getPeople(userRef)
    }


    fun getChatList(
        userRef: RequestBody ,    keyword: RequestBody
    ): Observable<ChatModel> {
        return apiManager.authService.getChatList(userRef,keyword)
    }

    fun getChatSeen(
        userRef: RequestBody ,    keyword: RequestBody
    ): Observable<ChatModel> {
        return apiManager.authService.getChatseen(userRef,keyword)
    }


    fun changePassword(
        userRef: RequestBody, oldPassword: RequestBody, newPassword: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.changePassword(userRef, newPassword, oldPassword)
    }

    fun chatNotification(
        fromRef: RequestBody, toRef: RequestBody, message: RequestBody
    ): Observable<SimpleResponse> {
        return apiManager.authService.chatNotification(fromRef, toRef, message)
    }

    fun polling(
        pollingId: RequestBody, pollByRef: RequestBody, pollOption: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.polling(pollingId, pollByRef, pollOption)
    }

    fun viewPetition(
        past: RequestBody
    ): Observable<ViewPetitionResponse> {
        return apiManager.authService.viewPetition(past)
    }

    fun getSignPetition(
        petitionId: RequestBody
    ): Observable<PetitionSignResponse> {
        return apiManager.authService.getSignPetition(petitionId)
    }

    fun getAllDonationRequest(
        userRef: RequestBody
    ): Observable<DonationRequestsResponse> {
        return apiManager.authService.getAllDonationRequest(userRef)
    }

    fun paymentStripe(
        cardToken: RequestBody, amount: RequestBody, description: RequestBody
    ): Observable<PaymentModel> {
        return apiManager.authService.payment(cardToken, amount, description)
    }

    fun buyEvent(
        userRef: RequestBody, eventId: RequestBody, ticketCount: RequestBody, totalAmt: RequestBody
    ): Observable<EventModel> {
        return apiManager.authService.buyEvent(userRef, ticketCount, eventId, totalAmt)
    }

    fun createPost(
        userRef: RequestBody,
        title: RequestBody,
        description: RequestBody,
        fileType: RequestBody,
        areaLimit: RequestBody,
        mediaFile: MultipartBody.Part,
    ): Observable<LoginResponse> {
        return apiManager.authService.createPost(
            userRef,
            title,
            description,
            fileType,
            areaLimit,
            mediaFile
        )
    }


    fun createEvent(
        userRef: RequestBody,
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        start_date: RequestBody,
        start_time: RequestBody,
        end_date: RequestBody,
        end_time: RequestBody,
        areaLimit: RequestBody,
        cover_image: MultipartBody.Part,
        ent_video: MultipartBody.Part,
    ): Observable<LoginResponse> {
        return apiManager.authService.createEvent(
            userRef,
            title,
            description,
            price,
            start_date,
            start_time,
            end_date,
            end_time,
            areaLimit,
            cover_image,
            ent_video
        )
    }

    fun addPetition(
        userRef: RequestBody,
        title: RequestBody,
        discription: RequestBody,
        petitionDate: RequestBody,
        websitelink: RequestBody,
        duration: RequestBody,
        signtureCount: RequestBody,
        mediaType: RequestBody,
        areaLimit: RequestBody,
        location: RequestBody,
        petitionMedia: MultipartBody.Part,
    ): Observable<LoginResponse> {
        return apiManager.authService.addPetition(
            userRef,
            title,
            discription,
            petitionDate,
            websitelink,
            duration,
            signtureCount,
            mediaType,
            areaLimit,
            location,
            petitionMedia
        )
    }

    fun requestDonation(
        userRef: RequestBody,
        title: RequestBody,
        description: RequestBody,
        donationGoal: RequestBody,
        fileType: RequestBody,
        tagPeopleArr: RequestBody,
        areaLimit: RequestBody,
        imageFile: MultipartBody.Part,
    ): Observable<LoginResponse> {
        return apiManager.authService.requestDonation(
            userRef,
            title,
            description,
            donationGoal,
            fileType,
            tagPeopleArr,
            areaLimit,
            imageFile
        )
    }

    fun getAttendees(
        eventId: RequestBody
    ): Observable<AttendeesModel> {
        return apiManager.authService.getAttendees(eventId)
    }

    fun eventPrivacy(
        userRef: RequestBody, eventId: RequestBody, action: RequestBody
    ): Observable<EventModel> {
        return apiManager.authService.eventPrivacy(userRef, eventId, action)
    }

    fun verifyOtp(email: RequestBody, otp: RequestBody): Observable<LoginResponse> {

        return apiManager.authService.verify(email, otp)

    }

}