import java.io.*;
import java.util.*;
 
public class Solution {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
 
        StringBuilder result = new StringBuilder();
        for (int tc = 1; tc <= T; tc++) {
            int N = Integer.parseInt(br.readLine());
 
            StringTokenizer st = new StringTokenizer(br.readLine());
            HashMap<Integer, Integer> map = new LinkedHashMap<>();
            int[] books = new int[N];
            for (int i = 0; i < N; i++) {
                books[i] = Integer.parseInt(st.nextToken());
                map.put(books[i], i);
            }
 
            int count = 1;
            int bookN = 1, bookL = map.get(1);
            while (bookN < N) {
 
                if (bookL > map.get(++bookN)) count++;
 
                bookL = map.get(bookN);
            }
 
            result.append("#" + tc + " " + count + "\n");
        }
         
        System.out.println(result);
    }
}