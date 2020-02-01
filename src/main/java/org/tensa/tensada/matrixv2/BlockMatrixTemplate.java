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
package org.tensa.tensada.matrixv2;

import java.util.Map;
import java.util.Optional;
import static java.util.stream.Collectors.*;
import java.util.stream.IntStream;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author Marcelo
 */
public class BlockMatrixTemplate extends HashMatriz<Dominio> {
    
    private HashMatriz<Indice> mapping;
    private Dominio dominioFinal;

    public BlockMatrixTemplate(Dominio domain) {
        super(domain);
    }

    public BlockMatrixTemplate(Matriz<Dominio> m) {
        super(m);
    }
    
    public void fixDominios(){
        Map<Integer, Optional<Integer>> maxFilaToCol = this.entrySet().stream()
                .collect(
                        groupingBy(
                                e -> e.getKey().getColumna(),
                                mapping( 
                                        e -> e.getValue().getFila() ,
                                        maxBy(Integer::compareTo)) ));
        
        Map<Integer, Optional<Integer>> maxColToFila = this.entrySet().stream()
                .collect(
                        groupingBy(
                                e -> e.getKey().getFila(),
                                mapping( 
                                        e -> e.getValue().getColumna(),
                                        maxBy(Integer::compareTo)) ));
        
        this.replaceAll((i,v) -> new Dominio(
                maxFilaToCol.get(i.getColumna()).orElse(0),
                maxColToFila.get(i.getFila()).orElse(0)));
        
        mapping = new HashMatriz<>(this.getDominio());
        
        
        for(  ParOrdenado key :this.getDominio()) {
            if(key.equals(Indice.D1)) {
                mapping.indexa(Indice.D1, Indice.E1);
                continue;
            }
            if(key.getFila() == 1 ) {
                mapping.indexa(key,new Indice( 
                        maxFilaToCol.get(
                                key.getColumna() -1 ).orElse(1)
                                + mapping.get(new Indice(1, key.getColumna()-1)).getFila(),
                        1 ));
                continue;
            }
            if(key.getColumna() == 1 ) {
                mapping.indexa(key,new Indice( 
                        1, 
                        maxColToFila.get(
                                key.getFila() -1 ).orElse(1)
                                + mapping.get(new Indice(key.getFila()-1,1)).getColumna() ));
                continue;
            }
            mapping.indexa(key,new Indice( 
                    maxFilaToCol.get(
                            key.getColumna() -1 ).orElse(1)
                            + mapping.get(new Indice(key.getFila(), key.getColumna()-1)).getFila(),
                    maxColToFila.get(
                            key.getFila() -1 ).orElse(1) 
                            + mapping.get(new Indice(key.getFila()-1,key.getColumna())).getColumna() ));
        }
        
        Dominio ultimoDominio = get(getDominio());
        Indice ultimoInicio = mapping.get(getDominio());
        
        dominioFinal = new Dominio(
                ultimoInicio.getFila() + ultimoDominio.getFila(),
                ultimoInicio.getColumna()+ ultimoDominio.getColumna());
        
    }
    
    public <V> Matriz<Matriz<V>> instanceBlocksMatrix(Matriz<V> m) {
        HashMatriz<Matriz<V>> tmp = new HashMatriz<>(getDominio());
        this.forEach((id, d) -> {
            tmp.put(id, new HashMatriz<>(d));
        });
        return tmp;
    }
    
    public <V> Matriz<V> instanceFullMatrix(Matriz<V> m) {
        Matriz<V> tmp = new HashMatriz<>(dominioFinal);
        return tmp;
    }
    
    public <V> Matriz<Matriz<V>> split(Matriz<V> m, Matriz<Matriz<V>> b) {
        if (!m.getDominio().equals(dominioFinal)) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (!b.getDominio().equals(getDominio())) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        
        for( ParOrdenado key :getDominio()) {
            Dominio dominioLocal = get(key);
            Indice base = mapping.get(key);
            Matriz<V> subMatriz = b.get(key);
            for( ParOrdenado subKey :dominioLocal) {
                Indice positionIdex = new Indice(
                        base.getFila() + subKey.getFila(),
                        base.getColumna() + subKey.getColumna());
                V unit = m.get(positionIdex);
                subMatriz.put(subKey, unit);
            }
        }
        return b;
    }
    
    public <V> Matriz<V> join(Matriz<Matriz<V>> b,Matriz<V> m) {
        if (!m.getDominio().equals(dominioFinal)) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        if (!b.getDominio().equals(getDominio())) {
            throw new IllegalArgumentException("matrices no compatibles");
        }
        
        for( ParOrdenado key :getDominio()) {
            Dominio dominioLocal = get(key);
            Indice base = mapping.get(key);
            Matriz<V> subMatriz = b.get(key);
            for( ParOrdenado subKey :dominioLocal) {
                V unit = subMatriz.get(subKey);
                m.indexa(
                        base.getFila() + subKey.getFila(),
                        base.getColumna() + subKey.getColumna(), 
                        unit);
            }
        }
        return m;
    }
    
}
