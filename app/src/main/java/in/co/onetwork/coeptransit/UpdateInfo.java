package in.co.onetwork.coeptransit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class UpdateInfo extends AppCompatActivity {
    FirebaseDatabase database= FirebaseDatabase.getInstance();;
    DatabaseReference myRef=database.getReference("user"); ;
    List<User> list;
    RecyclerView recycle;
    SharedPreferences sp;
    TextInputEditText ed1,ed2;
    MaterialBetterSpinner spinner; 
    String email,address,vown,coll,location;
    String b[]={"2 Wheeler","4 Wheeler","No vehicle"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        getSupportActionBar().setTitle("Update Information");
        spinner=(MaterialBetterSpinner)findViewById(R.id.vownd); 
        ed1=(TextInputEditText)findViewById(R.id.remail);
        ed2=(TextInputEditText)findViewById(R.id.addressr); 
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        coll=sp.getString("log",null);
        ArrayAdapter<String> array1=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,b);
        spinner.setAdapter(array1);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        list=new ArrayList<User>();
        myRef.child(coll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u=dataSnapshot.getValue(User.class);
                User u1=new User();
                //Toast.makeText(UpdateInfo.this, " "+u.getName(), Toast.LENGTH_SHORT).show();
                u1.setCollid(u.getCollid());
                u1.setName(u.getName());
                u1.setEmail(u.getEmail());
                u1.setVowned(u.getVowned());
                u1.setAddress(u.getAddress());
                list.add(u1);
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list, UpdateInfo.this);
                RecyclerView.LayoutManager recyce = new LinearLayoutManager(UpdateInfo.this);
                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator(new DefaultItemAnimator());
                recycle.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateInfo.this, "Error in data retrieval!"+databaseError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void refresh(){
        if(list.isEmpty())
            Toast.makeText(this, "No element found", Toast.LENGTH_SHORT).show();
        else {
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list, UpdateInfo.this);
            RecyclerView.LayoutManager recyce = new LinearLayoutManager(UpdateInfo.this);
            recycle.setLayoutManager(recyce);
            recycle.setItemAnimator(new DefaultItemAnimator());
            recycle.setAdapter(recyclerAdapter);
        }
    }
    public void update(View v){
        email=ed1.getText().toString();
        address=ed2.getText().toString();
        vown=spinner.getText().toString();
        if (!email.equals(""))
            myRef.child(coll).child("email").setValue(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isComplete()) {
                        list.clear();
                        ed1.setText("");
                        Toast.makeText(UpdateInfo.this, "Email updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(UpdateInfo.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        if (!address.equals(""))
            myRef.child(coll).child("address").setValue(address).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isComplete()) {
                        list.clear();
                        ed2.setText("");
                        Toast.makeText(UpdateInfo.this, "Address updated!", Toast.LENGTH_SHORT).show();
                        DataLongOperationAsynchTask d=new DataLongOperationAsynchTask();
                        d.execute(address.trim());
                        myRef.child(coll).child("location").setValue(location);
                        finish();
                    }
                    else
                        Toast.makeText(UpdateInfo.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        if (!vown.equals(""))
            myRef.child(coll).child("vowned").setValue(vown).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isComplete()) {
                        list.clear();
                        myRef.child(coll).child("requests").setValue(null);
                        myRef.child(coll).child("requested").setValue(null);
                        myRef.child(coll).child("friends").setValue(null);
                        Toast.makeText(UpdateInfo.this, "Mode of Transport updated!", Toast.LENGTH_SHORT).show();
                        spinner.setText("");
                        finish();
                    }
                    else
                        Toast.makeText(UpdateInfo.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(UpdateInfo.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            String add=params[0];
            try {
                add= URLEncoder.encode(add,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address="+add+"&sensor=false");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
                location=lat+","+lng;
                Toast.makeText(UpdateInfo.this, "location:"+location, Toast.LENGTH_SHORT).show();
                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater om=getMenuInflater();
        om.inflate(R.menu.main2,menu);
        MenuItem im=menu.findItem(R.id.item0);
        im.setTitle(sp.getString("log",null));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Powered by-");
                LayoutInflater factory = LayoutInflater.from(UpdateInfo.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(UpdateInfo.this,"Thanks",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(UpdateInfo.this, "Logged Out", Toast.LENGTH_SHORT).show();

                                sp.edit().clear().commit();
                                Intent i=new Intent(UpdateInfo.this,Login.class);
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
            case R.id.settings:
                startActivity(new Intent(UpdateInfo.this,Setting.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
