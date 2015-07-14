package br.com.alsoftware.androidtoolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.util.List;

/**
 * Created by AndreLuiz on 05/07/2015.
 */
public class CarroActivity extends AppCompatActivity {
    public static final String LISTA_CARROS_ESPORTIVOS_EXTRA = "LISTA_CARROS_ESPORTIVOS_EXTRA";

    private Toolbar mToolbarPrincipal;
    private Toolbar mToolbarRodape;
    private Fragment mFragment;
    private List<Carro> mCarros;
    private int mCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        Log.i("CarroActivity", "onCreate...");

        mToolbarPrincipal = (Toolbar)findViewById(R.id.tb_principal);
        mToolbarPrincipal.setTitle("Carros Esportivos");
        setSupportActionBar(mToolbarPrincipal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
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
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void configToolbarRodape(){
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
                Toast.makeText(CarroActivity.this, "Menu de configuraçoes acionado!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
