package com.genetic.salesman.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.genetic.salesman.R
import com.genetic.salesman.adapter.MenuAdapter
import com.genetic.salesman.adapter.NavigationDrawersAdapter
import com.genetic.salesman.customui.SpanningLinearLayoutManager
import com.genetic.salesman.databinding.ActivityMainBinding
import com.genetic.salesman.fragments.*
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.interfaces.ItemClickListener
import com.genetic.salesman.interfaces.NavigationDrawerItemClickListener
import com.genetic.salesman.model.Menus
import com.genetic.salesman.model.ProductCategoryItem
import com.genetic.salesman.model.ProductCategoryResponse
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.fragments.*
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ItemClickListener, NavigationDrawerItemClickListener {
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var resumeFragment: String? = null
    private var preference: Preference? = null
    private var menusArrayList: ArrayList<Menus>? = null
    private var itemClickListener: ItemClickListener? = null
    private var navigationDrawerItemClickListener: NavigationDrawerItemClickListener? = null
    private var menuAdapter: MenuAdapter? = null
    private var selectedItem = 0
    private var selectedFragment: Fragment? = null
    private var doubleBackToExitPressedOnce = false
    private var navigationDrawerAdapter: NavigationDrawersAdapter? = null
    private var productCategoryArrayList: ArrayList<ProductCategoryItem> = ArrayList()
    private var mProductCategory: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(this)
        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resumeFragment = getString(R.string.menu_home)

        navigationDrawerItemClickListener = this
        val layoutManager = LinearLayoutManager(this)
        val decorationItem = DividerItemDecoration(this, layoutManager.orientation)
        binding.navigationDrawerRecyclerview.addItemDecoration(decorationItem)
        binding.navigationDrawerRecyclerview.layoutManager = layoutManager
        navigationDrawerAdapter = NavigationDrawersAdapter(productCategoryArrayList, navigationDrawerItemClickListener!!, this)
        binding.navigationDrawerRecyclerview.adapter = navigationDrawerAdapter

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close)
        binding.drawerLayout.setDrawerListener(actionBarDrawerToggle)
        getProductCategories()
    }

    private fun getProductCategories() {
        Utils.showProgress(this)
        APIClient.getApiInterface().getProductCategory(preference?.getString(AppConstant.DEALER_ID, ""))
            .enqueue(object : Callback<ProductCategoryResponse>{
                override fun onResponse(call: Call<ProductCategoryResponse>, response: Response<ProductCategoryResponse>) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        val meta = body.meta
                        if (meta.code.equals("200")) {
                            if (navigationDrawerAdapter == null) {
                                navigationDrawerAdapter = NavigationDrawersAdapter(body.data, navigationDrawerItemClickListener!!, this@MainActivity)
                                binding.navigationDrawerRecyclerview.adapter = navigationDrawerAdapter
                            } else {
                                productCategoryArrayList = body.data
                                navigationDrawerAdapter!!.refreshList(productCategoryArrayList)
                            }
                        } else {
                            showError(meta.message)
                        }
                    } else {
                        showError(response.message())
                    }
                    loadData()

                }

                override fun onFailure(call: Call<ProductCategoryResponse>, t: Throwable) {
                    Utils.hideProgress()
                    showError("Error occurred!! Please try again later")
                    t.printStackTrace()
                    loadData()
                }

            })
    }

    private fun loadData() {
        itemClickListener = this
        menusArrayList = ArrayList()

        var menu = Menus()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_home)
        menu.activeIconPath = R.drawable.ic_home
        menu.isSelected = false
        menusArrayList!!.add(menu)

        menu = Menus()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_order)
        menu.activeIconPath = R.drawable.ic_order
        menu.isSelected = false
        menusArrayList!!.add(menu)

        menu = Menus()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_payment)
        menu.activeIconPath = R.drawable.ic_online_payment
        menu.isSelected = false
        menusArrayList!!.add(menu)

        menu = Menus()
        menu.menuId = 1
        menu.menuName = getString(R.string.menu_more)
        menu.activeIconPath = R.drawable.ic_more
        menu.isSelected = false
        menusArrayList!!.add(menu)

        menusArrayList!!.add(Menus())
        menuAdapter = MenuAdapter(menusArrayList!!, itemClickListener!!, this@MainActivity)

        val spanningLinearLayoutManager = SpanningLinearLayoutManager(this, menuAdapter!!.itemCount)
        spanningLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        spanningLinearLayoutManager.setScrollHorizontally(false)
        spanningLinearLayoutManager.setMaxItemsToShowInScreen(4)
        binding.bottomNavigationBar.layoutManager = spanningLinearLayoutManager
        binding.bottomNavigationBar.adapter = menuAdapter
        onItemClick(resumeFragment!!)
    }

    override fun onItemClick(menu_name: String) {
        if (menu_name == getString(R.string.menu_home)) {
            if (preference!!.getString(AppConstant.DEALER_ID,"").isEmpty()) {
                gotoLoginActivity()
            } else {
                if (selectedFragment !is HomeFragment) {
                    openFragment(HomeFragment(), false)
                }
                setSelected(0)
            }
        } else if (menu_name == getString(R.string.menu_order)) {
            if (preference!!.getString(AppConstant.DEALER_ID,"").isEmpty()) {
                gotoLoginActivity()
            } else {
                if (selectedFragment !is OrderFragment) {
                    openFragment(OrderFragment(), false)
                }
                setSelected(1)
            }
        } else if (menu_name == getString(R.string.menu_payment)) {
            if (preference!!.getString(AppConstant.DEALER_ID,"").isEmpty()) {
                gotoLoginActivity()
            } else {
                if (selectedFragment !is PaymentFragment) {
                    openFragment(PaymentFragment(), false)
                }
                setSelected(2)
            }
        } else if (menu_name == getString(R.string.menu_more)) {
            if (preference!!.getString(AppConstant.DEALER_ID,"").isEmpty()) {
                gotoLoginActivity()
            } else {
                if (selectedFragment !is MoreFragment) {
                    openFragment(MoreFragment(), false)
                }
                setSelected(3)
            }
        }
    }

    private fun gotoLoginActivity() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun backPressFromOtherFragment(){
        when (selectedFragment) {
            is CartFragment -> {
                openProductFragment()
            }
            is OrderDetailFragment -> {
                onItemClick(getString(R.string.menu_order))
            }
            is PaymentDetailFragment -> {
                onItemClick(getString(R.string.menu_payment))
            }
            is ProfileFragment -> {
                onItemClick(getString(R.string.menu_more))
            }
            is ChangePasswordFragment -> {
                onItemClick(getString(R.string.menu_more))
            }
            is ContactUsFragment -> {
                onItemClick(getString(R.string.menu_more))
            }
            is AddSalesmanFragment -> {
                onItemClick(getString(R.string.menu_more))
            }
            else -> {
                onItemClick(getString(R.string.menu_home))
            }
        }
    }

    fun openOtherFragment(fragment: Fragment) {
        openFragment(fragment, false)
        setSelected(4)
    }

    /**
     * Item Selected Color Change
     *
     * @param index selected item index
     */
    private fun setSelected(index: Int) {
        if (menusArrayList != null && menusArrayList!!.size > 0) {
            selectedItem = index
            for (i in menusArrayList!!.indices) {
                val menu = menusArrayList!![i]
                menu.isSelected = i == index
                menusArrayList!![i] = menu
            }
            if (menuAdapter != null) {
                menuAdapter!!.refreshList(menusArrayList!!)
            }
        }
    }

    /**
     * Open Fragment
     *
     * @param fragment Fragment whichever has to be opened
     * @param addToStack boolean whether fragment has to be added to back-stack
     */
    fun openFragment(fragment: Fragment, addToStack: Boolean) {
        selectedFragment = fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        if (addToStack) {
            transaction.addToBackStack(fragment.javaClass.name)
        }
        transaction.commit()
    }

    var toast: Toast? = null
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
            return
        }
        if (selectedFragment is HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                if (toast != null)
                    toast!!.cancel()
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            toast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG)
            toast!!.show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            backPressFromOtherFragment()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle?.syncState()
    }

    override fun onNavigationDrawerItemClick(productCategoryItem: ProductCategoryItem) {
        mProductCategory = ArrayList()
        mProductCategory?.add(productCategoryItem.id.toString())
        mProductCategory?.add(productCategoryItem.name)
        mProductCategory?.add(productCategoryItem.order.toString())
        mProductCategory?.add(productCategoryItem.slug)
        mProductCategory?.add(productCategoryItem.parentId)
        openProductFragment()
        closeDrawer()
    }

    private fun openProductFragment() {
        val bundle = Bundle()
        bundle.putStringArrayList(AppConstant.PRODUCT_CATEGORY, mProductCategory)
        val productFragment = ProductFragment()
        productFragment.arguments = bundle
        openOtherFragment(productFragment)
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawers()
    }

    fun setProductCategory(mProductCategory: ArrayList<String>) {
        this.mProductCategory = mProductCategory
        openProductFragment()
    }
}