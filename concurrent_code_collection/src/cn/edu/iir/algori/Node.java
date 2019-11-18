package cn.edu.iir.algori;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * @program: Ij_javatest
 * @description: Node 封装
 * @author: rainmaple
 * @date: 2019-10-28 19:10
 **/

public class Node {
    protected boolean isLeaf;
    protected boolean isRoot;
    protected Node parent;
    protected Node previous;
    protected Node next;

    protected List<Entry<Comparable,Object>> entries;
    protected List<Node> children;

    public Node(){
        this.isLeaf = isLeaf;
        entries = new ArrayList<Entry<Comparable, Object>>();
        if(!isLeaf){
            children = new ArrayList<Node>();
        }
    }

    public Node(boolean isLeaf,boolean isRoot){
        this.isLeaf = isLeaf;
        this.isRoot  = isRoot;
    }

    public Object get(Comparable key){
        if(isLeaf){
            for(Entry<Comparable,Object> entry:entries){
                if(entry.getKey().compareTo(key) == 0){
                    return entry.getValue();
                }
            }
            return null;
        }else{
            if(key.compareTo(entries.get(0)) > 0){

            }
        }
        return null;
    }
}
