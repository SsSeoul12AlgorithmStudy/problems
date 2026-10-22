/*

- 입력정수의 총 갯수 = q.length = ans.length = m
- q[i].length = 5로 고정

단순 풀이
1. 입력 정수 m개에 대해 사용한 숫자 기억
2. 사용불가 숫자(ans[i] = 0인경우) 판단 -> 없어도 될거같음.
3. 입력정수 정보는 정렬되어있음 따라서 순차적으로 조합을 확인

시간복잡도 계산
- 사실 잘모르겠음.
*/

class Solution {
    
    int n, m, result, ans[];
    boolean[] ipn; // ImPossibleNumber - true면 사용불가 숫자
    boolean[][] used; // i번째 입력정수가 사용한 숫자에 대해서 true
    
    private void init(int n, int[] ans) {
        this.n = n;
        this.m = ans.length;
        this.ans = ans;
        result = 0;
        
        ipn = new boolean[n + 1];
        used = new boolean[m][n + 1];
    }
    
    
    public int solution(int n, int[][] q, int[] ans) {
        
        init(n, ans);
        
        for(int i = 0; i < ans.length; i++) {
            
            for(int num : q[i]) {
                used[i][num] = true;
            }
            
            if(ans[i] == 0) {
                for(int j = 0; j < 5; j++) {
                    int num = q[i][j];
                    ipn[num] = true;
                }
            }
        }
        
        combination(0, 0, new int[]{ 0, 0, 0, 0, 0 });
        
        return result;
    }
    
    // ipn[i] = false인 숫자들에 대해서 5개 뽑아 순차적 조합
    private void combination(int num, int cnt, int[] comb) {
        
        // 기저
        if(cnt == 5) {
            if(isPossibleCode(comb)) result++;
            return;
        }
        
        for(int i = num + 1; i <= n; i++) {
            
            if(ipn[i]) continue;
            
            ipn[i] = true;
            comb[cnt] = i;
            combination(i, cnt + 1, comb);
            comb[cnt] = 0;
            ipn[i] = false;
        }
        
    }
    
    // 완성된 조합 코드에 대해서 q[i]에 ans[i]개만 일치하는지 확인
    private boolean isPossibleCode(int[] comb) {
        
        for(int i = 0; i < m; i++) {
            
            int cnt = 0;
            for(int num : comb) {
                if(used[i][num]) {
                    cnt++;
                }    
            }
            
            if(cnt != ans[i]) return false;
        }
        
        return true;
    }
}
