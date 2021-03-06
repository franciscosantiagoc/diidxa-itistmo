package mx.edu.itistmo.diidxaza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import mx.edu.itistmo.diidxaza.Fragments.BusZaFragment;
import mx.edu.itistmo.diidxaza.Fragments.DicZaFragment;
import mx.edu.itistmo.diidxaza.Funciones.SectionsPageAdapter;

public class MainActivityZa extends AppCompatActivity implements BusZaFragment.Comunicador{

    private ViewPager mViewPager;
    //private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_za);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPageAdapter =new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        setIconsToTabLayout();

    }

    //implementa los fragments mediante la clase SectionsPageAdapter
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapt= new SectionsPageAdapter(getSupportFragmentManager());
        adapt.addFragment(new DicZaFragment(),getString(R.string.tab_Dic_Za));
        adapt.addFragment(new BusZaFragment(),getString(R.string.tab_Bus_Za));
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
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_MenEsp) {
            //cambia a otro activity borrando la pila de actividades anteriores
            startActivity(new Intent(getBaseContext(), MainActivityEs.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
        if(id==R.id.action_MenSug){
            Intent intent = new Intent(this, Suggestionsza.class);
            intent.putExtra("Idioma",false);
            startActivity(intent);
        }
        if(id == R.id.action_MenAcer){
            Intent intent = new Intent (this, AcercaDe.class);
            intent.putExtra("Idioma",false);
            startActivity(intent);
        }
        if(id == R.id.action_MenSal){
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void envio() {
        tabLayout.getTabAt(0).select();
    }

}
