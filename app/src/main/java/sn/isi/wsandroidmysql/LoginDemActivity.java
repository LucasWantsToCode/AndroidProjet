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

public class LoginDemActivity extends AppCompatActivity {

    private EditText emailt;
    private EditText passwordt;

    private String usermail;

    private Button connect;
    private Button reg_nav;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dem);

        emailt = (EditText) findViewById(R.id.emaillogtxt);
        passwordt = (EditText) findViewById(R.id.passwordlogtxt);

        connect = (Button) findViewById(R.id.ConBtn);
        reg_nav = (Button) findViewById(R.id.reg_n);
        progressDialog = new ProgressDialog(LoginDemActivity.this);
        progressDialog.setMessage("Connexion en cours ...");

        reg_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginDemActivity.this, RegisterDemActivity.class);

                startActivity(i);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailt.getText().toString();
                usermail = email;
                String password = passwordt.getText().toString();
                String url = "http://10.0.2.2:8080/GestionOffres/DemandeurLoginServlet?email="+email+"&password="+password;
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
                Toast.makeText(LoginDemActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    if(resultrep.equals("OK")){
                        Toast.makeText(LoginDemActivity.this, "Données correctes " , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginDemActivity.this, BaseHomeActivity.class);
                        i.putExtra("usermail", usermail);

                        startActivity(i);
                    } else {
                        Toast.makeText(LoginDemActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
