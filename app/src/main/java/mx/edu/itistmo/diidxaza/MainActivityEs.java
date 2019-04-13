package mx.edu.itistmo.diidxaza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import mx.edu.itistmo.diidxaza.R;

public class MainActivityEs extends AppCompatActivity {//implements BusEsFragment.Comunicador{

    private ViewPager mViewPager;
    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_es);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPageAdapter =new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setIconsToTabLayout();
    }

    //implementa los fragments mediante la clase SectionsPageAdapter
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapt= new SectionsPageAdapter(getSupportFragmentManager());
        adapt.addFragment(new DicEsFragment(),getString(R.string.tab_Dic));
        adapt.addFragment(new BusEsFragment(),getString(R.string.tab_Bus));
        viewPager.setAdapter(adapt);
    }

    private void setIconsToTabLayout() {
        for(int i = 0;i<=1;i++){
            tabLayout.getTabAt(i).setIcon(getResource(i));
        }
    }

    //implementa iconos en el tablayout
    private int getResource(int pos){
        switch (pos){
            case 0:
                return R.drawable.ic_library_books_white_24dp;

            case 1:
                return R.drawable.ic_library_books_white_24dp;
            default:
                return 10;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_MenZap) {
            //cambia a otro activity borrando la pila de actividades anteriores
            startActivity(new Intent(getBaseContext(), MainActivityZa.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
            //finish();
        }
        if(id==R.id.action_MenSug){
            Intent intent = new Intent(this, Suggestions.class);
            intent.putExtra("Idioma",true);
            startActivity(intent);
        }
        if(id == R.id.action_MenAcer){
            Intent intent = new Intent (this, AcercaDe.class);
            intent.putExtra("Idioma",true);
            startActivity(intent);
        }
        if(id == R.id.action_MenSal){
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public void envio() {
        tabLayout.getTabAt(0).select();
    }
*/

}
