package in.co.onetwork.coeptransit;

/**
 * Created by abhi on 16/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHoder>{

    List<User> list;
    Context context;

    public RecyclerAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card,parent,false);
        MyHoder myHoder = new MyHoder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        User mylist = list.get(position);
        holder.collid.setText(mylist.getCollid());
        holder.name.setText(mylist.getName());
        holder.email.setText(mylist.getEmail());
        holder.mot.setText(mylist.getVowned());
        holder.add.setText(mylist.getAddress());
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

    class MyHoder extends RecyclerView.ViewHolder{
        TextView collid,name,email,mot,add;


        public MyHoder(View itemView) {
            super(itemView);
            collid = (TextView) itemView.findViewById(R.id.id);
            name= (TextView) itemView.findViewById(R.id.fname);
            email= (TextView) itemView.findViewById(R.id.femail);
            mot=(TextView)itemView.findViewById(R.id.fmode);
            add=(TextView)itemView.findViewById(R.id.add);

        }
    }

}