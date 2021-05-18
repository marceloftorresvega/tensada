/*
 * The MIT License
 *
 * Copyright 2019 Marcelo.
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
package org.tensa.tensada.matrix;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import static java.util.stream.Collectors.*;

/**
 *
 * @author Marcelo
 */
public class BlockMatriz<U> extends Matriz<Matriz<U>> {

    public BlockMatriz(Dominio dominio) {
        super(dominio);
    }

    public BlockMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends Matriz<U>> m) {
        super(dominio, m);
    }

    public BlockMatriz() {
        super();
    }

    public Matriz<U> build() {
        return merge();
    }

    public Matriz<U> merge() {
        this.replaceAll((i, matriz) -> {
            int maxFila = this.dominio.stream().filter(par -> par.getFila().equals(i.getFila()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getFila())
                    .max().orElse(0);

            int maxColumna = this.dominio.stream().filter(par -> par.getColumna().equals(i.getColumna()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getColumna())
                    .max().orElse(0);

            matriz.dominio = new Dominio(maxFila, maxColumna);
            return matriz;
        });

        int dominioMaxFila = this.entrySet().stream()
                .filter(e -> e.getKey().getColumna() == 1)
                .map(Entry<ParOrdenado, Matriz<U>>::getValue)
                .map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getFila)
                .sum();

        int dominioMaxColumna = this.entrySet().stream()
                .filter(e -> e.getKey().getFila() == 1)
                .map(Entry<ParOrdenado, Matriz<U>>::getValue)
                .map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getColumna)
                .sum();

        Dominio nuevoDominio = new Dominio(dominioMaxFila, dominioMaxColumna);
        return this.entrySet().stream()
                .reduce(new Matriz<>(nuevoDominio),
                        (Matriz<U> matriz, Entry<ParOrdenado, Matriz<U>> entry) -> {
                            ParOrdenado key = entry.getKey();
                            Matriz<U> childMatriz = entry.getValue();

                            int lastLeft = this.entrySet().stream()
                                    .filter(e -> e.getKey().getFila() == key.getFila())
                                    .filter(e -> e.getKey().getColumna() < key.getColumna())
                                    .mapToInt(e -> e.getValue().getDominio().getColumna())
                                    .sum();

                            int lastTop = this.entrySet().stream()
                                    .filter(e -> e.getKey().getColumna() == key.getColumna())
                                    .filter(e -> e.getKey().getFila() < key.getFila())
                                    .mapToInt(e -> e.getValue().getDominio().getFila())
                                    .sum();

                            childMatriz.forEach((ParOrdenado i, U val) -> {
                                matriz.indexa(lastTop > 0 ? lastTop - 1 : 0 + i.getFila(), lastLeft > 0 ? lastLeft - 1 : 0 + i.getColumna(), val);
                            });
                            return matriz;
                        }, (Matriz<U> matriz1, Matriz<U> matriz2) -> {
                            matriz1.putAll(matriz2);
                            return matriz1;
                        });
    }

    public void splitIn(Matriz<U> older) {

        this.replaceAll((i, matriz) -> {
            int maxFila = this.dominio.stream().filter(par -> par.getFila().equals(i.getFila()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getFila())
                    .max().orElse(0);

            int maxColumna = this.dominio.stream().filter(par -> par.getColumna().equals(i.getColumna()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getColumna())
                    .max().orElse(0);

            matriz.dominio = new Dominio(maxFila, maxColumna);
            return matriz;
        });

        int dominioMaxFila = this.entrySet().stream()
                .filter(e -> e.getKey().getColumna() == 1)
                .map(Entry<ParOrdenado, Matriz<U>>::getValue)
                .map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getFila)
                .sum();

        int dominioMaxColumna = this.entrySet().stream()
                .filter(e -> e.getKey().getFila() == 1)
                .map(Entry<ParOrdenado, Matriz<U>>::getValue)
                .map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getColumna)
                .sum();

        Dominio superDominio = new Dominio(dominioMaxFila, dominioMaxColumna);
        if (superDominio.equals(older.getDominio())) {

            this.getDominio().stream()
                    .forEach((ParOrdenado key) -> {

                        Matriz<U> childMatriz = get(key);

                        int lastLeft = this.entrySet().stream()
                                .filter(e -> e.getKey().getFila() == key.getFila())
                                .filter(e -> e.getKey().getColumna() < key.getColumna())
                                .mapToInt(e -> e.getValue().getDominio().getColumna())
                                .sum();

                        int lastTop = this.entrySet().stream()
                                .filter(e -> e.getKey().getColumna() == key.getColumna())
                                .filter(e -> e.getKey().getFila() < key.getFila())
                                .mapToInt(e -> e.getValue().getDominio().getFila())
                                .sum();

                        childMatriz.getDominio().stream()
                                .forEach((ParOrdenado i) -> {
                                    U val = older.get(new Indice(lastTop > 0 ? lastTop - 1 : 0 + i.getFila(),
                                            lastLeft > 0 ? lastLeft - 1 : 0 + i.getColumna()));
                                    childMatriz.put(i, val);
                                });

                    });

        } else {
            throw new IllegalArgumentException("Matrices no compatibles");
        }
    }

    public static <U> BlockMatriz<U> wrapper(Matriz<Dominio> domains, Matriz<U> older) {

        Map<Integer, Integer> filasMaximas = domains.entrySet().stream()
                .collect(groupingBy(e -> e.getKey().getColumna(), collectingAndThen(mapping(e -> e.getValue().getFila(), maxBy(Integer::compare)), o -> o.orElse(1))));

        Map<Integer, Integer> columnasMaximas = domains.entrySet().stream()
                .collect(groupingBy(e -> e.getKey().getFila(), collectingAndThen(mapping(e -> e.getValue().getColumna(), maxBy(Integer::compare)), o -> o.orElse(1))));

        Map<ParOrdenado, Dominio> extDomains = domains.getDominio().stream()
                .map(k -> new Matriz.SimpleEntry(k, new Dominio(filasMaximas.get(k.getColumna()), columnasMaximas.get(k.getFila()))))
                .collect(toMap(Matriz.Entry<ParOrdenado, Dominio>::getKey, Matriz.Entry<ParOrdenado, Dominio>::getValue));

        Map<ParOrdenado, ParOrdenado> extOffset = domains.getDominio().stream()
                .map(k -> {

                    int fila = extDomains.entrySet().stream()
                            .filter(e -> ((ParOrdenado) e.getKey()).getColumna() < k.getColumna())
                            .map(Entry::getValue)
                            .mapToInt(Dominio::getFila)
                            .sum();
                    int columna = extDomains.entrySet().stream()
                            .filter(e -> ((ParOrdenado) e.getKey()).getFila() < k.getFila())
                            .map(Entry::getValue)
                            .mapToInt(Dominio::getColumna)
                            .sum();

                    return new Matriz.SimpleEntry(k, new Indice(fila, columna));
                })
                .collect(toMap(Entry<ParOrdenado, ParOrdenado>::getKey, Entry<ParOrdenado, ParOrdenado>::getValue));

        BlockMatriz<U> wrapperBlockMatrixProvider = new BlockMatriz<>(domains.getDominio());

        domains.getDominio().forEach(k -> wrapperBlockMatrixProvider.put(k, new MatrizWrapperImpl(extDomains.get(k), extOffset.get(k), older)));

        return wrapperBlockMatrixProvider;
    }

    private static class MatrizWrapperImpl<U> extends Matriz<U> {

        @Override
        public void putAll(Map<? extends ParOrdenado, ? extends U> m) {
            m.forEach((k,v)-> base.put(translateKey(k), v));
        }

        @Override
        public U get(Object key) {
            return base.get(translateKey((ParOrdenado) key));
        }

        @Override
        public U put(ParOrdenado key, U value) {
            return base.put(translateKey(key), value);
        }

        @Override
        public Set<ParOrdenado> keySet() {
            return this.getDominio().stream()
                    .filter(k -> base.containsKey(translateKey(k)))
                    .collect(toSet());
        }

        @Override
        public Collection<U> values() {
            return this.getDominio().stream()
                    .map(k -> base.get(translateKey(k)))
                    .filter(Objects::nonNull)
                    .collect(toList());
        }

        @Override
        public Set<Entry<ParOrdenado, U>> entrySet() {
            return this.getDominio().stream()
                    .map(k -> new Matriz.SimpleImmutableEntry<>(k, base.get(translateKey(k))))
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .collect(toSet());
        }

        @Override
        public U replace(ParOrdenado key, U value) {
            return base.replace(translateKey(key), value);
        }

        @Override
        public void forEach(BiConsumer<? super ParOrdenado, ? super U> action) {
            this.getDominio().stream()
                    .map(k -> new Matriz.SimpleImmutableEntry<>(k, base.get(translateKey(k))))
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .forEach(e -> action.accept(e.getKey(), e.getValue()));
        }

        @Override
        public void replaceAll(BiFunction<? super ParOrdenado, ? super U, ? extends U> function) {
            this.getDominio().stream()
                    .filter(k -> base.containsKey(translateKey(k)))
                    .forEach(k -> base.replace(k, function.apply(k, base.get(translateKey(k)))));
        }

        private final Matriz<U> base;
        private final ParOrdenado offset;

        public MatrizWrapperImpl(Dominio dmn, ParOrdenado offset, Matriz<U> base) {
            super(dmn);
            this.base = base;
            this.offset = offset;
        }

        private ParOrdenado translateKey(ParOrdenado key) {
            return new Indice(offset.getFila() + key.getFila(), offset.getColumna() + key.getColumna());
        }
    }
}
