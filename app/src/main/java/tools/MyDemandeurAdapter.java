package tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sn.isi.wsandroidmysql.R;
import sn.isi.wsandroidmysql.entities.Demandeur;

public class MyDemandeurAdapter extends BaseAdapter {
    private List<Demandeur> demandeurs;
    private LayoutInflater myInflater;

    public MyDemandeurAdapter(Context context, ArrayList<Demandeur> demandeurs)
    {
        this.myInflater = LayoutInflater.from(context);
        this.demandeurs = demandeurs;
    }
    @Override
    public int getCount() {
        return this.demandeurs.size();
    }

    @Override
    public Object getItem(int position) {
        return this.demandeurs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder {
        TextView textid;
        TextView textnom;
        TextView textprenom;
        TextView textemail;
        TextView textpassword;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyDemandeurAdapter.ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitem_demandeur, null);
            holder = new MyDemandeurAdapter.ViewHolder();
            holder.textid = (TextView) convertView.findViewById(R.id.txtid);
            holder.textnom = (TextView) convertView.findViewById(R.id.txtnom);
            holder.textprenom = (TextView) convertView.findViewById(R.id.txtprenom);
            holder.textemail = (TextView) convertView.findViewById(R.id.txtemail);
            holder.textpassword = (TextView) convertView.findViewById(R.id.txtpassword);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textid.setText(demandeurs.get(position).getId()+"");
        holder.textnom.setText(demandeurs.get(position).getNom());
        holder.textprenom.setText(demandeurs.get(position).getPrenom());
        holder.textemail.setText(demandeurs.get(position).getEmail());
        holder.textpassword.setText(demandeurs.get(position).getPassword());

        return convertView;
    }
}
