package party.moha.www.hahaexpressmail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import party.moha.www.hahaexpressmail.Service.MailListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView email_account;
    private List<Map<String,String>> data = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email_account = (ListView)findViewById(R.id.main_choose_mail);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this,SendEmailActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //启动监听服务
        Intent intent = new Intent(MainActivity.this, MailListener.class);
        startService(intent);

        try{
            loadAccount();
            System.out.println("bilibili***"+data.toString());
            email_account.setAdapter(new SimpleAdapter(
                    this,
                    data,
                    R.layout.main_choose_mail_item,
                    new String[]{"alias","address"},
                    new int[]{R.id.main_choose_mail_alias,R.id.main_choose_mail_address})
            );
        }catch (Exception e){
            System.out.println("bilibili***"+e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_me) {
            Intent intent = new Intent(this,AboutMeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this,SendEmailActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_receiver) {
            Intent intent = new Intent(this,ReceiverActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadAccount(){
        try{
            File f=getApplicationContext().getDatabasePath("usermail.db").getParentFile();
            if(!f.exists())
                f.mkdirs();
            String FullPath=f.getPath()+"/usermail.db";
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(FullPath, null);

            String sql = "select alias,username,receiver_server from EmailAccount";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{});
            while (cursor.moveToNext()){
                Map<String,String> item = new HashMap<>();

                String alias = cursor.getString(0);
                String username = cursor.getString(1);
                String receiver = cursor.getString(2);
                String host = receiver.substring(receiver.indexOf('.')+1);

                item.put("alias",alias);
                item.put("address",username+"@"+host);

                data.add(item);
            }
        }catch (Exception e){
            System.out.println("bilibili"+e.getMessage());
        }
    }
}
