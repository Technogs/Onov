package com.application.onovapplication.debate.vlive.ui.main.fragments;


import com.application.onovapplication.debate.vlive.protocol.ClientProxy;
import com.application.onovapplication.debate.vlive.ui.live.HostPKLiveActivity;

public class PKHostInFragment extends AbsPageFragment {
    @Override
    protected int onGetRoomListType() {
        return ClientProxy.ROOM_TYPE_PK;
    }

    @Override
    protected Class<?> getLiveActivityClass() {
        return HostPKLiveActivity.class;
    }
}
