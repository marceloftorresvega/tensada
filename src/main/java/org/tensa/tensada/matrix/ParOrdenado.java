package org.tensa.tensada.matrix;

/**
 *
 * @author mtorres
 */
public interface ParOrdenado {

    @Override
    boolean equals(Object obj);

    /**
     * Get the value of columna
     *
     * @return the value of columna
     */
    Integer getColumna();

    /**
     * Get the value of fila
     *
     * @return the value of fila
     */
    Integer getFila();

    @Override
    int hashCode();
    
    ParOrdenado transpuesta();
    
}
