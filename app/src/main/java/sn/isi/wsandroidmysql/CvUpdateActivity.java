package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import tools.MyAdapterDomaine;
import tools.MyCvAdapter;

public class CvUpdateActivity extends AppCompatActivity {

    private ListView listdom;
    private ListView list;
    private ArrayList<Domaine> restdom = new ArrayList<>();
    private ArrayList<Cv> rest = new ArrayList<>();
    private ProgressDialog progressDialog;
    private EditText format;
    private EditText compet;
    private Spinner domai;
    private EditText passio;
    private Button ajout;
    private String usermail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_update);

        Intent it = getIntent();
        usermail = it.getStringExtra("usermail");
        format = (EditText) findViewById(R.id.formationtxtu);
        compet = (EditText) findViewById(R.id.compettxtu);
        domai = (Spinner) findViewById(R.id.domtxtu);
        passio = (EditText) findViewById(R.id.passiontxtu);

        ajout = (Button) findViewById(R.id.ModifCvBtn);
        progressDialog = new ProgressDialog(CvUpdateActivity.this);
        progressDialog.setMessage("Connexion en cours ...");

        String urldom = "http://10.0.2.2:8080/GestionOffres/ListDomaine";
        ClientHttpDom clientHttpdom = new ClientHttpDom();
        clientHttpdom.execute(urldom);

        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formation = format.getText().toString();
                String competences = compet.getText().toString();

                Domaine doma = (Domaine) domai.getSelectedItem();
                String passion = passio.getText().toString();


                String url = "http://10.0.2.2:8080/GestionOffres/Cv?action=update&usermail="+usermail+"&formation="+formation+"&competences="+competences+"&dom_id="+doma.getId()+"&passion="+passion;
                CvUpdateActivity.ClientHttp clientHttp = new CvUpdateActivity.ClientHttp();
                clientHttp.execute(url);
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
                Toast.makeText(CvUpdateActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(CvUpdateActivity.this, "Données correctes " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CvUpdateActivity.this, BaseHomeActivity.class);

                        startActivity(i);
                    } else {
                        Toast.makeText(CvUpdateActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected class ClientHttpDom extends AsyncTask<String, Void, String> {

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
                Toast.makeText(CvUpdateActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    ArrayAdapter<Domaine> domadpt = new ArrayAdapter<Domaine>(getApplicationContext(), android.R.layout.simple_list_item_1);

                    for(int i =0; i<=response.length(); i++){
                        JSONObject rep = response.getJSONObject(i);

                        String resultrep = rep.getString("response");
                        int id_dom = Integer.parseInt(rep.getString("id"));
                        String nom = rep.getString("nom");

                        Domaine domaine = new Domaine();
                        domaine.setId(id_dom);
                        domaine.setNom(nom);
                        if(resultrep.equals("OK")){

                            Toast.makeText(CvUpdateActivity.this, "Voici vos comptes " , Toast.LENGTH_LONG).show();
                            restdom.add(domaine);
                            domai.setAdapter(domadpt);
                            domadpt.add(domaine);
                            MyAdapterDomaine domaines = new MyAdapterDomaine(CvUpdateActivity.this, restdom);
                            listdom.setAdapter(domaines);

                        } else {
                            Toast.makeText(CvUpdateActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
