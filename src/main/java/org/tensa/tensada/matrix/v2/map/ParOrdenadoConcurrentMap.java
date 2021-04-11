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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toConcurrentMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 */
public class ParOrdenadoConcurrentMap<T, R extends ConcurrentMap<ParOrdenado, T>> implements ConcurrentMap<ParOrdenado, T> {

    private final ConcurrentMap<ParOrdenado, R> container;
    private final UnaryOperator<ParOrdenado> macroMaper;
    private final UnaryOperator<ParOrdenado> microMaper;
    private final BinaryOperator<ParOrdenado> revertMaper;

    public ParOrdenadoConcurrentMap(ConcurrentMap<ParOrdenado, R> container, UnaryOperator<ParOrdenado> macroMaper, UnaryOperator<ParOrdenado> microMaper, BinaryOperator<ParOrdenado> revertMaper) {
        this.container = container;
        this.macroMaper = macroMaper;
        this.microMaper = microMaper;
        this.revertMaper = revertMaper;
    }

    @Override
    public T putIfAbsent(ParOrdenado k, T v) {
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(k2 -> c.putIfAbsent(k2, v)).apply(k))
                .orElse(null);
    }

    @Override
    public boolean remove(Object o, Object o1) {
        ParOrdenado k = (ParOrdenado) o;
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(k2 -> c.remove(k2, o1)).apply(k))
                .orElse(false);
    }

    @Override
    public boolean replace(ParOrdenado k, T v, T v1) {
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(k2 -> c.replace(k2, v, v1)).apply(k))
                .orElse(null);
    }

    @Override
    public T replace(ParOrdenado k, T v) {
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(k2 -> c.replace(k2, v)).apply(k))
                .orElse(null);
    }

    @Override
    public int size() {
        return container.values().parallelStream()
                .collect(summingInt(ConcurrentMap::size));
    }

    @Override
    public boolean isEmpty() {
        return container.values().parallelStream()
                .map(ConcurrentMap::values)
                .allMatch(Collection::isEmpty);
    }

    @Override
    public boolean containsKey(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(c::containsKey).apply(k))
                .orElse(false);
    }

    @Override
    public boolean containsValue(Object o) {
        return container.values().parallelStream()
                .anyMatch(r -> r.containsValue(o));
    }

    @Override
    public T get(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(c::get).apply(k))
                .orElse(null);
    }

    @Override
    public T put(ParOrdenado k, T v) {
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(k2 -> c.put(k2, v)).apply(k))
                .orElse(null);
    }

    @Override
    public T remove(Object o) {
        ParOrdenado k = (ParOrdenado) o;
        return macroMaper
                .andThen(container::get)
                .andThen(Optional::ofNullable)
                .apply(k)
                .map(c -> microMaper.andThen(c::remove).apply(k))
                .orElse(null);
    }

    @Override
    public void putAll(Map<? extends ParOrdenado, ? extends T> map) {
        map.forEach((k, v) -> {
            macroMaper
                    .andThen(container::get)
                    .andThen(Optional::ofNullable)
                    .apply(k)
                    .ifPresent(c -> microMaper.andThen(k2 -> c.put(k2, v)).apply(k));
        });
    }

    @Override
    public void clear() {
        container.values().parallelStream().forEach(ConcurrentMap::clear);
    }

    @Override
    public Set<ParOrdenado> keySet() {
        return container.entrySet().parallelStream().flatMap(e -> {
            return e.getValue().keySet().stream().map(k -> revertMaper.apply(e.getKey(), k));
        }).collect(toSet());
    }

    @Override
    public Collection<T> values() {
        return container.values().parallelStream()
                .map(ConcurrentMap::values).flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public Set<Entry<ParOrdenado, T>> entrySet() {
        return container.entrySet().parallelStream().map(e -> {
            return e.getValue().entrySet().stream()
                    .collect(toConcurrentMap(ek -> revertMaper.apply(e.getKey(), ek.getKey()), Entry::getValue));
        })
                .map(ConcurrentMap::entrySet)
                .flatMap(Set::stream)
                .collect(toSet());
    }

}
