/*
[코딩테스트 접근 순서 지키기]
1. 단순 풀이
2. 시간 복잡도 계산
3. 문제 분석
4. 적절한 알고리즘 및 자료구조 선택

단순 접근
- 핵심은 힌트를 구매하냐 안하냐.
- 힌트 구매 >> 비용증가
- 힌트 사용 >> 비용 감소

안되면 가지치기 해보자

[제한 조건]
- N <= 16
- K <= 20

[알고리즘]
- DFS 완전탐색 + 백트래킹
- 각 스테이지에서 구매 O/X 두 가지 분기
- 구매 시 hintCount 갱신
- 재귀 호출 후 hintCount 원상복구
- 해당 스테이지의 힌트권은 가능한 만큼 모두 사용

[시간복잡도]
1. 분기 수: 2 (구매 O/X)
2. 재귀 깊이: N (선택 분기는 N-1개)
3. 호출당 작업량: O(K)
   - 힌트권 추가: O(K)
   - 힌트권 복구: O(K)

=> O(K * 2^N)

[공간복잡도]
- hintCount 배열: O(N)
- DFS 호출 스택: O(N)
=> O(N)

총 소요시간 : 2시간 초과

** 코테 풀이 접근 지키기. 적절해보이는 알고리즘? 맞다. 아니 맞을 수도 있다. 그렇기에 쉬운거 부터 ㅇㅇ
*/

class Solution {
    
    static int N, result;
    static int[] hintCount;
    static int[][] cost2, hint2;
    
    public int solution(int[][] cost, int[][] hint) {
        N = cost.length;
        result = Integer.MAX_VALUE;
        cost2 = cost;
        hint2 = hint;
        
        hintCount = new int[N];
        
        dfs(0, 0);
        return result;
    }
    
    // num - stage 번호
    // sum - 총 비용
    private static void dfs(int num, int sum) {
        
        // 현재 스테이지에 힌트 쿠폰 사용여부에 따른 비용 계산
        int c = hintCount[num] >= N ? N - 1 : hintCount[num];
        sum += cost2[num][c];
        
        // n번째는 구매 구분이 X
        if(num == N - 1) {
            result = Math.min(result, sum);
            return;
        }
        
        // 힌트 구매O
        int hintPrice = hint2[num][0];
        for(int i = 1; i < hint2[num].length; i++) {
            int hintNum = hint2[num][i] - 1;
            hintCount[hintNum]++;
        }
        
        dfs(num + 1, sum + hintPrice);
        
        // 힌트 구매X 
        for(int i = 1; i < hint2[num].length; i++) {
            int hintNum = hint2[num][i] - 1;
            hintCount[hintNum]--;
        }
        
        dfs(num + 1, sum);
    }
}
