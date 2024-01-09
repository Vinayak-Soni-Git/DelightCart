package com.example.delightcart.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.delightcart.Adapters.HomeViewPagerAdapter
import com.example.delightcart.Fragments.Categories.AccessoriesFragment
import com.example.delightcart.Fragments.Categories.ClotheFragment
import com.example.delightcart.Fragments.Categories.ElectronicFragment
import com.example.delightcart.Fragments.Categories.FurnitureFragment
import com.example.delightcart.Fragments.Categories.LaptopsFragment
import com.example.delightcart.Fragments.Categories.MainCategoryFragment
import com.example.delightcart.Fragments.Categories.MobileFragment
import com.example.delightcart.Fragments.Categories.TabletsFragment
import com.example.delightcart.Fragments.Categories.WatchFragment
import com.example.delightcart.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false)
//        initiatePrimaryCard()
//        initiateSecondaryCard()
//        initiateThirdCard()
//        initiateFourthCard()
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerHome: ViewPager2 = view.findViewById(R.id.viewpagerHome)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val categoriesFragment = arrayListOf(
            MainCategoryFragment(),
            ElectronicFragment(),
            MobileFragment(),
            TabletsFragment(),
            WatchFragment(),
            AccessoriesFragment(),
            LaptopsFragment(),
            ClotheFragment(),
            FurnitureFragment()
        )

        viewPagerHome.isUserInputEnabled = false

        val viewPagerAdapter =
            HomeViewPagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        viewPagerHome.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Electronics"
                2 -> tab.text = "Mobiles"
                3 -> tab.text = "Tablets"
                4 -> tab.text = "Watches"
                5 -> tab.text = "Accessories"
                6 -> tab.text = "Laptops"
                7 -> tab.text = "Clothes"
                8 -> tab.text = "Furniture's"
            }
        }.attach()

    }

//    private fun initiatePrimaryCard(){
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewPrimary)
//        recyclerView.setHasFixedSize(true)
//        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//
//
//
//        val arrayList: ArrayList<DataModel> = ArrayList()
//        for(i in 0 until PrimarySliderData().nameArray.size){
//            arrayList.add(DataModel(PrimarySliderData().nameArray[i], PrimarySliderData().versionArray[i],
//                PrimarySliderData().id[i], PrimarySliderData().drawableArray[i]))
//        }
//
//
//        val adapter = CustomSliderAdapter(arrayList)
//        recyclerView.adapter = adapter
//    }

//    private fun initiateSecondaryCard(){
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewSecondary)
//        recyclerView.setHasFixedSize(true)
//        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//
//        val arrayList: ArrayList<DataModel> = ArrayList()
//        for (i in 0 until SecondarySliderData().nameArray.size){
//            arrayList.add(
//                DataModel(SecondarySliderData().nameArray[i], SecondarySliderData().versionArray[i],
//                    SecondarySliderData().id[i], SecondarySliderData().drawableArray[i])
//            )
//        }
//        val adapter = CustomSliderAdapter(arrayList)
//        recyclerView.adapter = adapter
//    }
//    
//    private fun initiateThirdCard(){
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewThird)
//        recyclerView.setHasFixedSize(true)
//        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//        
//        val arrayList: ArrayList<CategoriesModel> = ArrayList()
//        for (i in 0 until ElectronicsSliderData().titles.size){
//            arrayList.add(CategoriesModel(ElectronicsSliderData().titles[i], ElectronicsSliderData().images[i]))
//        }
//        val adapter = CustomSmallSliderAdapter(arrayList)
//        recyclerView.adapter = adapter
//    }
//    private fun initiateFourthCard(){
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFourth)
//        recyclerView.setHasFixedSize(true)
//        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//        
//        val arrayList: ArrayList<CategoriesModel> = ArrayList()
//        for (i in 0 until ClothesSliderData().titles.size){
//            arrayList.add(CategoriesModel(ClothesSliderData().titles[i], ClothesSliderData().images[i]))
//        }
//        val adapter = CustomSmallSliderAdapter(arrayList)
//        recyclerView.adapter = adapter
//    }
}