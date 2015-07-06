package br.com.alsoftware.androidtoolbar;

//import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbarPrincipal;
    private Toolbar mToolbarRodape;
    private Fragment mFragment;

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
        mFragment = (CarroFragment)getSupportFragmentManager().findFragmentByTag("tag_carroFragment");
        if (mFragment == null){
            mFragment = new CarroFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, mFragment, "tag_carroFragment");
            ft.commit();
        }
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


}
