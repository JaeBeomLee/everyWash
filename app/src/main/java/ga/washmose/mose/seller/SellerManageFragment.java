package ga.washmose.mose.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ga.washmose.mose.R;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by leejaebeom on 2016. 10. 10..
 */

public class SellerManageFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ManagePagerAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_manage, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.seller_manage_tab);
        viewPager = (ViewPager)view.findViewById(R.id.seller_manage_pager);
        adapter = new ManagePagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public static class ManagePagerAdapter extends FragmentPagerAdapter {

        private static int PagerNum = 2;

        public ManagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ManageProcessFragment.newInstance(0, "세탁 진행중");
                case 1:
                    return ManageCompleteFragment.newInstance(1, "세탁 완료");
                default:
                    return ManageProcessFragment.newInstance(0, "세탁 진행중");

            }
        }

        @Override
        public int getCount() {
            return PagerNum;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "세탁 진행중";
                case 1:
                    return "세탁 완료";
                default:
                    return "세탁 진행중";
            }
        }
    }
}
