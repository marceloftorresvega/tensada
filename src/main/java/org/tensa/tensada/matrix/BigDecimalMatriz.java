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
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;


public class BigDecimalMatriz extends NumericMatriz<BigDecimal> {
    
    private final static BigDecimal extEuler[] = {
        new BigDecimal("15514534163557086905"),
        BigDecimal.ZERO,
        new BigDecimal("-4087072509293123892361"),
        BigDecimal.ZERO,
        new BigDecimal("1252259641403629865468285"),
        BigDecimal.ZERO,
        new BigDecimal("-441543893249023104553682821"),
        BigDecimal.ZERO,
        new BigDecimal("177519391579539289436664789665"),
        BigDecimal.ZERO
    };
    
    private final static HashMap<Integer,BigDecimal> cacheBern = new HashMap<>();

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
    public BigDecimal suma(BigDecimal sum1, BigDecimal sum2) {
        return sum1.add(sum2);
    }

    @Override
    public BigDecimal inversoAditivo(BigDecimal sum1) {
        return sum1.negate();
    }

    @Override
    public BigDecimal cos(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<15; n++) {
            int v2n = 2*n;
            BigDecimal content = ang.pow(v2n).divide(factBd(v2n), MathContext.DECIMAL128);
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal sen(BigDecimal ang) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<15; n++) {
            int v2np1 = 2*n+1;
            BigDecimal content = ang.pow(v2np1).divide(factBd(v2np1), MathContext.DECIMAL128);
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
        if (ang.abs(MathContext.DECIMAL128).doubleValue()>= Math.PI/2.0) {
            throw new IllegalArgumentException();
        }
        return sen(ang).divide(cos(ang), MathContext.DECIMAL128);
//        BigDecimal acum = BigDecimal.ZERO;
//        BigDecimal v4 = BigDecimal.valueOf(4);
//        BigDecimal v1 = BigDecimal.ONE;
//        for (int n =1; n<15; n++) {
//            int v2n = 2*n;
//            BigDecimal b2n = bernBd(v2n);
//            BigDecimal f2n = factBd(v2n);
//            BigDecimal content = b2n.multiply(v4.negate().pow(n, MathContext.DECIMAL128), MathContext.DECIMAL128).multiply(v1.subtract(v4.pow(n)), MathContext.DECIMAL128).divide(f2n, MathContext.DECIMAL128).multiply(ang.pow(v2n-1, MathContext.DECIMAL128), MathContext.DECIMAL128);
//            acum = acum.add(content);
//        }
//        return acum;
        
    }

    @Override
    public BigDecimal sec(BigDecimal ang) {
        if (ang.abs(MathContext.DECIMAL128).doubleValue()>= Math.PI/2.0) {
            throw new IllegalArgumentException();
        }
//        BigDecimal acum = BigDecimal.ZERO;
//        for (int n =0; n<18; n++) {
//            int v2n = 2*n;
//            BigDecimal veu2n = eulerBd(v2n);
//            BigDecimal vfact2n = factBd(v2n);
//            BigDecimal content = ang.pow(v2n, MathContext.DECIMAL128).divide(vfact2n, MathContext.DECIMAL128).multiply(veu2n, MathContext.DECIMAL128);
//            if( potMenos1(n)>0) {
//                acum = acum.add(content);
//            } else {
//                acum = acum.subtract(content);
//            }
//        }
//        return acum;
        return cos(ang).pow(-1, MathContext.DECIMAL128);
    }

    @Override
    public BigDecimal csc(BigDecimal ang) {
        if (ang.abs(MathContext.DECIMAL128).doubleValue()>= Math.PI) {
            throw new IllegalArgumentException();
        }
        return sen(ang).pow(-1, MathContext.DECIMAL128);
        
//        BigDecimal acum = BigDecimal.ZERO;
//        BigDecimal v2 = BigDecimal.valueOf(2);
//        BigDecimal v1 = BigDecimal.ONE;
//        for (int n =1; n<25; n++) {
//            int v2n = 2*n;
//            int v2nm1 = 2*n -1;
//            BigDecimal b2n = bernBd(v2n);
//            BigDecimal f2n = factBd(v2n);
//            BigDecimal content = v1.negate().pow(n-1).multiply(v2.pow(v2nm1, MathContext.DECIMAL128).subtract(v1), MathContext.DECIMAL128).multiply(b2n, MathContext.DECIMAL128)
//                    .divide(f2n, MathContext.DECIMAL128).multiply(ang.pow(v2nm1, MathContext.DECIMAL128), MathContext.DECIMAL128);
//            acum = acum.add(content);
//        }
//        return acum.multiply(v2);
    }

