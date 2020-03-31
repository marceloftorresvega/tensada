package org.tensa.tensada.matrix;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mtorres
 * @param <V>
 */
public class Matriz<V> extends HashMap<ParOrdenado, V> implements Map<ParOrdenado, V>, Serializable, Closeable {

    protected Dominio dominio;

    public Dominio getDominio() {
        return dominio;
    }

    public Matriz(Dominio dominio) {
        this.dominio = dominio;
    }

    public Matriz() {
        this.dominio = new Dominio(Indice.D1);
    }

    public Matriz(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m) {
        super(m);
        this.dominio = dominio;
    }

    public Matriz<V> indexa(Integer fila, Integer columna, V valor) {
        this.put(new Indice(fila, columna), valor);
        return this;
    }

    @Override
    public void close() throws IOException {
        this.clear();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(this.dominio);
        stream.writeObject(Collections.synchronizedMap(this));
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.dominio = (Dominio) stream.readObject();
        this.putAll((Map) stream.readObject());
    }

}
