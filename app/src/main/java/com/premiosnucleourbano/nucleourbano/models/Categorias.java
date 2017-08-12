package com.premiosnucleourbano.nucleourbano.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ander on 3/08/2017.
 */

@IgnoreExtraProperties
public class Categorias {
    public String nombre;

    public Categorias(){
    }

    public Categorias(String nombre,String img) {
        this.nombre = nombre;
    }


}
