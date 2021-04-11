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
import org.tensa.tensada.matrix.v2.map.ParOrdenadoSiArray;

/**
 *
 * @author Marcelo
 */
public class DoubleSiArrayMatrizInstanciaMatrizImpl implements InstanciaMatriz<Double> {

    private static final String MSG_WRONG_COLUMN = "solo tolera una columna";

    @Override
    public Matriz<Double> instancia(Dominio dominio) {
        if (dominio.getColumna()>1) {
            throw new IndexOutOfBoundsException(MSG_WRONG_COLUMN);
        }
        return new MatrizImpl(dominio, new Double[dominio.getFila()]);
    }

    @Override
    public Matriz<Double> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends Double> m) {
        if (dominio.getColumna()>1) {
            throw new IndexOutOfBoundsException(MSG_WRONG_COLUMN);
        }
        Double[] data = new Double[dominio.getFila()];
        m.forEach((i,v)->  data[i.getFila()] = v );
        return new MatrizImpl(dominio, data);
    }

    @Override
    public Matriz<Double> instancia(Matriz<Double> m) {
        if (m.getDominio().getColumna()>1) {
            throw new IndexOutOfBoundsException(MSG_WRONG_COLUMN);
        }
        Double[] data = new Double[m.getDominio().getFila()];
        m.forEach((i,v)->  data[i.getFila()] = v );
        return new MatrizImpl(m.getDominio(), data);
        
    }

    private static class MatrizImpl extends ParOrdenadoSiArray<Double> implements Matriz<Double> {

        private final Dominio dominio;

        public MatrizImpl(Dominio dominio, Double[] core) {
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
