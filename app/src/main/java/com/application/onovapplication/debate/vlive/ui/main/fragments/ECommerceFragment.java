package com.application.onovapplication.debate.vlive.ui.main.fragments;

import com.application.onovapplication.debate.vlive.protocol.ClientProxy;
import com.application.onovapplication.debate.vlive.ui.live.ECommerceLiveActivity;


class cECommerceFragment extends AbsPageFragment {
    @Override
    protected int onGetRoomListType() {
        return ClientProxy.ROOM_TYPE_ECOMMERCE;
    }

    @Override
    protected Class<?> getLiveActivityClass() {
        return ECommerceLiveActivity.class;
    }
}