    @Override
    public BigDecimal arcsen(BigDecimal x) {
        if (x.abs(MathContext.DECIMAL128).doubleValue()>= 1.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v4 = BigDecimal.valueOf(4);
        for (int n =0; n<90; n++) {
            int v2n = 2*n;
            int v2np1 = v2n+1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal fn = factBd(n);
            BigDecimal f2n = factBd(v2n);
            BigDecimal content = f2n.divide(v4.pow(n, MathContext.DECIMAL128).multiply(fn.pow(2, MathContext.DECIMAL128), MathContext.DECIMAL128).multiply(v2np1Bd, MathContext.DECIMAL128), MathContext.DECIMAL128).multiply(x.pow(v2np1, MathContext.DECIMAL128), MathContext.DECIMAL128);
            acum = acum.add(content);
        }
        return acum;
    }

    @Override
    public BigDecimal arccos(BigDecimal x) {
        BigDecimal pi = BigDecimal.valueOf(Math.PI);
        BigDecimal v2 = BigDecimal.valueOf(2);
        return pi.divide(v2, MathContext.DECIMAL128).subtract(arcsen(x), MathContext.DECIMAL128);
    }

    @Override
    public BigDecimal arctan(BigDecimal x) {
        if (x.abs(MathContext.DECIMAL128).doubleValue()>= 1.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<50; n++) {
            int v2np1 = 2*n +1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal content = x.pow(v2np1, MathContext.DECIMAL128).divide(v2np1Bd, MathContext.DECIMAL128);
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
        for (int n =0; n<30; n++) {
            int v2np1 = 2*n +1;
            BigDecimal f2np1 = factBd(v2np1);
            acum = acum.add(x.pow(v2np1, MathContext.DECIMAL128).divide(f2np1, MathContext.DECIMAL128));
        }
        return acum;
    }

    @Override
    public BigDecimal cosh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<30; n++) {
            int v2n = 2*n;
            BigDecimal f2n = factBd(v2n);
            acum = acum.add(x.pow(v2n, MathContext.DECIMAL128).divide(f2n, MathContext.DECIMAL128));
        }
        return acum;
    }

    @Override
    public BigDecimal tanh(BigDecimal x) {
        if (x.abs(MathContext.DECIMAL128).doubleValue()>= Math.PI/2.0) {
            throw new IllegalArgumentException();
        }
        return senh(x).divide(cosh(x), MathContext.DECIMAL128);
//        BigDecimal acum = BigDecimal.ZERO;
//        BigDecimal v4 = BigDecimal.valueOf(4);
//        BigDecimal v1 = BigDecimal.ONE;
//        for (int n =1; n<15; n++) {
//            int v2n = 2*n;
//            BigDecimal b2n = bernBd(v2n);
//            BigDecimal f2n = factBd(v2n);
//            BigDecimal content = b2n.multiply(v4.pow(n, MathContext.DECIMAL128), MathContext.DECIMAL128).multiply(v4.pow(n, MathContext.DECIMAL128).subtract(v1, MathContext.DECIMAL128))
//                    .divide(f2n, MathContext.DECIMAL128)
//                    .multiply(x.pow(v2n-1, MathContext.DECIMAL128), MathContext.DECIMAL128);
//            acum = acum.add(content);
//        }
//        return acum;
    }

    @Override
    public BigDecimal arcsenh(BigDecimal x) {
        if (x.abs(MathContext.DECIMAL128).doubleValue()>= 1.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal v4 = BigDecimal.valueOf(4);
        for (int n =0; n<15; n++) {
            int v2n = 2*n;
            int v2np1 = v2n+1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal fn = factBd(n);
            BigDecimal f2n = factBd(v2n);
            BigDecimal content = f2n.divide(v4.pow(n, MathContext.DECIMAL128).multiply(fn.pow(2, MathContext.DECIMAL128), MathContext.DECIMAL128).multiply(v2np1Bd, MathContext.DECIMAL128), MathContext.DECIMAL128).multiply(x.pow(v2np1, MathContext.DECIMAL128), MathContext.DECIMAL128);
            
            if( potMenos1(n)>0) {
                acum = acum.add(content);
            } else {
                acum = acum.subtract(content);
            }
        }
        return acum;
    }

    @Override
    public BigDecimal arccosh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO ;
        BigDecimal v2 = BigDecimal.valueOf(2);
        for (int n=1 ; n< 15; n++) {
            int v2n = 2*n;
            BigDecimal v2nBd = BigDecimal.valueOf(v2n);
            BigDecimal fac2n = factBd(v2n);
            BigDecimal facn = factBd(n);
            
            acum = acum.add(
                fac2n.divide(v2.pow(v2n, MathContext.DECIMAL128), MathContext.DECIMAL128)
                        .divide(facn.pow(2, MathContext.DECIMAL128), MathContext.DECIMAL128)
                        .multiply(x.pow(-v2n, MathContext.DECIMAL128), MathContext.DECIMAL128).divide(v2nBd, MathContext.DECIMAL128)
            );
        }
        
        return ln(v2.multiply(x, MathContext.DECIMAL128)).subtract(acum);
        
    }

