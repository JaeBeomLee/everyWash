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

import java.util.ArrayList;
import java.util.List;

import ga.washmose.mose.OrderData;
import ga.washmose.mose.R;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by leejaebeom on 2016. 10. 10..
 */

public class SellerManageFragment extends Fragment {
    private static String ARG_ING = "ing";
    private static String ARG_COMPLETE = "complete";
    TabLayout tabLayout;
    ViewPager viewPager;
    ManagePagerAdapter adapter;

    ArrayList<OrderData> ingDatas, completeDatas;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingDatas = getArguments().getParcelableArrayList(ARG_ING);
            completeDatas = getArguments().getParcelableArrayList(ARG_COMPLETE);
        }
    }

    public static SellerManageFragment newInstance(ArrayList<OrderData> ingDatas, ArrayList<OrderData> completeDatas) {
        SellerManageFragment fragment = new SellerManageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ING, ingDatas);
        args.putParcelableArrayList(ARG_COMPLETE, completeDatas);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_manage, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.seller_manage_tab);
        viewPager = (ViewPager)view.findViewById(R.id.seller_manage_pager);
        adapter = new ManagePagerAdapter(getFragmentManager(), ingDatas, completeDatas);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        adapter.notifyDataSetChanged();
        return view;
    }

    public static class ManagePagerAdapter extends FragmentPagerAdapter {

        private static int PagerNum = 2;
        ArrayList<OrderData> ingDatas, completeDatas;
        public ManagePagerAdapter(FragmentManager fm, ArrayList<OrderData> ingDatas, ArrayList<OrderData> completeDatas) {
            super(fm);
            this.ingDatas = ingDatas;
            this.completeDatas = completeDatas;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ManageProcessFragment.newInstance(0, "세탁 진행중", ingDatas);
                case 1:
                    return ManageCompleteFragment.newInstance(1, "세탁 완료", completeDatas);
                default:
                    return ManageProcessFragment.newInstance(0, "세탁 진행중", ingDatas);

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
