package com.app.thefruitsspirit.repository

import com.app.thefruitsspirit.genricdatacontainer.Resource
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
import com.app.thefruitsspirit.retrofit.ApiService
import com.app.thefruitsspirit.retrofit.ResponseHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LoginSignUpRepository @Inject constructor(
    private val apiService: ApiService,
    private val responseHandler: ResponseHandler
) {

    // signUp
    suspend fun userSignUp(
        partMap: Map<String, RequestBody>,
        image: MultipartBody.Part?,
    ): Resource<SignUpResponse> {
        return try {
            responseHandler.handleResponse(apiService.userSignUp(partMap, image))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //Login
    suspend fun login(hashMap: HashMap<String, String>): Resource<SignUpResponse> {
        return try {
            responseHandler.handleResponse(apiService.login(hashMap))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    //OtpVerification
    suspend fun otpVerification(otp: String, email: String): Resource<SignUpResponse> {
        return try {
            responseHandler.handleResponse(apiService.otpVerification(otp, email))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //ResendOtp
    suspend fun resendOtp(email: String): Resource<CommanResponse> {
        return try {
            responseHandler.handleResponse(apiService.resendOtp(email))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //getHomeApi
    suspend fun getHomeApi(): Resource<HomeResponse> {
        return try {
            responseHandler.handleResponse(apiService.getHomeApi())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //GetProfile
    suspend fun getProfile(): Resource<ProfileResponse> {
        return try {
            responseHandler.handleResponse(apiService.getProfile())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // editProfileUp
    suspend fun editProfile(
        partMap: Map<String, RequestBody>,
        image: MultipartBody.Part?,

        ): Resource<EditProfileResponse> {
        return try {
            responseHandler.handleResponse(apiService.editProfile(partMap, image))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

//    //getNotification
//    suspend fun getNotification(): Resource<NotificationResponse> {
//        return try {
//            responseHandler.handleResponse(apiService.getNotification())
//        } catch (e: Exception) {
//            responseHandler.handleException(e)
//        }
//    }

    //getPrivacyPolicy
    suspend fun getTermsPrivacyApi(type: String): Resource<TermasPrivacyResponse> {
        return try {
            responseHandler.handleResponse(apiService.getTermsPrivacyApi(type))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //DeActiveAccount
    suspend fun deActiveApi(): Resource<DeActiveAccountResponse> {
        return try {
            responseHandler.handleResponse(apiService.deActiveApi())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //getFaqs
    suspend fun getFaqsApi(): Resource<FaqsResponse> {
        return try {
            responseHandler.handleResponse(apiService.getFaqsApi())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //ContactUs
    suspend fun getSupportApi(hashMap: HashMap<String, String>): Resource<GetSupportResponse> {
        return try {
            responseHandler.handleResponse(apiService.getSupportApi(hashMap))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //logOut
    suspend fun logOutApi(): Resource<LogOutResponse> {
        return try {
            responseHandler.handleResponse(apiService.logOutApi())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //getBenefits
    suspend fun getBenefits(): Resource<BenfitsResponse> {
        return try {
            responseHandler.handleResponse(apiService.getBenefits())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //getOneProductBenefits
    suspend fun getOneProductBenefitsApi(id: String): Resource<OneProductBenefitsResponse> {
        return try {
            responseHandler.handleResponse(apiService.getOneProductBenefitsApi(id))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //getRecipes
    suspend fun getRecipesApi(type : String): Resource<GetBonusResponse> {
        return try {
            responseHandler.handleResponse(apiService.getRecipesApi(type))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //recipesProduct
    suspend fun recipesProduct(id: String): Resource<RecipiesProductResponse> {
        return try {
            responseHandler.handleResponse(apiService.recipesProduct(id))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //getResource
    suspend fun getResourceApi(): Resource<ResourceResponse> {
        return try {
            responseHandler.handleResponse(apiService.getResourceApi())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    //recipesProduct
    suspend fun resourceProduct(id: String): Resource<ResourceOneProductResponse> {
        return try {
            responseHandler.handleResponse(apiService.resourceProduct(id))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //ConsumeProduct
    suspend fun consumeProductApi(id: String): Resource<ProductConsumeResponse> {
        return try {
            responseHandler.handleResponse(apiService.consumeProductApi(id))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    //Summary
    suspend fun summaryApi(map: HashMap<String, String>): Resource<SummaryResponse> {
        return try {
            responseHandler.handleResponse(apiService.summaryApi(map))
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    //getNotification
    suspend fun getNotification(): Resource<GetNotificationResponse> {
        return try {
            responseHandler.handleResponse(apiService.getNotification())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    // Social login
//    suspend fun socialLogin(map : HashMap<String , String>): Resource<SocialLoginResponse> {
//        return try {
//            responseHandler.handleResponse(apiService.socialLogin(map))
//        } catch (e: Exception) {
//            responseHandler.handleException(e)
//        }
//    }
}



