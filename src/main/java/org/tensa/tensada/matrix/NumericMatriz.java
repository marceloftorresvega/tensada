package org.tensa.tensada.matrix;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.function.UnaryOperator;
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

    private final static long[] hardFact =   {1,    1,   2, 6,    24, 120,  720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    private final static long[] hardEule =   {1,    0,  -1, 0,     5,   0,  -61,    0,  1385,      0   -50521,        0,  2702765,            0,   -199360981,              0,    19391512145L,                0,   -2404879675441L,                   0,     370371188237525L, 0, -69348874393137901L, 0};
    private final static double[] hardBern = {1, -1/2, 1/6, 0, -1/30,   0, 1/42,    0, -1/30,      0,    5/66,        0, -691/2730,           0,          7/6,              0,       -3617/510,                0,        433867/798,                   0,          -174611/330};
    public NumericMatriz(Dominio dominio) {
        super(dominio);
    }

    public NumericMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends N> m) {
        super(dominio, m);
    }

    public NumericMatriz() {
        super();
    }

    @Override
    public NumericMatriz<N> indexa(Integer fila, Integer columna, N valor) {
        this.put(new Indice(fila, columna), valor);
        return this;
    }
    
    public abstract N mapper(double value);

    public abstract N getCeroValue();

    public abstract N getUnoValue();

    public abstract N suma(N sum1, N sum2);

    public abstract N resta(N sum1, N sum2);

    public abstract N inversoAditivo(N sum1);

    public abstract N cos(N ang);

    public abstract N sen(N ang);

    public abstract N tan(N ang);

    public abstract N sec(N ang);

    public abstract N csc(N ang);

    public abstract N arcsen(N x);

    public abstract N arccos(N x);

    public abstract N arctan(N x);

    public abstract N senh(N x);

    public abstract N cosh(N x);

    public abstract N tanh(N x);

    public abstract N arcsenh(N x);

    public abstract N arccosh(N x);

    public abstract N arctanh(N x);

    public abstract N exp(N x);

    public abstract N ln(N x);
    
    public abstract N abs(N x);
    
    public abstract N pow(N a, N x);

    public abstract N multiplica(N prod1, N prod2);

    public abstract N divide(N numerador, N denominador);
    
    public abstract N inversoMultiplicativo(N prod);

    public abstract NumericMatriz<N> instancia(Dominio dominio);

    public abstract NumericMatriz<N> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends N> m);
    
    public long fact(int n) {
        return hardFact[n];
    }
    
    public long euler(int n) {
        return hardEule[n];
    }
    
    public double bern(int n) {
        return hardBern[n];
    }
    
    public int potMenos1(int n ) {
        return ( (n & 1) == 0 )? 1 : -1;
    }

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
            .collect(Collectors.toMap(e -> e.getKey(),
                            e -> multiplica(e.getValue(), escala),
                            (v1,v2) -> v1,
                            () -> instancia(this.dominio)
                    ));

    }

    public NumericMatriz<N> adicion(NumericMatriz<N> sumando) {        
        if( !this.dominio.equals(sumando.dominio)){
            throw new IllegalArgumentException("matrices no compatibles");
        }

        return this.dominio.stream()
                .collect(Collectors.toMap(Function.identity(),
                        index -> suma(this.get(index), sumando.get(index)),
                        (v1,v2) -> v1,
                        () -> instancia(this.dominio)));

    }

    public NumericMatriz<N> substraccion(NumericMatriz<N> sumando) {        
        if( !this.dominio.equals(sumando.dominio)){
            throw new IllegalArgumentException("matrices no compatibles");
        }

        return this.dominio.stream()
                .collect(Collectors.toMap(Function.identity(),
                        index -> resta(this.get(index), sumando.get(index)),
                        (v1,v2) -> v1,
                        () -> instancia(this.dominio)));
    }

    public NumericMatriz<N> producto(NumericMatriz<N> parte) {

        return productoFlexible(parte,UnaryOperator.identity(),UnaryOperator.identity());

    }
    
    private NumericMatriz<N> productoFlexible(NumericMatriz<N> parte, UnaryOperator<ParOrdenado> punto, UnaryOperator<ParOrdenado> tenso) {

        ParOrdenado este = punto.apply(dominio);
        ParOrdenado otro = tenso.apply(parte.dominio);
        if (este.getColumna() != otro.getFila().intValue()) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        int n = este.getFila();
        int m = este.getColumna();
        int p = otro.getColumna();

        Dominio resultante = new Dominio(n, p);
        return resultante.stream()
            .collect(Collectors.toMap(Function.identity(),
                        index-> IntStream.rangeClosed(1, m)
                            .mapToObj(k -> {

                                Indice ik = new Indice(index.getFila(), k);
                                Indice kj = new Indice(k, index.getColumna());

                                return multiplica(
                                        punto.andThen(this::get).apply(ik),
                                        tenso.andThen(parte::get).apply(kj));

                            })
                            .reduce(this.getCeroValue(),
                                    this::suma),
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

        return this.productoFlexible(prod,ParOrdenado::transpuesta, UnaryOperator.identity());
    }

    public NumericMatriz<N> distanciaE2() {
        return this.productoPunto(this);
    }

    public NumericMatriz<N> productoTensorial(NumericMatriz<N> parte) {
        
        return this.productoFlexible(parte, UnaryOperator.identity(), ParOrdenado::transpuesta);
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

                    N valor = this.multiplica(this.get(idx), this.matrizIesima(idx).determinante().get(Indice.E1));

                    return ((1 + j) % 2 == 0) ? valor : this.inversoAditivo(valor);

                }).collect(Collectors.reducing(this::suma));

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
                        .reduce(this.getCeroValue(),
                                (v, k) -> this.suma(v, this.get(k)),
                                (v1, v2) -> this.suma(v1, v2)
                        )
                );

    }

    public NumericMatriz<N> norma() {
        NumericMatriz<N> retorno = this.instancia(new Dominio(Indice.D1));
        N norma = this.values().stream()
                .map(a -> this.multiplica(a, a))
                .reduce(this::suma)
                .map(N::doubleValue)
                .map(Math::sqrt)
                .map(this::mapper)
                .orElseGet(this::getCeroValue);
        retorno.put(Indice.D1, norma);
        return retorno;
    }
    
    public NumericMatriz<N> matrizIdentidad() {

        return matrizIdentidad(dominio);

    }

    public NumericMatriz<N> matrizRotacion(NumericMatriz<N> eje, N angulo) {

        if (eje.dominio.getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        N coT = cos(angulo);
        N siT = sen(angulo);

        try (
            NumericMatriz<N> ux = eje.skewSymMatrix();
            NumericMatriz<N> uut = eje.productoTensorial(eje);
            NumericMatriz<N> id = ux.matrizIdentidad();
            NumericMatriz<N> idCot = id.productoEscalar(coT);
            NumericMatriz<N> uxSit = ux.productoEscalar(siT);
            NumericMatriz<N> uut1noCot = uut.productoEscalar(resta(getUnoValue(), coT));
            NumericMatriz<N> adIdCotUxSit =idCot.adicion(uxSit);) {

//    //        return ux.productoEscalar(siT)
//    //                .adicion(id.substraccion(uut).productoEscalar(coT))
//    //                .adicion(uut);
//            return id.productoEscalar(coT)
//                    .adicion(ux.productoEscalar(siT))
//                    .adicion(uut.productoEscalar(restaDirecta(getUnoValue(), coT)));
                return adIdCotUxSit.adicion(uut1noCot);
                
        } catch (IOException ex) {
           throw new RejectedExecutionException("matrizRotacion", ex);
        }


    }

    public NumericMatriz<N> matrizRotacion(N angulo) {
        return matrizRotacion(this, angulo);
    }

    @Override
    public String toString() {
        return "DoubleMatriz{" + this.entrySet().stream().map(v -> v.getKey().getFila() + ":" + v.getKey().getColumna() + "/" + v.getValue() + ',').reduce("", (s, v) -> s + v) + '}';
    }
}
