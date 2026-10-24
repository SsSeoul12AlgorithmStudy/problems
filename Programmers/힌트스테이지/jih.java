class Solution {
    
    private int answer = Integer.MAX_VALUE;
    private int N;
    private int[][] Cost, Hint;
    private int[] selected;
    
    public int solution(int[][] cost, int[][] hint) {    
        N = cost.length;
        Cost = cost;
        Hint = hint;
        
        selected = new int[N];
        
        solve(0);
        
        return answer;
    }
    
    private void solve(int depth){
        if(depth >= N){
            int[] ticket = new int[N + 1];
            for(int i = 0; i < N - 1; i++){
                for(int j = 1; j < Hint[0].length; j++){
                    if(selected[i] == 1){
                        if(ticket[Hint[i][j]] < N - 1) ticket[Hint[i][j]]++;
                    }
                }
            }
            
            int sum = 0;
            for(int i = 0; i < N - 1; i++){
                if(selected[i] == 1) sum += Hint[i][0];
            }
            
            for(int i = 0; i < N; i++){
                sum += Cost[i][ticket[i + 1]];
            }
            
            if(answer > sum){
                answer = sum;
            }
            
            return;
        }
        
        selected[depth] = 1;
        solve(depth + 1);
        
        selected[depth] = 0;
        solve(depth + 1);
    }
}