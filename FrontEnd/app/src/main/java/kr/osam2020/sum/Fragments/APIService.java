package kr.osam2020.sum.Fragments;

import kr.osam2020.sum.Notification.MyResponse;
import kr.osam2020.sum.Notification.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAwt7QyzU:APA91bFTui3o4m43Qj5R-_s1qz4tCNtVqhLgWBGME8aHSV6J44y2UE_tD8NH-ZX8LrB0HLnaETaLOQAqvkhwdAR_lFwXeneJ_9wY7lJ4QecaqWcZMTb_QSXGFYDAYC10f0ncpadDw4pw"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
