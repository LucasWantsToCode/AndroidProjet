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

public class LoginEntrepriseActivity extends AppCompatActivity {

    private EditText emailt;
    private EditText passwordt;
    private String entmail;

    private Button connect;
    private Button reg_nav;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_entreprise);

        emailt = (EditText) findViewById(R.id.emaillogtxte);
        passwordt = (EditText) findViewById(R.id.passwordlogtxte);

        connect = (Button) findViewById(R.id.ConBtne);
        reg_nav = (Button) findViewById(R.id.reg_ne);
        progressDialog = new ProgressDialog(LoginEntrepriseActivity.this);
        progressDialog.setMessage("Connexion en cours ...");

        reg_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginEntrepriseActivity.this, RegisterEntActivity.class);

                startActivity(i);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailt.getText().toString();
                String password = passwordt.getText().toString();
                entmail = email;
                String url = "http://10.0.2.2:8080/GestionOffres/EntrepriseLogin?email="+email+"&password="+password;
                LoginEntrepriseActivity.ClientHttp clientHttp = new LoginEntrepriseActivity.ClientHttp();
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
                Toast.makeText(LoginEntrepriseActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(LoginEntrepriseActivity.this, "Données correctes " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginEntrepriseActivity.this, ListCvBActivity.class);
                        i.putExtra("entmail", entmail);

                        startActivity(i);
                    } else {
                        Toast.makeText(LoginEntrepriseActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
