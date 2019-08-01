package com.mks.mwambu.mymap;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//=========================
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NUMBER = "com.exemple.application.example.EXTRA_NUMBER";
    public static final String EXTRA_PRE = "com.exemple.application.example.EXTRA_PRE";
    public static final String EXTRA_NOM = "com.exemple.application.example.EXTRA_NOM";
    public static final String EXTRA_DERNIER = "com.exemple.application.example.EXTRA_DERNIER";
    public static final String EXTRA_INSCRIT = "com.exemple.application.example.EXTRA_INSCRIT";

    EditText tel, passe;
    int taille = 0;
    String[] Password= new String[20];
    String[] Numero = new String[20];
    String[] Nom = new String[20];
    String[] Prenom = new String[20];
    String [] Inscrit= new String[20];
    String [] Positionnement = new String [20];
    String idNom,idDate;
    String idPrenom,idder;
    Button _Enre;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tel = (EditText) findViewById(R.id.tel);
        passe = (EditText) findViewById(R.id.passe);
        btn = (Button)findViewById(R.id.inscription);
        _Enre=(Button) findViewById(R.id.connecter);


        getJSON("http://192.168.43.190/android/membres.php");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),inscriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //===================================================================================================

        tel.addTextChangedListener(inscription);
        passe.addTextChangedListener(inscription);

        _Enre.setEnabled(!tel.getText().toString().trim().isEmpty() && !passe.getText().toString().trim().isEmpty() );


       // ========================================================================================================

    }


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
        taille = 0;
        String[] heroes = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);
            heroes[i] = obj.getString( "id")+"."+obj.getString( "nom")+"-"+obj.getString("prenom");
            Password[i] = obj.getString( "password");
            Numero[i]= obj.getString( "tel");
            Nom[i]= obj.getString("nom");
            Prenom[i] = obj.getString("prenom");
            Inscrit[i] = obj.getString("date_inscription");
            Positionnement[i] = obj.getString("derniere");
            taille ++;
        }

    }

    public void OnLogin(View view){


        String telphone =  this.tel.getText().toString();
        String passe = this.passe.getText().toString();
        int i ;
        int vrai =0;

        String mdp = shail(passe);

        for (i = 0; i < taille; i++) {
            if (Numero[i].equals(telphone ) && Password[i].equals(mdp)){


                idNom=Nom[i];
                idPrenom=Prenom[i];
                idDate=Inscrit[i];
                idder= Positionnement[i];

                //Ajouter dans table online

                String numero;
                String latitude;
                String longitude;
                Toast.makeText(this,"BIENVENU "+idNom,Toast.LENGTH_SHORT).show();
                numero = telphone ;
                latitude = "0";
                longitude = "0";

                String type="position";

                refrech refrech = new refrech(this);

                refrech.execute(type,longitude,latitude,numero );

                mesagg();
                vrai =1;
            }
        }

        if (i == taille && vrai == 0){
            Toast.makeText(this, "Desolé mot de passe ou numero incorrect",Toast.LENGTH_LONG).show();
        }
    }

    public void mesagg(){


        EditText telephone = (EditText) findViewById(R.id.tel);
        String tel= telephone.getText().toString();
        String nom= idNom;
        String preom=idPrenom;


        Intent intent = new Intent(getApplicationContext(),Acceuil.class);

        intent.putExtra(EXTRA_NUMBER , tel);
        intent.putExtra(EXTRA_NOM,nom);
        intent.putExtra(EXTRA_PRE,preom);

        startActivity(intent);
        finish();
    }

    //Fonction qui hash un String

    public String shail(String nom) {
        String hashStr = null;
        String password = nom;
        try {
            MessageDigest md = MessageDigest.getInstance ("SHA-1");
            md.update (password.getBytes ());
            BigInteger hash = new BigInteger (1, md.digest ());
            hashStr = hash.toString (16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        }
        return hashStr;
    }

    //============================================================
    private TextWatcher inscription = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String tele = tel.getText().toString().trim();
            String passee = passe.getText().toString().trim();

            _Enre.setEnabled(!tele.isEmpty() && !passee.isEmpty() && !tele.isEmpty() && !passee.isEmpty() );

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
