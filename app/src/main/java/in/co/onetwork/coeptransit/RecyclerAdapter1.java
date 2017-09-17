package in.co.onetwork.coeptransit;

/**
 * Created by abhi on 16/9/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.MyHoder>{
    List<User> list;
    Context context;
    String collid;
    FirebaseDatabase database= FirebaseDatabase.getInstance();;
    DatabaseReference myRef=database.getReference("user");

    public RecyclerAdapter1(List<User> list, Context context,String user) {
        this.list = list;
        this.context = context;
        collid=user;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card2,parent,false);
        MyHoder myHoder = new MyHoder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        final User mylist = list.get(position);
        holder.collid.setText(mylist.getCollid());
        holder.name.setText(mylist.getName());
        holder.email.setText(mylist.getEmail());
        holder.mot.setText(mylist.getVowned());
        holder.add.setText(mylist.getAddress());
        holder.req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(mylist.getCollid()).child("requests").push().setValue(collid).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){
                            myRef.child(collid).child("requested").push().setValue(mylist.getCollid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "Request has been made.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception e){



        }

        return arr;

    }

    class MyHoder extends RecyclerView.ViewHolder {
        TextView collid,name,email,mot,add;
        FloatingActionButton req;


        public MyHoder(View itemView) {
            super(itemView);
            collid = (TextView) itemView.findViewById(R.id.fid);
            name= (TextView) itemView.findViewById(R.id.fname);
            email= (TextView) itemView.findViewById(R.id.femail);
            mot=(TextView)itemView.findViewById(R.id.fmode);
            add=(TextView)itemView.findViewById(R.id.fadd);
            req=(FloatingActionButton) itemView.findViewById(R.id.req);
        }
    }

}