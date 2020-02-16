package org.tensa.tensada.matrix;

import org.tensa.tensada.vector.Double3DVector;
import org.tensa.tensada.vector.impl.DoubleVector3DImpl;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author mtorres
 */
public class FloatMatriz extends NumericMatriz<Float> {

    public FloatMatriz(Dominio dominio) {
        super(dominio);
    }

    public FloatMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends Float> m) {
        super(dominio, m);
    }

    public FloatMatriz(Matriz<Float> m) {
        super(m.dominio, m);
    }

    public static FloatMatriz fromVector(Double3DVector vector) {

        Dominio dominio = new Dominio(3, 1);

        FloatMatriz matriz = new FloatMatriz(dominio);
        return (FloatMatriz) matriz
                .indexa(1, 1, (float)vector.getX())
                .indexa(2, 1, (float)vector.getY())
                .indexa(3, 1, (float)vector.getZ());
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

    public FloatMatriz productoTensorialOptimizado(FloatMatriz parte) {

        if (this.dominio.getColumna() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (parte.dominio.getFila() != 1) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        int n = this.dominio.getFila();
        int p = parte.dominio.getColumna();

        Dominio resultante = new Dominio(n, p);

        return new FloatMatriz(
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

    public static FloatMatriz matrizRotacionOptimizada(FloatMatriz eje, double angulo) {
        if (eje.dominio.getFila() < 3) {
            throw new IllegalArgumentException("matrices no compatibles");
        }

        Dominio dominio = new Dominio(3, 3);

        FloatMatriz rotador = new FloatMatriz(dominio);
        float coT = (float)Math.cos(angulo);
        float siT = (float)Math.sin(angulo);

        float ux = eje.get(Indice.E1);
        float uy = eje.get(Indice.E2);
        float uz = eje.get(Indice.E3);
        float uxx = ux * ux;
        float uzz = uz * uz;
        float uyy = uy * uy;
        float uxy = ux * uy;
        float uxz = ux * uz;
        float uzy = uz * uy;

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
    public Float getCeroValue() {
        return (float) 0;
    }

    @Override
    public Float sumaDirecta(Float sum1, Float sum2) {
        return sum1 + sum2;
    }

    @Override
    public Float productoDirecto(Float prod1, Float prod2) {
        return prod1 * prod2;
    }

    @Override
    public FloatMatriz instancia(Dominio dominio) {
        return new FloatMatriz(dominio);
    }

    @Override
    public FloatMatriz instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends Float> m) {
        return new FloatMatriz(dominio, m);
    }

    @Override
    public Float getUnoValue() {
        return (float) 1;
    }

    @Override
    public Float inversoAditivo(Float sum1) {
        return -sum1;
    }

    @Override
    public Float inversoMultiplicativo(Float prod) {
        return 1 / prod;
    }

    @Override
    public Float cos(Double angulo) {
        return (float)Math.cos(angulo);
    }

    @Override
    public Float sin(Double angulo) {
        return (float)Math.sin(angulo);
    }

    @Override
    public FloatMatriz matrizRotacion(Double angulo) {
        return (FloatMatriz) super.matrizRotacion(angulo);
    }

    @Override
    public FloatMatriz producto(NumericMatriz<Float> parte) {
        return (FloatMatriz) super.producto(parte);
    }

    @Override
    public Float restaDirecta(Float sum1, Float sum2) {
        return sum1 - sum2;
    }

}
