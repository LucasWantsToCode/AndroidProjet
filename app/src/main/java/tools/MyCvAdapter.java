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
import sn.isi.wsandroidmysql.entities.Cv;
import sn.isi.wsandroidmysql.entities.Demandeur;

public class MyCvAdapter extends BaseAdapter {
    private List<Cv> cvs;
    private LayoutInflater myInflater;

    public MyCvAdapter(Context context, ArrayList<Cv> cvs)
    {
        this.myInflater = LayoutInflater.from(context);
        this.cvs = cvs;
    }
    @Override
    public int getCount() {
        return this.cvs.size();
    }

    @Override
    public Object getItem(int position) {
        return this.cvs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textid;
        TextView textformation;
        TextView textcompetence;
        TextView textdemandeur;
        TextView textdomaine;
        TextView textpassion;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        MyCvAdapter.ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitem_cv, null);
            holder = new MyCvAdapter.ViewHolder();
            holder.textid = (TextView) convertView.findViewById(R.id.txtid);
            holder.textformation = (TextView) convertView.findViewById(R.id.txtformaton);
            holder.textcompetence = (TextView) convertView.findViewById(R.id.txtcompetences);
            holder.textdemandeur = (TextView) convertView.findViewById(R.id.txtdemandeur);
            holder.textdomaine = (TextView) convertView.findViewById(R.id.txtdomaine);
            holder.textpassion = (TextView) convertView.findViewById(R.id.txtpassion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textid.setText(cvs.get(position).getId()+"");
        holder.textformation.setText(cvs.get(position).getFormation());
        holder.textcompetence.setText(cvs.get(position).getCompetences());
        holder.textdemandeur.setText(cvs.get(position).getDemandeur().getId()+"");
        holder.textdomaine.setText(cvs.get(position).getDomaine().getId()+"");
        holder.textpassion.setText(cvs.get(position).getPassion());

        return convertView;
    }
}
