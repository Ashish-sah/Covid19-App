package com.ashish.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

//since we using custom listview that's why we need custom adapter
public class MyCustomAdapter  extends ArrayAdapter<CountryModel> {

    private  Context context;
    private List<CountryModel> countryModelList;
    //code for search bar
    private List<CountryModel> countryModelListFiltered;


    public MyCustomAdapter(Context context, List<CountryModel> countryModelList) {
        super(context, R.layout.list_custom_item,countryModelList);
        //in super for second parameter we pass the resource for which we have to design the layout
        this.context=context;
        this.countryModelList=countryModelList;
        this.countryModelListFiltered=countryModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //here we set all the data in the layout which we get from url in country Model
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item,null,true);
        TextView  tvCountryname=view.findViewById(R.id.tvCountryName);
        ImageView imageView=view.findViewById(R.id.imageFlag);

        tvCountryname.setText(countryModelListFiltered.get(position).getCountry());
        //now we see it for image whenever we get image from url we use glide Library
        Glide.with(context).load(countryModelListFiltered.get(position).getFlag()).into(imageView);

        return view;
    }
    //this method return the size of list
    @Override
    public int getCount() {
        return countryModelListFiltered.size();
    }
    //now we get the item from filtered list bcoz we had passed original list to it
    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return countryModelListFiltered.get(position);
    }
   //it give id of the position
    @Override
    public long getItemId(int position) {
        return position;
    }
//function that is called in affected country class
    @Override
    public Filter getFilter() {
        //Object of filter class
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = countryModelList.size();
                    //passing values to filter result
                    filterResults.values = countryModelList;

                }else{
                    List<CountryModel> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(CountryModel itemsModel:countryModelList){
                        if(itemsModel.getCountry().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                countryModelListFiltered = (List<CountryModel>) results.values;
                AffectedCountries.countryModelList = (List<CountryModel>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
