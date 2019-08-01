package com.mks.mwambu.mymap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,LocationListener {
    public static final String EXTRA_NUMBER = "com.exemple.application.example.EXTRA_NUMBER";
    public static final String EXTRA_PRE = "com.exemple.application.example.EXTRA_PRE";
    public static final String EXTRA_NOM = "com.exemple.application.example.EXTRA_NOM";

    private static  LatLng MYPOSITION = new LatLng(-10.716667, 25.466667);

    private int taille =0;
    private LocationManager lm;
    String[] Nom=new String[20];
    String[] Long=new String[20];
    String[] Lat=new String[20];
    String[] Num=new String[20];
    String text;
    private static final int PERMS_CALL_ID = 1234;
    private GoogleMap mMap;
    int t = 0;
    String idnom;
    String idprenom;

    String json;
    int count;
    int var=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Button retour=(Button)findViewById(R.id.retour);

        Intent intent = getIntent();
        text = intent.getStringExtra(Acceuil.EXTRA_NUMBER);
        idnom = intent.getStringExtra(Acceuil.EXTRA_NOM);
        idprenom = intent.getStringExtra(Acceuil.EXTRA_PRE);
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


        //getJSON("http://192.168.43.8/android/maps.php");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        content();

        //=====================================================================================

    }
    //=========================ACTUALISATION DES DONNEES==========================
    public void content(){
        count++;

        ///INNITIliaRTION

        taille = 0;
        json=null;


        refreshee(1000);
    }
    private void refreshee(int seconde){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getJSON("http://192.168.43.190/android/maps.php");
                content();

            }
        };
        handler.postDelayed(runnable,seconde);
    }

    //============================================================================

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
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            this.Nom[i] = obj.getString( "prenom");
            this.Long[i] = obj.getString( "longitude");
            this.Lat[i] = obj.getString( "latitude");
            this.Num[i] = obj.getString( "tel");
            taille++;
        }

    }


    //FIN IMPORTATION//

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {


        mMap = map;
        mMap.clear();
        // Add some markers to the map, and add a data object to each marker.
        if (Nom[0] != null){
            for (int i = 0;i<taille; i++) {
                double latitude =Double.parseDouble(this.Lat[i]);
                double longitude =Double.parseDouble(this.Long[i]);
                String nom = this.Nom[i];
                MYPOSITION = new LatLng(latitude , longitude);
                Toast.makeText(this, nom+" / "+latitude+ " / "+longitude,Toast.LENGTH_SHORT).show();
                if (Num[i].equals(text)){
                    mMap.addMarker(new MarkerOptions()
                            .position(MYPOSITION)
                            .title(nom)
                            .snippet("Un Utilisateur")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                else {

                    mMap.addMarker(new MarkerOptions()
                            .position(MYPOSITION)
                            .title(nom)
                            .snippet("Un Utilisateur")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }
        }

        mMap.setOnMarkerClickListener(this);
    }


    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();

    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },PERMS_CALL_ID);

            return;
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        if(lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER )){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
        }
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL_ID){
            checkPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(lm != null) {
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {



        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        MYPOSITION = new LatLng(latitude , longitude);
        onMapReady(mMap);

        Intent intent = getIntent();
        String numero = intent.getStringExtra(Acceuil.EXTRA_NUMBER);

        String lat=String.valueOf(latitude);
        String longi =String.valueOf(longitude);

        String type="update_action";
        refrech refrech = new refrech(this);
        refrech.execute(type,longi,lat,numero);



    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
