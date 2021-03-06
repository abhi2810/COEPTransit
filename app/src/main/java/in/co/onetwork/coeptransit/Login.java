package in.co.onetwork.coeptransit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {


    DatabaseReference root= FirebaseDatabase.getInstance().getReference();
    DatabaseReference user=root.child("user").getRef();
    TextInputEditText ed1,ed2;
    String use,pass;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        ed1=(TextInputEditText)findViewById(R.id.userid);
        ed2=(TextInputEditText)findViewById(R.id.password);
        sp=getSharedPreferences("login",Context.MODE_PRIVATE);
    }
    public void login(View v){
        final ProgressDialog pr=new ProgressDialog(this);
        pr.setTitle("Logging in");
        pr.setMessage("Please wait!");
        pr.show();
        use=ed1.getText().toString();
        pass=ed2.getText().toString();
        if(use.equals("")||pass.equals("")){
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            pr.dismiss();
        }else{
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(use)){
                        user.child(use).child("pass").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String p=dataSnapshot.getValue(String.class);
                                if(pass.equals(p)){
                                    pr.dismiss();
                                    Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    sp.edit().putString("log",use).commit();
                                    startActivity(new Intent(Login.this,Homescreen.class));
                                }else{
                                    Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    pr.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Login.this, "Error! please check internet.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(Login.this, "user doesn't exist", Toast.LENGTH_SHORT).show();
                        pr.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Login.this, "Error! please check internet."+databaseError.toString(), Toast.LENGTH_SHORT).show();
                    pr.dismiss();
                }
            });
        }
    }
    public void signup(View v){
        startActivity(new Intent(this,Signup.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater om=getMenuInflater();
        om.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Powered by-");
                LayoutInflater factory = LayoutInflater.from(Login.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Login.this,"Thanks",Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.setView(view);
                dialog.show();
                break;
            case R.id.item2:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
