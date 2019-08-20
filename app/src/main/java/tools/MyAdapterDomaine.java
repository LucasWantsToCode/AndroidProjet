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
import sn.isi.wsandroidmysql.entities.Domaine;

public class MyAdapterDomaine extends BaseAdapter {
    private List<Domaine> domaines;
    private LayoutInflater myInflater;

    public MyAdapterDomaine(Context context, ArrayList<Domaine> domaines)
    {
        this.myInflater = LayoutInflater.from(context);
        this.domaines = domaines;
    }
    @Override
    public int getCount() {
        return this.domaines.size();
    }

    @Override
    public Object getItem(int position) {
        return this.domaines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder {
        TextView textid;
        TextView textnom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyAdapterDomaine.ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitem_domaine, null);
            holder = new MyAdapterDomaine.ViewHolder();
            holder.textid = (TextView) convertView.findViewById(R.id.txtid);
            holder.textnom = (TextView) convertView.findViewById(R.id.txtnom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textid.setText(domaines.get(position).getId()+"");
        holder.textnom.setText(domaines.get(position).getNom());

        return convertView;
    }
}
