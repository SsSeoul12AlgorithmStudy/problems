import java.io.*;
import java.util.*;
 
public class Solution {
    /*
    * 26-8-6 22:10 ~
    *
    * N을 P개의 자연수로 나누었을 때 최댓값을 구하자
    *
    * dp[i][j] : i를 j개의 자연수로 나눌 때 곱의 최댓값
    * dp[i][j] = max(k * dp[i - k][j - 1])
 
     * */
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int N, P;
 
    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for(int test = 1; test <= T; test++){
            init();
            long answer = solve();
            System.out.println("#" + test + " " + answer);
        }
    }
 
    static void init() throws IOException{
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
    }
 
    static long solve(){
        long[][] dp = new long[N + 1][P + 1];
 
        for(int i = 1; i <= N; i++){
            dp[i][1] = i;
        }
 
        for(int j = 2; j <= P; j++){
            for(int i = 1; i <= N; i++){
                for(int k = 1; k < i; k++){
                    dp[i][j] = Math.max(dp[i][j], k * dp[i - k][j - 1]);
                }
            }
        }
 
        return dp[N][P];
    }
}