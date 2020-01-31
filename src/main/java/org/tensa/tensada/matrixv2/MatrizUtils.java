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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.tensa.tensada.matrix.Dominio;
import org.tensa.tensada.matrix.Indice;
import org.tensa.tensada.matrix.ParOrdenado;

/**
 *
 * @author lorenzo
 */
public class MatrizUtils {
    
    public static <V> Matriz<V> iesima(ParOrdenado i,Matriz<V> m) {
        return new MatrizIesimaWrapper<>(i, m);
    }
    
    public static <V> Matriz<V> transpuesta(Matriz<V> m) {
        return new MatrizTranspuestaWrapper<>(m);
    }
    
    public static <V> Matriz<V> diagonal(Matriz<V> m) {
        return new MatrizDiagonalWrapper<>(m);
    }
    
    private static class MatrizDiagonalWrapper<V> implements Matriz<V> {
        
        private final Matriz<V> origen;

        public MatrizDiagonalWrapper(Matriz<V> m) {
            this.origen = m;
        }

        @Override
        public Dominio getDominio() {
            return origen.getDominio();
        }

        @Override
        public Matriz<V> instancia(Dominio dominio) {
            return origen.instancia(dominio);
        }
        
        @Override
        public Matriz<V> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m) {
            return origen.instancia(dominio, m);
        }
        
        @Override
        public Matriz<V> instancia(Matriz<V> m) {
            return origen.instancia(m);
        }
        
        @Override
        public Matriz<V> indexa(Integer i, Integer j, V valor) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Matriz<V> indexa(ParOrdenado i, V valor) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public Matriz<V> transpuesta() {
            return this;
        }
        
        @Override
        public Matriz<V> iesima(ParOrdenado i) {
            return MatrizUtils.iesima(i, this);
        }
        
        @Override
        public Matriz<V> diagonal() {
            return this;
        }

        @Override
        public int size() {
            return (int)origen.entrySet().stream()
                    .filter( e -> e.getKey().getColumna().equals(e.getKey().getFila()))
                    .count();
        }
        
        @Override
        public boolean isEmpty() {
            return !origen.entrySet().stream()
                    .filter( e -> e.getKey().getColumna().equals(e.getKey().getFila()))
                    .findFirst().isPresent();
        }
        
