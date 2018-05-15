package news.chen.yu.ionic;

import android.content.Intent;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.UMAuthListener;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Vector;

public class UMShare extends CordovaPlugin {
    public static Vector<SHARE_MEDIA> mediaList = new Vector<SHARE_MEDIA>();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            JSONObject options = args.getJSONObject(0);
            this.open(options, callbackContext);
            return true;
        }

        if (action.equals("auth")) {
            Log.i(UMCommon.TAG, "start auth");
            JSONObject options = args.getJSONObject(0);
            this.auth(options, callbackContext);
            return true;
        }


        return false;
    }

    private void open(JSONObject options, final CallbackContext callbackContext) {
        try {
            String url = options.getString("url");
            String image = options.getString("image");
            String title = options.getString("title");
            String desc = options.getString("desc");
            Log.i("u-share", url);
            Log.i("u-share", image);
            Log.i("u-share", title);
            Log.i("u-share", desc);
            UMWeb web = new UMWeb(url);
            web.setTitle(title);
            web.setThumb(new UMImage(cordova.getActivity(), image));
            web.setDescription(desc);
            SHARE_MEDIA[] list = new SHARE_MEDIA[mediaList.size()];
            mediaList.toArray(list);
            new ShareAction(cordova.getActivity())
                    .setDisplayList(list)
                    .withMedia(web)
                    .setCallback(new UMShareListener() {
                        /**
                         * @descrption 分享开始的回调
                         * @param platform 平台类型
                         */
                        @Override
                        public void onStart(SHARE_MEDIA platform) {

                        }

                        /**
                         * @descrption 分享成功的回调
                         * @param platform 平台类型
                         */
                        @Override
                        public void onResult(SHARE_MEDIA platform) {
                            Log.i("u-share", "On result");
                            callbackContext.success(1);
                        }

                        /**
                         * @descrption 分享失败的回调
                         * @param platform 平台类型
                         * @param t 错误原因
                         */
                        @Override
                        public void onError(SHARE_MEDIA platform, Throwable t) {
                            Log.i("u-share", t.getLocalizedMessage());
                            callbackContext.error(t.getLocalizedMessage());
                        }

                        /**
                         * @descrption 分享取消的回调
                         * @param platform 平台类型
                         */
                        @Override
                        public void onCancel(SHARE_MEDIA platform) {
                            callbackContext.error("用户取消");
                        }
                    }).open();
        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    private void auth(JSONObject options, final CallbackContext callbackContext) {
        try {
            SHARE_MEDIA platform;
            int platformIndex = options.getInt("platform");
            switch (platformIndex) {
                case 1:
                    platform = SHARE_MEDIA.WEIXIN;
                    break;
                case 6:
                    platform = SHARE_MEDIA.QQ;
                    break;
                default:
                    platform = SHARE_MEDIA.WEIXIN;
            }


            UMShareAPI.get(cordova.getActivity()).getPlatformInfo(cordova.getActivity(), platform, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    JSONObject json = new JSONObject(map);
                    callbackContext.success(json);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    callbackContext.error(throwable.getLocalizedMessage());
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    callbackContext.error("用户取消");
                }
            });
        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(cordova.getActivity()).onActivityResult(requestCode, resultCode, data);
    }
}