/*
 * The MIT License
 *
 * Copyright 2020 Marcelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tensa.tensada.matrix;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;


public class BigDecimalMatriz extends NumericMatriz<BigDecimal> {

    public BigDecimalMatriz(Dominio dominio) {
        super(dominio);
    }

    public BigDecimalMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends BigDecimal> m) {
        super(dominio, m);
    }

    public BigDecimalMatriz() {
        super();
    }

    @Override
    public BigDecimal mapper(double value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigDecimal getCeroValue() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getUnoValue() {
        return BigDecimal.ONE;
    }

    @Override
    public BigDecimal sumaDirecta(BigDecimal sum1, BigDecimal sum2) {
        return sum1.add(sum2);
    }

    @Override
    public BigDecimal inversoAditivo(BigDecimal sum1) {
        return sum1.negate();
    }

    @Override
    public BigDecimal cos(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2n = 2*n;
            BigDecimal content = ang.pow(v2n).divide(BigDecimal.valueOf(fact(v2n)));
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal sin(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2np1 = 2*n+1;
            BigDecimal content = ang.pow(v2np1).divide(BigDecimal.valueOf(fact(v2np1)));
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal tan(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v4 = BigDecimal.valueOf(4);
        BigDecimal v1 = BigDecimal.ONE;
        for (int n =1; n<10; n++) {
            int v2n = 2*n;
            BigDecimal b2n = BigDecimal.valueOf(bern(v2n));
            BigDecimal f2n = BigDecimal.valueOf(fact(v2n));
            BigDecimal content = b2n.multiply(v4.negate().pow(n)).multiply(v1.subtract(v4.pow(n))).divide(f2n).multiply(ang.pow(v2n-1));
            acum = acum.add(content);
        }
        return acum;
        
    }

    @Override
    public BigDecimal sec(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2n = 2*n;
            BigDecimal veu2n = BigDecimal.valueOf(euler(v2n));
            BigDecimal content = ang.pow(v2n).divide(BigDecimal.valueOf(fact(v2n))).multiply(veu2n);
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal csc(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v2 = BigDecimal.valueOf(2);
        BigDecimal v1 = BigDecimal.ONE;
        for (int n =1; n<10; n++) {
            int v2n = 2*n;
            int v2nm1 = 2*n -1;
            BigDecimal b2n = BigDecimal.valueOf(bern(v2n));
            BigDecimal f2n = BigDecimal.valueOf(fact(v2n));
            BigDecimal content = v1.negate().pow(n-1).multiply(v2.pow(v2nm1).subtract(v1)).multiply(b2n)
                    .divide(f2n).multiply(ang.pow(v2nm1));
            acum = acum.add(content);
        }
        return acum.multiply(v2);
    }

    @Override
    public BigDecimal arcsen(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v4 = BigDecimal.valueOf(4);
        for (int n =0; n<10; n++) {
            int v2np1 = 2*n+1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal fn = BigDecimal.valueOf(fact(n));
            BigDecimal f2n = BigDecimal.valueOf(fact(2*n));
            BigDecimal content = f2n.divide(v4.pow(n).multiply(fn.pow(2)).multiply(v2np1Bd)).multiply(x.pow(v2np1));
            acum = acum.add(content);
        }
        return acum;
    }

    @Override
    public BigDecimal arccos(BigDecimal x) {
        BigDecimal pi = BigDecimal.valueOf(Math.PI);
        BigDecimal v2 = BigDecimal.valueOf(2);
        return pi.divide(v2).subtract(arcsen(x));
    }

    @Override
    public BigDecimal arctan(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2np1 = 2*n +1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal content = x.pow(v2np1).divide(v2np1Bd);
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal senh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2np1 = 2*n +1;
            BigDecimal f2np1 = BigDecimal.valueOf(fact(v2np1));
            acum = acum.add(x.pow(v2np1).divide(f2np1));
        }
        return acum;
    }

    @Override
    public BigDecimal cosh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2n = 2*n;
            BigDecimal f2n = BigDecimal.valueOf(fact(v2n));
            acum = acum.add(x.pow(v2n).divide(f2n));
        }
        return acum;
    }

    @Override
    public BigDecimal tanh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v4 = BigDecimal.valueOf(4);
        BigDecimal v1 = BigDecimal.ONE;
        for (int n =1; n<10; n++) {
            int v2n = 2*n;
            BigDecimal b2n = BigDecimal.valueOf(bern(v2n));
            BigDecimal f2n = BigDecimal.valueOf(fact(v2n));
            BigDecimal content = b2n.multiply(v4.pow(n)).multiply(v4.pow(n).subtract(v1)).divide(f2n).multiply(x.pow(v2n-1));
            acum = acum.add(content);
        }
        return acum;
    }

    @Override
    public BigDecimal arcsenh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v4 = BigDecimal.valueOf(4);
        for (int n =0; n<10; n++) {
            int v2np1 = 2*n+1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal fn = BigDecimal.valueOf(fact(n));
            BigDecimal f2n = BigDecimal.valueOf(fact(2*n));
            BigDecimal content = f2n.divide(v4.pow(n).multiply(fn.pow(2)).multiply(v2np1Bd)).multiply(x.pow(v2np1));
            
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal arctanh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<10; n++) {
            int v2np1 = 2*n +1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal content = x.pow(v2np1).divide(v2np1Bd);
            acum = acum.add(content);
        }
        return acum;
    }

    @Override
    public BigDecimal exp(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for(int n = 0 ; n < 11 ; n++) {
            acum = acum.add(x.pow(n).divide(BigDecimal.valueOf(fact(n))));
        }
        return acum;
    }

//    public BigDecimal cos(Double sum1) {
//        return BigDecimal.valueOf(Math.cos(sum1.doubleValue()));
//    }
//
//    public BigDecimal sin(Double sum1) {
//        return BigDecimal.valueOf(Math.sin(sum1.doubleValue()));
//    }
    @Override
    public BigDecimal ln(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal semiS = x.subtract(BigDecimal.ONE).divide(x.add(BigDecimal.ONE));
        
        for ( int n = 0; n<11; n++) {
            int v2nm1 = 2* n +1;
            BigDecimal v2nm1bd = BigDecimal.valueOf(v2nm1);
            acum = acum.add(v2nm1bd.multiply(semiS.pow(v2nm1)));
        }
        return acum.add(acum);
    }

    @Override
    public BigDecimal abs(BigDecimal x) {
        return x.abs();
    }

    @Override
    public BigDecimal pow(BigDecimal a, BigDecimal x) {
        return a.pow(x.intValue());
    }

    @Override
    public BigDecimal productoDirecto(BigDecimal prod1, BigDecimal prod2) {
        return prod1.multiply(prod2);
    }

    @Override
    public BigDecimal inversoMultiplicativo(BigDecimal prod) {
        return BigDecimal.ONE.divide(prod, RoundingMode.HALF_UP);
    }

    @Override
    public NumericMatriz<BigDecimal> instancia(Dominio dominio) {
        return new BigDecimalMatriz(dominio);
    }

    @Override
    public NumericMatriz<BigDecimal> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends BigDecimal> m) {
        return new BigDecimalMatriz(dominio, m);
    }

    @Override
    public BigDecimal restaDirecta(BigDecimal sum1, BigDecimal sum2) {
        return sum1.subtract(sum2);
    }
    
}
