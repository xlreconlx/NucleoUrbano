package com.premiosnucleourbano.nucleourbano.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.premiosnucleourbano.nucleourbano.R;
import com.premiosnucleourbano.nucleourbano.models.Categorias;

/**
 * Created by ander on 3/08/2017.
 */

public class CategoriaViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;

    public CategoriaViewHolder(View itemView){
        super(itemView);

        titleView = (TextView)itemView.findViewById(R.id.cate_tittle);
    }

    public void bindToCategoria(Categorias cate){
        titleView.setText(cate.nombre);
     }

}
