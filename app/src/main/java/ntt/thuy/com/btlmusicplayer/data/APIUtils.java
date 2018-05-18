package ntt.thuy.com.btlmusicplayer.data;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class APIUtils {
    public static final String BASE_URL = "http://api.soundcloud.com";

    public static APIInterface getApiInterface(){
        return RetrofitClient.getClient(BASE_URL).create(APIInterface.class);
    }
}
