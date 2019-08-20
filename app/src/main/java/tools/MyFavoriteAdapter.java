package tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sn.isi.wsandroidmysql.R;
import sn.isi.wsandroidmysql.entities.Favorite;

public class MyFavoriteAdapter extends BaseAdapter {

    private ArrayList<Favorite> favorites;
    private LayoutInflater myInflater;

    public MyFavoriteAdapter(Context context, ArrayList<Favorite> favorites)
    {
        this.myInflater = LayoutInflater.from(context);
        this.favorites = favorites;
    }

    @Override
    public int getCount() {
        return this.favorites.size();
    }

    @Override
    public Object getItem(int position) {
        return this.favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textid;
        TextView textdemandeur;
        TextView textoffre;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitem_fav, null);
            holder = new ViewHolder();
            holder.textid = (TextView) convertView.findViewById(R.id.txtid);
            holder.textdemandeur = (TextView) convertView.findViewById(R.id.txtdemandeur);
            holder.textoffre = (TextView) convertView.findViewById(R.id.txtoffre);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textid.setText(favorites.get(position).getId()+"");
        holder.textdemandeur.setText(favorites.get(position).getDemandeur().getId()+"");
        holder.textoffre.setText(favorites.get(position).getOffre().getId()+"");

        return convertView;
    }
}
