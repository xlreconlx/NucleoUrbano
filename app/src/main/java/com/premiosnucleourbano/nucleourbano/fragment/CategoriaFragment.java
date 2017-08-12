package com.premiosnucleourbano.nucleourbano.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by ander on 3/08/2017.
 */

public class CategoriaFragment extends CategoriaListFragment {

    public CategoriaFragment(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All Categorias
        return databaseReference.child("categorias");
    }

}