        @Override
        public boolean containsKey(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public V get(Object key) {
            if (!(key instanceof ParOrdenado)) {
                return null;
            }
            
            ParOrdenado parKey = (ParOrdenado)key;
            if( parKey.getFila().equals(parKey.getColumna())) {
                return origen.get(parKey);
            } else {
                return null;
            }
        }
        
        @Override
        public V put(ParOrdenado key, V value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void putAll(Map<? extends ParOrdenado, ? extends V> m) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public Set<ParOrdenado> keySet() {
            return origen.getDominio().stream().collect(Collectors.toSet());
        }
        
        @Override
        public Collection<V> values() {
            return origen.entrySet().stream()
                    .filter( e -> e.getKey().getColumna().equals(e.getKey().getFila()))
                    .map(e -> e.getValue())
                    .collect(Collectors.toList());
        }
        
        @Override
        public Set<Map.Entry<ParOrdenado, V>> entrySet() {
            return origen.entrySet().stream()
                    .filter( e -> e.getKey().getColumna().equals(e.getKey().getFila()))
                    .collect(Collectors.toSet());
        }
    }

    private static class MatrizTranspuestaWrapper<V> implements Matriz<V> {
        
        private final Matriz<V> origen;

        public MatrizTranspuestaWrapper(Matriz<V> m) {
            this.origen = m;
        }

        @Override
        public Dominio getDominio() {
            return (Dominio)origen.getDominio().transpuesta();
        }

        @Override
        public Matriz<V> instancia(Dominio dominio) {
            return origen.instancia(dominio);
        }
        
        @Override
        public Matriz<V> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m) {
            return origen.instancia(dominio, m);
        }
        
        @Override
        public Matriz<V> instancia(Matriz<V> m) {
            return origen.instancia(m);
        }
        
        @Override
        public Matriz<V> indexa(Integer i, Integer j, V valor) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public Matriz<V> indexa(ParOrdenado i, V valor) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public Matriz<V> transpuesta() {
            return origen;
        }
        
        @Override
        public Matriz<V> iesima(ParOrdenado i) {
            return MatrizUtils.iesima(i, this);
        }
        
        @Override
        public Matriz<V> diagonal() {
            return MatrizUtils.diagonal(origen);
        }
        
        @Override
        public int size() {
            return this.entrySet().size();
        }
        
        @Override
        public boolean isEmpty() {
            return origen.isEmpty();
        }
        
        @Override
        public boolean containsKey(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public V get(Object key) {
            
            if (!(key instanceof ParOrdenado)) {
                return null;
            }
            
            ParOrdenado parKey = (ParOrdenado)key;
            return origen.get(parKey.transpuesta());
        }
        
        @Override
        public V put(ParOrdenado key, V value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void putAll(Map<? extends ParOrdenado, ? extends V> m) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public Set<ParOrdenado> keySet() {
            return origen.keySet().stream()
                    .map(k -> k.transpuesta())
                    .collect(Collectors.toSet());
        }
        
        @Override
        public Collection<V> values() {
            return origen.values();
        }
        
        @Override
        public Set<Map.Entry<ParOrdenado, V>> entrySet() {
            return origen.entrySet().stream()
                    .map(e ->{
                return new Entry<ParOrdenado, V>() {

                    @Override
                    public ParOrdenado getKey() {
                        return e.getKey().transpuesta();
                    }

                    @Override
                    public V getValue() {
                        return e.getValue();
                    }

                    @Override
                    public V setValue(V value) {
                        e.setValue(value);
                        return value;
                    }
                };
            }).collect(Collectors.toSet());
        }
    }

    private static class MatrizIesimaWrapper<V> implements Matriz<V> {

        private final Matriz<V> origen;
        private final ParOrdenado iesimoKey;

        public MatrizIesimaWrapper(ParOrdenado i, Matriz<V> origen) {
            this.origen = origen;
            this.iesimoKey = i;
        }


        @Override
        public Dominio getDominio() {
            Integer fila = origen.getDominio().getFila();
            Integer columna = origen.getDominio().getColumna();
            return new Dominio(fila-1, columna-1);
        }

        @Override
        public Matriz<V> instancia(Dominio dominio) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Matriz<V> instancia(Dominio dominio, Map<? extends ParOrdenado, ? extends V> m) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Matriz<V> instancia(Matriz<V> m) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Matriz<V> indexa(Integer i, Integer j, V valor) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Matriz<V> indexa(ParOrdenado i, V valor) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Matriz<V> transpuesta() {
            return MatrizUtils.transpuesta(this);
        }

        @Override
        public Matriz<V> iesima(ParOrdenado i) {
            return MatrizUtils.iesima(i, this);
        }

        @Override
        public Matriz<V> diagonal() {
            return MatrizUtils.diagonal(this);
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isEmpty() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean containsKey(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public V get(Object key) {
            
            if (!(key instanceof ParOrdenado)) {
                return null;
            }
            
            ParOrdenado parKey = (ParOrdenado)key;
            int columna = iesimoKey.getColumna();
            int fila = iesimoKey.getFila();
            
            if ( parKey.getColumna() >= columna ) {
                columna = parKey.getColumna() + 1;
            }
            if ( parKey.getFila()>= fila ) {
                fila = parKey.getFila() + 1;
            }
            return origen.get(new Indice(fila, columna));
        }

        @Override
        public V put(ParOrdenado key, V value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void putAll(Map<? extends ParOrdenado, ? extends V> m) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set<ParOrdenado> keySet() {
            return this.entrySet().stream()
                    .map(e -> e.getKey())
                    .collect(Collectors.toSet());
        }

        @Override
        public Collection<V> values() {
            return this.entrySet().stream()
                    .map(e -> e.getValue())
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<Entry<ParOrdenado, V>> entrySet() {
            
            Set<Entry<ParOrdenado, V>> es = origen.entrySet();
            int columna;
            int fila;
            columna = iesimoKey.getColumna();
            fila = iesimoKey.getFila();
            
            Stream<Entry<ParOrdenado, V>> menor = es.stream()
                    .filter(e ->  e.getKey().getFila()< fila && e.getKey().getColumna() < columna );
            
            Stream<Entry<ParOrdenado, V>> mayor = es.stream()
                    .filter(e ->  e.getKey().getFila() > fila && e.getKey().getColumna() > columna )
                    .map( e -> getEntry(getFilaMas(getColumnaMas(e.getKey())), e.getValue()));
            
            Stream<Entry<ParOrdenado, V>> fmayor = es.stream()
                    .filter(e ->  e.getKey().getFila() > fila && e.getKey().getColumna() < columna )
                    .map( e -> getEntry(getFilaMas(e.getKey()), e.getValue()));
            
            Stream<Entry<ParOrdenado, V>> cmayor = es.stream()
                    .filter(e ->  e.getKey().getFila() < fila && e.getKey().getColumna() > columna )
                    .map( e -> getEntry(getColumnaMas(e.getKey()), e.getValue()));
            
            return Stream.concat(Stream.concat(menor, mayor), Stream.concat(fmayor, cmayor))
                    .collect(Collectors.toSet());
        }
        
        private ParOrdenado getFilaMas( ParOrdenado i) {
            Integer fila = i.getFila() + 1;
            Integer columna = i.getColumna();
            return new Indice(fila, columna);
        }
        
        private ParOrdenado getColumnaMas( ParOrdenado i) {
            Integer fila = i.getFila();
            Integer columna = i.getColumna() +1 ;
            return new Indice(fila, columna);
        }
        
        private Entry<ParOrdenado, V> getEntry(ParOrdenado k, V valor) {
            return new Entry<ParOrdenado, V>() {

                @Override
                public ParOrdenado getKey() {
                    return k;
                }

                @Override
                public V getValue() {
                    return valor;
                }

                @Override
                public V setValue(V value) {
                    return valor;
                }
            };
        }

        
    }
}
