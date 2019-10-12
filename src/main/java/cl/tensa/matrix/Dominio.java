package cl.tensa.matrix;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.*;

/**
 *
 * @author mtorres
 */
public final class Dominio extends AbstractCollection<ParOrdenado> implements ParOrdenado, Collection<ParOrdenado> {

    private final Indice indice;
    public Dominio(Integer fila, Integer columna) {
        indice = new Indice(fila, columna);
        
    }
    public Dominio(ParOrdenado parOrdenado) {
        indice = new Indice(parOrdenado);
        
    }

    @Override
    public Iterator<ParOrdenado> iterator() {
        return IntStream.rangeClosed(1, indice.getColumna())
                .boxed()
                .flatMap(col -> IntStream.rangeClosed(1, indice.getFila())
                .mapToObj(fil -> (ParOrdenado) new Indice(fil,col)))
                .iterator();
    }

    @Override
    public int size() {
        return indice.getFila() * indice.getColumna();
    }

    @Override
    public Integer getColumna() {
        return indice.getColumna();
    }

    @Override
    public Integer getFila() {
        return indice.getFila();
    }

    @Override
    public ParOrdenado transpuesta() {
        return indice.transpuesta();
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
        
        final ParOrdenado other = (ParOrdenado) obj;
        
        return Objects.equals(indice.getFila(), other.getFila())
                && Objects.equals(indice.getColumna(), other.getColumna());
    }

    @Override
    public int hashCode() {
        return indice.hashCode(); 
    }

    

}
