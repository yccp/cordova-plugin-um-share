package news.chen.yu.ionic;

import android.content.Intent;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class UMShare extends CordovaPlugin {
    public static Vector<SHARE_MEDIA> mediaList = new Vector<SHARE_MEDIA>();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            String param = args.getString(0);
            this.open(param, callbackContext);
            callbackContext.success();
            return true;
        }

        return false;
    }

    private void open(String param, final CallbackContext callbackContext) {
        try {
            JSONObject options = new JSONObject(param);
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
                            callbackContext.success(0);
                        }
                    }).open();
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