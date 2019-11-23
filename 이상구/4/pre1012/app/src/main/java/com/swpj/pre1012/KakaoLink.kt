package com.swpj.pre1012


import android.content.Context
import android.widget.Toast
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.util.helper.log.Logger


class KakaoLink {
    object KakaoLinkProvider {


        // 공유하기 눌렀을 때 처리
        fun sendKakaoLink(context: Context, roomName: String) {

            val params = FeedTemplate
                .newBuilder(
                    ContentObject.newBuilder(
                        "어디서 만나지?",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder()
                            .setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()
                    )
                        .setDescrption("약속장소 정하기!").build()
                )
                .setSocial(
                    SocialObject.newBuilder()
                        .setLikeCount(10).setCommentCount(20).setSharedCount(30).setViewCount(40).build()
                )
                //.addButton(ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("'https://developers.kakao.com").setMobileWebUrl("'https://developers.kakao.com").build()))
                .addButton(
                    ButtonObject(
                        "앱에서 바로 확인", LinkObject.newBuilder()
                            .setWebUrl("'https://developers.kakao.com")
                            .setMobileWebUrl("'https://developers.kakao.com")
                            .setAndroidExecutionParams("roomName=${roomName}")
                            //.setIosExecutionParams("key1=value1")
                            .build()
                    )
                )
                .build()

            val serverCallbackArgs = HashMap<String, String>()
            serverCallbackArgs["user_id"] = "\${current_user_id}"
            serverCallbackArgs["product_id"] = "\${shared_product_id}"

            // 상세페이지 접근 리스너
            KakaoLinkService.getInstance()
                .sendDefault(context, params, object : ResponseCallback<KakaoLinkResponse>() {
                    override fun onFailure(errorResult: ErrorResult) {
                        Logger.e(errorResult.toString())

                    }

                    override fun onSuccess(result: KakaoLinkResponse) {


                    }
                })
        }
    }


}

