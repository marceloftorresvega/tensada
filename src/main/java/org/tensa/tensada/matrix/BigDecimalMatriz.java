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
import java.math.RoundingMode;
import java.util.Map;


public class BigDecimalMatriz extends NumericMatriz<BigDecimal> {

    public BigDecimalMatriz(Dominio dominio) {
        super(dominio);
    }

    public BigDecimalMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends BigDecimal> m) {
        super(dominio, m);
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
        return BigDecimal.ZERO.subtract(sum1);
    }

    @Override
    public BigDecimal cos(Double sum1) {
        return BigDecimal.valueOf(Math.cos(sum1.doubleValue()));
    }

    @Override
    public BigDecimal sin(Double sum1) {
        return BigDecimal.valueOf(Math.sin(sum1.doubleValue()));
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
