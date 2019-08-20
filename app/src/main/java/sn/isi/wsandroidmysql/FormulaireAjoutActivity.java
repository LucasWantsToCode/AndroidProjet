package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class FormulaireAjoutActivity extends AppCompatActivity {

    private EditText numero;
    private EditText nomClient;
    private EditText prenomClient;
    private EditText telClient;
    private EditText solde;
    private Button connexion;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_ajout);
        numero = (EditText) findViewById(R.id.numerotxt);
        nomClient = (EditText) findViewById(R.id.nomclienttxt);
        prenomClient = (EditText) findViewById(R.id.prenomclienttxt);
        telClient = (EditText) findViewById(R.id.telclienttxt);
        solde = (EditText) findViewById(R.id.soldetxt);

        connexion = (Button) findViewById(R.id.Ajoutbtn);
        progressDialog = new ProgressDialog(FormulaireAjoutActivity.this);
        progressDialog.setMessage("Connexion en cours ...");

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = numero.getText().toString();
                String nom = nomClient.getText().toString();
                String prenom = prenomClient.getText().toString();
                String tel = telClient.getText().toString();
                Double sol = Double.parseDouble(solde.getText().toString());
                String url = "http://10.0.2.2:8080/androidwebapp/Api?numero="+num+"&nomClient="+nom+"&prenomClient="+prenom+"&telClient="+tel+"&solde="+sol;
                ClientHttp clientHttp = new ClientHttp();
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
                Toast.makeText(FormulaireAjoutActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(FormulaireAjoutActivity.this, "Compte ajouté " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(FormulaireAjoutActivity.this, MainActivity.class);

                        startActivity(i);
                    } else {
                        Toast.makeText(FormulaireAjoutActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
