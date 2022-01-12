package com.application.onovapplication.debate.vlive.ui.main.fragments;

import com.application.onovapplication.debate.vlive.protocol.ClientProxy;
import com.application.onovapplication.debate.vlive.ui.live.SingleHostLiveActivity;


public class SingleHostFragment extends AbsPageFragment {
    @Override
    protected int onGetRoomListType() {
        return ClientProxy.ROOM_TYPE_SINGLE;
    }

    @Override
    protected Class<?> getLiveActivityClass() {
        return SingleHostLiveActivity.class;
    }
}
