/*
 * The MIT License
 *
 * Copyright 2020 Marcelo.
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 */
public class InjectedMatriz<N> implements Matriz<N>{
    
    private Matriz<N> origen;

    public InjectedMatriz(Matriz<N> origen) {
        this.origen = origen;
    }

    /**
     * Get the value of origen
     *
     * @return the value of origen
     */
    public Matriz<N> getOrigen() {
        return origen;
    }

    @Override
    public Dominio getDominio() {
        return origen.getDominio();
    }

    @Override
    public Matriz<N> instancia(Dominio dominio) {
        return origen.instancia(dominio);
    }

    @Override
    public Matriz<N> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends N> m) {
        return origen.instancia(dominio, m);
    }

    @Override
    public Matriz<N> instancia(Matriz<N> m) {
        return origen.instancia(m);
    }

    @Override
    public Matriz<N> indexa(Integer i, Integer j, N valor) {
        return origen.indexa(i, j, valor);
    }

    @Override
    public Matriz<N> indexa(ParOrdenado i, N valor) {
        return origen.indexa(i, valor);
    }

    @Override
    public Matriz<N> transpuesta() {
        return origen.transpuesta();
    }

    @Override
    public Matriz<N> iesima(ParOrdenado i) {
        return origen.iesima(i);
    }

    @Override
    public Matriz<N> diagonal() {
        return origen.diagonal();
    }

    @Override
    public int size() {
        return origen.size();
    }

    @Override
    public boolean isEmpty() {
        return origen.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return origen.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return origen.containsValue(o);
    }

    @Override
    public N get(Object o) {
        return origen.get(o);
    }

    @Override
    public N put(ParOrdenado k, N v) {
        return origen.put(k, v);
    }

    @Override
    public N remove(Object o) {
        return origen.remove(o);
    }

    @Override
    public void putAll(Map<? extends ParOrdenado, ? extends N> map) {
        origen.putAll(map);
    }

    @Override
    public void clear() {
        origen.clear();
    }

    @Override
    public Set<ParOrdenado> keySet() {
        return origen.keySet();
    }

    @Override
    public Collection<N> values() {
        return origen.values();
    }

    @Override
    public Set<Entry<ParOrdenado, N>> entrySet() {
        return origen.entrySet();
    }

    @Override
    public N getOrDefault(Object o, N v) {
        return origen.getOrDefault(o, v);
    }

    @Override
    public void forEach(BiConsumer<? super ParOrdenado, ? super N> bc) {
        origen.forEach(bc);
    }

    @Override
    public void replaceAll(BiFunction<? super ParOrdenado, ? super N, ? extends N> bf) {
        origen.replaceAll(bf);
    }

    @Override
    public N putIfAbsent(ParOrdenado k, N v) {
        return origen.putIfAbsent(k, v);
    }

    @Override
    public boolean remove(Object o, Object o1) {
        return origen.remove(o, o1);
    }

    @Override
    public boolean replace(ParOrdenado k, N v, N v1) {
        return origen.replace(k, v, v1);
    }

    @Override
    public N replace(ParOrdenado k, N v) {
        return origen.replace(k, v);
    }

    @Override
    public N computeIfAbsent(ParOrdenado k, Function<? super ParOrdenado, ? extends N> fnctn) {
        return origen.computeIfAbsent(k, fnctn);
    }

    @Override
    public N computeIfPresent(ParOrdenado k, BiFunction<? super ParOrdenado, ? super N, ? extends N> bf) {
        return origen.computeIfPresent(k, bf);
    }

    @Override
    public N compute(ParOrdenado k, BiFunction<? super ParOrdenado, ? super N, ? extends N> bf) {
        return origen.compute(k, bf);
    }

    @Override
    public N merge(ParOrdenado k, N v, BiFunction<? super N, ? super N, ? extends N> bf) {
        return origen.merge(k, v, bf);
    }

    @Override
    public int hashCode() {
        return origen.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return origen.equals(o);
    }

    @Override
    public String toString() {
        return origen.toString();
    }

}
