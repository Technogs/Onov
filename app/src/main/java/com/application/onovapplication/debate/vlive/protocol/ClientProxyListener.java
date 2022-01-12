package com.application.onovapplication.debate.vlive.protocol;


import com.application.onovapplication.debate.vlive.protocol.model.response.AppVersionResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.AudienceListResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.CreateRoomResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.CreateUserResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.EditUserResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.EnterRoomResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.LeaveRoomResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.LoginResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.ModifyUserStateResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.OssPolicyResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.RefreshTokenResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.RoomListResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.SeatStateResponse;



public interface ClientProxyListener {
    void onAppVersionResponse(AppVersionResponse response);

    void onRefreshTokenResponse(RefreshTokenResponse refreshTokenResponse);

    void onOssPolicyResponse(OssPolicyResponse response);

//    void onMusicLisResponse(MusicListResponse response);

//    void onGiftListResponse(GiftListResponse response);

    void onRoomListResponse(RoomListResponse response);

    void onCreateUserResponse(CreateUserResponse response);

    void onEditUserResponse(EditUserResponse response);

    void onLoginResponse(LoginResponse response);

    void onCreateRoomResponse(CreateRoomResponse response);

    void onEnterRoomResponse(EnterRoomResponse response);

    void onLeaveRoomResponse(LeaveRoomResponse response);

    void onAudienceListResponse(AudienceListResponse response);

    void onRequestSeatStateResponse(SeatStateResponse response);

    void onModifyUserStateResponse(ModifyUserStateResponse response);

//    void onSendGiftResponse(SendGiftResponse response);

//    void onGiftRankResponse(GiftRankResponse response);

//    void onGetProductListResponse(ProductListResponse response);

//    void onProductStateChangedResponse(String productId, int state, boolean success);

//    void onProductPurchasedResponse(boolean success);

    void onSeatInteractionResponse(long processId, String userId, int seatNo, int type);

    void onResponseError(int requestType, int error, String message);
}