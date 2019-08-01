package com.mks.mwambu.mymap;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class inscriptionActivity extends AppCompatActivity {

    public static final String EXTRA_NUMBER = "com.exemple.application.example.EXTRA_NUMBER";

    Button _btnEnr, _btnConn;
    EditText txttel, passe, nom, prenom;
    int taille = 0;
    String[] Password= new String[10];
    String[] Numero = new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription2);

        txttel = (EditText) findViewById(R.id.txttel);
        passe=(EditText) findViewById(R.id.txtpass);
        nom = (EditText) findViewById(R.id.txtnom);
        prenom = (EditText) findViewById(R.id.txtprenom);
        _btnEnr = (Button) findViewById(R.id.btnEnr) ;

        Button retour=(Button) findViewById(R.id.retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentw = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intentw);
                finish();

            }
        });


        txttel.addTextChangedListener(inscription);
        nom.addTextChangedListener(inscription);
        txttel.addTextChangedListener(inscription);
        passe.addTextChangedListener(inscription);

        _btnEnr.setEnabled(!nom.getText().toString().trim().isEmpty() && !prenom.getText().toString().trim().isEmpty() && !txttel.getText().toString().trim().isEmpty() && !passe.getText().toString().trim().isEmpty() );

        getJSON("http://192.168.43.190/android/membres.php");

    }

    //=====================================================================================================

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
            taille ++;
        }

    }

    public void OnLogin(View view){


        String telphone =  this.txttel.getText().toString();
        String passe = this.passe.getText().toString();
        String nom=this.nom.getText().toString();
        String prenom=this.prenom.getText().toString();
        int i ;
        int vrai =0;
        int verifactio=0;
        for (i = 0; i < taille; i++) {
            if (Numero[i].equals(telphone )){

                Toast.makeText(this,"Desolé, Ce numero est deja utilisé",Toast.LENGTH_SHORT).show();
                vrai =1;
                verifactio=1;
            }
        }
        if(verifactio==0){

            String type="reg";
            BackgroundTask backgroundTask= new BackgroundTask(getApplicationContext());
            backgroundTask.execute(type,nom,prenom,telphone,passe);

            mesagg();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }


        /*if (i == taille && vrai == 0){
            Toast.makeText(this, "Desolé mot de passe ou numero incorrect",Toast.LENGTH_SHORT).show();
        }*/
    }

    public void mesagg(){

        Toast.makeText(this,"Bienvenu Mr(Mme) "+nom.getText()+". Connectez vous a present",Toast.LENGTH_SHORT).show();

    }

    //=========================================================================================





    private TextWatcher inscription = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nome = nom.getText().toString().trim();
            String prenome = prenom.getText().toString().trim();
            String tele = txttel.getText().toString().trim();
            String passee = passe.getText().toString().trim();

            _btnEnr.setEnabled(!nome.isEmpty() && !prenome.isEmpty() && !tele.isEmpty() && !passee.isEmpty() );

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
