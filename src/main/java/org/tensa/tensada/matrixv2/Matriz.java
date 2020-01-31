/*
 * The MIT License
 *
 * Copyright 2020 lorenzo.
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
package org.tensa.tensada.matrixv2;

import java.util.Map;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author lorenzo
 * @param <V>
 */
public interface Matriz<V> extends Map<ParOrdenado, V> {
    
    Dominio getDominio();
    

    Matriz<V> instancia(Dominio dominio);
    Matriz<V> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m);
    Matriz<V> instancia(Matriz<V> m);
    
    Matriz<V> indexa(Integer i,Integer j, V valor);
    Matriz<V> indexa(ParOrdenado i, V valor);
    
    Matriz<V> transpuesta();
    Matriz<V> iesima(ParOrdenado i);
    Matriz<V> diagonal();
    
}
