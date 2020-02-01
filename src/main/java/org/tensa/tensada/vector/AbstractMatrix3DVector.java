package org.tensa.tensada.vector;

import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.Indice;
import java.util.Objects;
import org.tensa.tensada.matrixv2.*;


public abstract class AbstractMatrix3DVector extends AbstractBaseDouble3DVector {
    
    private DoubleMatriz data;

    public AbstractMatrix3DVector(DoubleMatriz data) {
        this.data = Objects.requireNonNull(data);
    }

    public AbstractMatrix3DVector() {
        Matriz<Double> tmp = (Matriz) new HashMatriz<>(new Dominio(3, 1));
        this.data = (DoubleMatriz)tmp;
    }

    public AbstractMatrix3DVector(double x, double y, double z) {
        Matriz<Double> tmp = (Matriz) new HashMatriz<>(new Dominio(3, 1));
        this.data = (DoubleMatriz)tmp;
        this.location(x, y, z);
    }

    public AbstractMatrix3DVector(Double3DVector src) {
        Matriz<Double> tmp = (Matriz) new HashMatriz<>(new Dominio(3, 1));
        this.data = (DoubleMatriz)tmp;;
        this.set(src);
    }

    @Override
    public double getX() {
        return data.get(Indice.E1);
    }

    @Override
    public void setX(double x) {
        this.data.put(Indice.E1, x);
    }

    @Override
    public double getY() {
        return data.get(Indice.E2);
    }

    @Override
    public void setY(double y) {
        this.data.put(Indice.E1, y);
    }

    @Override
    public double getZ() {
        return data.get(Indice.E3);
    }

    @Override
    public void setZ(double z) {
        this.data.put(Indice.E1, z);
    }

    @Override
    public void location(double x, double y, double z) {
        this.data
                .indexa(1, 1, x)
                .indexa(2, 1, y)
                .indexa(3, 1, z);
    }


    public DoubleMatriz getData() {
        return data;
    }

    public void setData(DoubleMatriz data) {
        this.data = Objects.requireNonNull(data);
    }

    @Override
    public Double3DVector add(Double3DVector dp) {
        
        return ((DoubleMatriz)this.data.adicion(dp.toMatriz())).toVector(this.newInstanceOf());
    }

    @Override
    public Double3DVector diff(Double3DVector dp) {
        return ((DoubleMatriz)this.data.substraccion(dp.toMatriz())).toVector(this.newInstanceOf());
        
    }

    @Override
    public Double3DVector escalar(double escala) {
        return ((DoubleMatriz)this.data.productoEscalar(escala)).toVector(this.newInstanceOf());
    }

    @Override
    public double distanceSq(Double3DVector point) {
        return this.data.substraccion(point.toMatriz()).distanciaE2().get(Indice.E1);
    }

    @Override
    public DoubleMatriz toMatriz() {
        return data; 
    }
    
}
