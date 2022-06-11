package com.sep.billardapp.Helpers;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/***
 * Used to find clusters and merge circles
 */
public class UnionFind<E> {

    // Maps elements to entries
    Map<E, Entry> es=new HashMap<>();

    // counts number of partitions
    int partitionCount;

    /***
     * An entry in the data structure
     */
    class Entry{

        E elem;
        Entry parent=this;
        int weight=1;
        public Entry(E e) {
            elem=e;
        }
    }

    /**
     * Get the entry for an element. If no such entry exists yet, a new one is created
     */
    Entry entryFor(E e){
        Entry r = es.get(e);
        if(r==null) {
            es.put(e, r=new Entry(e));
            ++partitionCount;
        }
        return r;
    }

    /***
     * Get the root entry of the tree that e is part of
     */
    Entry getRoot(Entry e){
        while(e.parent!=e){
            Entry parent=e.parent;
            e.parent=parent.parent;
            e=parent;
        }
        return e;
    }

    /***
     * Declares the two elements as equivalent
     */
    public E union(E elem1, E elem2){
        Entry e1=getRoot(entryFor(elem1));
        Entry e2=getRoot(entryFor(elem2));
        if(e1==e2) return e1.elem;
        --partitionCount;
        if(e1.weight>e2.weight){
            e2.parent=e1;
            e1.weight+=e2.weight;
            return e1.elem;
        }else{
            e1.parent=e2;
            e2.weight+=e1.weight;
            return e2.elem;
        }
    }

    /***
     * Get all a the equivalence classes as sets
     */
    public Collection<Set<E>> partitions(){
        Map<Entry, Set<E>> ret=new HashMap<UnionFind<E>.Entry, Set<E>>();
        for(Entry e: es.values()){
            Entry root=getRoot(e);
            //System.out.println(root);
            Set<E> partition=ret.get(root);
            if(partition==null)
                ret.put(root, partition=new HashSet<>());
            partition.add(e.elem);
        }
        return ret.values();
    }

}
