package tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sn.isi.wsandroidmysql.R;
import sn.isi.wsandroidmysql.entities.Compte;

public class MyCompteAdapter extends BaseAdapter {

    private ArrayList<Compte> comptes;
    private LayoutInflater myInflater;

    public MyCompteAdapter(Context context, ArrayList<Compte> comptes)
    {
        this.myInflater = LayoutInflater.from(context);
        this.comptes = comptes;
    }
    @Override
    public int getCount() {
        return this.comptes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.comptes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textnumero;
        TextView textnom;
        TextView textprenom;
        TextView texttel;
        TextView textsolde;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitem_compte, null);
            holder = new ViewHolder();
            holder.textnumero = (TextView) convertView.findViewById(R.id.txtnumero);
            holder.textnom = (TextView) convertView.findViewById(R.id.txtnom);
            holder.textprenom = (TextView) convertView.findViewById(R.id.txtprenom);
            holder.texttel = (TextView) convertView.findViewById(R.id.txttel);
            holder.textsolde = (TextView) convertView.findViewById(R.id.txtsolde);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textnumero.setText(comptes.get(position).getNumero());
        holder.textnom.setText(comptes.get(position).getNom());
        holder.textprenom.setText(comptes.get(position).getPrenom());
        holder.texttel.setText(comptes.get(position).getTel());
        holder.textsolde.setText(comptes.get(position).getSolde()+"");

        return convertView;
    }
}
