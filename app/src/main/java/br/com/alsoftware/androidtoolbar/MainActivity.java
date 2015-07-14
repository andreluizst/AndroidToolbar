package br.com.alsoftware.androidtoolbar;

//import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.view.Window;
import android.view.WindowManager;
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
    public static final String LISTA_DE_CARROS_EXTRA = "listaDeCarrosExtra";
    public static final String DRAWER_ITEM_SELECTED_EXTRA = "drawerItemSelectedExtra";

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
    private List<Carro> mCarros;
    private int mItemDrawerSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setColorStatusBar();
        }*/
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

        if (savedInstanceState != null){
            mCarros = savedInstanceState.getParcelableArrayList(LISTA_DE_CARROS_EXTRA);
            mItemDrawerSelected = savedInstanceState.getInt(DRAWER_ITEM_SELECTED_EXTRA);
        }else{
            mCarros = getListaDeCarrosCom(10);
            mItemDrawerSelected = 0;
        }

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


    // CATEGORIES
    private List<PrimaryDrawerItem> getSetCategoryList(){
        String[] names = new String[]{"Todos os Carros", "Carros de Luxo", "Carros Esportivos", "Carros para Colecionadores", "Carros Populares"};
        int[] icons = new int[]{R.drawable.car_1, R.drawable.car_1, R.drawable.car_2, R.drawable.car_3, R.drawable.car_4};
        int[] iconsSelected = new int[]{R.drawable.car_selected_1, R.drawable.car_selected_1, R.drawable.car_selected_2, R.drawable.car_selected_3, R.drawable.car_selected_4};
        List<PrimaryDrawerItem> list = new ArrayList<>();

        for(int i = 0; i < names.length; i++){
            PrimaryDrawerItem aux = new PrimaryDrawerItem();
            aux.setName( names[i] );
            aux.setIcon(ContextCompat.getDrawable(this, icons[i]));
            aux.setTextColor(getResources().getColor(R.color.black));
            aux.setSelectedIcon(ContextCompat.getDrawable(this, iconsSelected[i]));
            aux.setSelectedTextColor(getResources().getColor(R.color.white));
            aux.setIdentifier(i);
            list.add( aux );
        }
        return(list);
    }



    // PERSON
    private Pessoa getPersonByEmail( List<Pessoa> list, ProfileDrawerItem p ){
        Pessoa aux = null;
        for(int i = 0; i < list.size(); i++){
            if( list.get(i).getProfile().getEmail().equalsIgnoreCase( p.getEmail() ) ){
                aux = list.get(i);
                break;
            }
        }
        return( aux );
    }

    private List<Pessoa> getSetProfileList(){
        String[] names = new String[]{"User 1", "User 2", "User 3", "User 4"};
        String[] emails = new String[]{"emailUser_1_@gmail.com", "emailUser_2_@gmail.com", "emailUser_3_@gmail.com", "emailUser_4_@gmail.com"};
        int[] photos = new int[]{R.drawable.person_1, R.drawable.person_2, R.drawable.person_3, R.drawable.person_4};
        int[] background = new int[]{R.drawable.gallardo, R.drawable.vyron, R.drawable.corvette, R.drawable.paganni_zonda};
        List<Pessoa> list = new ArrayList<>();

        for(int i = 0; i < names.length; i++){
            ProfileDrawerItem aux = new ProfileDrawerItem();
            aux.setName(names[i]);
            aux.setEmail(emails[i]);
            aux.setIcon(ContextCompat.getDrawable(this, photos[i]));

            Pessoa p = new Pessoa();
            p.setProfile(aux);
            p.setBackground(background[i]);

            list.add( p );
        }
        return(list);
    }

    private int getPersonPositionByEmail( List<Pessoa> list, ProfileDrawerItem p ){
        for(int i = 0; i < list.size(); i++){
            if( list.get(i).getProfile().getEmail().equalsIgnoreCase( p.getEmail() ) ){
                return(i);
            }
        }
        return( -1 );
    }



    // CAR
    public static List<Carro> getListaDeCarrosCom(int qtd){
        return(getListaDeCarrosCom(qtd, 0));
    }

    public static List<Carro> getListaDeCarrosCom(int qtd, int category){
        String[] models = new String[]{"Gallardo", "Vyron", "Corvette", "Pagani Zonda", "Porsche 911 Carrera", "BMW 720i", "DB77", "Mustang", "Camaro", "CT6"};
        String[] brands = new String[]{"Lamborghini", " bugatti", "Chevrolet", "Pagani", "Porsche", "BMW", "Aston Martin", "Ford", "Chevrolet", "Cadillac"};
        int[] categories = new int[]{2, 1, 2, 1, 1, 4, 3, 2, 4, 1};
        int[] photos = new int[]{R.drawable.gallardo, R.drawable.vyron, R.drawable.corvette, R.drawable.paganni_zonda, R.drawable.porsche_911, R.drawable.bmw_720, R.drawable.db77, R.drawable.mustang, R.drawable.camaro, R.drawable.ct6};
        String description = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos. Lorem Ipsum sobreviveu não só a cinco séculos, como também ao salto para a editoração eletrônica, permanecendo essencialmente inalterado. Se popularizou na década de 60, quando a Letraset lançou decalques contendo passagens de Lorem Ipsum, e mais recentemente quando passou a ser integrado a softwares de editoração eletrônica como Aldus PageMaker.";
        List<Carro> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            Carro c = new Carro( models[i % models.length], brands[ i % brands.length], photos[i % models.length] );
            c.setDescricao(description);
            c.setCategoria(categories[ i % brands.length ] );
            c.setFone("33221155");

            if(category != 0 && c.getCategoria() != category){
                continue;
            }

            listAux.add(c);
        }
        return(listAux);
    }

    public List<Carro> getCarsByCategory(int category){
        List<Carro> listAux = new ArrayList<>();
        for(int i = 0; i < mCarros.size() ; i++) {
            if(category != 0 && mCarros.get(i).getCategoria() != category){
                continue;
            }

            listAux.add(mCarros.get(i));
        }
        return(listAux);
    }

    public List<Carro> getListCars(){
        return(mCarros);
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
        Drawer drawer = null;
        drawer = new DrawerBuilder().withActivity(this)
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
                .addDrawerItems(/*
                        new PrimaryDrawerItem().withName("Carros esportivos")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_1)).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Carros de luxo")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_2)).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Carros de colecionardor")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_3)).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Carros populares")
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.car_4)).withIdentifier(4),*/
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
                        /*for (int count = 0; count < mDrawerLeft.getDrawerItems().size(); count++) {
                            if (mDrawerItemClickedIndex == count && mDrawerItemClickedIndex <= 3) {
                                PrimaryDrawerItem pdi = (PrimaryDrawerItem) mDrawerLeft.getDrawerItems().get(count);
                                pdi.setIcon(ContextCompat.getDrawable(MainActivity.this, getCarroDrawerItem(count, false)));
                                break;
                            }
                        }
                        if (i <= 3) {
                            ((PrimaryDrawerItem) iDrawerItem).setIcon(ContextCompat
                                    .getDrawable(MainActivity.this, getCarroDrawerItem(i, true)));
                        }*/
                        mDrawerItemClickedIndex = i;
                        mItemDrawerSelected = i;
                        //mDrawerLeft.getAdapter().notifyDataSetChanged();
                        switch (iDrawerItem.getIdentifier()) {
                            case 2:
                                it = new Intent(MainActivity.this, CarroActivity.class);
                                it.putExtra("categoria", 2);
                                it.putParcelableArrayListExtra(CarroActivity.LISTA_CARROS_ESPORTIVOS_EXTRA,
                                        (ArrayList<Carro>) getListaDeCarrosCom(10, 2));

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
        List<PrimaryDrawerItem> categorias = getSetCategoryList();
        if(categorias != null && categorias.size() > 0){
            for( int i = categorias.size(); i > 0; i--){
                drawer.addItem(categorias.get(i-1), 0);
            }
            drawer.setSelection(mItemDrawerSelected);
        }
        return drawer;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setColorStatusBar(){
        Log.i("MainActivity", "setColorStatusBar...");
        int alpha = 50;
        getWindow().setStatusBarColor(Color.argb(alpha, 253, 152, 0));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(LISTA_DE_CARROS_EXTRA, (ArrayList<Carro>) mCarros);
        outState.putInt(DRAWER_ITEM_SELECTED_EXTRA, mItemDrawerSelected);
    }
}
