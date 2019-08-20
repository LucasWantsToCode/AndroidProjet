package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterEntActivity extends AppCompatActivity {

    private EditText nomt;
    private EditText emailt;
    private EditText passwordt;
    private String entmail;

    private Button inscrire;
    private Button conn_nav;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ent);

        nomt = (EditText) findViewById(R.id.nomtxte);
        emailt = (EditText) findViewById(R.id.emailtxte);
        passwordt = (EditText) findViewById(R.id.passwordtxte);

        inscrire = (Button) findViewById(R.id.InscrireBtne);
        conn_nav = (Button) findViewById(R.id.connect_ne);

        progressDialog = new ProgressDialog(RegisterEntActivity.this);
        progressDialog.setMessage("Connexion en cours ...");



        conn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterEntActivity.this, LoginEntrepriseActivity.class);

                startActivity(i);
            }
        });

        inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomt.getText().toString();
                String email = emailt.getText().toString();
                entmail = email;
                String password = passwordt.getText().toString();
                String url = "http://10.0.2.2:8080/GestionOffres/Entreprise?nom="+nom+"&email="+email+"&password="+password;
                RegisterEntActivity.ClientHttp clientHttp = new RegisterEntActivity.ClientHttp();
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
                Toast.makeText(RegisterEntActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(RegisterEntActivity.this, "Compte ajouté " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegisterEntActivity.this, ListCvBActivity.class);
                        i.putExtra("entmail", entmail);

                        startActivity(i);
                    } else {
                        Toast.makeText(RegisterEntActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
