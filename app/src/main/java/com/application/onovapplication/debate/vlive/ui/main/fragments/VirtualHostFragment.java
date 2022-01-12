package com.application.onovapplication.debate.vlive.ui.main.fragments;

import com.application.onovapplication.debate.vlive.protocol.ClientProxy;
import com.application.onovapplication.debate.vlive.ui.live.VirtualHostLiveActivity;


public class VirtualHostFragment extends AbsPageFragment {
    @Override
    protected int onGetRoomListType() {
        return ClientProxy.ROOM_TYPE_VIRTUAL_HOST;
    }

    @Override
    protected Class<?> getLiveActivityClass() {
        return VirtualHostLiveActivity.class;
    }
}
