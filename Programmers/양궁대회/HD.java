/*

09:17 ~ 

라이언이 가장 큰 점수 차이로 우승할 수 있는 경우의 라이언 점수판 리턴


최종점수가 같을경우 - 어피치 승리
k점에 대해 어피치 a발 라이언 b발 - > a = b 이면 어피치 k점 득점

시간복잡도
n 최대 - 10
10 ~ 0점 - 11

완탐해도 시간적 문제 없지않나?

실수1 - 중복제거안하고 순열조회해버림. -> 시간초과나서 당황
실수2 - lion = other 주소값 복사해버림. 당연한건데 바보같았음.
실수3 - 점수차가 같은경우 other[i] > lion[i] 을 찾을때까지 검사하고 갱신함.

오늘은 바보임.

*/

class Solution {
    
    int n, maxDiff;
    int[] peach, lion;
    
    public void init(int n, int[] info) {
        this.n = n;
        peach = info;
        maxDiff = -1;
        lion = new int[11];
    }
    
    public int[] solution(int n, int[] info) {
        
        init(n, info);
        
        int[] result = new int[11];
        dfs(result, 0, 0);
        
        if(maxDiff > 0) return lion;
        
        return new int[] { -1 };
    }
    
    
    private void dfs(int[] other, int start, int depth) {
        
        // depth == n 이 될때까지 중복 조합 만들기
        // 가지치기 1 인덱스 i other[i]
        if(depth == n) {
            compareScore(other);
            return;
        }
        
        // [실수1]
        for(int i = start; i < 11; i++) {
            if(other[i] > peach[i]) continue;
            other[i]++;
            dfs(other, i, depth + 1);
            other[i]--;
        }
    }
    
    private void compareScore(int[] other) {
        
        // 1. 점수 계산
        int lionScore = 0;
        int peachScore = 0;
        for(int i = 0; i < 11; i++) {
            int score = 10 - i;
            
            // 둘다 못맞추면 노카운트
            if(peach[i] == 0 && other[i] == 0) continue;
            
            // 라이언이 더 많이 맞췄다.
            if(peach[i] < other[i]) {
                lionScore += score;
            } else { // 동률이거나 피치가 더 많이 맞췄다.
                peachScore += score;
            }
        }
        
        // 2. 점수 차에 따른 갱신
        
        if(peachScore >= lionScore) return;
        
        int nowDiff = lionScore - peachScore;
        
        if(nowDiff > maxDiff) {
            lion = other.clone();
            maxDiff = nowDiff;
            return;
        }
        
        if(nowDiff == maxDiff) {
            for(int i = 10; i >= 0; i--) {
                
                if(other[i] == lion[i]) continue;
                // 현재 케이스가 더 큰경우만 갱신
                if(other[i] > lion[i]) lion = other.clone();
                // 더 작으면 갱신안해야함.
                break;
            }
        }
    }
}
