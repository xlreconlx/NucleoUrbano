package com.premiosnucleourbano.nucleourbano.models;

import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by ander on 8/08/2017.
 */
@IgnoreExtraProperties
public class Votos {
    public Long votos;

    public Votos(){}

    public Votos(Long votos){
        this.votos = votos;
    }
}
