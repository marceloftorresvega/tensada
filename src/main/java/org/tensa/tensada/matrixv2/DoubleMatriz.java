package org.tensa.tensada.matrixv2;

import java.util.Map;
import org.tensa.tensada.vector.Double3DVector;
import org.tensa.tensada.vector.impl.DoubleVector3DImpl;
import java.util.Objects;
import java.util.stream.Collectors;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author mtorres
 */
public class DoubleMatriz extends NumericMatriz<Double> {

    public DoubleMatriz(Matriz<Double> origen) {
        super(origen);
    }

    
    public DoubleMatriz fromVector(Double3DVector vector) {

        Dominio dominio = new Dominio(3, 1);

        DoubleMatriz matriz = (DoubleMatriz)instancia(dominio);
        return (DoubleMatriz) matriz
                .indexa(1, 1, vector.getX())
                .indexa(2, 1, vector.getY())
                .indexa(3, 1, vector.getZ());
    }

    public DoubleVector3DImpl toVector() {
        if (this.getDominio().getFila() != 3 || this.getDominio().getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        return new DoubleVector3DImpl(
                this.get(Indice.E1),
                this.get(Indice.E2),
                this.get(Indice.E3));
    }
    
    public Double3DVector toVector(Double3DVector vector){
        if (this.getDominio().getFila() != 3 || this.getDominio().getColumna() != 1) {
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

        if (this.getDominio().getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (parte.getDominio().getFila() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        int n = this.getDominio().getFila();
        int p = parte.getDominio().getColumna();

        Dominio resultante = new Dominio(n, p);

        return (DoubleMatriz)instancia(
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

    public DoubleMatriz matrizRotacionOptimizada(DoubleMatriz eje, double angulo) {
        if (eje.getDominio().getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        Dominio dominio = new Dominio(3, 3);

        DoubleMatriz rotador = (DoubleMatriz)instancia(getDominio());
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
    public Double cos(Double sum1) {
        return Math.cos(sum1);
    }

    @Override
    public Double sin(Double sum1) {
        return Math.sin(sum1);
    }

    @Override
    public NumericMatriz<Double> instancia(Dominio dominio) {
        return new DoubleMatriz(this.getOrigen().instancia(dominio));
    }

    @Override
    public NumericMatriz<Double> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends Double> m) {
        return new DoubleMatriz(this.getOrigen().instancia(dominio, m));
    }

    @Override
    public NumericMatriz<Double> instancia(Matriz<Double> m) {
        return new DoubleMatriz(this.getOrigen().instancia(m));
    }

}
