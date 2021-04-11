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
package org.tensa.tensada.matrix.v2.impl;

import org.tensa.tensada.matrix.v2.TerminalMath;


public class DoubleTerminalMathImpl implements TerminalMath<Double> {

    @Override
    public Double mapper(Number v) {
        return v.doubleValue();
    }

    @Override
    public Double getCeroValue() {
        return 0.0;
    }

    @Override
    public Double getUnoValue() {
        return 1.0;
    }

    @Override
    public Double sumaDirecta(Double sum1, Double sum2) {
        return sum1 + sum2;
    }

    @Override
    public Double restaDirecta(Double sus1, Double sus2) {
        return sus1 - sus2;
    }

    @Override
    public Double inversoAditivo(Double sum1) {
        return -sum1;
    }

    @Override
    public Double productoDirecto(Double prod1, Double prod2) {
        return prod1 * prod2;
    }

    @Override
    public Double inversoMultiplicativo(Double divisor) {
        return 1 / divisor;
    }

    @Override
    public Double divisionDirecto(Double dividendo, Double divisor) {
        return dividendo / divisor;
    }

    @Override
    public Double cos(Double deg) {
        return Math.cos(deg);
    }

    @Override
    public Double sin(Double deg) {
        return Math.sin(deg);
    }

    @Override
    public Double raiz(Double v) {
        return Math.sqrt(v);
    }

    @Override
    public Double cuadrado(Double v) {
        return v * v;
    }

    @Override
    public Double log(Double v) {
        return Math.log(v);
    }

    @Override
    public Double exp(Double v) {
        return Math.exp(v);
    }

    @Override
    public Double tanh(Double v) {
        return Math.tanh(v);
    }

    @Override
    public Double abs(Double v) {
        return Math.abs(v);
    }
    
}
