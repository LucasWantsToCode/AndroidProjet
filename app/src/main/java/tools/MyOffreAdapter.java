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
import sn.isi.wsandroidmysql.entities.Offre;

public class MyOffreAdapter extends BaseAdapter {

    private List<Offre> offres;
    private LayoutInflater myInflater;

    public MyOffreAdapter(Context context, ArrayList<Offre> offres)
    {
        this.myInflater = LayoutInflater.from(context);
        this.offres = offres;
    }
    @Override
    public int getCount() {
        return this.offres.size();
    }

    @Override
    public Object getItem(int position) {
        return this.offres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textid;
        TextView textdateo;
        TextView textlibelleo;
        TextView textentreprise;
        TextView textdomaine;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyOffreAdapter.ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitem_offres, null);
            holder = new MyOffreAdapter.ViewHolder();
            holder.textid = (TextView) convertView.findViewById(R.id.txtido);
            holder.textdateo = (TextView) convertView.findViewById(R.id.txtdateo);
            holder.textlibelleo = (TextView) convertView.findViewById(R.id.txtlibelleo);
            holder.textentreprise = (TextView) convertView.findViewById(R.id.txtentreprise);
            holder.textdomaine = (TextView) convertView.findViewById(R.id.txtdomaine);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textid.setText(offres.get(position).getId()+"");
        holder.textdateo.setText(offres.get(position).getDateO());
        holder.textlibelleo.setText(offres.get(position).getLibelle());
        holder.textentreprise.setText(offres.get(position).getEntreprise().getId()+"");
        holder.textdomaine.setText(offres.get(position).getDomaine().getId()+"");


        return convertView;
    }
}
