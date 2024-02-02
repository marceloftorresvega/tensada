package org.tensa.tensada.matrix;

import java.math.MathContext;
import org.tensa.tensada.vector.Double3DVector;
import org.tensa.tensada.vector.impl.DoubleVector3DImpl;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author mtorres
 */
public class DoubleMatriz extends NumericMatriz<Double> {

    public DoubleMatriz(Dominio dominio) {
        super(dominio);
    }

    public DoubleMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends Double> m) {
        super(dominio, m);
    }

    public DoubleMatriz(Matriz<Double> m) {
        super(m.dominio, m);
    }

    public DoubleMatriz() {
        super();
    }

    public static DoubleMatriz fromVector(Double3DVector vector) {

        Dominio dominio = new Dominio(3, 1);

        DoubleMatriz matriz = new DoubleMatriz(dominio);
        return (DoubleMatriz) matriz
                .indexa(1, 1, vector.getX())
                .indexa(2, 1, vector.getY())
                .indexa(3, 1, vector.getZ());
    }

    public DoubleVector3DImpl toVector() {
        if (this.dominio.getFila() != 3 || this.dominio.getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        return new DoubleVector3DImpl(
                this.get(Indice.E1),
                this.get(Indice.E2),
                this.get(Indice.E3));
    }
    
    public Double3DVector toVector(Double3DVector vector){
        if (this.dominio.getFila() != 3 || this.dominio.getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        
        if(Objects.isNull(vector)){
            return toVector();
        }
        
        vector.location(
            this.get(Indice.E1),
            this.get(Indice.E2),
            this.get(Indice.E3));
        return vector;
    }

    public DoubleMatriz productoTensorialOptimizado(DoubleMatriz parte) {

        if (this.dominio.getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (parte.dominio.getFila() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        int n = this.dominio.getFila();
        int p = parte.dominio.getColumna();

        Dominio resultante = new Dominio(n, p);

        return new DoubleMatriz(
                resultante,
                resultante.stream()
                .collect(Collectors.toMap(
                                index -> index,
                                index
                                -> {

                                    Indice ik = new Indice(index.getFila(), 1);
                                    Indice kj = new Indice(1, index.getColumna());

                                    return this.get(ik)
                                    * parte.get(kj);

                                }
                        ))
        );
    }

    public static DoubleMatriz matrizRotacionOptimizada(DoubleMatriz eje, double angulo) {
        if (eje.dominio.getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        Dominio dominio = new Dominio(3, 3);

        DoubleMatriz rotador = new DoubleMatriz(dominio);
        double coT = Math.cos(angulo);
        double siT = Math.sin(angulo);

        double ux = eje.get(Indice.E1);
        double uy = eje.get(Indice.E2);
        double uz = eje.get(Indice.E3);
        double uxx = ux * ux;
        double uzz = uz * uz;
        double uyy = uy * uy;
        double uxy = ux * uy;
        double uxz = ux * uz;
        double uzy = uz * uy;

        rotador
                .indexa(1, 1,
                        coT + uxx * (1 - coT))
                .indexa(1, 2,
                        uxy * (1 - coT) - uz * siT)
                .indexa(1, 3,
                        uxz * (1 - coT) + uy * siT)
                .indexa(2, 1,
                        uxy * (1 - coT) + uz * siT)
                .indexa(2, 2,
                        coT + uyy * (1 - coT))
                .indexa(2, 3,
                        uzy * (1 - coT) - ux * siT)
                .indexa(3, 1,
                        uxz * (1 - coT) - uy * siT)
                .indexa(3, 2,
                        uzy * (1 - coT) + ux * siT)
                .indexa(3, 3,
                        coT + uzz * (1 - coT));

        return rotador;
    }

    @Override
    public Double mapper(double value) {
        return value;
    }

    @Override
    public Double getCeroValue() {
        return (double) 0;
    }

    @Override
    public Double sumaDirecta(Double sum1, Double sum2) {
        return sum1 + sum2;
    }

    @Override
    public Double productoDirecto(Double prod1, Double prod2) {
        return prod1 * prod2;
    }

    @Override
    public DoubleMatriz instancia(Dominio dominio) {
        return new DoubleMatriz(dominio);
    }

    @Override
    public DoubleMatriz instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends Double> m) {
        return new DoubleMatriz(dominio, m);
    }

    @Override
    public Double getUnoValue() {
        return (double) 1;
    }

    @Override
    public Double inversoAditivo(Double sum1) {
        return -sum1;
    }

    @Override
    public Double inversoMultiplicativo(Double prod) {
        return 1 / prod;
    }

    @Override
    public Double cos(Double ang) {
        return Math.cos(ang);
    }

    @Override
    public Double sin(Double ang) {
        return Math.sin(ang);
    }

    @Override
    public Double tan(Double ang) {
        return Math.tan(ang);
    }

    @Override
    public Double sec(Double ang) {
        return 1.0 / Math.cos(ang);
    }

    @Override
    public Double csc(Double ang) {
        return 1.0 / Math.sin(ang);
    }

    @Override
    public Double arcsen(Double x) {
        return Math.asin(x);
    }

    @Override
    public Double arccos(Double x) {
        return Math.acos(x);
    }

    @Override
    public Double arctan(Double x) {
        return Math.atan(x);
    }

    @Override
    public Double senh(Double x) {
        return Math.sinh(x);
    }

    @Override
    public Double cosh(Double x) {
        return Math.cosh(x);
    }

    @Override
    public Double tanh(Double x) {
        return Math.tanh(x);
    }

    @Override
    public Double arcsenh(Double x) {
        if (x >= 1.0) {
            throw new IllegalArgumentException();
        }
        double acum = 0 ;
        for (int n=0 ; n< 6; n++) {
            double v2np1 = 2*n +1;
            double fn = fact(2*n);
            Double vfnp2 = pow((double)fact(n),2.);
            double content = fn / pow(4.,(double)n) / vfnp2 / v2np1 * pow(x,v2np1);
            if (potMenos1(n)>0) {
                acum+= content;
            } else {
                acum-= content;
            }
        }
        return acum;
    }

    @Override
    public Double arccosh(Double x) {
        double acum = 0 ;
        for (int n=1 ; n< 6; n++) {
            int v2n = 2*n;
            double fac2n = fact(v2n);
            double facn = fact(n);
            
            acum += fac2n / pow(2.,(double)v2n) / pow(facn,2.) * pow(x,(double)-v2n)/(double)v2n;
        }
        
        return ln(2*x) - acum;
        
    }

    @Override
    public Double arctanh(Double x) {
        double acum = 0;
        for (int n = 0; n < 6; n++) {
            double v2nm1 = 2*n +1;
            acum += pow(x,v2nm1) / v2nm1;
        }
        return acum;
    }

    @Override
    public Double exp(Double x) {
        return Math.exp(x);
    }

    @Override
    public Double ln(Double x) {
        return Math.log(x);
    }

    @Override
    public Double abs(Double x) {
        return Math.abs(x);
    }

    @Override
    public Double pow(Double a, Double x) {
        return Math.pow(a, x);
    }

    @Override
    public DoubleMatriz matrizRotacion(Double angulo) {
        return (DoubleMatriz) super.matrizRotacion(angulo);
    }

    @Override
    public DoubleMatriz producto(NumericMatriz<Double> parte) {
        return (DoubleMatriz) super.producto(parte);
    }

    @Override
    public Double restaDirecta(Double sum1, Double sum2) {
        return sum1 - sum2;
    }

}
