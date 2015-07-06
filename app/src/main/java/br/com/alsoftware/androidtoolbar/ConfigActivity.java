package br.com.alsoftware.androidtoolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by AndreLuiz on 05/07/2015.
 */
public class ConfigActivity extends AppCompatActivity {
    private Toolbar mToolbarPrincipal;
    private Toolbar mToolbarRodape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mToolbarPrincipal = (Toolbar)findViewById(R.id.tb_principal);
        mToolbarPrincipal.setTitle("ConfigActivity");
        setSupportActionBar(mToolbarPrincipal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Toast.makeText(ConfigActivity.this, "Menu de configuraçoes acionado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
