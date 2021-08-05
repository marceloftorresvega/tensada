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
package org.tensa.tensada.matrix.v2.impl;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toConcurrentMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;
import org.tensa.tensada.matrix.v2.InstanciaMatriz;
import org.tensa.tensada.matrix.v2.Matriz;
import org.tensa.tensada.matrix.v2.MatrizMath;
import org.tensa.tensada.matrix.v2.TerminalMath;

public class SimpleMatrizMathImpl<N extends Number> implements MatrizMath<N> {

    private final TerminalMath<N> terminalMath;

    private final InstanciaMatriz<N> instanciaMatriz;

    public SimpleMatrizMathImpl(TerminalMath<N> terminalMath, InstanciaMatriz<N> instanciaMatriz) {
        this.terminalMath = terminalMath;
        this.instanciaMatriz = instanciaMatriz;
    }

    @Override
    public TerminalMath<N> getTerminalMath() {
        return terminalMath;
    }

    @Override
    public InstanciaMatriz<N> getInstanciaMatriz() {
        return instanciaMatriz;
    }

    @Override
    public Matriz<N> productoEscalar(Matriz<N> m, Number e) {
        return m.entrySet().parallelStream()
                .collect(() -> instanciaMatriz.instancia(m.getDominio()),
                        (m2, e1) -> m2.put(e1.getKey(), terminalMath.productoDirecto(e1.getValue(), terminalMath.mapper(e))),
                        (m1, m2) -> m1.putAll(m2));
    }

