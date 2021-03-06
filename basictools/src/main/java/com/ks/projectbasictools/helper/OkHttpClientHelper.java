package com.ks.projectbasictools.helper;

import android.content.Context;

import com.ks.projectbasictools.retrofit_cache.EnhancedCacheInterceptor;
import com.ks.projectbasictools.constants.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @作者JTL.
 * @日期2018/3/15.
 * @说明：OkHttpClient帮助类
 */

public class OkHttpClientHelper {
    private OkHttpClient okHttpClient;
    private static Context sContext;
    private static boolean mUseCache;

    private OkHttpClientHelper() {
        init();
    }

    public static OkHttpClientHelper getInstance(Context context, boolean useCache) {
        mUseCache = useCache;
        sContext = context;
        return OkHttpClientHolder.instance;
    }

    /**
     * OkHttpClient 基本设置
     */
    private void init() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //添加HttpLogging拦截器，方便观察，上传和返回json
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new EnhancedCacheInterceptor(sContext));
        //基本设置
        builder.readTimeout(AppConstants.READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(AppConstants.WRITE_TIME, TimeUnit.SECONDS)
                .connectTimeout(AppConstants.CONNECT_TIME, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>(16);

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(HttpUrl.parse(url.host()), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                });
        /*if (mUseCache) {
            //添加Cache拦截器，有网时添加到缓存中，无网时取出缓存
            File file = FileUtils.getInstance().getCacheFolder();
            Cache cache = new Cache(file, 1024 * 1024 * 100);
            builder.cache(cache).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetworkUtils.isNetworkConnected(sContext)) {
                        Request newRequest = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();

                        return chain.proceed(newRequest);
                    } else {
                        int maxTime = 24 * 60 * 60;
                        Response response = chain.proceed(request);
                        Response newResponse = response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                                .removeHeader("Progma")
                                .build();

                        return newResponse;
                    }
                }
            });
        }*/
        okHttpClient = builder.build();
    }

    private static class OkHttpClientHolder {
        private static OkHttpClientHelper instance = new OkHttpClientHelper();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
