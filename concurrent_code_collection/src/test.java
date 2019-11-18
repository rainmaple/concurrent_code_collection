import java.util.Scanner;

public class test {
    public static void main(String args[]) {
//        Scanner in = new Scanner(System.in);
//        int group_num = in.nextInt();
//        int a[] = new int[group_num];
//        for(int i=0;i<group_num;i++) {
//            int e_days = in.nextInt();
//            for(int j=0;j<e_days;j++){
//                a[j]=in.nextInt();
//            }


//        }

//        Object o = new Object(){
////            public boolean equals(Object obj){
////                return true;
////            }
////        };
////        System.out.println(o.equals("Fred"));
        ((test)null).test();
    }
    public static void test(){
        System.out.println("test");
    }
    private void sort(int[] nums, int[] smaller, int[] pos, int from, int to) {
        if (from >= to) return;
        int m = (from + to) / 2;
        sort(nums, smaller, pos, from, m);
        sort(nums, smaller, pos, m + 1, to);
        int[] merged = new int[to - from + 1];
        int i = from, j = m + 1, k = 0, jump = 0;
        while (i <= m || j <= to) {
            if (i > m) {
                jump++;
                merged[k++] = pos[j++];
            } else if (j > to) {
                smaller[pos[i]] += jump;
                merged[k++] = pos[i++];
            } else if (nums[pos[i]] <= nums[pos[j]]) {
                smaller[pos[i]] += jump;
                merged[k++] = pos[i++];
            } else {
                jump++;
                merged[k++] = pos[j++];
            }
        }
        for (int p = 0; p < merged.length; p++) pos[from + p] = merged[p];
    }
}
