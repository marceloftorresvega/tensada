package cl.tensa.vector;

import cl.tensa.matrix.DoubleMatriz;
import java.util.Arrays;

/**
 *
 * @author mtorres
 */
public abstract class AbstractBaseDouble3DVector implements Double3DVector {
    
    public static double distanceSq(Double3DVector sp, Double3DVector dp) {
        double dx = sp.getX() - dp.getX();
        double dy = sp.getY() - dp.getY();
        double dz = sp.getZ() - dp.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    public static double distance(Double3DVector sp, Double3DVector dp) {
        return Math.sqrt(distanceSq(sp, dp));
    }

    public static AbstractBaseDouble3DVector diff(Double3DVector sp, Double3DVector dp) {
        double dx = sp.getX() - dp.getX();
        double dy = sp.getY() - dp.getY();
        double dz = sp.getZ() - dp.getZ();
        return (AbstractBaseDouble3DVector) sp.newInstanceOf(dx, dy, dz);
    }

    public static AbstractBaseDouble3DVector add(Double3DVector sp, Double3DVector dp) {
        double dx = sp.getX() + dp.getX();
        double dy = sp.getY() + dp.getY();
        double dz = sp.getZ() + dp.getZ();
        
        return (AbstractBaseDouble3DVector) sp.newInstanceOf(dx, dy, dz);
    }

    public static AbstractBaseDouble3DVector normal(Double3DVector sp) {
        double radio = sp.distance(sp.newInstanceOf(0, 0, 0));
        return (AbstractBaseDouble3DVector) sp.newInstanceOf(sp.getX() / radio, sp.getY() / radio, sp.getZ() / radio);
    }

    public static AbstractBaseDouble3DVector escalar(Double3DVector sp, double escala) {
        return (AbstractBaseDouble3DVector) sp.newInstanceOf(sp.getX() * escala, sp.getY() * escala, sp.getZ() * escala);
    }

    public AbstractBaseDouble3DVector() {
    }

    public AbstractBaseDouble3DVector(Double3DVector src) {
        this.set(src);
    }

    public AbstractBaseDouble3DVector(double x, double y, double z) {
        this.location(x, y, z);
    }
    
    
    @Override
    public double distanceSq(Double3DVector point) {
        return distanceSq(this, point);
    }

    @Override
    public double distance(Double3DVector point) {
        return distance(this, point);
    }


    @Override
    public Double3DVector diff(Double3DVector dp) {
        return diff(this, dp);
    }

    @Override
    public Double3DVector add(Double3DVector dp) {
        return add(this, dp);
    }

    @Override
    public Double3DVector normal() {
        return normal(this);
    }

    @Override
    public Double3DVector escalar(double escala) {
        return escalar(this, escala);
    }

    @Override
    public DoubleMatriz toMatriz() {
        return DoubleMatriz.fromVector(this);
    }
    
    @Override
    public Double3DVector set(Double3DVector dp) {
        this.location(dp.getX(), dp.getY(), dp.getZ());
        return this;
    }

    @Override
    public String toString() {
        return "Point3D{" + "x=" + getX() + ", y=" + getY() + ", z=" + getZ() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        
        if(this != obj ){
            
            if(!(obj instanceof Double3DVector) ){
                return false;
            } else {
                Double3DVector double3DVector = (Double3DVector) obj;
                return this.getX() == double3DVector.getX() 
                        && this.getY() == double3DVector.getY()
                        && this.getZ() == double3DVector.getZ();
            }
        } else {
            return true;
        }
        
        
        
    }

    @Override
    public int hashCode() {
        double[] dcarray = new double[3];
        dcarray[0] =this.getX();
        dcarray[1] =this.getY();
        dcarray[2] =this.getZ();
        return 67 + Arrays.hashCode(dcarray);
                
    }
    
}
