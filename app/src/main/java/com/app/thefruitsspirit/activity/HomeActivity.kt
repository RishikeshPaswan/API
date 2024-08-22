package com.app.thefruitsspirit.activity


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.databinding.ActivityHomeBinding
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    var navController: NavController? = null
    private var navView: BottomNavigationView? = null
    var type = ""
    var id = ""
    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() {
            return ActivityHomeBinding::inflate
        }

    override fun setup() {
        type = intent.getStringExtra("type").toString()
        id = intent.getStringExtra("id").toString()
        Log.d("dssdaghhsda", "" + type)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostHomeFragment) as NavHostFragment
        navController = navHostFragment.navController
        navView?.setupWithNavController(navController!!)
        navView?.itemIconTintList = null


        navController?.addOnDestinationChangedListener { container, destination, listener ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.llBottomSheet.visibility = View.VISIBLE
                    binding.apply {
                        ivHome.isVisible()
                        ivHomeUnselect.isGone()

                        ivBenefit.isGone()
                        ivBenefitUnselect.isVisible()

                        ivRecipes.isGone()
                        ivRecipesUnselect.isVisible()

                        ivResource.isGone()
                        ivResourceUnselect.isVisible()

                        ivSummary.isGone()
                        ivSummaryUnselect.isVisible()
                    }
                }

                R.id.benefitFragment -> {
                    binding.llBottomSheet.visibility = View.VISIBLE
                }

                R.id.recipesFragment -> {
                    binding.llBottomSheet.visibility = View.VISIBLE
                }

                R.id.resourceFragment -> {
                    binding.llBottomSheet.visibility = View.VISIBLE
                }

                R.id.summaryFragment -> {
                    binding.llBottomSheet.visibility = View.VISIBLE
                }

                else -> {
                    binding.llBottomSheet.visibility = View.GONE

                }
            }
//            val bundle = Bundle()
//            bundle.putString("type",type)
//            bundle.putString("id", id)
//            navController?.navigate(R.id.recipesDetailsFragments,bundle)

        }

        when (type) {


            "1" -> {
                navController!!.navigate(R.id.seeAllFragments)

//                val bundle = Bundle()
//                bundle.putString("type",type)
//                bundle.putString("id", id)
//                navController?.navigate(R.id.recipesDetailsFragments,bundle)
            }

            "2" -> {
//                navController!!.navigate(R.id.recipesFragment)
                val bundle = Bundle()
                bundle.putString("type",type)
                bundle.putString("id", id)
                navController?.navigate(R.id.recipesDetailsFragments,bundle)
            }

            "3" -> {
//                navController!!.navigate(R.id.benefitFragment)
                val bundle = Bundle()
                bundle.putString("type",type)
                bundle.putString("id", id)
                navController?.navigate(R.id.recipesDetailsFragments,bundle)
            }

            "4" -> {
//                navController!!.navigate(R.id.resourceFragment)
                val bundle = Bundle()
                bundle.putString("type",type)
                bundle.putString("id", id)
                navController?.navigate(R.id.recipesDetailsFragments,bundle)
            }
        }

        onClick()
    }

    private fun onClick() {
        binding.apply {
            llHome.setOnClickListener {
                ivHome.isVisible()
                ivHomeUnselect.isGone()

                ivBenefit.isGone()
                ivBenefitUnselect.isVisible()

                ivRecipes.isGone()
                ivRecipesUnselect.isVisible()

                ivResource.isGone()
                ivResourceUnselect.isVisible()

                ivSummary.isGone()
                ivSummaryUnselect.isVisible()
                navController?.navigate(R.id.homeFragment)
            }

            llBenefits.setOnClickListener {
                ivHome.isGone()
                ivHomeUnselect.isVisible()

                ivBenefit.isVisible()
                ivBenefitUnselect.isGone()

                ivRecipes.isGone()
                ivRecipesUnselect.isVisible()

                ivResource.isGone()
                ivResourceUnselect.isVisible()

                ivSummary.isGone()
                ivSummaryUnselect.isVisible()
                navController?.navigate(R.id.benefitFragment)
            }

            llRecipes.setOnClickListener {
                ivHome.isGone()
                ivHomeUnselect.isVisible()

                ivBenefit.isGone()
                ivBenefitUnselect.isVisible()

                ivRecipes.isVisible()
                ivRecipesUnselect.isGone()

                ivResource.isGone()
                ivResourceUnselect.isVisible()

                ivSummary.isGone()
                ivSummaryUnselect.isVisible()
                navController?.navigate(R.id.recipesFragment)
            }

            llResource.setOnClickListener {
                ivHome.isGone()
                ivHomeUnselect.isVisible()

                ivBenefit.isGone()
                ivBenefitUnselect.isVisible()

                ivRecipes.isGone()
                ivRecipesUnselect.isVisible()

                ivResource.isVisible()
                ivResourceUnselect.isGone()

                ivSummary.isGone()
                ivSummaryUnselect.isVisible()
                navController?.navigate(R.id.resourceFragment)
            }

            llSummary.setOnClickListener {
                ivHome.isGone()
                ivHomeUnselect.isVisible()

                ivBenefit.isGone()
                ivBenefitUnselect.isVisible()

                ivRecipes.isGone()
                ivRecipesUnselect.isVisible()

                ivResource.isGone()
                ivResourceUnselect.isVisible()

                ivSummary.isVisible()
                ivSummaryUnselect.isGone()
                navController?.navigate(R.id.summaryFragment)
            }
        }
    }

}