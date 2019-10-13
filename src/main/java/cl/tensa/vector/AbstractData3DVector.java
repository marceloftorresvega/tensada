package cl.tensa.vector;

import java.util.Objects;

/**
 *
 * @author mtorres
 */
public abstract class AbstractData3DVector extends AbstractBaseDouble3DVector {
    
    private Data3D data;

    public AbstractData3DVector() {
        this.data = new Data3D();
    }

    public AbstractData3DVector(Data3D data) {
        this.data = Objects.requireNonNull(data);
    }
    public AbstractData3DVector(double x, double y, double z) {
        this.data = new Data3D(x, y, z);
    }

    public AbstractData3DVector(Double3DVector src) {
        this.data = new Data3D();
        this.set(src);
    }

    public Data3D getData() {
        return data;
    }

    public void setData(Data3D data) {
        this.data = Objects.requireNonNull(data);
    }

    @Override
    public double getX() {
        return this.data.x;
    }

    @Override
    public void setX(double x) {
        this.data.x = x;
    }

    @Override
    public double getY() {
        return this.data.y;
    }

    @Override
    public void setY(double y) {
        this.data.y = y;
    }

    @Override
    public double getZ() {
        return this.data.z;
    }

    @Override
    public void setZ(double z) {
        this.data.z = z;
    }

    @Override
    public void location(double x, double y, double z) {
        this.data.x = x;
        this.data.y = y;
        this.data.z = z;
    }


    
}
