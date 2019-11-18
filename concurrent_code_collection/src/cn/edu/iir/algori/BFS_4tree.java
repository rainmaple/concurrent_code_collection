package cn.edu.iir.algori;/**
 * @program: Ij_javatest
 * @description: ${description}
 * @author: rainmaple
 * @date: 2019-11-12 18:57
 **/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @ClassName BFS_4tree
 * @Description: 层次遍历
 * @Author rainmaple
 * @Date 2019/11/12 
 * @Version V1.0
 **/
/**
 * Definition for a binary tree node.
 */
public class BFS_4tree {
    public List<List<Integer>> leverlOrder(TreeNode root){
        List<List<Integer>> ansList = new ArrayList<>();
        if(root == null) return ansList;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> ans = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                ans.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            ansList.add(ans);
        }
        return ansList;
    }
}