    @Override
    public Matriz<N> rotacion(Matriz<N> eje, Number phi) {

        if (eje.getDominio().getFila() < 3 && eje.getDominio().getColumna() == 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        N coT = terminalMath.cos(terminalMath.mapper(phi));
        N siT = terminalMath.sin(terminalMath.mapper(phi));

        try (
                Matriz<N> ux = skewSymMatrix(eje);
                Matriz<N> uut = productoTensorial(eje, eje);
                Matriz<N> id = identidad(ux.getDominio());
                Matriz<N> idCot = productoEscalar(id, coT);
                Matriz<N> uxSit = productoEscalar(ux, siT);
                Matriz<N> uut1noCot = productoEscalar(uut, terminalMath.restaDirecta(terminalMath.getUnoValue(), coT));
                Matriz<N> adIdCotUxSit = adicion(idCot, uxSit);) {
            return adicion(adIdCotUxSit, uut1noCot);

        } catch (IOException ex) {
            //
            throw new RejectedExecutionException("matrizRotacion", ex);
        }
    }

    @Override
    public Matriz<N> matrizIesima(Matriz<N> m, ParOrdenado i) {

        return m.entrySet().parallelStream()
                .filter(e -> !e.getKey().getColumna().equals(i.getColumna()))
                .filter(e -> !e.getKey().getFila().equals(i.getFila()))
                .collect(toConcurrentMap(
                        (Entry<ParOrdenado, N> e) -> {
                            ParOrdenado idx = e.getKey();
                            Integer fila = idx.getFila();
                            Integer columna = idx.getColumna();
                            return new Indice(
                                    fila < i.getFila() ? fila : fila - 1,
                                    columna < i.getColumna() ? columna : columna - 1
                            );
                        },
                        Entry::getValue,
                        (v1, v2) -> v1,
                        () -> instanciaMatriz.instancia(new Dominio(
                                m.getDominio().getFila() - 1,
                                m.getDominio().getColumna() - 1))));
    }

    @Override
    public Matriz<N> transpuesta(Matriz<N> m) {
        return m.entrySet().parallelStream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().transpuesta(), e.getValue()))
                .collect(toConcurrentMap(
                        Entry::getKey,
                        Entry::getValue,
                        (a, b) -> a,
                        () -> instanciaMatriz.instancia(m.getDominio())
                ));
    }

    @Override
    public Matriz<N> matrizSimetrica(Matriz<N> m) {
        Dominio dominio = m.getDominio();
        if (dominio.getFila() != dominio.getColumna()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        return productoEscalar(adicion(m, transpuesta(m)), terminalMath.inversoMultiplicativo(terminalMath.mapper(2)));
    }

    @Override
    public Matriz<N> matrizAntisimetrica(Matriz<N> m) {
        Dominio dominio = m.getDominio();
        if (dominio.getFila() != dominio.getColumna()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        return productoEscalar(sustraccion(m, transpuesta(m)), terminalMath.inversoMultiplicativo(terminalMath.mapper(2)));
    }

    @Override
    public Matriz<N> skewSymMatrix(Matriz<N> m) {
        Dominio dominio = m.getDominio();

        if (dominio.getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (dominio.getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        N ux = m.getOrDefault(Indice.E1, terminalMath.getCeroValue());
        N uy = m.getOrDefault(Indice.E2, terminalMath.getCeroValue());
        N uz = m.getOrDefault(Indice.E3, terminalMath.getCeroValue());

        Matriz<N> local = instanciaMatriz.instancia(new Dominio(3, 3));
        local.put(Indice.E2T, terminalMath.inversoAditivo(uz));
        local.put(Indice.E3.transpuesta(), uy);
        local.put(Indice.E2, uz);
        local.put(new Indice(2, 3), terminalMath.inversoAditivo(ux));
        local.put(Indice.E3, terminalMath.inversoAditivo(uy));
        local.put(new Indice(3, 2), ux);
        return local;
    }

    @Override
    public Matriz<N> matrizAdjunta(Matriz<N> m) {
        Dominio dominio = m.getDominio();
        if (dominio.equals((ParOrdenado) Indice.D1)) {
            return instanciaMatriz.instancia(m);
        }

        if (dominio.equals((ParOrdenado) Indice.D2)) {
            Matriz<N> local = instanciaMatriz.instancia(dominio);
            local.put(Indice.E1, m.get(Indice.D2));
            local.put(Indice.E2, m.get(Indice.E2T));
            local.put(Indice.E2T, m.get(Indice.E2));
            local.put(Indice.D2, m.get(Indice.D1));
            return local;
        }

        return m.entrySet().parallelStream()
                .collect(toConcurrentMap(e -> e.getKey(), (Entry<ParOrdenado, N> e) -> {
                    N valor = determinante(matrizIesima(m, e.getKey()));

                    return (e.getKey().getFila() + e.getKey().getColumna()) % 2 == 0 ? valor : terminalMath.inversoAditivo(valor);
                },
                        (v1, v2) -> v1,
                        () -> instanciaMatriz.instancia(dominio)));
    }

    @Override
    public Matriz<N> matrizCofactores(Matriz<N> m) {
        Matriz<N> transpuesta = transpuesta(m);
        Matriz<N> matrizAdjunta = matrizAdjunta(transpuesta);
        transpuesta.clear();
        return matrizAdjunta;
    }

    @Override
    public Matriz<N> matrizInversa(Matriz<N> m) {
        N det = determinante(m);
        try (Matriz<N> cofactores = matrizCofactores(m)) {
            return productoEscalar(cofactores, terminalMath.inversoMultiplicativo(det));
        } catch (IOException ex) {
            throw new RejectedExecutionException("inversa", ex);
        }
    }

    @Override
    public N norma(Matriz<N> m) {
        return m.values().parallelStream()
                .map(terminalMath::cuadrado)
                .collect(
                        collectingAndThen(
                                reducing(terminalMath.getCeroValue(), terminalMath::sumaDirecta),
                                terminalMath::raiz));
    }

    @Override
    public N traza(Matriz<N> m) {
        Dominio dominio = m.getDominio();

        if (dominio.getColumna().intValue() != dominio.getFila().intValue()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        return IntStream.rangeClosed(1, dominio.getFila()).parallel()
                .mapToObj(i -> new Indice(i, i))
                .map(k -> m.getOrDefault(k, terminalMath.getCeroValue()))
                .reduce(
                        terminalMath.getCeroValue(),
                        terminalMath::sumaDirecta
                );
    }

    @Override
    public N determinante(Matriz<N> m) {
        Dominio dominio = m.getDominio();

        if (dominio.equals((ParOrdenado) Indice.D1)) {
            return m.getOrDefault(Indice.D1, terminalMath.getCeroValue());
        }

        return IntStream.rangeClosed(1, (int) dominio.getColumna()).parallel()
                .mapToObj(j -> {
                    Indice idx = new Indice(1, j);

                    N valor = terminalMath.productoDirecto(m.getOrDefault(idx, terminalMath.getCeroValue()), determinante(matrizIesima(m, idx)));

                    return ((1 + j) % 2 == 0) ? valor : terminalMath.inversoAditivo(valor);

                }).reduce(terminalMath.getCeroValue(), terminalMath::sumaDirecta);

    }

    @Override
    public Matriz<N> adicion(Matriz<N> sumando1, Matriz<N> sumando2) {
        return this.adicion(Stream.of(sumando1, sumando2).toArray(i -> new Matriz[i]));
    }

    @Override
    public Matriz<N> adicion(Matriz<N>... sumando) {
        Dominio dominio0 = sumando[0].getDominio();
        if (!Stream.of(sumando).skip(1).map(Matriz::getDominio).allMatch(dominio0::equals)) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        return Stream.of(sumando).parallel()
                .map(Matriz::entrySet)
                .flatMap(Set::stream)
                .filter(e -> Objects.nonNull(e.getValue()))
                .collect(toConcurrentMap(
                        Entry::getKey,
                        Entry::getValue,
                        terminalMath::sumaDirecta,
                        () -> instanciaMatriz.instancia(dominio0)));
    }

    @Override
    public Matriz<N> sustraccion(Matriz<N> minuendo, Matriz<N> sustraendo) {
        return this.sustraccion(Stream.of(minuendo, sustraendo).toArray(i -> new Matriz[i]));
    }

    @Override
    public Matriz<N> sustraccion(Matriz<N>... sustraendo) {
        Dominio dominio0 = sustraendo[0].getDominio();
        if (!Stream.of(sustraendo).skip(1).map(Matriz::getDominio).allMatch(dominio0::equals)) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        Matriz<N> minuendo = sustraendo[0];
        Stream<Entry<ParOrdenado, N>> m = minuendo.entrySet().stream().filter(e -> Objects.nonNull(e.getValue()));
        Stream<Entry<ParOrdenado, N>> s = Stream.of(sustraendo).skip(1)
                .map(Matriz::entrySet)
                .flatMap(Set::stream)
                .filter(e -> Objects.nonNull(e.getValue()))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), terminalMath.inversoAditivo(e.getValue())));
        return Stream.concat(m, s).parallel()
                .collect(toConcurrentMap(
                        Entry::getKey,
                        Entry::getValue,
                        terminalMath::sumaDirecta,
                        () -> instanciaMatriz.instancia(minuendo.getDominio())));
    }

    @Override
    public Matriz<N> producto(Matriz<N> multiplo1, Matriz<N> multiplo2) {
        return productoFlexible(multiplo1, UnaryOperator.identity(), multiplo2, UnaryOperator.identity());
    }

    @Override
    public Matriz<N> productoPunto(Matriz<N> multiplo1, Matriz<N> multiplo2) {
        return productoFlexible(multiplo1, ParOrdenado::transpuesta, multiplo2, UnaryOperator.identity());
    }

    @Override
    public Matriz<N> productoTensorial(Matriz<N> multiplo1, Matriz<N> multiplo2) {
        return productoFlexible(multiplo1, UnaryOperator.identity(), multiplo2, ParOrdenado::transpuesta);
    }

    @Override
    public Matriz<N> productoKronecker(Matriz<N> multiplo1, Matriz<N> multiplo2) {
        int pdfila = multiplo2.getDominio().getFila();
        int pdcolumna = multiplo2.getDominio().getColumna();
        int p = multiplo1.getDominio().getFila() * pdfila;
        int q = multiplo1.getDominio().getColumna() * pdcolumna;
        Dominio nuevoDominio = new Dominio(p, q);

        return multiplo1.entrySet().parallelStream()
                .flatMap(global
                        -> productoEscalar(multiplo2, global.getValue())
                        .entrySet().stream()
                        .map(local -> {
                            ParOrdenado gKey = global.getKey();
                            ParOrdenado lKey = local.getKey();
                            return new AbstractMap.SimpleEntry<>(
                                    new Indice(
                                            lKey.getFila() + (gKey.getFila() - 1) * pdfila,
                                            lKey.getColumna() + (gKey.getColumna() - 1) * pdcolumna),
                                    local.getValue());
                        }))
                .collect(
                        toConcurrentMap(
                                Entry::getKey,
                                Entry::getValue,
                                (v, v1) -> v,
                                () -> instanciaMatriz.instancia(nuevoDominio)));
    }

    @Override
    public Matriz<N> productoCruz(Matriz<N> multiplo1, Matriz<N> multiplo2) {
        return producto(skewSymMatrix(multiplo1), multiplo2);
    }

    @Override
    public Matriz<N> productoCruz(Matriz<N>... multiplo) {
        boolean ncols = Arrays.stream(multiplo).map(Matriz::getDominio).map(Dominio::getColumna).allMatch(c -> c == 1);
        if (!ncols) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        boolean nfils = Arrays.stream(multiplo).map(Matriz::getDominio).map(Dominio::getFila).allMatch(f -> f == multiplo.length);
        if (!nfils) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        Dominio dominio = new Dominio(multiplo.length, multiplo.length);
        Dominio dominioSalida = new Dominio(multiplo.length, 1);
        Matriz<N> crossMatriz = IntStream.rangeClosed(1, multiplo.length).parallel()
                .mapToObj(i -> multiplo[i - 1].entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(new Indice(i, e.getKey().getFila()), e.getValue())))
                .flatMap(Function.identity())
                .collect(toConcurrentMap(
                        Entry::getKey,
                        Entry::getValue,
                        (v, v1) -> v,
                        () -> instanciaMatriz.instancia(dominio)));

        Matriz<N> vector = IntStream.rangeClosed(1, multiplo.length).parallel().boxed()
                .collect(toConcurrentMap(
                        i -> new Indice(i, 1),
                        i -> determinante(matrizIesima(crossMatriz, new Indice(multiplo.length, i))),
                        (v1, v2) -> v1,
                        () -> instanciaMatriz.instancia(dominioSalida)));
        crossMatriz.clear();
        return vector;

    }

    @Override
    public Matriz<N> uno(Dominio dominio) {

        return dominio.parallelStream()
                .collect(
                        () -> instanciaMatriz.instancia(dominio),
                        (m, k) -> m.put(k, terminalMath.getUnoValue()),
                        (m1, m2) -> m1.putAll(m2));
    }

    @Override
    public Matriz<N> identidad(Dominio dominio) {
        return identidad(dominio.getFila());
    }

    @Override
    public Matriz<N> identidad(Integer dominio) {

        return IntStream.rangeClosed(1, dominio).parallel()
                .mapToObj(i -> new Indice(i, i))
                .collect(
                        () -> instanciaMatriz.instancia(new Dominio(dominio, dominio)),
                        (m, k) -> m.put(k, terminalMath.getUnoValue()),
                        (m1, m2) -> m1.putAll(m2));
    }

    private Matriz<N> productoFlexible(Matriz<N> mul1, UnaryOperator<ParOrdenado> conductoPunto, Matriz<N> mul2, UnaryOperator<ParOrdenado> conductoTen) {

        ParOrdenado este = conductoPunto.apply(mul1.getDominio());
        ParOrdenado otro = conductoTen.apply(mul2.getDominio());
        if (este.getColumna() != otro.getFila().intValue()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        int n = este.getFila();
        int m = este.getColumna();
        int p = otro.getColumna();

        Dominio resultante = new Dominio(n, p);
        return IntStream.rangeClosed(1, m).parallel()
                .mapToObj((int k) -> {
                    Stream<AbstractMap.SimpleEntry<ParOrdenado, N>> tes = resultante.stream().map(index -> new AbstractMap.SimpleEntry<>(index, conductoPunto.andThen(i -> mul1.getOrDefault(i, terminalMath.getCeroValue())).apply(new Indice(index.getFila(), k))));
                    Stream<AbstractMap.SimpleEntry<ParOrdenado, N>> pes = resultante.stream().map(index -> new AbstractMap.SimpleEntry<>(index, conductoTen.andThen(i -> mul2.getOrDefault(i, terminalMath.getCeroValue())).apply(new Indice(k, index.getColumna()))));
                    return Stream.concat(tes, pes).map(e
                            -> new AbstractMap.SimpleEntry<>(
                                    new Indice(
                                            (k - 1) * n + (e.getKey().getFila() - 1),
                                            (k - 1) * p + (e.getKey().getColumna() - 1)),
                                    e.getValue()));
                })
                .flatMap(Function.identity())
                .collect(
                        collectingAndThen(
                                toConcurrentMap(
                                        Entry::getKey,
                                        Entry::getValue,
                                        terminalMath::productoDirecto),
                                pm -> pm.entrySet().parallelStream())
                )
                .collect(
                        toConcurrentMap(
                                e -> new Indice(
                                        (e.getKey().getFila() % n) + 1,
                                        (e.getKey().getColumna() % p) + 1),
                                Entry::getValue,
                                terminalMath::sumaDirecta,
                                () -> instanciaMatriz.instancia(resultante)
                        )
                );
    }

}
