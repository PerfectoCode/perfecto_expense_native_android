package app.perfecto.com.expencemanager.data.network;



import java.util.concurrent.TimeUnit;

import app.perfecto.com.expencemanager.BuildConfig;
import app.perfecto.com.expencemanager.data.db.model.User;
import app.perfecto.com.expencemanager.data.network.model.LoginRequest;
import app.perfecto.com.expencemanager.utils.AppConstants;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Abhijit on 10-11-2017.
 */

public interface ApiCall {

    @POST(ApiEndPoint.ENDPOINT_SERVER_LOGIN)
    Observable<User> doServerLogin(@Body LoginRequest.ServerLoginRequest request);






    class Factory {
        private static final int NETWORK_CALL_TIMEOUT = 60;

        public static ApiCall create() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);

            builder.addInterceptor(loggingInterceptor);
            builder.readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(ApiCall.class);
        }
    }
}
