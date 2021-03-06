package in.co.onetwork.coeptransit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class Nearby extends AppCompatActivity {
    FirebaseDatabase database= FirebaseDatabase.getInstance();;
    DatabaseReference myRef=database.getReference("user");
    List<User> list;
    RecyclerView recycle;
    SharedPreferences sp;
    String coll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        getSupportActionBar().setTitle("Nearby");
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        coll=sp.getString("log",null);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        list=new ArrayList<User>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    User u=dataSnapshot1.getValue(User.class);
                    String v=u.getVowned();
                    long f=dataSnapshot.child("friends").getChildrenCount();
                    if(!v.equalsIgnoreCase("No vehicle")){
                        if (v.equalsIgnoreCase("2 wheeler")&&f==0){
                            //Toast.makeText(Nearby.this, "friends"+f, Toast.LENGTH_SHORT).show();
                            User u1=new User();
                            u1.setCollid(u.getCollid());
                            u1.setName(u.getName());
                            u1.setEmail(u.getEmail());
                            u1.setVowned(u.getVowned());
                            u1.setAddress(u.getAddress());
                            list.add(u1);
                        }
                        if (v.equalsIgnoreCase("4 Wheelers")&&f<3){
                            User u1=new User();
                            u1.setCollid(u.getCollid());
                            u1.setName(u.getName());
                            u1.setEmail(u.getEmail());
                            u1.setVowned(u.getVowned());
                            u1.setAddress(u.getAddress());
                            list.add(u1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Nearby.this, "Error in data retrieval!"+databaseError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void refresh(View v){

        if(list.isEmpty()) {
            Toast.makeText(this, "No element found", Toast.LENGTH_SHORT).show();
        }
        else {
            RecyclerAdapter1 recyclerAdapter = new RecyclerAdapter1(list, Nearby.this,coll);
            RecyclerView.LayoutManager recyce = new LinearLayoutManager(Nearby.this);
            recycle.setLayoutManager(recyce);
            recycle.setItemAnimator(new DefaultItemAnimator());
            recycle.setAdapter(recyclerAdapter);
        }
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
                LayoutInflater factory = LayoutInflater.from(Nearby.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Nearby.this,"Thanks",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Nearby.this, "Logged Out", Toast.LENGTH_SHORT).show();

                                sp.edit().clear().commit();
                                Intent i=new Intent(Nearby.this,Login.class);
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

