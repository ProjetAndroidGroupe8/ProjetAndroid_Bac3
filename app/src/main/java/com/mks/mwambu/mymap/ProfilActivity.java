package com.mks.mwambu.mymap;

import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfilActivity extends AppCompatActivity {

    public static final String EXTRA_NUMBER = "com.exemple.application.example.EXTRA_NUMBER";
    public static final String EXTRA_PRE = "com.exemple.application.example.EXTRA_PRE";
    public static final String EXTRA_NOM = "com.exemple.application.example.EXTRA_NOM";


    String idnom;
    String idprenom;
    String text;
    String inscrite;
    String dernierepos;

    int taille = 0;
    String[] Password= new String[20];
    String[] Numero = new String[20];
    String[] Nom = new String[20];
    String[] Prenom = new String[20];
    String [] Inscrit= new String[20];
    String [] Positionnement = new String [20];

    String dae;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil2);

        Toast.makeText(this, dae,Toast.LENGTH_SHORT).show();
        getJSON("http://192.168.43.190/android/membres.php");

        Intent intent = getIntent();

        text = intent.getStringExtra(Acceuil.EXTRA_NUMBER);
        idnom = intent.getStringExtra(Acceuil.EXTRA_NOM);
        idprenom = intent.getStringExtra(Acceuil.EXTRA_PRE);

        Button retour=(Button)findViewById(R.id.retour);
        TextView nom=(TextView)findViewById(R.id.nom);
        TextView prenom=(TextView)findViewById(R.id.prenom);
        TextView tel=(TextView)findViewById(R.id.telephone);



        nom.setText("Nom : "+idnom);
        prenom.setText("Prenom : "+idprenom);
        tel.setText("Telephone : "+text);


        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //RECUPERATION DU NUMERO

                //FIN RECUP NUMERO
                Intent intentw = new Intent(getApplicationContext(),Acceuil.class);
                intentw.putExtra(EXTRA_NUMBER,text);
                intentw.putExtra(EXTRA_NUMBER,text);
                intentw.putExtra(EXTRA_NOM,idnom);
                intentw.putExtra(EXTRA_PRE,idprenom);

                startActivity(intentw);
                finish();
            }
        });
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

    public void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);

            Password[i] = obj.getString( "password");
            Numero[i]= obj.getString( "tel");
            Nom[i]= obj.getString("nom");
            Prenom[i] = obj.getString("prenom");
            Inscrit[i] = obj.getString("date_inscription");
            Positionnement[i] = obj.getString("derniere");
            taille ++;
        }
        for (int i = 0; i < taille; i++) {
            if (Numero[i].equals(text)){

                dae = Inscrit[i];


            }
        }
        TextView inscrti=(TextView)findViewById(R.id.inscrit);
        inscrti.setText("Inscrit le "+dae);

    }

    //FIN IMPORTATION//
}
