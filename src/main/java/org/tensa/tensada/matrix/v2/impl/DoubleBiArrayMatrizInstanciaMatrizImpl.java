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

import java.io.IOException;
import java.util.Map;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.ParOrdenado;
import org.tensa.tensada.matrix.v2.InstanciaMatriz;
import org.tensa.tensada.matrix.v2.Matriz;
import org.tensa.tensada.matrix.v2.map.ParOrdenadoBiArray;


public class DoubleBiArrayMatrizInstanciaMatrizImpl implements InstanciaMatriz<Double> {

    @Override
    public Matriz<Double> instancia(Dominio dominio) {
        Double[][] core = new Double[dominio.getFila()][dominio.getColumna()];
        return new MatrizImpl(dominio, core);
    }

    @Override
    public Matriz<Double> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends Double> m) {
        Double[][] core = new Double[dominio.getFila()][dominio.getColumna()];
        m.forEach((k,v) -> {
            core[k.getFila()][k.getColumna()] = v;
        });
        return new MatrizImpl(dominio, core);
    }

    @Override
    public Matriz<Double> instancia(Matriz<Double> m) {
        Dominio dominio = m.getDominio();
        Double[][] core = new Double[dominio.getFila()][dominio.getColumna()];
        m.forEach((k,v) -> {
            core[k.getFila()][k.getColumna()] = v;
        });
        return new MatrizImpl(dominio, core);
    }

    private static class MatrizImpl extends ParOrdenadoBiArray<Double> implements Matriz<Double> {

        private final Dominio dominio;

        public MatrizImpl(Dominio dominio, Double[][] core) {
            super(core);
            this.dominio = dominio;
        }

        @Override
        public Dominio getDominio() {
            return dominio;
        }

        @Override
        public void close() throws IOException {
            // vacio
        }
    }

}
