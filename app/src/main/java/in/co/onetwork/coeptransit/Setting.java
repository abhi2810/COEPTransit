package in.co.onetwork.coeptransit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
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

public class Setting extends AppCompatActivity {
    SharedPreferences sp;
    String coll,vm;
    long f;
    FirebaseDatabase database= FirebaseDatabase.getInstance();;
    DatabaseReference myRef=database.getReference("user"); ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Settings");
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        coll=sp.getString("log",null);
        myRef.child(coll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vm=dataSnapshot.child("vowned").getValue(String.class);
                //Toast.makeText(Setting.this, "vm"+vm, Toast.LENGTH_SHORT).show();
                f=dataSnapshot.child("friends").getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void nearby(View v){
        myRef.child(coll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vm=dataSnapshot.child("vowned").getValue(String.class);
                //Toast.makeText(Setting.this, "vm"+vm, Toast.LENGTH_SHORT).show();
                f=dataSnapshot.child("friends").getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(vm.equalsIgnoreCase("2 wheeler")||vm.equalsIgnoreCase("4 wheeler")){
            Toast.makeText(this, "you can't request for carpool since you already have a vehicle.", Toast.LENGTH_SHORT).show();
        }else {
            if(f==1){
                Toast.makeText(this, "You already have a ride.", Toast.LENGTH_SHORT).show();
            }else{
            startActivity(new Intent(this, Nearby.class));
        }}
    }
    public void pending(View v){
        myRef.child(coll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vm=dataSnapshot.child("vowned").getValue(String.class);
                //Toast.makeText(Setting.this, "vm"+vm, Toast.LENGTH_SHORT).show();
                f=dataSnapshot.child("friends").getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(vm.equalsIgnoreCase("no vehicle")){
            Toast.makeText(this, "you can't see requests since you don't have a Vehicle.", Toast.LENGTH_SHORT).show();
        }else{
            if((vm.equalsIgnoreCase("2 wheeler")&&f==0)||(vm.equalsIgnoreCase("4 wheeler")&f<3)) {
                startActivity(new Intent(this, Pending.class));
            }else
                Toast.makeText(this, "You have no place to give lift.", Toast.LENGTH_SHORT).show();
        }
    }
    public void friends(View v){
        startActivity(new Intent(this,friends.class));
    }
    public void update(View v){
        startActivity(new Intent(this,UpdateInfo.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater om=getMenuInflater();
        om.inflate(R.menu.main2,menu);
        MenuItem im=menu.findItem(R.id.item0);
        im.setTitle(sp.getString("log",null));
        MenuItem im1=menu.findItem(R.id.settings);
        im1.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Powered by-");
                LayoutInflater factory = LayoutInflater.from(Setting.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Setting.this,"Thanks",Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.setView(view);
                dialog.show();
                break;
            case R.id.action_settings:
                AlertDialog.Builder dial = new AlertDialog.Builder(this);
                dial.setTitle("Do You Want to LogOut?");
                dial.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Setting.this, "Logged Out", Toast.LENGTH_SHORT).show();

                                sp.edit().clear().commit();
                                Intent i=new Intent(Setting.this,Login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                            }
                        });
                dial.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDial = dial.create();
                alertDial.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
