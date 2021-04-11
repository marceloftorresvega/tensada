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
package org.tensa.tensada.matrix.v2;

import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 */
public interface MatrizMath<N extends Number> {
    
    TerminalMath<N> getTerminalMath();
    
    InstanciaMatriz<N> getInstanciaMatriz();
    
    Matriz<N> productoEscalar(Matriz<N> m, Number e);
    
    Matriz<N> rotacion(Matriz<N> eje, Number phi);
    
    Matriz<N> matrizIesima(Matriz<N> m, ParOrdenado i);
    
    Matriz<N> transpuesta(Matriz<N> m);
    
    Matriz<N> skewSymMatrix(Matriz<N> m);
    
    Matriz<N> matrizSimetrica(Matriz<N> m);
    
    Matriz<N> matrizAntisimetrica(Matriz<N> m);
    
    Matriz<N> matrizAdjunta(Matriz<N> m);
    
    Matriz<N> matrizCofactores(Matriz<N> m);
    
    Matriz<N> matrizInversa(Matriz<N> m);
    
    N norma(Matriz<N> m);
    
    N traza(Matriz<N> m);
    
    N determinante(Matriz<N> m);
    
    Matriz<N> adicion(Matriz<N> sumando1, Matriz<N> sumando2);
    
    Matriz<N> adicion(Matriz<N> ... sumando);
    
    Matriz<N> sustraccion(Matriz<N> minuendo, Matriz<N> sustraendo);
    
    Matriz<N> producto(Matriz<N> multiplo1, Matriz<N> multiplo2);
    
    Matriz<N> productoPunto(Matriz<N> multiplo1, Matriz<N> multiplo2);
    
    Matriz<N> productoTensorial(Matriz<N> multiplo1, Matriz<N> multiplo2);
    
    Matriz<N> productoKronecker(Matriz<N> multiplo1, Matriz<N> multiplo2);
    
    Matriz<N> productoCruz(Matriz<N> multiplo1, Matriz<N> multiplo2);
    
    Matriz<N> productoCruz(Matriz<N> ... multiplo);
    
    Matriz<N> uno(Dominio dominio);
    
    Matriz<N> identidad(Dominio dominio);
    
    Matriz<N> identidad(Integer dominio);
}
