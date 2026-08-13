class Solution {
    /*
    -2 3 6 1 -3 -1 -2 -4
    2 -3 6 -1 3 1 2 -4
    
    이 중에서 연속합 중 최대를 구해야함
    
    슬라이딩 윈도우는 안됨
    
    dp인가?
    
    dp[i] : i번 원소를 마지막 원소로 가지는 연속합 중 최댓값
    dp[i] = max(dp[i - 1] + A[i], A[i])
    
    */
    public long solution(int[] sequence) {
        long answer = 0;
        
        int N = sequence.length;
        
        int[] pulse1 = new int[N];
        for(int i = 0; i < N; i++){
            if(i % 2 == 0){
                pulse1[i] = sequence[i];
            }
            else{
                pulse1[i] = -sequence[i];
            }
        }
        
        int[] pulse2 = new int[N];
        for(int i = 0; i < N; i++){
            if(i % 2 == 1){
                pulse2[i] = sequence[i];
            }
            else{
                pulse2[i] = -sequence[i];
            }
        }
        
        return Math.max(getMaxSum(N, pulse1), getMaxSum(N, pulse2));
    }
    
    private long getMaxSum(int N, int[] A){
        long maxSum = -50_000_000_001L;
        
        long[] dp = new long[N];
        
        dp[0] = A[0];
        maxSum = Math.max(maxSum, dp[0]);
        for(int i = 1; i < N; i++){
            dp[i] = Math.max(dp[i - 1], 0) + A[i];
            maxSum = Math.max(maxSum, dp[i]);
        }
        
        return maxSum;
    }
}