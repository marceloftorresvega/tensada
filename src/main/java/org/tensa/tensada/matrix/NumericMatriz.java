package org.tensa.tensada.matrix;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author mtorres
 * @param <N>
 */
public abstract class NumericMatriz<N extends Number> extends Matriz<N> {

    public NumericMatriz(Dominio dominio) {
        super(dominio);
    }

    public NumericMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends N> m) {
        super(dominio, m);
    }

    @Override
    public NumericMatriz<N> indexa(Integer fila, Integer columna, N valor) {
        this.put(new Indice(fila, columna), valor);
        return this;
    }

    public abstract N getCeroValue();

    public abstract N getUnoValue();

    public abstract N sumaDirecta(N sum1, N sum2);

    public abstract N inversoAditivo(N sum1);

    public abstract N cos(Double sum1);

    public abstract N sin(Double sum1);

    public abstract N productoDirecto(N prod1, N prod2);
    
    public abstract N inversoMultiplicativo(N prod);

    public abstract NumericMatriz<N> instancia(Dominio dominio);

    public abstract NumericMatriz<N> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends N> m);

    @Override
    public N get(Object key) {
        return super.getOrDefault(key, this.getCeroValue());

    }

    @Override
    public N put(ParOrdenado key, N value) {
        if (value.equals(this.getCeroValue())) {
            return super.remove(key);
        } else {
            return super.put(key, value);

        }
    }

    public NumericMatriz<N> productoEscalar(N escala) {
        return this.entrySet().stream()
            .collect(Collectors.toMap(
                            e -> e.getKey(),
                            e -> productoDirecto(e.getValue(), escala),
                            (v1,v2) -> v1,
                            () -> instancia(this.dominio)
                    ));

    }

    public NumericMatriz<N> adicion(NumericMatriz<N> sumando) {        
        if( !this.dominio.equals(sumando.dominio)){
            throw new IllegalArgumentException("matrices no compatibles");
        }

        return this.dominio.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        index -> sumaDirecta(this.get(index), sumando.get(index)),
                        (v1,v2) -> v1,
                        () -> instancia(this.dominio)));

    }

    public NumericMatriz<N> substraccion(NumericMatriz<N> sumando) {        
        if( !this.dominio.equals(sumando.dominio)){
            throw new IllegalArgumentException("matrices no compatibles");
        }

        return this.dominio.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        index -> sumaDirecta(this.get(index), inversoAditivo(sumando.get(index))),
                        (v1,v2) -> v1,
                        () -> instancia(this.dominio)));
    }

    public NumericMatriz<N> producto(NumericMatriz<N> parte) {

        if (this.dominio.getColumna().intValue() != parte.dominio.getFila().intValue()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        int n = this.dominio.getFila();
        int m = this.dominio.getColumna();
        int p = parte.dominio.getColumna();

        Dominio resultante = new Dominio(n, p);
        return resultante.stream()
            .collect(Collectors.toMap(
                        Function.identity(),
                        index-> IntStream.rangeClosed(1, m)
                            .mapToObj(k -> {

                                Indice ik = new Indice(index.getFila(), k);
                                Indice kj = new Indice(k, index.getColumna());

                                return productoDirecto(
                                        this.get(ik),
                                        parte.get(kj));

                            })
                            .reduce(this.getCeroValue(),
                                    (v1, v2) -> this.sumaDirecta(v1, v2)),
                        (v1,v2) -> v1,
                        () -> instancia(resultante)
                    ));

    }
    public NumericMatriz<N> productoKronecker(NumericMatriz<N> prod){
        int pdfila = prod.dominio.getFila();
        int pdcolumna = prod.dominio.getColumna();
        int p = this.dominio.getFila() * pdfila;
        int q = this.dominio.getColumna() * pdcolumna;
        Dominio nuevoDominio = new Dominio(p, q);
        
        return this.entrySet().stream()
                .map(global -> prod.productoEscalar(global.getValue()).entrySet().stream()
                        .collect(Collectors.toMap(local -> {
                                ParOrdenado gKey = global.getKey();
                                ParOrdenado lKey = local.getKey();

                                return new Indice(
                                        lKey.getFila()    + (gKey.getFila()    - 1) * pdfila,
                                        lKey.getColumna() + (gKey.getColumna() - 1) * pdcolumna);
                        }, local -> local.getValue()))
                        
                )
                .reduce(instancia(nuevoDominio), (r,m) -> {
                    r.putAll(m);
                    return r;
                }, (a,b) -> {
                    a.putAll(b);
                    return a;
                });
    }

    public NumericMatriz<N> transpuesta() {

        Dominio resultante = new Dominio(this.dominio.transpuesta());

        return this.entrySet().stream()
                .collect(Collectors.toMap(e ->  e.getKey().transpuesta(),
                                e -> e.getValue(),
                                (v1 , v2) -> v1,
                                () -> instancia(resultante)));

    }

    public NumericMatriz<N> productoPunto(NumericMatriz<N> prod) {

        try(NumericMatriz<N> transpuesta = this.transpuesta()) {
            return transpuesta.producto(prod);
        } catch (IOException ex) {
            throw new RejectedExecutionException("productoPunto", ex);
        }

    }

    public NumericMatriz<N> distanciaE2() {
        return this.productoPunto(this);
    }

    public NumericMatriz<N> productoTensorial(NumericMatriz<N> parte) {
        try (NumericMatriz<N> transpuesta = parte.transpuesta()) {
            return producto(transpuesta );
        } catch (IOException ex) {
            throw new RejectedExecutionException("productoTensorial", ex);
        }

    }

    public NumericMatriz<N> productoCruz(NumericMatriz<N> parte) {

        return this.skewSymMatrix().producto(parte);
    }

    public NumericMatriz<N> skewSymMatrix() {

        if (this.dominio.getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (this.dominio.getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        N ux = this.get(Indice.E1);
        N uy = this.get(Indice.E2);
        N uz = this.get(Indice.E3);

        return instancia(new Dominio(3, 3))
                .indexa(1, 2, inversoAditivo(uz))
                .indexa(1, 3, uy)
                .indexa(2, 1, uz)
                .indexa(2, 3, inversoAditivo(ux))
                .indexa(3, 1, inversoAditivo(uy))
                .indexa(3, 2, ux);
    }

    public NumericMatriz<N> determinante() {
        if (dominio.equals((ParOrdenado)Indice.D1)) {
            return this.instancia(dominio, this);
        }

        Optional<N> det = IntStream.rangeClosed(1, (int) dominio.getColumna())
                .mapToObj(j -> {
                    Indice idx = new Indice(1, j);

                    N valor = this.productoDirecto(this.get(idx), this.matrizIesima(idx).determinante().get(Indice.E1));

                    return ((1 + j) % 2 == 0) ? valor : this.inversoAditivo(valor);

                }).collect(Collectors.reducing(this::sumaDirecta));

        return this.instancia(new Dominio(1, 1)).indexa(1, 1, det.get());

    }

    public NumericMatriz<N> matrizIesima(ParOrdenado i) {

        return this.instancia(
                new Dominio(
                        dominio.getFila() - 1,
                        dominio.getColumna() - 1),
                this.entrySet().stream()
                .filter(e -> !e.getKey().getColumna().equals(i.getColumna()))
                .filter(e -> !e.getKey().getFila().equals(i.getFila()))
                .collect(Collectors.toMap(
                        (Entry<ParOrdenado, N> e) -> {
                            ParOrdenado idx = e.getKey();
                            return new Indice(
                                    idx.getColumna() < i.getColumna() ? idx.getColumna() : idx.getColumna() - 1,
                                    idx.getFila() < i.getFila() ? idx.getFila() : idx.getFila() - 1);
                        }, 
                        Entry::getValue,
                        (v1, v2) -> v1,
                        () -> instancia(new Dominio(
                                dominio.getFila() - 1,
                                dominio.getColumna() - 1)))));

    }

    public NumericMatriz<N> adjunta() {
        if (dominio.equals((ParOrdenado)Indice.D1)) {
            return this.instancia(dominio, this);
        }

        if (dominio.equals((ParOrdenado)Indice.D2)) {
            return this.instancia(dominio)
                    .indexa(1, 1, this.get(Indice.D2))
                    .indexa(2, 1, inversoAditivo(this.get(Indice.E2T)))
                    .indexa(1, 2, inversoAditivo(this.get(Indice.E2)))
                    .indexa(2, 2, this.get(Indice.D1));
        }
        return this.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), (Entry<ParOrdenado, N> e) -> {
                            N valor = this.matrizIesima(e.getKey()).determinante().get(Indice.E1);

                            return (e.getKey().getFila() + e.getKey().getColumna()) % 2 == 0 ? valor : inversoAditivo(valor);
                        },
                        (v1,v2) -> v1,
                        () -> this.instancia(dominio)));
    }
    
    public NumericMatriz<N> cofactores(){
        try (NumericMatriz<N> transpuesta = this.transpuesta()) {
            return transpuesta.adjunta();
        } catch (IOException ex) {
            throw new RejectedExecutionException("cofactores", ex);
        }
    }
    
    public NumericMatriz<N> inversa(){
        N det = this.determinante().get(Indice.D1);
        try (NumericMatriz<N> cofactores = this.cofactores()) {
            return cofactores.productoEscalar( inversoMultiplicativo(det));
        } catch (IOException ex) {
            throw new RejectedExecutionException("inversa", ex);
        }
    }

    public NumericMatriz<N> matrizUno(Dominio dominio1) {

        return dominio1.stream()
                .collect(Collectors.toMap(
                                k -> k,
                                k -> getUnoValue(),
                                (v1,v2) -> getUnoValue(),
                                () -> instancia(dominio1)
                        ));

    }

    public NumericMatriz<N> matrizUno() {

        return matrizUno(dominio);

    }

    public NumericMatriz<N> matrizIdentidad(Dominio dominio) {

        if (dominio.getColumna().intValue() != dominio.getFila().intValue()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        
        return dominio.stream()
                .filter(k -> k.getFila().intValue() == k.getColumna().intValue())
                .collect(Collectors.toMap(
                                k -> k,
                                k -> getUnoValue(),
                                (v1,v2) -> getUnoValue(),
                                () -> instancia(dominio)
                        ));

    }

    public NumericMatriz<N> traza() {

        if (dominio.getColumna().intValue() != dominio.getFila().intValue()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        
        return instancia(new Dominio(1, 1))
                .indexa(1, 1, dominio.stream()
                        .filter(k -> k.getFila().intValue() == k.getColumna().intValue())
                        .reduce(
                                this.getCeroValue(),
                                (v, k) -> this.sumaDirecta(v, this.get(k)),
                                (v1, v2) -> this.sumaDirecta(v1, v2)
                        )
                );

    }

    public NumericMatriz<N> matrizIdentidad() {

        return matrizIdentidad(dominio);

    }

    public NumericMatriz<N> matrizRotacion(NumericMatriz<N> eje, Double angulo) {

        if (eje.dominio.getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        try (
            NumericMatriz<N> ux = eje.skewSymMatrix();
            NumericMatriz<N> uut = eje.productoTensorial(eje);
            NumericMatriz<N> id = ux.matrizIdentidad()) {

            N coT = cos(angulo);
            N siT = sin(angulo);

    //        return ux.productoEscalar(siT)
    //                .adicion(id.substraccion(uut).productoEscalar(coT))
    //                .adicion(uut);
            return id.productoEscalar(coT)
                    .adicion(ux.productoEscalar(siT))
                    .adicion(uut.productoEscalar(sumaDirecta(getUnoValue(), inversoAditivo(coT))));
        } catch (IOException ex) {
           throw new RejectedExecutionException("matrizRotacion", ex);
        }


    }

    public NumericMatriz<N> matrizRotacion(Double angulo) {
        return matrizRotacion(this, angulo);
    }

    @Override
    public String toString() {
        return "DoubleMatriz{" + this.entrySet().stream().map(v -> v.getKey().getFila() + ":" + v.getKey().getColumna() + "/" + v.getValue() + ',').reduce("", (s, v) -> s + v) + '}';
    }
}
