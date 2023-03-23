import com.example.buildupfrontend.GlobalApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {
    private const val BASE_URL = "http://3.39.183.184"

    //HTTP 통신시 통신 정보를 인터셉트하여 로그로 출력
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AppInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                .setLevel(HttpLoggingInterceptor.Level.BODY)
                .setLevel(HttpLoggingInterceptor.Level.HEADERS)
        )
        .build()

    //리트로핏 정의
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) //(AppInterceptor())
            .build()
    }

    //리트로핏 객체 생성
    fun <T> create(service: Class<T>): T {
        return getRetrofit().create(service)
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val accessToken = GlobalApplication.prefs.getString("accessToken", "") // ViewModel에서 지정한 key로 JWT 토큰을 가져온다.

            val newRequest = request().newBuilder()
                .addHeader("Authorization","Bearer $accessToken") // 헤더에 authorization라는 key로 JWT 를 넣어준다.
                .build()
            proceed(newRequest)
        }
    }
}