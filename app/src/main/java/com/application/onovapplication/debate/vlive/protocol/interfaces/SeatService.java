package com.application.onovapplication.debate.vlive.protocol.interfaces;


import com.application.onovapplication.debate.vlive.protocol.model.body.RequestModifySeatStateBody;
import com.application.onovapplication.debate.vlive.protocol.model.body.RequestSeatInteractionBody;
import com.application.onovapplication.debate.vlive.protocol.model.response.BooleanResponse;
import com.application.onovapplication.debate.vlive.protocol.model.response.LongResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SeatService {
    @POST("ent/v1/room/{roomId}/users/{userId}/seats")
    Call<LongResponse> requestSeatInteraction(@Header("token") String token, @Path("roomId") String roomId,
                                              @Path("userId") String userId, @Body RequestSeatInteractionBody body);

    @POST("ent/v1/room/{roomId}/seat")
    Call<BooleanResponse> requestModifySeatStates(@Header("token") String token, @Path("roomId") String roomId,
                                                  @Body RequestModifySeatStateBody body);
}