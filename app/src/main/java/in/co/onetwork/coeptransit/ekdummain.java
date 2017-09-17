package in.co.onetwork.coeptransit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ekdummain extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekdummain);
        getSupportActionBar().hide();
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);


        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);

                }
                catch(InterruptedException e){
                    e.printStackTrace();
                } finally {
                    if(sp.getString("log",null)==null) {
                        Intent i = new Intent(ekdummain.this, Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(ekdummain.this, Homescreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }
                }
            }
        };
        timer.start();
    }
}
