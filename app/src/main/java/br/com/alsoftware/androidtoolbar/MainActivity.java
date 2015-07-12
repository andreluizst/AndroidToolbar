package br.com.alsoftware.androidtoolbar;

//import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbarPrincipal;
    private Toolbar mToolbarRodape;
    private Fragment mFragment;
    private Drawer mDrawerLeft;
    private Drawer mDrawerRight;
    private AccountHeader mDrawerHeader;
    private int mDrawerItemClickedIndex;
    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Toast.makeText(MainActivity.this, "onCheckedChanged=" + (b?"true":"false"), Toast.LENGTH_SHORT).show();
        }
    };
    private FloatingActionMenu mFloatingActionMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbarPrincipal = (Toolbar)findViewById(R.id.tb_principal);
        mToolbarPrincipal.setTitle("AndroidToolbar");
        mToolbarPrincipal.setSubtitle("Exemplo");
        mToolbarPrincipal.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mToolbarPrincipal);

        mToolbarRodape = (Toolbar)findViewById(R.id.tb_Included_Rodape);
        mToolbarRodape.inflateMenu(R.menu.menu_toolbar_rodape);
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
                Toast.makeText(MainActivity.this, "Menu de configuraçoes acionado!", Toast.LENGTH_SHORT).show();
                startConfigActivity();
            }
        });
        mFragment = (CarroFragmentList)getSupportFragmentManager().findFragmentByTag("tag_carroFragment");
        if (mFragment == null){
            mFragment = new CarroFragmentList();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, mFragment, "tag_carroFragment");
            ft.commit();
        }

        /*//Drawer da direita
        mDrawerRight = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                //.withToolbar(mToolbarPrincipal) //Desativar para não dar problema no ícone Hanburger para o DrawerLeft
                .withDrawerGravity(Gravity.END)
                .withDisplayBelowToolbar(true) // Comportamento padrão =  belowToolbar(false)
                .withSelectedItem(-1) // nenhum item selecionado
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(new SecondaryDrawerItem().withName("Carros esportivos")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_1)),
                        new SecondaryDrawerItem().withName("Carros de luxo")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_2)),
                        new SecondaryDrawerItem().withName("Carros de colecionardor")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_3)),
                        new SecondaryDrawerItem().withName("Carros populares")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_4))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(MainActivity.this, "Drawer onItemClick on position " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(MainActivity.this, "Drawer onItemLongClick on position " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();*/

        // Cabeçalho do Drawer da Esquerda
        mDrawerHeader = getDrawerHeader(savedInstanceState);
        // Drawer da Esquerda
        mDrawerLeft = getDrawerMainActivity(savedInstanceState);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);*/
        mFloatingActionMenu = (FloatingActionMenu)findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mn_Config) {
            startConfigActivity();
        }

        return true;
    }

    private void startConfigActivity(){
        Intent it = new Intent(this, ConfigActivity.class);
        startActivity(it);
    }

    public static List<Carro> getListaDeCarrosCom(int qtd){

        Log.i("MainActivity", "getListaDeCarrosCom...");

        String[] models = new String[]{"Gallardo", "Vyron", "Corvette", "Pagani Zonda", "Porsche 911 Carrera", "BMW 720i", "DB77", "Mustang", "Camaro", "CT6"};
        String[] brands = new String[]{"Lamborghini", " bugatti", "Chevrolet", "Pagani", "Porsche", "BMW", "Aston Martin", "Ford", "Chevrolet", "Cadillac"};
        int[] photos = new int[]{R.drawable.gallardo, R.drawable.vyron, R.drawable.corvette, R.drawable.paganni_zonda, R.drawable.porsche_911, R.drawable.bmw_720, R.drawable.db77, R.drawable.mustang, R.drawable.camaro, R.drawable.ct6};
        List<Carro> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            Carro c = new Carro( models[i % models.length], brands[ i % brands.length ], photos[i % models.length] );
            listAux.add(c);
        }
        return(listAux);
    }

    public int getCarroDrawerItem(int posicao, boolean selecionado){
      switch (posicao){
          case 0:
              return selecionado ? R.drawable.car_selected_1 : R.drawable.car_1;
          case 1:
              return selecionado ? R.drawable.car_selected_2 : R.drawable.car_2;
          case 2:
              return selecionado ? R.drawable.car_selected_3 : R.drawable.car_3;
          case 3:
              return selecionado ? R.drawable.car_selected_4 : R.drawable.car_4;
      }
        return -1;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLeft.isDrawerOpen() || (mDrawerRight != null && mDrawerRight.isDrawerOpen())){
            if (mDrawerLeft.isDrawerOpen())
                mDrawerLeft.closeDrawer();
            if (mDrawerRight != null && mDrawerRight.isDrawerOpen())
                mDrawerRight.closeDrawer();
        }else {
            if (mFloatingActionMenu != null && mFloatingActionMenu.isOpened())
                mFloatingActionMenu.close(true);
            else
                super.onBackPressed();
        }
    }

    private AccountHeader getDrawerHeader(Bundle savedInstanceState){
        return new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.ct6)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .addProfiles(
                        new ProfileDrawerItem().withName("Perfil 01").withEmail("pessoa01@gmail.com")
                                .withIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.person_1)),
                        new ProfileDrawerItem().withName("Perfil 02").withEmail("pessoa02@hotmail.com")
                                .withIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.person_2)),
                        new ProfileDrawerItem().withName("Perfil 03").withEmail("pessoa03@yahoo.com")
                                .withIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.person_3)),
                        new ProfileDrawerItem().withName("Perfil 04").withEmail("pessoa04@live.com")
                                .withIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.person_4))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        Toast.makeText(MainActivity.this, "onProfileChanged", Toast.LENGTH_SHORT).show();
                        mDrawerHeader.setBackgroundRes(R.drawable.mustang);
                        return false;
                    }
                })
                .build();
    }

    private Drawer getDrawerMainActivity(Bundle savedInstanceState){
        return new DrawerBuilder().withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withToolbar(mToolbarPrincipal)
                .withTranslucentStatusBar(false)
                        //.withStatusBarColorRes(R.color.blue_deep)
                .withDrawerGravity(Gravity.LEFT)
                .withAccountHeader(mDrawerHeader)
                .withDisplayBelowToolbar(false)
                .withSelectedItem(0)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Carros esportivos")
                            .withIcon(ContextCompat.getDrawable(this, R.drawable.car_1)).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Carros de luxo")
                            .withIcon(ContextCompat.getDrawable(this, R.drawable.car_2)).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Carros de colecionardor")
                            .withIcon(ContextCompat.getDrawable(this, R.drawable.car_3)).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Carros populares")
                            .withIcon(ContextCompat.getDrawable(this, R.drawable.car_4)).withIdentifier(4),
                        new SectionDrawerItem().withName("Configurações"),
                        new SwitchDrawerItem().withName("Notificações")
                            .withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener),
                        new ToggleDrawerItem().withName("Notícias")
                            .withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Intent it = null;
                        Toast.makeText(MainActivity.this, "Drawer onItemClick on position " + i, Toast.LENGTH_SHORT).show();
                        for (int count = 0; count < mDrawerLeft.getDrawerItems().size(); count++) {
                            if (mDrawerItemClickedIndex == count && mDrawerItemClickedIndex <= 3) {
                                PrimaryDrawerItem pdi = (PrimaryDrawerItem) mDrawerLeft.getDrawerItems().get(count);
                                pdi.setIcon(ContextCompat.getDrawable(MainActivity.this, getCarroDrawerItem(count, false)));
                                break;
                            }
                        }
                        if (i <= 3) {
                            ((PrimaryDrawerItem) iDrawerItem).setIcon(ContextCompat
                                    .getDrawable(MainActivity.this, getCarroDrawerItem(i, true)));
                        }
                        mDrawerItemClickedIndex = i;
                        mDrawerLeft.getAdapter().notifyDataSetChanged();
                        switch (iDrawerItem.getIdentifier()) {
                            case 1:
                                it = new Intent(MainActivity.this, CarroActivity.class);
                                startActivity(it);
                                break;
                        }
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(MainActivity.this, "Drawer onItemLongClick on position " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();
    }
}
