package cl.tensa.vector.impl;

import cl.tensa.vector.AbstractFast3DVector;
import cl.tensa.vector.Double3DVector;

/**
 *
 * @author mtorres
 */
public final class DoubleVector3DImpl extends AbstractFast3DVector{
    
    public static final DoubleVector3DImpl E1 = new DoubleVector3DImpl(1, 0, 0);
    public static final DoubleVector3DImpl E2 = new DoubleVector3DImpl(0, 1, 0);
    public static final DoubleVector3DImpl E3 = new DoubleVector3DImpl(0, 0, 1);

    public DoubleVector3DImpl() {
    }

    public DoubleVector3DImpl(double x, double y, double z) {
        super(x, y, z);
    }

    
    public DoubleVector3DImpl(Double3DVector src){
        super(src);
    }

    @Override
    public Double3DVector newInstanceOf(double x, double y, double z) {
        return new DoubleVector3DImpl(x, y, z);
    }

    @Override
    public Double3DVector newInstanceOf() {
        return new DoubleVector3DImpl();
    }

    @Override
    public Double3DVector newInstanceOf(Double3DVector src) {
        return new DoubleVector3DImpl(src);
    }

    
}
