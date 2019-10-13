package cl.tensa.vector;

/**
 *
 * @author mtorres
 */
public abstract class AbstractFast3DVector extends AbstractBaseDouble3DVector {
    
    private double x;
    private double y;
    private double z;

    public AbstractFast3DVector() {
        super();
    }

    public AbstractFast3DVector(double x, double y, double z) {
        super(x, y, z);
    }

    public AbstractFast3DVector(Double3DVector src) {
        super(src);
    }
    
    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



}
