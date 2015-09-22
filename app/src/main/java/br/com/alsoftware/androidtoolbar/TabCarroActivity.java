package br.com.alsoftware.androidtoolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.com.alsoftware.androidtoolbar.adapters.PagerAdapterCarros;
import br.com.alsoftware.androidtoolbar.util.SlidingTabLayout;

/**
 * Created by AndreLuiz on 05/07/2015.
 */
public class TabCarroActivity extends AppCompatActivity {
    public static final String LISTA_CARROS_ESPORTIVOS_EXTRA = "LISTA_CARROS_ESPORTIVOS_EXTRA";

    private Toolbar mToolbarPrincipal;
    private Toolbar mToolbarRodape;
    private Fragment mFragment;
    private List<Carro> mCarros;
    private int mCategoria;
    private Snackbar mSnackBar;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_carros);

        Log.i("--> TabCarroActivity", "onCreate...");

        mToolbarPrincipal = (Toolbar)findViewById(R.id.tb_principal);
        mToolbarPrincipal.setTitle("Carros em Tabs e ViewPagers");
        setSupportActionBar(mToolbarPrincipal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.stlTabs);
        /*
        Ao usar TABs personalizadas deve-se configurar BackgourndColor e SelectedIndicatorColors
        via código para evitar bugs na view.
         */
        mSlidingTabLayout.setCustomTabView(R.layout.tab_custom_view, R.id.txtvTabTitulo);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.corPrimaria));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.corDestaque));
        //DistributeEvenly faz com que todas as TABs tenham a mesma largura
        mSlidingTabLayout.setDistributeEvenly(true);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(new PagerAdapterCarros(getSupportFragmentManager(), this));

        /*mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        mSlidingTabLayout.setViewPager(mViewPager);


        /*if (savedInstanceState == null){
            mCarros = getIntent().getParcelableArrayListExtra(LISTA_CARROS_ESPORTIVOS_EXTRA);
            mCategoria = getIntent().getIntExtra("categoria", 0);
        }

        //configToolbarRodape();//Não está mais sendo usado nessa versão

        mFragment = (CarroFragmentList)getSupportFragmentManager().findFragmentByTag("tag_carroListCardFragment");
        if (mFragment == null){
            //mFragment = CarroFragmentList.buildWithLayout(CarroFragmentList.CARD_LIST);
            mFragment = CarroFragmentList.buildWithParams(CarroFragmentList.CARD_LIST,
                    mCarros, mCategoria);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, mFragment, "tag_carroListCardFragment");
            ft.commit();
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    /*private void configToolbarRodape(){
        mToolbarRodape = (Toolbar)findViewById(R.id.tb_Included_Rodape);
        mToolbarRodape.inflateMenu(R.menu.menu_toolbar_rodape);//Não está sendo usado mais nessa versão.
        mToolbarRodape.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            Intent intent = null;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_facebook:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.facebook.com"));
                        break;
                    case R.id.mn_linkedin:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.linkedin.com"));
                        break;
                }
                startActivity(intent);
                return true;
            }
        });
        mToolbarRodape.findViewById(R.id.img_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TabCarroActivity.this, "Menu de configuraçoes acionado!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
