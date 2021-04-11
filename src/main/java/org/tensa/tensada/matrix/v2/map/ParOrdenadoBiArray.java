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
package org.tensa.tensada.matrix.v2.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 * @param <T>
 */
public class ParOrdenadoBiArray<T> implements ConcurrentMap<ParOrdenado, T> {

    public ParOrdenadoBiArray(T[][] core) {
        this.core = core;
    }

    private final T[][] core;

    @Override
    public T putIfAbsent(ParOrdenado k, T v) {
        return isNull(core[k.getFila() - 1][k.getColumna() - 1]) ? core[k.getFila() - 1][k.getColumna() - 1] = v : null;
    }

    @Override
    public boolean remove(Object o, Object o1) {
        ParOrdenado k = (ParOrdenado) o;
        return nonNull(core[k.getFila() - 1][k.getColumna() - 1]) && core[k.getFila() - 1][k.getColumna() - 1].equals(o1) && null == (core[k.getFila() - 1][k.getColumna() - 1] = null);
    }

    @Override
    public boolean replace(ParOrdenado k, T v, T v1) {
        return nonNull(core[k.getFila() - 1][k.getColumna() - 1]) && core[k.getFila() - 1][k.getColumna() - 1].equals(v) && v1 == (core[k.getFila() - 1][k.getColumna() - 1] = v1);
    }

    @Override
    public T replace(ParOrdenado k, T v) {
        T ex;
        return nonNull(ex = core[k.getFila() - 1][k.getColumna() - 1]) && v == (core[k.getFila() - 1][k.getColumna() - 1] = v) ? ex : null;
    }

    @Override
    public int size() {
        return core.length * core[0].length;
    }

    @Override
    public boolean isEmpty() {
        return !Arrays.stream(core).flatMap(Arrays::stream).parallel().anyMatch(Objects::nonNull);
    }

    @Override
    public boolean containsKey(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return nonNull(core[k.getFila() - 1][k.getColumna() - 1]);
    }

    @Override
    public boolean containsValue(Object o) {
        return Arrays.stream(core).flatMap(Arrays::stream).parallel().anyMatch(o::equals);
    }

    @Override
    public T get(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return core[k.getFila() - 1][k.getColumna() - 1];
    }

    @Override
    public T put(ParOrdenado k, T v) {
        T ex = core[k.getFila() - 1][k.getColumna() - 1];
        core[k.getFila() - 1][k.getColumna() - 1] = v;
        return ex;
    }

    @Override
    public T remove(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        T ex = core[k.getFila() - 1][k.getColumna() - 1];
        core[k.getFila() - 1][k.getColumna() - 1] = null;
        return ex;
    }

    @Override
    public void putAll(Map<? extends ParOrdenado, ? extends T> map) {
        map.entrySet().parallelStream().forEach(e -> {
            ParOrdenado k = e.getKey();
            T v = e.getValue();
            core[k.getFila() - 1][k.getColumna() - 1] = v;
        });
    }

    @Override
    public void clear() {
        Arrays.stream(core).parallel().forEach(cols -> Arrays.fill(cols, null));
    }

    @Override
    public Set<ParOrdenado> keySet() {
        return IntStream.rangeClosed(1, core.length)
                .mapToObj(i -> IntStream.rangeClosed(1, core[0].length)
                .mapToObj(j -> new Indice(i, j)))
                .flatMap(Function.identity()).collect(Collectors.toSet());
    }

    @Override
    public Collection<T> values() {
        return Arrays.stream(core).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<ParOrdenado, T>> entrySet() {
        return IntStream.rangeClosed(1, core.length)
                .mapToObj(i -> IntStream.rangeClosed(1, core[0].length)
                .mapToObj(j -> (ParOrdenado) new Indice(i, j)))
                .flatMap(Function.identity())
                .map(MatEntry::new)
                .collect(Collectors.toSet());
    }

    class MatEntry implements Entry<ParOrdenado, T> {

        private final ParOrdenado k;

        public MatEntry(ParOrdenado k) {
            this.k = k;
        }

        @Override
        public ParOrdenado getKey() {
            return k;
        }

        @Override
        public T getValue() {
            return core[k.getFila() - 1][k.getColumna() - 1];
        }

        @Override
        public T setValue(T v) {
            T ex = core[k.getFila() - 1][k.getColumna() - 1];
            core[k.getFila() - 1][k.getColumna() - 1] = v;
            return ex;
        }

    }

}
