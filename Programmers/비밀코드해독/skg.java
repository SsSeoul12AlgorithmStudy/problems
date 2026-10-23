import java.util.*;

class Solution {
    boolean[] pick;
    int n, m;
    int[][] q;
    int[] ans;
    int[] arr;
    int answer = 0;

    public int solution(int n, int[][] q, int[] ans) {
        this.n = n;
        this.q = q;
        this.ans = ans;
        this.m = ans.length;
        pick = new boolean[n + 1];
        
        arr = new int[5];
        bt(0, 0);
        
        
        return answer;
    }
    
    Set<Integer> picked = new TreeSet<>();
    
    
    static int cnt;
  
    
    // 1부터 n까지 중에 5개를 고르기
    void bt(int k, int last) {
        if (k == 5) {
            if (verify()) {
                answer++;
            }
            
            return;
        }
        
        for (int i = last+1; i <= n; i++) {
            arr[k] = i;
            bt(k + 1, i);
        }
    }
    
    // 선택된 수들이 가능한지 확인
    boolean verify() {
        for (int i = 0; i < m; i++) {
            int[] nums = q[i];
            int cnt = ans[i];
            int correct = 0;
            for (int num : nums) {
                for (int a : arr) {
                    if (num == a)   correct++;
                }
            }
            if (correct != cnt) return false;
        }
        
        return true;
    }
}

/*
걍 백트래킹 하면서 프루닝 하면 안되나? 
근데 수가 1부터 n까지 있음.
이 중에 서로 다른 정수 5개
n이 30까지니까 그냥 무식하게 하기?

입력 정수에서 2개 골라서 pick[i] = true로 바꾸고 진행
다음 입력 정수에서 이미 고른 녀석들 제외하고 pick[i] = true로 바꾸고 진행
만약 true로 바뀐 애들이 5개가 넘어간다면 실패.
만약 마지막까지 진행했는데 골라진 애들이 5개보다 적다면 그 수만큼 n을 곱해서 가능한 경우에 더하기

백트래킹 안에 백트래킹?
각 단계마다 조합으로 5개 중 x개 선택

만약 선택된 수가 5개보다 많아지면 바로 리턴

m이 10개밖에 없으니까 각각의 선택에서 선택한 수를 보관?

각 단계마다 백트래킹(조합) -> 지금까지 진행 상황과 대조 - 5개보다 많아지면 바로 종료


아 뭔가 생각은 나는데 구현이 좀 그렇네..
조합을 고르고 복원을 어떻게 하지?

각 단계마다 Set을 둔다..
그리고 다음 단계로 넘어갈 때마다 Set을 합쳐서 개수 확인?

오름차순..

아니면 1~n 중에 5개를 뽑고 무작정 대조하기?

*/
