package sn.isi.wsandroidmysql;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import sn.isi.wsandroidmysql.entities.Cv;
import sn.isi.wsandroidmysql.entities.Demandeur;
import sn.isi.wsandroidmysql.entities.Domaine;
import tools.MyCvAdapter;

public class ListCvBActivity extends AppCompatActivity {

    private  String entmail;
    private ListView list;
    private ArrayList<Cv> rest = new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cv_b);
        list = (ListView) findViewById(R.id.listCv);
        progressDialog = new ProgressDialog(ListCvBActivity.this);
        progressDialog.setMessage("Connexion en cours ...");
        String url = "http://10.0.2.2:8080/GestionOffres/ListCv";
        ClientHttp clientHttp = new ClientHttp();
        clientHttp.execute(url);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent it = getIntent();
        entmail = it.getStringExtra("entmail");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int idcv = rest.get(position).getId();
                final String formationcv= rest.get(position).getFormation();
                final String competencecv= rest.get(position).getCompetences();
                final int idDem = rest.get(position).getDemandeur().getId();
                final int idDom = rest.get(position).getDomaine().getId();
                final String passioncv = rest.get(position).getPassion();
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListCvBActivity.this);

                dialog.setTitle("Download Cv");
                dialog.setMessage("Choisir une action");
                final View addView = getLayoutInflater().inflate(R.layout.listitem_cv, null);


                dialog.setView(addView);


                dialog.setPositiveButton("Telecharger ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        createPdf(idcv, formationcv.toUpperCase(), competencecv.toUpperCase(), idDem, idDom, passioncv.toUpperCase());

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

    private void createPdf(int someint1, String sometext, String sometext2, int someint2, int someint3, String sometext3){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);

        canvas.drawText("ID Cv:", 05, 90, paint);
        canvas.drawText(String.valueOf(someint1), 90, 90, paint);
        canvas.drawText("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++", 05, 100, paint);
        canvas.drawText("Formation:", 05, 120, paint);
        canvas.drawText(sometext, 90, 120, paint);
        canvas.drawText("Competences:", 05, 140, paint);
        canvas.drawText(sometext2, 90, 140, paint);
        canvas.drawText("ID Demandeur:", 05, 160, paint);
        canvas.drawText(String.valueOf(someint2), 90, 160, paint);
        canvas.drawText("ID Domaine:", 05, 180, paint);
        canvas.drawText(String.valueOf(someint3), 90, 180, paint);
        canvas.drawText("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++", 05, 190, paint);
        canvas.drawText("Passion:", 05, 200, paint);
        canvas.drawText(sometext3, 90, 200, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Download/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"document-"+someint1+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        //document.close();
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
                Toast.makeText(ListCvBActivity.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i =0; i<=response.length(); i++){
                        JSONObject rep = response.getJSONObject(i);

                        String resultrep = rep.getString("response");
                        int id = Integer.parseInt(rep.getString("id"));
                        String formation = rep.getString("formation");
                        String competence = rep.getString("competences");
                        int demandeur = Integer.parseInt(rep.getString("demandeur"));
                        int domaine = Integer.parseInt(rep.getString("domaine"));
                        String passion = rep.getString("passion");

                        Cv cv = new Cv();
                        cv.setId(id);
                        cv.setFormation(formation);
                        cv.setCompetences(competence);
                        Demandeur de = new Demandeur();
                        de.setId(demandeur);
                        cv.setDemandeur(de);
                        Domaine dom = new Domaine();
                        dom.setId(domaine);
                        cv.setDomaine(dom);
                        cv.setPassion(passion);

                        if(resultrep.equals("OK")){

                            Toast.makeText(ListCvBActivity.this, "Voici vos comptes " , Toast.LENGTH_LONG).show();
                            rest.add(cv);
                            MyCvAdapter cva = new MyCvAdapter(ListCvBActivity.this, rest);

                            list.setAdapter(cva);

                        } else {
                            Toast.makeText(ListCvBActivity.this, "Parametres de connexion non valides", Toast.LENGTH_LONG).show();
                        }
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
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addoffer) {
            Intent i = new Intent(ListCvBActivity.this, AddOfferActivity.class);
            i.putExtra("entmail", entmail);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
