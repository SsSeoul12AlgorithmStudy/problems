class Solution {
    /*
    26/10/7 22:26 ~ 24:20
    
    두 도둑이 모든 물건을 훔쳤을때, A도둑이 남긴 흔적의 누적합의 최솟값을 구하자
    
    ===
    
    완전탐색으로 풀 수 있나?
    - 경우의 수가 2^40개여서 쉽지 않을 것 같다...
    - 역시 시간초과 발생
    
    A의 흔적의 합을 최소화해야 한다
    
    어디를 최적화할 수 있을까?
    - 파라매트릭 서치나 dp가 가능한가?
    - 합이 m 미만인 숫자들을 제외하고 남은 숫자들의 합을 최소화해야한다
    - 뭔가 냅색의 향기가 난다
    
    - i번째를 b가 훔치면 b가 훔칠 수 있는 수는 줄고, a가 훔치는 수는 늘어난다  
    
    - 다음과 같이 정의하면
        - dp[i][j][0] : i번째까지 봤고 j를 더 쓸 수 있을 때 i번째 물건을 b가 훔칠 때 얻을 수 있는 a의 누적합의 최솟값
        - dp[i][j][1] : i번째 물건을 훔치지 않을 때
        - 구하는 답은 min(dp[last][j][0], dp[last][j][1]). 이때 j < m이고 dp값 < n이어야 함
        - dp값 >= n이면 -1 리턴
    
    // i번째를 선택하거나 선택하지 않으면서 a누적합의 최솟값을 구한다
    dp[i][j] = min(dp[i - 1][j + info[i][1]], dp[i - 1][j] + info[i][0]) // b가 선택
    
    info[][] 
    1 2 2
    2 3 1
    
    n = 4, m = 4
    
    dp[][]
    i=4 : 1 3 5 
    i=3 : 0 2 4
    i=2 : 0 1 3
    i=1 : 0 0 2
    
    
    */
    
    public int solution(int[][] info, int n, int m) { 
        int count = info.length;
        
        int[][] dp = new int[count + 1][m + 1];
        
        for(int i = 1; i <= count; i++){
            for(int j = 1; j <= m; j++){
                if(j + info[i - 1][1] <= m){
                    dp[i][j] = Math.min(dp[i - 1][j + info[i - 1][1]], dp[i - 1][j] + info[i - 1][0]);
                }
                else{
                    dp[i][j] = dp[i - 1][j] + info[i - 1][0];
                }  
            }
        }
        
        int answer = Integer.MAX_VALUE;
        
        for(int i = 1; i <= m; i++){
            if(answer > dp[count][i]){
                answer = dp[count][i];
            }
        }
        
        // for(int i = 1; i <= count; i++){
        //     System.out.println(Arrays.toString(dp[i]));
        // }
        
        if(answer >= n) {
            return -1;
        }
        
        return answer;
    }
  
}