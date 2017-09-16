package in.co.onetwork.coeptransit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ekdummain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekdummain);
        getSupportActionBar().hide();


        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);

                }
                catch(InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent openMain = new Intent(ekdummain.this, Login.class);
                    startActivity(openMain);
                    finish();
                }
            }
        };
        timer.start();
    }
}
