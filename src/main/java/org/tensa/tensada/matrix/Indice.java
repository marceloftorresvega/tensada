package org.tensa.tensada.matrix;

import java.util.Objects;

/**
 * par de elementos
 * @author mtorres
 */
public class Indice implements ParOrdenado {

    public static final Indice E1 = new Indice(1, 1);
    public static final Indice E2 = new Indice(2, 1);
    public static final Indice E3 = new Indice(3, 1);
    public static final Indice E2T = new Indice(1, 2);
    
    public static final Indice D1 = E1;
    public static final Indice D2 = new Indice(2, 2);
    public static final Indice D3 = new Indice(3, 3);
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.fila);
        hash = 37 * hash + Objects.hashCode(this.columna);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
//        if (getClass() != obj.getClass()) {
        if (! (obj instanceof ParOrdenado)) {
            return false;
        }
        final Indice other = (Indice) obj;
        if (!Objects.equals(this.fila, other.fila)) {
            return false;
        }
        if (!Objects.equals(this.columna, other.columna)) {
            return false;
        }
        return true;
    }

    public Indice(Integer fila, Integer columna) {
        this.fila = fila;
        this.columna = columna;
    }
    
    public Indice(ParOrdenado parOrdenado) {
        this.fila = parOrdenado.getFila();
        this.columna = parOrdenado.getColumna();
    }
    
    private final Integer fila;


    private final Integer columna;
    
    /**
     * Get the value of fila
     *
     * @return the value of fila
     */
    @Override
    public Integer getFila() {
        return fila;
    }

    /**
     * Get the value of columna
     *
     * @return the value of columna
     */
    @Override
    public Integer getColumna() {
        return columna;
    }

    @Override
    public ParOrdenado transpuesta() {
        return new Indice(columna, fila);
    }


}
