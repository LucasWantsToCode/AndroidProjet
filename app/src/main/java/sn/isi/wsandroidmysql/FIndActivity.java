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

public class FIndActivity extends AppCompatActivity {

    private EditText numero;
    private EditText soldetxt;
    private Button search;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        numero = (EditText) findViewById(R.id.numerotxt);
        soldetxt = (EditText) findViewById(R.id.soldetext);
        search = (Button) findViewById(R.id.Search_btn);
        progressDialog = new ProgressDialog(FIndActivity.this);
        progressDialog.setMessage("Connexion en cours ...");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = numero.getText().toString();
                String url = "http://10.0.2.2:8080/androidwebapp/ApiFind?numero="+num;
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
                Toast.makeText(FIndActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    JSONObject rep = response.getJSONObject(0);
                    String resultrep = rep.getString("response");
                    String resultcompte =  rep.getString("compte");
                    if(resultrep.equals("OK")){

                        soldetxt.setText(resultcompte);
                        Toast.makeText(FIndActivity.this, "Compte trouvé ", Toast.LENGTH_LONG).show();
                        //Intent i = new Intent(FIndActivity.this, MainActivity.class);

                        //startActivity(i);
                    } else {
                        Toast.makeText(FIndActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
