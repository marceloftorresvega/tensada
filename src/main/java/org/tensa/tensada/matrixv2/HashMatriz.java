/*
 * The MIT License
 *
 * Copyright 2020 lorenzo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tensa.tensada.matrixv2;

import java.util.HashMap;
import java.util.Map;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;


public class HashMatriz<V> extends HashMap<ParOrdenado, V> implements Matriz<V> {

    private final Dominio domain;
    
    @Override
    public Dominio getDominio() {
        return domain;
    }

    public HashMatriz(Dominio domain, Map<? extends ParOrdenado, ? extends V> m) {
        super(m);
        this.domain = domain;
    }

    public HashMatriz(Dominio domain) {
        this.domain = domain;
    }

    public HashMatriz(Matriz<V> m) {
        super(m);
        this.domain = m.getDominio();
    }

    @Override
    public Matriz<V> indexa(Integer i, Integer j, V valor) {
        return this.indexa(new Indice(i, j), valor);
    }

    @Override
    public Matriz<V> indexa(ParOrdenado i, V valor) {
        this.put(i, valor);
        return this;
    }

    @Override
    public Matriz<V> transpuesta() {
        return MatrizUtils.transpuesta(this);
    }

    @Override
    public Matriz<V> iesima(ParOrdenado i) {
        return MatrizUtils.iesima(i, this);
    }

    @Override
    public Matriz<V> diagonal() {
        return MatrizUtils.diagonal(this);
    }

    @Override
    public Matriz<V> instancia(Dominio dominio) {
        return new HashMatriz<>(dominio);
    }

    @Override
    public Matriz<V> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m) {
        return new HashMatriz<>(domain, m);
    }

    @Override
    public Matriz<V> instancia(Matriz<V> m) {
        return new HashMatriz<>(m);
    }

    
}
