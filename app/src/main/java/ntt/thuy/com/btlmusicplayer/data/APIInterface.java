package ntt.thuy.com.btlmusicplayer.data;

import java.util.List;

import ntt.thuy.com.btlmusicplayer.Config;
import ntt.thuy.com.btlmusicplayer.model.Track;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public interface APIInterface {
    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<Track>> getRecentTracks(@Query("created_at") String date,
                                      @Query("limit") int limit,
                                      @Query("") String filter);
}