    @Override
    public BigDecimal arctanh(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for (int n =0; n<30; n++) {
            int v2np1 = 2*n +1;
            BigDecimal v2np1Bd = BigDecimal.valueOf(v2np1);
            BigDecimal content = x.pow(v2np1, MathContext.DECIMAL128).divide(v2np1Bd, MathContext.DECIMAL128);
            acum = acum.add(content);
        }
        return acum;
    }

    @Override
    public BigDecimal exp(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        for(int n = 0 ; n < 40 ; n++) {
            acum = acum.add(
                    x.pow(n, MathContext.DECIMAL128).divide(factBd(n), MathContext.DECIMAL128)
                    , MathContext.DECIMAL128);
        }
        return acum;
    }
    
    private BigDecimal factBd(int n) {
        if ( n< 21) {
            return BigDecimal.valueOf(fact(n));
        } else {
            BigDecimal acum = BigDecimal.valueOf(fact(20));
            for (int i = 21; i<=n; i++) {
                acum = acum.multiply(BigDecimal.valueOf(i), MathContext.DECIMAL128);
            }
            return acum;
        }
    }
    
    private BigDecimal bernBd(int n) {
        if ( n<21) {
            return BigDecimal.valueOf(bern(n));
        } else {
            BigDecimal cache = cacheBern.get(n);
            if (cache!= null) {
                return cache;
            }
            BigDecimal Bn = factBd(n).negate();
            BigDecimal acum = BigDecimal.ZERO;
            for (int k = 0; k<n; k++) {
                acum = acum.add(
                        bernBd(k).divide(
                                factBd(k)
                                , MathContext.DECIMAL128).divide(
                                        factBd(n+1-k), 
                                        MathContext.DECIMAL128)
                        , MathContext.DECIMAL128);
            }
            Bn = Bn.multiply(acum, MathContext.DECIMAL128);
            cacheBern.put(n, Bn);
            return Bn;
        }
    }
    
    private BigDecimal eulerBd(int n) {
        if (n<24) {
            return BigDecimal.valueOf(euler(n));
        } else {
            return extEuler[n-24];
        }
    }
    
    @Override
    public BigDecimal ln(BigDecimal x) {
        BigDecimal acum = BigDecimal.ZERO;
        BigDecimal semiS = x.subtract(BigDecimal.ONE).divide(x.add(BigDecimal.ONE), MathContext.DECIMAL128);
        
        // 30 Float
        // 70 Double
        // 140 BigDecimal
        for ( int n = 0; n<140; n++) {
            int v2np1 = 2* n +1;
            BigDecimal v2np1bd = BigDecimal.valueOf(v2np1);
            acum = acum.add(semiS.pow(v2np1, MathContext.DECIMAL128).divide(v2np1bd, MathContext.DECIMAL128));
        }
        return acum.add(acum);
    }

    @Override
    public BigDecimal abs(BigDecimal x) {
        return x.abs();
    }

    @Override
    public BigDecimal pow(BigDecimal a, BigDecimal x) {
        return a.pow(x.intValue(),MathContext.DECIMAL128);
    }

    @Override
    public BigDecimal multiplica(BigDecimal prod1, BigDecimal prod2) {
        return prod1.multiply(prod2,MathContext.DECIMAL128);
    }

    @Override
    public BigDecimal inversoMultiplicativo(BigDecimal prod) {
        return BigDecimal.ONE.divide(prod, MathContext.DECIMAL128);
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
    public BigDecimal resta(BigDecimal sum1, BigDecimal sum2) {
        return sum1.subtract(sum2);
    }
    
}
