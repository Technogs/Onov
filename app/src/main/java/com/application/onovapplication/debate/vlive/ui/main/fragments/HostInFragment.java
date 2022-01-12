package com.application.onovapplication.debate.vlive.ui.main.fragments;

import com.application.onovapplication.debate.vlive.protocol.ClientProxy;
import com.application.onovapplication.debate.vlive.ui.live.MultiHostLiveActivity;


public class HostInFragment extends AbsPageFragment {
    @Override
    protected int onGetRoomListType() {
        return ClientProxy.ROOM_TYPE_HOST_IN;
    }

    @Override
    protected Class<?> getLiveActivityClass() {
        return MultiHostLiveActivity.class;
    }
}
