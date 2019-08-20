package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import sn.isi.wsandroidmysql.entities.Compte;
import sn.isi.wsandroidmysql.entities.Demandeur;
import sn.isi.wsandroidmysql.entities.Favorite;
import sn.isi.wsandroidmysql.entities.Offre;
import tools.MyCompteAdapter;
import tools.MyFavoriteAdapter;

public class ListFavActivity extends AppCompatActivity {

    private ListView list;
    private ArrayList<Favorite> rest = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String usermail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fav);
        list = (ListView) findViewById(R.id.listFav);
        Intent i = getIntent();
        usermail = i.getStringExtra("usermail");
        progressDialog = new ProgressDialog(ListFavActivity.this);
        progressDialog.setMessage("Connexion en cours ...");
        String url = "http://10.0.2.2:8080/GestionOffres/ListFavorite?usermail="+usermail;
        ClientHttp clientHttp = new ClientHttp();
        clientHttp.execute(url);

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
                Toast.makeText(ListFavActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i =0; i<=response.length(); i++){
                        JSONObject rep = response.getJSONObject(i);

                        String resultrep = rep.getString("res");
                        int id = Integer.parseInt(rep.getString("id"));
                        int demandeur = Integer.parseInt(rep.getString("demandeur"));
                        int offre = Integer.parseInt(rep.getString("offre"));

                        Favorite favorite = new Favorite();
                        favorite.setId(id);
                        Demandeur dem = new Demandeur();
                        dem.setId(demandeur);
                        favorite.setDemandeur(dem);
                        Offre off = new Offre();
                        off.setId(offre);
                        favorite.setOffre(off);

                        if(resultrep.equals("OK")){

                            Toast.makeText(ListFavActivity.this, "Voici vos comptes " , Toast.LENGTH_LONG).show();
                            rest.add(favorite);
                            MyFavoriteAdapter favorites = new MyFavoriteAdapter(ListFavActivity.this, rest);

                            list.setAdapter(favorites);

                        } else {
                            Toast.makeText(ListFavActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
