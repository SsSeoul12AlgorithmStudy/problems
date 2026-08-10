/*
 dp 접근
 - 근데 구현 점화식은 접근을 잘못했음. 그래서 취약한 dp부분이라 GPT 형님 도움받음.
 */

class Solution {
    public int solution(int sticker[]) {
        int N = sticker.length;
        
        if(N == 1) return sticker[0];
        
        int[] dp = new int[N];
        dp[0] = sticker[0];
        dp[1] = Math.max(sticker[0], sticker[1]);
        
        for(int i = 2; i < N - 1; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + sticker[i]);
        }
        
        int[] dp2 = new int[N];
        dp2[1] = sticker[1];
        
        for (int i = 2; i < N; i++) {
            dp2[i] = Math.max(dp2[i - 1], dp2[i - 2] + sticker[i]);
        }
        
        return Math.max(dp[N - 2], dp2[N - 1]);
    }
}