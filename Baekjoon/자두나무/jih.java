package 자두나무;

import java.util.*;
import java.io.*;

public class Main {
    /*
    * 26/11/27 9:19 ~
    *
    * 자두가 2개의 나무를 W번 오가면서 먹을 수 있는 자두 개수의 최댓값을 구하는 함수를 작성하기
    *
    * 반례 : 0초에 1번 나무에 있는 것이라 1초에 2번 나무로 갈 수 있다
    * - 케이스 : T = 1, W = 1, 자두가 떨어지는 나무 = 2
    * */

    static int T, W;
    static int[][] fruit;
    static int[][][] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        T = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());

        fruit = new int[T][2];
        for(int i = 0; i < T; i++){
            int tree = Integer.parseInt(br.readLine()) - 1;
            fruit[i][tree] = 1;
        }

        dp = new int[T][W][2];
        for(int i = 0; i < T; i++){
            for(int j = 0; j < W; j++){
                for(int k = 0; k < 2; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }

        int answer = Math.max(findMaxFruit(0, 0, 0), findMaxFruit(0, 1, 1));

        System.out.println(answer);
    }

    // depth 위치에서 W번 이동할 수 있을 때 얻을 수 있는 과일의 최대 개수
    static int findMaxFruit(int depth, int move, int cur){
        if(move >= W){
            int sum = 0;
            for(int i = depth; i < T; i++){
                sum += fruit[i][cur];
            }
            return sum;
        }

        if(depth >= T){
             return 0;
        }

        int ret = dp[depth][move][cur];
        if(ret != -1) return ret;

        ret = Math.max(findMaxFruit(depth + 1, move + 1, 1 - cur), findMaxFruit(depth + 1, move, cur)) + fruit[depth][cur];

        return dp[depth][move][cur] = ret;
    }
}
