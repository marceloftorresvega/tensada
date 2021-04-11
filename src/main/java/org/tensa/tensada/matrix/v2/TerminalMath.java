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

import java.util.Map;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 * @param <N>
 */
public interface TerminalMath<N extends Number> {
    
    N mapper(Number v);

    N getCeroValue();

    N getUnoValue();

    N sumaDirecta(N sum1, N sum2);

    N restaDirecta(N sus1, N sus2);

    N inversoAditivo(N sum1);

    N cos(N deg);

    N sin(N deg);

    N productoDirecto(N prod1, N prod2);

    N inversoMultiplicativo(N divisor);

    N divisionDirecto(N dividendo, N divisor);
    
    N raiz(N v);
    
    N cuadrado(N v);
    
    N log(N v);
    
    N exp(N v);
    
    N tanh(N v);
    
    N abs(N v);
}
