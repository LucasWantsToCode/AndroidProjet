package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import sn.isi.wsandroidmysql.entities.Domaine;
import sn.isi.wsandroidmysql.entities.Entreprise;
import sn.isi.wsandroidmysql.entities.Offre;
import tools.MyOffreAdapter;

public class SearchActivity extends AppCompatActivity {

    private EditText mtcle;
    private Button search;
    private ListView list;
    private ArrayList<Offre> rest = new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mtcle = (EditText) findViewById(R.id.txtmotcle);
        search = (Button) findViewById(R.id.searchBtn);
        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("Connexion en cours ...");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String motcle = mtcle.getText().toString();
                String url = "http://10.0.2.2:8080/GestionOffres/ListeOffres?action=searchoffre&&motcle="+motcle;
                ClientHttp clientHttp = new ClientHttp();
                clientHttp.execute(url);

                list = (ListView) findViewById(R.id.listOffrerech);
            }
        });
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
                Toast.makeText(SearchActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i =0; i<=response.length(); i++){
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

                            Toast.makeText(SearchActivity.this, "Voici vos comptes " , Toast.LENGTH_LONG).show();
                            rest.add(offre);
                            MyOffreAdapter cva = new MyOffreAdapter(SearchActivity.this, rest);

                            list.setAdapter(cva);

                        } else {
                            Toast.makeText(SearchActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
