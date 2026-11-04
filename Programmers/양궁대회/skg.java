/*

어피치 -> 라이언

k점을 더 많이 맞힌 사람이 k점을 가져간다. (동률일 경우 어피치가 가져감)
(누구도 맞치지 않은 경우는 둘 다 못 가져감)

최종 점수가 같은 경우 어피치가 우승자
라이언이 불쌍하군

어피치는 이미 쐈음.
라이언이 가장 큰 점수차로 우승하기 위해서 n발을 어떤 과녁에 맞춰야 하는지 return
우승할 수 없는 경우는 -1 리턴

info -> 처음부터 10점, 9점, 8점..을 맞힌 화살 개수

여러 경우가 있는 경우 가장 낮은 점수를 더 많이 맞힌 경우를 return.

특정 과녁 점수를 얻기 위해서는 (어피치가 맞힌 화살 개수 + 1)만큼 맞춰야 함.

브루트포스? -> 
n = 9
어피치 : 0 0 1 2 0 1 1 1 1 1 1 
라이언 : 1 1 2 3 1 2 2 2 2 2 2
k점 * 2만큼 얻는다.
(k점 * 2)/(소모 화살 개수) = 화살당 얻을 수 있는 점수
10 9  8  7   6  5  4  3  2  1  0
20 18 8 14/3 12 5  4  3  2  1  0
1  1  2      1  2  2

knapsack?
소모 화살 개수 = 무게
k점 * 2 = 가치

가치 20 18 16 14 12 10 8 6 4 2 0
무게 1  1  2  3  1  2  2 2 2 2 2

    0   1   2   3   4   5   6   7   8   9
20  0   20  20  20  20  20  20  20  20  20 => 10..00
18  0   20  38  38  38  38  38  38  38  38 => 11...00
16  0   20  38  38  54  54  54  54  54  54 => 10..0, 11..0, 111..0
14  0   20  38  38  54  54  54  68  68  68 => 
12  0   20  38  50  54  66  66  68  80  80
10  0   20  38  50  54  66  66  76  80  80
8   0   20  38  50  54  66  66  76  80  84
6   0   20  38  50  54  66  66  76  80  84
4   0   20  38  50  54  66  66  76  80  84
...
0   0   20  38  50  54  66  66  76  80  84

아 근데 생각해보니까 맞힌 화살 개수를 리턴해야 하네..
비트로 어떤 점수를 가져왔는지 기록? 그러면 되겠다.
dp 배열 + 비트 배열

dp로 할 때 정렬하고 시작했었나?

dp[i][j] : i번째 물품까지 고려하고, 최대로 담을 수 있는 무게가 j일 때 얻을 수 있는 최대 가치
dp[i][j] = Math.max(dp[i-1][j-w[i]] + v[i], dp[i-1][j]);

최대 점수가 아니라 최대 점수차! 상대 점수가 있을 때 뺏어오는 게 더 이득!!!

화살이 남으면 0점에 꽂아넣기

*/

class Solution {
    int[][] dp;
    int[][] bit;
    
    public int[] solution(int n, int[] info) {
        int[] answer = new int [11];
        
        int[] weights = new int[11];
        for (int i = 0; i <= 10; i++) {
            weights[i] = info[i] + 1;
        }
        
        int[] values = new int[11];
        for (int i = 0; i <= 10; i++) {
            if (info[i] > 0) {
                values[i] = (10 - i) * 2;
            } else {
                values[i] = (10 - i);
            }
        }
        
        dp = new int[11][n+1];
        bit = new int[11][n+1];
        
        // 첫 번째 행 먼저 진행 (초기화)
        for (int j = 0; j <= n; j++) {
            if (j < weights[0]) continue;
            dp[0][j] = values[0];
            bit[0][j] = occupyBit(bit[0][j], 0);
        }
        
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j <= n; j++) {
                if (j < weights[i] || dp[i-1][j] > dp[i-1][j-weights[i]] + values[i]) {
                    dp[i][j] = dp[i-1][j];
                    bit[i][j] = bit[i-1][j];
                } else {
                    dp[i][j] = dp[i-1][j-weights[i]] + values[i];
                    bit[i][j] = occupyBit(bit[i-1][j-weights[i]], i);
                }
            }
        }
        
        // 23 
        System.out.println(dp[10][n]);
        
        int finalBit = bit[10][n];
        int idx = 0;
        while (finalBit > 0) {
            answer[idx++] = finalBit % 2;
            finalBit = finalBit /= 2;
        }
    
        int arrowCnt = 0;
        for (int i = 0; i <= 10; i++) {
            if (answer[i] > 0) {
                answer[i] = info[i] + 1;
                arrowCnt += answer[i];
            }
        }
        
        if (n > arrowCnt) {
            answer[10] = n - arrowCnt;
        }
        
        printArr(answer);
        
        int oppScore = 0;
        for (int i = 0; i <= 10; i++) {
            if (info[i] > 0) {
                oppScore += (10 - i);
            }
        }
        System.out.println("opp:" + oppScore);
        if (dp[10][n] <= oppScore)  return new int[] {-1};
    
        
        return answer;
    }
    
    // i번째 과녁 점유한 것으로 비트 변화
    public int occupyBit(int bit, int i) {
        int t = (1 << i);
        return bit | t;
    }
    
    public void printArr(int[] arr) {
        System.out.println("===");
        for (int a : arr) {
            System.out.print(a + "-");
            
        }
        System.out.println();
    }
}
