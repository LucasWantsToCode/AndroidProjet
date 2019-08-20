package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import sn.isi.wsandroidmysql.entities.Cv;
import sn.isi.wsandroidmysql.entities.Demandeur;
import sn.isi.wsandroidmysql.entities.Domaine;
import sn.isi.wsandroidmysql.entities.Entreprise;
import sn.isi.wsandroidmysql.entities.Offre;
import tools.MyCvAdapter;
import tools.MyOffreAdapter;

public class BaseHomeActivity extends AppCompatActivity {

    private  String usermail;
    private  String entmail;
    private ListView list;
    private ArrayList<Offre> rest = new ArrayList<>();
    private ProgressDialog progressDialog;
    private EditText comm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent it = getIntent();
        usermail = it.getStringExtra("usermail");
        list = (ListView) findViewById(R.id.listOffre);

        progressDialog = new ProgressDialog(BaseHomeActivity.this);
        progressDialog.setMessage("Connexion en cours ...");
        String url = "http://10.0.2.2:8080/GestionOffres/ListeOffres?action=listoffredem&&usermail="+usermail;
        ClientHttp clientHttp = new ClientHttp();
        clientHttp.execute(url);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int ioffre = rest.get(position).getId();
                AlertDialog.Builder dialog = new AlertDialog.Builder(BaseHomeActivity.this);

                dialog.setTitle("Ajouter aux favoris - Commenter");
                dialog.setMessage("Choisir une action");
                final View addView = getLayoutInflater().inflate(R.layout.commentaire_layout, null);


                comm = (EditText) addView.findViewById(R.id.libelletext);

                dialog.setView(addView);


                dialog.setPositiveButton("Commentaire ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = comm.getText().toString();
                        String urlcom = "http://10.0.2.2:8080/GestionOffres/Commentaire?usermail="+usermail+"&off_id="+ioffre+"&libelle="+comment;
                        ClientHttpCom clientHttpcom = new ClientHttpCom();
                        clientHttpcom.execute(urlcom);
                    }
                });

                dialog.setNegativeButton("Favoris ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String urlfav = "http://10.0.2.2:8080/GestionOffres/Favorite?action=store&usermail="+usermail+"&off_id="+ioffre;
                        ClientHttpFav clientHttpfav = new ClientHttpFav();
                        clientHttpfav.execute(urlfav);
                    }
                });

                dialog.setNeutralButton("Annuler ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();
            }
        });

    }

    protected class ClientHttpCom extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                ResponseHandler<String> tunnel = new BasicResponseHandler();
                String result = client.execute(get, tunnel);

                return result;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(result == null){
                Toast.makeText(BaseHomeActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(BaseHomeActivity.this, "Données correctes " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(BaseHomeActivity.this, BaseHomeActivity.class);
                        i.putExtra("usermail", usermail);

                        startActivity(i);
                    } else {
                        Toast.makeText(BaseHomeActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected class ClientHttpFav extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                ResponseHandler<String> tunnel = new BasicResponseHandler();
                String result = client.execute(get, tunnel);

                return result;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(result == null){
                Toast.makeText(BaseHomeActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(BaseHomeActivity.this, "Données correctes " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(BaseHomeActivity.this, BaseHomeActivity.class);
                        i.putExtra("usermail", usermail);

                        startActivity(i);
                    } else {
                        Toast.makeText(BaseHomeActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
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
            Intent i = new Intent(BaseHomeActivity.this, CvUpdateActivity.class);
            i.putExtra("usermail", usermail);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_favorites) {
            Intent i = new Intent(BaseHomeActivity.this, ListFavActivity.class);
            i.putExtra("usermail", usermail);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_searchOffre) {
            Intent i = new Intent(BaseHomeActivity.this, SearchActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class ClientHttp extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                ResponseHandler<String> tunnel = new BasicResponseHandler();
                String result = client.execute(get, tunnel);

                return result;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(result == null){
                Toast.makeText(BaseHomeActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i = 0; i<=response.length(); i++){
                        JSONObject rep = response.getJSONObject(i);

                        String resultrep = rep.getString("response");
                        int id = Integer.parseInt(rep.getString("id"));
                        String dateO = rep.getString("dateO");
                        String libelle = rep.getString("libelle");
                        int entreprise = Integer.parseInt(rep.getString("entreprise"));
                        int domaine = Integer.parseInt(rep.getString("domaine"));


                        Offre offre = new Offre();
                        offre.setId(id);
                        offre.setDateO(dateO);
                        offre.setLibelle(libelle);
                        Entreprise et = new Entreprise();
                        et.setId(entreprise);
                        offre.setEntreprise(et);
                        Domaine dom = new Domaine();
                        dom.setId(domaine);
                        offre.setDomaine(dom);

                        if(resultrep.equals("OK")){

                            Toast.makeText(BaseHomeActivity.this, "Voici vos comptes " , Toast.LENGTH_LONG).show();
                            rest.add(offre);

                            MyOffreAdapter cva = new MyOffreAdapter(BaseHomeActivity.this, rest);

                            list.setAdapter(cva);

                        } else {
                            Toast.makeText(BaseHomeActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
