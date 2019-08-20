package sn.isi.wsandroidmysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    private Button ListCompte;
    private Button AjoutCompte;
    private Button RechercheSolde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AjoutCompte = (Button) findViewById(R.id.GotoAddAccount_nav);
        RechercheSolde = (Button) findViewById(R.id.gotoAmount_nav);

        AjoutCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterDemActivity.class);

                startActivity(i);
            }
        });

        RechercheSolde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginEntrepriseActivity.class);

                startActivity(i);
            }
        });

    }


}
