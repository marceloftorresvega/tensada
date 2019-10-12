package cl.tensa.matrix;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mtorres
 * @param <V>
 */
public class Matriz<V> extends HashMap<ParOrdenado, V> implements Map<ParOrdenado, V> {

    protected Dominio dominio;

    public Dominio getDominio() {
        return dominio;
    }

    public Matriz(Dominio dominio) {
        this.dominio = dominio;
    }

    public Matriz(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m) {
        super(m);
        this.dominio = dominio;
    }

    public Matriz<V> indexa(Integer fila, Integer columna, V valor) {
        this.put(new Indice(fila, columna), valor);
        return this;
    }


}
