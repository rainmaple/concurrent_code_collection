package cn.edu.iir.algori;

/**
 * @program: Ij_javatest
 * @description: B 树的操作接口
 * @author: rainmaple
 * @date: 2019-10-28 16:54
 **/
public interface B_treeOp {
    public Object get(Comparable key);
    public void remove(Comparable key);
    public void insertOrUpdate(Comparable key,Object obj);
}
