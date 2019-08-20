package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
import java.util.List;

import sn.isi.wsandroidmysql.entities.Compte;
import tools.MyCompteAdapter;

public class ListCompteActivity extends AppCompatActivity {

    private ListView list;
    private ArrayList<Compte> rest = new ArrayList<>();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_compte);
        list = (ListView) findViewById(R.id.listCompte);
        progressDialog = new ProgressDialog(ListCompteActivity.this);
        progressDialog.setMessage("Connexion en cours ...");
        String url = "http://10.0.2.2:8080/androidwebapp/ApiList";
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
                Toast.makeText(ListCompteActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i =0; i<=response.length(); i++){
                        JSONObject rep = response.getJSONObject(i);

                        String resultrep = rep.getString("res");
                        String numero = rep.getString("numero");
                        String nom = rep.getString("nom");
                        String prenom = rep.getString("prenom");
                        String tel = rep.getString("tel");
                        Double solde = Double.parseDouble(rep.getString("solde"));

                        Compte compte = new Compte();
                        compte.setNumero(numero);
                        compte.setNom(nom);
                        compte.setPrenom(prenom);
                        compte.setTel(tel);
                        compte.setSolde(solde);
                        if(resultrep.equals("OK")){

                            Toast.makeText(ListCompteActivity.this, "Voici vos comptes " , Toast.LENGTH_LONG).show();
                            rest.add(compte);
                            MyCompteAdapter comptes = new MyCompteAdapter(ListCompteActivity.this, rest);

                            list.setAdapter(comptes);

                        } else {
                            Toast.makeText(ListCompteActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
