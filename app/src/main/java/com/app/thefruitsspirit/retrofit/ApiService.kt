package com.app.thefruitsspirit.retrofit


import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.model.BenfitsResponse
import com.app.thefruitsspirit.model.CommanResponse
import com.app.thefruitsspirit.model.DeActiveAccountResponse
import com.app.thefruitsspirit.model.EditProfileResponse
import com.app.thefruitsspirit.model.FaqsResponse
import com.app.thefruitsspirit.model.GetBonusResponse
import com.app.thefruitsspirit.model.GetNotificationResponse
import com.app.thefruitsspirit.model.GetSupportResponse
import com.app.thefruitsspirit.model.HomeResponse
import com.app.thefruitsspirit.model.LogOutResponse
import com.app.thefruitsspirit.model.OneProductBenefitsResponse
import com.app.thefruitsspirit.model.ProductConsumeResponse
import com.app.thefruitsspirit.model.ProfileResponse
import com.app.thefruitsspirit.model.RecipesResponse
import com.app.thefruitsspirit.model.RecipiesProductResponse
import com.app.thefruitsspirit.model.ResourceOneProductResponse
import com.app.thefruitsspirit.model.ResourceResponse
import com.app.thefruitsspirit.model.SignUpResponse
import com.app.thefruitsspirit.model.SummaryResponse
import com.app.thefruitsspirit.model.TermasPrivacyResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ApiService {
    //signUp
    @Multipart
    @POST(ApiConstants.SIGNUP)
    suspend fun userSignUp(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?,
    ): SignUpResponse

    //login
    @POST(ApiConstants.LOGIN)
    suspend fun login(@Body hashMap: HashMap<String, String>): SignUpResponse

    //Verification
    @FormUrlEncoded
    @POST(ApiConstants.OTP_VERIFY)
    suspend fun otpVerification(
        @Field("otp") otp: String,
        @Field("email") email: String
    ): SignUpResponse

    //ResendOtp
    @FormUrlEncoded
    @POST(ApiConstants.RESEND_OTP)
    suspend fun resendOtp(@Field("email") email: String): CommanResponse


    // GetHomeApi
    @GET(ApiConstants.HOME_GET)
    suspend fun getHomeApi(): HomeResponse

    // UserProfile
    @GET(ApiConstants.USER_PROFILE)
    suspend fun getProfile(): ProfileResponse


    //editProfileUp
    @Multipart
    @PUT(ApiConstants.EDIT_PROFILE)
    suspend fun editProfile(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?,
    ): EditProfileResponse


    // GetTermsAboutPrivacy
    @GET(ApiConstants.TERMS_PRIVACY)
    suspend fun getTermsPrivacyApi(@Query("type") type: String): TermasPrivacyResponse


    //DeActiveAccount
    @POST(ApiConstants.DE_ACTIVE_ACCOUNT)
    suspend fun deActiveApi(): DeActiveAccountResponse


    // FaqsResponse
    @GET(ApiConstants.FAQS)
    suspend fun getFaqsApi(): FaqsResponse

    //GetSupportApi
    @POST(ApiConstants.SUPPORT_CONTACT)
    suspend fun getSupportApi(@Body hashMap: HashMap<String, String>): GetSupportResponse


    //LogOut
    @PUT(ApiConstants.LOG_OUT)
    suspend fun logOutApi(): LogOutResponse


    // Benefits
    @GET(ApiConstants.BENEFITS_GET)
    suspend fun getBenefits(): BenfitsResponse


    // GetTermsAboutPrivacy
    @GET(ApiConstants.ONE_PRODUCT_BENEFITS)
    suspend fun getOneProductBenefitsApi(@Query("productId") id: String): OneProductBenefitsResponse

    // RecipesGet
//    @GET(ApiConstants.RECIPES_GET)
    @GET(ApiConstants.RECIPES_GET)
    suspend fun getRecipesApi(@Query("type") id: String): GetBonusResponse

    //Recipes
    @GET(ApiConstants.RECIPES_PRODUCT)
    suspend fun recipesProduct(@Query("productId") id: String): RecipiesProductResponse


    // ResourceGet
    @GET(ApiConstants.RESOURCE_GET)
    suspend fun getResourceApi(): ResourceResponse

    //Resource
    @GET(ApiConstants.PRODUCT_GROW)
    suspend fun resourceProduct(@Query("productId") id: String): ResourceOneProductResponse

    // ConsumeProduct
    @FormUrlEncoded
    @POST(ApiConstants.PRODUCT_CONSUME)
    suspend fun consumeProductApi(@Field("productId") id: String): ProductConsumeResponse

    // Summary
    @POST(ApiConstants.SUMMARY)
    suspend fun summaryApi(@Body map: HashMap<String, String>): SummaryResponse

    // getNotification
    @GET(ApiConstants.GET_NOTIFICATION)
    suspend fun getNotification(): GetNotificationResponse


//    // set Social Login
//    @FormUrlEncoded
//    @POST(ApiConstants.SOCIAL_LOGIN)
//    suspend fun socialLogin(@FieldMap map: HashMap<String, String>): SocialLoginResponse
}

