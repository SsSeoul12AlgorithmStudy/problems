import java.io.*;
import java.util.*;

public class Solution {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int N;
    static PriorityQueue<int []> pq;

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for(int test = 1; test <= T; test++){
            init();
            int answer = solve();
            System.out.println("#" + test + " " + answer);
        }
    }

    static void init() throws IOException {
        // N
        N = Integer.parseInt(br.readLine());

        // pq
        pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        for(int i = 0; i < N; i++){
            int vol = Integer.parseInt(st.nextToken());
            pq.offer(new int[] { vol, i + 1 });
        }
    }

    static int solve(){
        int scan = 1;
        int prevPos = 0;
        while(!pq.isEmpty()){
            if(prevPos > pq.peek()[1]){
                scan++;
            }
            prevPos = pq.poll()[1];
        }
        return scan;
    }
}
