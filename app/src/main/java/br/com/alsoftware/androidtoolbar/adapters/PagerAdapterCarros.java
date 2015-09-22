package br.com.alsoftware.androidtoolbar.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import br.com.alsoftware.androidtoolbar.CarroFragmentList;
import br.com.alsoftware.androidtoolbar.R;

/**
 * Created by AndreLuiz on 16/07/2015.
 */
public class PagerAdapterCarros extends FragmentPagerAdapter {
    private Context mContext;
    // De acordo com o padrão de design do Android Lolipop as TABs devem conter texto em CAIXA ALTA
    private String mTitulos[] = new String[]{"LISTA", "CARD VIEW"};
    private int mAlturaDosIconesEmPixels;
    private int[] mIcones = new int[]{R.drawable.car_1, /*R.drawable.car_1,*/ R.drawable.car_2,
            R.drawable.car_3, R.drawable.car_4};


    public PagerAdapterCarros(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        double scala = context.getResources().getDisplayMetrics().density;
        mAlturaDosIconesEmPixels = (int)(24 * scala + 0.5f);
    }



    @Override
    public Fragment getItem(int position) {
        CarroFragmentList fragment = null;
        switch (position){
            case 0:
                fragment = CarroFragmentList.buildWithLayout(CarroFragmentList.DEFAULT_LIST);
                break;
            case 1:
                fragment = CarroFragmentList.buildWithLayout(CarroFragmentList.CARD_LIST);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitulos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable = ContextCompat.getDrawable(mContext, mIcones[position]);
        drawable.setBounds(0, 0, mAlturaDosIconesEmPixels, mAlturaDosIconesEmPixels);
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
        //return mTitulos[position];
    }
}
