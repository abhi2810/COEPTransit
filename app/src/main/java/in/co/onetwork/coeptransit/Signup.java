package in.co.onetwork.coeptransit;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Signup extends AppCompatActivity {
    TextInputEditText ed1,ed2,ed3,ed4,ed5;
    MaterialBetterSpinner sp1,sp2;
    String collid,name,email,pass,cpass,year,vo;
    String a[]={"FE","SE","TE","BE"};
    String b[]={"2 Wheeler","4 Wheeler","No vehicle"};
    DatabaseReference root= FirebaseDatabase.getInstance().getReference();
    DatabaseReference user=root.child("user").getRef();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign Up");
        ArrayAdapter<String> array=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,a);
        ArrayAdapter<String> array1=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,b);
        ed1=(TextInputEditText)findViewById(R.id.collid);
        ed2=(TextInputEditText)findViewById(R.id.name);
        ed3=(TextInputEditText)findViewById(R.id.email);
        ed4=(TextInputEditText)findViewById(R.id.pass);
        ed5=(TextInputEditText)findViewById(R.id.cpass);
        sp1=(MaterialBetterSpinner)findViewById(R.id.year);
        sp2=(MaterialBetterSpinner)findViewById(R.id.vown);
        sp1.setAdapter(array);
        sp2.setAdapter(array1);
    }
    public void signup(View v){
        final ProgressDialog p=new ProgressDialog(this);
        p.setTitle("Signing Up");
        p.setMessage("Loading");
        p.show();
        collid=ed1.getText().toString();
        name=ed2.getText().toString();
        email=ed3.getText().toString();
        pass=ed4.getText().toString();
        cpass=ed5.getText().toString();
        year=sp1.getText().toString();
        vo=sp2.getText().toString();
        if(collid.equals("")||name.equals("")||email.equals("")||pass.equals("")||cpass.equals("")||year.equals("")||year.equals("")||vo.equals("")) {
            Toast.makeText(this, "Field/s is/are empty.", Toast.LENGTH_SHORT).show();
            p.dismiss();
        }else {
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(collid)) {
                        Toast.makeText(Signup.this, "Username already taken.", Toast.LENGTH_SHORT).show();
                        p.dismiss();
                    }else{
                        if (pass.equals(cpass) && pass.length() >= 8) {
                            User u = new User(collid,name,pass,year,email,vo);
                            user.child(collid).setValue(u).addOnCompleteListener(Signup.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        Toast.makeText(Signup.this, "Signed up!", Toast.LENGTH_SHORT).show();
                                        p.dismiss();
                                        finish();
                                    } else {
                                        Toast.makeText(Signup.this, "Error" + task.toString(), Toast.LENGTH_SHORT).show();
                                        p.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Signup.this, "Passwords don't match or is less than 8 characters", Toast.LENGTH_LONG).show();
                            p.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Signup.this, "Error! please check internet."+databaseError.toString(), Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            });
        }
    }
}
