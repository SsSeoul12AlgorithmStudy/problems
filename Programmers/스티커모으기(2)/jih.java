class Solution {
    /*
    26-8-10 22:10 ~ 
    
    연속되지 않는 숫자들을 선택할 때, 최댓값을 구하자
    
    ===
    
    스티커 개수 <= 1만, 숫자 <= 100
    
    dp[i][0] : i번째를 제외했을 때 얻을 수 있는 최대합
    dp[i][1] : i번째를 선택했을 때 얻을 수 있는 최대합
    
    dp[i][0] = max(dp[i - 1][0], dp[i - 1][1])
    dp[i][1] = max(dp[i - 1][0] + sticker[i], dp[i - 1][1])

    1번째를 포함했느냐, 아니냐 여부를 인덱스로 추가해야할 듯
    */
    public int solution(int sticker[]) {
        int N = sticker.length;
        int[][][] dp = new int[N][2][2]; 
        
        if(N == 1){
            return sticker[0];
        }
        
        dp[0][1][1] = sticker[0];
        for(int i = 1; i < N; i++){
            dp[i][0][0] = Math.max(dp[i - 1][0][0], dp[i - 1][1][0]);
            dp[i][0][1] = Math.max(dp[i - 1][0][1], dp[i - 1][1][1]);
            dp[i][1][0] = Math.max(dp[i - 1][0][0] + sticker[i], dp[i - 1][1][0]);
            dp[i][1][1] = Math.max(dp[i - 1][0][1] + sticker[i], dp[i - 1][1][1]);
        }

        return Math.max(dp[N - 1][0][0], Math.max(dp[N - 1][0][1], dp[N - 1][1][0]));
    }
}