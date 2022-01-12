package com.application.onovapplication.debate.vlive.ui.main.fragments;

import androidx.fragment.app.Fragment;


import com.application.onovapplication.debate.vlive.AgoraLiveApplication;
import com.application.onovapplication.debate.vlive.Config;
import com.application.onovapplication.debate.vlive.protocol.ClientProxyListener;
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
import com.application.onovapplication.debate.vlive.ui.main.MainActivity;




public abstract class AbstractFragment extends Fragment implements ClientProxyListener {
    protected AgoraLiveApplication application() {
        return (AgoraLiveApplication) getContext().getApplicationContext();
    }

    MainActivity getContainer() {
        return (MainActivity) getActivity();
    }

    protected Config config() {
        return application().config();
    }

    @Override
    public void onAppVersionResponse(AppVersionResponse response) {

    }

    @Override
    public void onRefreshTokenResponse(RefreshTokenResponse refreshTokenResponse) {

    }

    @Override
    public void onOssPolicyResponse(OssPolicyResponse response) {

    }

//    @Override
//    public void onMusicLisResponse(MusicListResponse response) {
//
//    }
//
//    @Override
//    public void onGiftListResponse(GiftListResponse response) {
//
//    }

    @Override
    public void onRoomListResponse(RoomListResponse response) {

    }

    @Override
    public void onCreateUserResponse(CreateUserResponse response) {

    }

    @Override
    public void onLoginResponse(LoginResponse response) {

    }

    @Override
    public void onEditUserResponse(EditUserResponse response) {

    }

    @Override
    public void onCreateRoomResponse(CreateRoomResponse response) {

    }

    @Override
    public void onEnterRoomResponse(EnterRoomResponse response) {

    }

    @Override
    public void onLeaveRoomResponse(LeaveRoomResponse response) {

    }

    @Override
    public void onAudienceListResponse(AudienceListResponse response) {

    }

    @Override
    public void onRequestSeatStateResponse(SeatStateResponse response) {

    }

    @Override
    public void onModifyUserStateResponse(ModifyUserStateResponse response) {

    }

//    @Override
//    public void onSendGiftResponse(SendGiftResponse response) {
//
//    }
//
//    @Override
//    public void onGiftRankResponse(GiftRankResponse response) {
//
//    }

//    @Override
//    public void onGetProductListResponse(ProductListResponse response) {
//
//    }
//
//    @Override
//    public void onProductStateChangedResponse(String productId, int state, boolean success) {
//
//    }
//
//    @Override
//    public void onProductPurchasedResponse(boolean success) {
//
//    }

    @Override
    public void onSeatInteractionResponse(long processId, String userId, int seatNo, int type) {

    }

    @Override
    public void onResponseError(int requestType, int error, String message) {

    }
}