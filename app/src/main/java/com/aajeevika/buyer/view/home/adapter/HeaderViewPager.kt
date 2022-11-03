package com.aajeevika.buyer.view.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.aajeevika.buyer.databinding.ListItemViewPagerBinding
import com.aajeevika.buyer.model.data_model.BannerData
import com.aajeevika.buyer.utility.UtilityActions.redirectTo

class HeaderViewPager(
    private val layoutInflater: LayoutInflater,
    private var dataList: ArrayList<BannerData>
) : PagerAdapter() {
    private val cardViewMap = HashMap<Int, CardView>()


    override fun getCount() = dataList.size


    fun setData(dataList: ArrayList<BannerData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewDataBinding = ListItemViewPagerBinding.inflate(layoutInflater, container, false)

        dataList[position].let { banner ->
            viewDataBinding.image = banner.image
            viewDataBinding.cardView.setOnClickListener {
                banner.action?.let { it1 -> viewDataBinding.cardView.context.redirectTo(it1) }
            }
        }

        cardViewMap[position] = viewDataBinding.cardView

        container.addView(viewDataBinding.root)
        return viewDataBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
