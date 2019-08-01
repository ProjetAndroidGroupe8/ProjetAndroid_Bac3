package com.mks.mwambu.mymap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Acceuil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_NUMBER = "com.exemple.application.example.EXTRA_NUMBER";
    public static final String EXTRA_PRE = "com.exemple.application.example.EXTRA_PRE";
    public static final String EXTRA_NOM = "com.exemple.application.example.EXTRA_NOM";
    public static final String EXTRA_DERNIER = "com.exemple.application.example.EXTRA_DERNIER";
    public static final String EXTRA_INSCRIT = "com.exemple.application.example.EXTRA_INSCRIT";

    String text ;
    String idnom ;
    String idprenom ;
    String idDate ;
    String idDer ;
    String text2;

    ListView listView;
    String idnom2;
    String idprenom2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //==========================RECUPERATION DES DONNEES===========================//

        Intent intent = getIntent();
        text = intent.getStringExtra(MainActivity.EXTRA_NUMBER);

        text2 = intent.getStringExtra(MapsActivity.EXTRA_NUMBER);

        idnom = intent.getStringExtra(MainActivity.EXTRA_NOM);
        idprenom = intent.getStringExtra(MainActivity.EXTRA_PRE);

        idDate = intent.getStringExtra(MainActivity.EXTRA_INSCRIT);

        idnom2 = intent.getStringExtra(MapsActivity.EXTRA_NOM);
        idprenom2 = intent.getStringExtra(MapsActivity.EXTRA_PRE);

        //=================================================================//

        listView = (ListView) findViewById(R.id.listView);
        getJSON("http://192.168.43.190/android/base.php");

        //FIN DONNER

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //IMPORTAION DES MEMBRE DEJA INSCRIT//

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            heroes[i] =" * "+ obj.getString( "nom")+"-"+obj.getString("prenom");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        listView.setAdapter(arrayAdapter);
    }

    //FIN IMPORTATION//

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
        getMenuInflater().inflate(R.menu.acceuil, menu);
        //VARIABLE NUMERO
        Intent intent = getIntent();

        TextView vuewer =(TextView)findViewById(R.id.id_visiteur);
        TextView numero_visiteur =(TextView)findViewById(R.id.numero_visiteur);

        if(idnom!= null){
            vuewer.setText(idnom+" "+idprenom);
        }else if (idnom2!=null){
            vuewer.setText(idnom2+ " "+idprenom2);
        }
        if (text!=null){
            numero_visiteur.setText(text);
        }else if(text2!=null){
            numero_visiteur.setText(text2);
        }

        //FIN
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

        if (id == R.id.nav_ligne) {
            // Handle the camera action
        } else if (id == R.id.nav_carte) {

            Intent intent = getIntent();

             text = intent.getStringExtra(inscriptionActivity.EXTRA_NUMBER);
             idnom = intent.getStringExtra(MainActivity.EXTRA_NOM);
             idprenom = intent.getStringExtra(MainActivity.EXTRA_PRE);


            Intent intente = new Intent(getApplicationContext() ,MapsActivity.class);
            intente.putExtra(EXTRA_NUMBER,text);
            intente.putExtra(EXTRA_NOM,idnom);
            intente.putExtra(EXTRA_PRE,idprenom);
            startActivity(intente);
            finish();




        } else if (id == R.id.nav_profil) {


            Intent intent = getIntent();

            text = intent.getStringExtra(inscriptionActivity.EXTRA_NUMBER);
            idnom = intent.getStringExtra(MainActivity.EXTRA_NOM);
            idprenom = intent.getStringExtra(MainActivity.EXTRA_PRE);
            idDate = intent.getStringExtra(MainActivity.EXTRA_INSCRIT);
            idDer = intent.getStringExtra(MainActivity.EXTRA_DERNIER);

            Intent intente = new Intent(getApplicationContext() ,ProfilActivity.class);
            intente.putExtra(EXTRA_NUMBER,text);
            intente.putExtra(EXTRA_NOM,idnom);
            intente.putExtra(EXTRA_PRE,idprenom);
            intent.putExtra(EXTRA_DERNIER,idDer);
            intent.putExtra(EXTRA_INSCRIT,idDate);
            startActivity(intente);
            finish();


        } else if (id == R.id.nav_deco) {

            Intent intent = getIntent();
            text = intent.getStringExtra(MainActivity.EXTRA_NUMBER);
            idnom = intent.getStringExtra(MainActivity.EXTRA_NOM);
            idprenom = intent.getStringExtra(MainActivity.EXTRA_PRE);

            String type="deconnexion";
            backgroundWorker backgroundWorker = new backgroundWorker(this);
            backgroundWorker.setMot("Musique");
            backgroundWorker.execute(type,text);

            Intent intente = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intente);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
