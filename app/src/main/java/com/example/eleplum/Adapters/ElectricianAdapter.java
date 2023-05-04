package com.example.eleplum.Adapters;

import static com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment.userLocLatitude;
import static com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment.userLocLongitude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.AdapterListener.ElectricianAdapterListener;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElectricianAdapter extends RecyclerView.Adapter<ElectricianAdapter.ViewHolder> {

    ArrayList<Electrician>electricians;
    Context context;
    private ElectricianAdapterListener listener;
    public ElectricianAdapter(ArrayList<Electrician>electricians,Context context,ElectricianAdapterListener listener) {
        this.electricians=electricians;
        this.context=context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.view_nearbyelectrician, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ElectricianAdapter.ViewHolder holder, int position) {

        Electrician electrician=electricians.get(position);
        Picasso.get().load(electrician.getImageURL()).into(holder.image);
        holder.name.setText(electrician.getName());
        holder.rating.setText(electrician.getRating()+"");
        Double dis=new Utils().getDistance(electrician.getLatitude(),electrician.getLongitude(),userLocLatitude,userLocLongitude);
        holder.distance.setText(String.format("%.1f",dis) +" Km");

        // on clicking call icon
        holder.callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCallIconClick(electricians.get(position));
            }
        });




    }

    @Override
    public int getItemCount() {
        return electricians.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public CircleImageView image;
        public TextView name;
        public TextView rating;
        public TextView distance;
        public ImageView callImageView;
        public ImageView chatImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.electricianPic);
            name=itemView.findViewById(R.id.electricianName);
            rating=itemView.findViewById(R.id.electricianRating);
            distance=itemView.findViewById(R.id.distance);
            callImageView=itemView.findViewById(R.id.ele_callIcon);
            chatImageView=itemView.findViewById(R.id.ele_chatIcon);
        }
    }
}
