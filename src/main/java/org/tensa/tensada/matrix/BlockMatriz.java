/*
 * The MIT License
 *
 * Copyright 2019 Marcelo.
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

import java.util.Map;

/**
 *
 * @author Marcelo
 */
public class BlockMatriz<U> extends Matriz<Matriz<U>> {

    public BlockMatriz(Dominio dominio) {
        super(dominio);
    }

    public BlockMatriz(Dominio dominio, Map<? extends ParOrdenado, ? extends Matriz<U>> m) {
        super(dominio, m);
    }
    
    public Matriz<U> build(){
        return merge();
    }
    
    public Matriz<U> merge(){
        this.replaceAll((i,matriz) ->  {
            int maxFila = this.dominio.stream().filter( par -> par.getFila().equals(i.getFila()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getFila())
                    .max().orElse(0);
            
            int maxColumna = this.dominio.stream().filter( par -> par.getColumna().equals(i.getColumna()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getColumna())
                    .max().orElse(0);
            
            matriz.dominio = new Dominio(maxFila, maxColumna);
            return matriz;
        });
        
        int dominioMaxFila = this.values().stream().map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getFila)
                .sum();
        
        int dominioMaxColumna = this.values().stream().map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getColumna)
                .sum();
        
        Dominio nuevoDominio = new Dominio(dominioMaxFila, dominioMaxColumna);
        return this.entrySet().stream()
                .reduce(new Matriz<U>(nuevoDominio), 
                        (Matriz<U> matriz,Entry<ParOrdenado, Matriz<U>> entry) -> {
                            ParOrdenado key = entry.getKey();
                            Matriz<U> childMatriz = entry.getValue();

                            int lastLeft = this.entrySet().stream()
                                    .filter( e -> e.getKey().getFila() == key.getFila())
                                    .filter( e -> e.getKey().getColumna() < key.getColumna())
                                    .mapToInt( e -> e.getValue().getDominio().getColumna())
                                    .sum();

                            int lastTop = this.entrySet().stream()
                                    .filter( e -> e.getKey().getColumna()== key.getColumna())
                                    .filter( e -> e.getKey().getFila() < key.getFila())
                                    .mapToInt( e -> e.getValue().getDominio().getFila())
                                    .sum();


                            childMatriz.forEach( (ParOrdenado i,U val)-> {
                                matriz.indexa(lastTop>0? lastTop-1:0 + i.getFila() ,lastLeft>0? lastLeft-1:0 + i.getColumna() , val);
                            }); 
                            return matriz;
                    }, (Matriz<U> matriz1,Matriz<U> matriz2) -> {
                        matriz1.putAll(matriz2);
                        return matriz1;
                    });
    }
    
    public void splitIn(Matriz<U> older) {
        
        this.replaceAll((i,matriz) ->  {
            int maxFila = this.dominio.stream().filter( par -> par.getFila().equals(i.getFila()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getFila())
                    .max().orElse(0);
            
            int maxColumna = this.dominio.stream().filter( par -> par.getColumna().equals(i.getColumna()))
                    .map(par -> this.getOrDefault(par, new Matriz<>(new Dominio(0, 0))))
                    .map(mat -> mat.getDominio())
                    .mapToInt(dom -> dom.getColumna())
                    .max().orElse(0);
            
            matriz.dominio = new Dominio(maxFila, maxColumna);
            return matriz;
        });
        
        int dominioMaxFila = this.values().stream().map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getFila)
                .sum();
        
        int dominioMaxColumna = this.values().stream().map(Matriz<U>::getDominio)
                .mapToInt(Dominio::getColumna)
                .sum();
        
        Dominio superDominio = new Dominio(dominioMaxFila, dominioMaxColumna);
        if (superDominio.equals(older.getDominio())) {
            
            this.getDominio().stream()
                    
                    .forEach( (ParOrdenado key) -> {
                    
                            Matriz<U> childMatriz = get(key);

                            int lastLeft = this.entrySet().stream()
                                    .filter( e -> e.getKey().getFila() == key.getFila())
                                    .filter( e -> e.getKey().getColumna() < key.getColumna())
                                    .mapToInt( e -> e.getValue().getDominio().getColumna())
                                    .sum();

                            int lastTop = this.entrySet().stream()
                                    .filter( e -> e.getKey().getColumna() == key.getColumna())
                                    .filter( e -> e.getKey().getFila() < key.getFila())
                                    .mapToInt( e -> e.getValue().getDominio().getFila())
                                    .sum();


                            childMatriz.getDominio().stream()
                                    .forEach( (ParOrdenado i)-> {
                                U val = older.get( new Indice(lastTop>0? lastTop-1:0 + i.getFila(),
                                        lastLeft>0? lastLeft-1:0 + i.getColumna()));
                                childMatriz.put(i, val);
                            }); 
                            
                    
                    });
            
        } else {
            throw new IllegalArgumentException("Matrices no compatibles");
        }
    }
}
