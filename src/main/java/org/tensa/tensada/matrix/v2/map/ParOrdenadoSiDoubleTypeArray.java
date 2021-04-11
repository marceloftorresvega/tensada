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
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 */
public class ParOrdenadoSiDoubleTypeArray implements ConcurrentMap<ParOrdenado, Double> {

    public ParOrdenadoSiDoubleTypeArray(double[] core) {
        this.core = core;
    }

    private final double[] core;

    @Override
    public Double putIfAbsent(ParOrdenado k, Double v) {
        return isNull(core[k.getFila() - 1]) ? core[k.getFila() - 1] = v : null;
    }

    @Override
    public boolean remove(Object o, Object o1) {
        ParOrdenado k = (ParOrdenado) o;
        Double v = (Double) o1;
        return nonNull(v) && 0 == v.compareTo(core[k.getFila() - 1]) && 0 == (core[k.getFila() - 1] = 0);
    }

    @Override
    public boolean replace(ParOrdenado k, Double v, Double v1) {
        return nonNull(v) && nonNull(v1) && 0 == v.compareTo(core[k.getFila() - 1]) && v1 == (core[k.getFila() - 1] = v1);
    }

    @Override
    public Double replace(ParOrdenado k, Double v) {
        Double ex;
        return nonNull(ex = core[k.getFila() - 1]) && v == (core[k.getFila() - 1] = v) ? ex : null;
    }

    @Override
    public int size() {
        return core.length;
    }

    @Override
    public boolean isEmpty() {
        return !Arrays.stream(core).parallel().anyMatch(Objects::nonNull);
    }

    @Override
    public boolean containsKey(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return nonNull(core[k.getFila() - 1]);
    }

    @Override
    public boolean containsValue(Object o) {
        return Arrays.stream(core).parallel().anyMatch(o::equals);
    }

    @Override
    public Double get(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return core[k.getFila() - 1];
    }

    @Override
    public Double put(ParOrdenado k, Double v) {
        Double ex = core[k.getFila() - 1];
        core[k.getFila() - 1] = v;
        return ex;
    }

    @Override
    public Double remove(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        Double ex = core[k.getFila() - 1];
        core[k.getFila() - 1] = .0;
        return ex;
    }

    @Override
    public void putAll(Map<? extends ParOrdenado, ? extends Double> map) {
        map.entrySet().parallelStream().forEach(e -> {
            ParOrdenado k = e.getKey();
            Double v = e.getValue();
            core[k.getFila() - 1] = v;
        });
    }

    @Override
    public void clear() {
        Arrays.parallelSetAll(core, (i) -> 0);
    }

    @Override
    public Set<ParOrdenado> keySet() {
        return IntStream.rangeClosed(1, core.length)
                .mapToObj(i -> new Indice(i, 1))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Double> values() {
        return DoubleStream.of(core).parallel().boxed().collect(toList());
    }

    @Override
    public Set<Entry<ParOrdenado, Double>> entrySet() {
        return IntStream.rangeClosed(1, core.length)
                .mapToObj(i -> (ParOrdenado) new Indice(i, 1))
                .map(MatEntry::new)
                .collect(Collectors.toSet());
    }

    class MatEntry implements Entry<ParOrdenado, Double> {

        private final ParOrdenado k;

        public MatEntry(ParOrdenado k) {
            this.k = k;
        }

        @Override
        public ParOrdenado getKey() {
            return k;
        }

        @Override
        public Double getValue() {
            return core[k.getFila() - 1];
        }

        @Override
        public Double setValue(Double v) {
            Double ex = core[k.getFila() - 1];
            core[k.getFila() - 1] = v;
            return ex;
        }

    }

}
