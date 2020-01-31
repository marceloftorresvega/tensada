package org.tensa.tensada.vector;

import org.tensa.tensada.matrixv2.DoubleMatriz;

/**
 *
 * @author mtorres
 */
public interface Double3DVector extends Cloneable {

    Double3DVector add(Double3DVector dp);

    Double3DVector diff(Double3DVector dp);

    double distance(Double3DVector point);

    double distanceSq(Double3DVector point);

    Double3DVector escalar(double escala);

    double getX();

    double getY();

    double getZ();

    void location(double x, double y, double z);

    Double3DVector normal();

    Double3DVector set(Double3DVector dp);

    void setX(double x);

    void setY(double y);

    void setZ(double z);

    DoubleMatriz toMatriz();
    
    Double3DVector newInstanceOf();
    
    Double3DVector newInstanceOf(double x, double y, double z);
    
    Double3DVector newInstanceOf(Double3DVector src);
}
