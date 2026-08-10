class Solution {
   static int[] dp;
	static boolean[] selected;
    
    public int solution(int sticker[]) {
      int answer = 0;
      int n = sticker.length;
      dp = new int[n];
      selected = new boolean[n];
      
      if(sticker.length == 1) return sticker[0];
      
      answer = makeSet(sticker, 0);
      return answer;
    }
  
    static int makeSet(int[] sticker, int idx) {

		    // 첫 번째 스티커를 뜯는 경우 (마지막 스티커는 사용 불가)
        dp[0] = sticker[0];
        dp[1] = sticker[0];
        for (int i = 2; i < sticker.length; i++) {
            //이전 스티커까지의 합과 지금껄 뜯는 경우의 합 중 큰 것
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + sticker[i]);
        }
        int tmp = dp[sticker.length-2];

        // 첫 번째 스티커를 뜯지 않는 경우 (마지막 스티커 사용 가능)
        dp = new int[sticker.length];
        dp[0] = 0;
        dp[1] = sticker[1];
        for (int i = 2; i < sticker.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + sticker[i]);
        }

        // 두 경우 중 최댓값
        return Math.max(tmp, dp[sticker.length - 1]);
	}
}
