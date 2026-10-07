/*
문제가 좀 어렵네..
그러니까 정리하자면 경찰한테 붙잡히지 않도록 모든 물건을 훔쳤을 떄, A 도둑이 남긴 누적 흔적의 최솟값을 구하여라~
불가능하면 -1 반환.

흔적 개수는 1,2,3 중 하나.

브루트포스로 구현? 
백트래킹으로 구현?
흔적 개수 초과될 때 프루닝..
근데 이제 기울어진 트리가 되도록?
B가 흔적 남기는 걸 우선해서 백트래킹.
가능한 경우가 최초로 생기면 그게 정답.

근데 이렇게 하면 지금은 A가 훔치고 나중에 B가 더 많이 훔칠 수 있는 경우를 포착 못함.
(조기 종료 해버려서)
그럼 결국 백트래킹 끝까지 해야 하네..

A, B 흔적 개수가 다른 게 너무 헷갈리네..

모든 경우를 살펴보지 않고도 판단할 수 있는 방법이 있을까?
B한테 몰빵하고, A한테 빼주기?
근데 이렇게 한다면 뭘 뺄지 어떻게 정할까?

우선순위 큐로 A 흔적 오름차순, B 흔적 오름차순?
B 흔적 비용들을 배열로 둔다.
B흔적 1 List
B흔적 2 List
B흔적 3 List
이렇게 두고 A 흔적 비용을 List에 추가
각 배열에 대해서 A 흔적 비용 오름차순 정렬

만약 B 흔적 총합이 6이고 m이 4면은 B 흔적을 3 이상 제거해야 함.
이 때 가능한 경우는
1 1 1
2 1
2 2
3

7 이상 제거 시에는
3 3 3
3 3 2
3 3 1
3 2 2
3 2 1 1
3 1 1 1 1
2 2 1 1 1

7부터?
1 1 1 1 1 1 1
2 1 1 1 1 1


DP?


*/
import java.util.*;

class Solution {
    int stuff_cnt ;
    int n, m;
    int[][] info;
    int answer = Integer.MAX_VALUE;
    int goal;
    int[] arr = new int[4];
    // traceA[k] : B 흔적 비용이 k인 A 흔적 비용의 배열
    List<Integer>[] traceA = new List[4];
    
    public int solution(int[][] info, int n, int m) {
        stuff_cnt = info.length;
        this.info = info;
        this.n = n;
        this.m = m;
        int sumB = 0;
        for (int i = 0; i < stuff_cnt; i++) {
            sumB += info[i][1];
        }
        for (int i = 1; i <= 3; i++) {
            traceA[i] = new ArrayList<>();
        }
        for (int i = 0; i < stuff_cnt; i++) {
            traceA[info[i][1]].add(info[i][0]);
        }
        for (int i = 1; i <= 3; i++) {
            traceA[i].sort((a,b) -> Integer.compare(a,b));
        }
        goal = sumB - (m - 1);
        System.out.println("goal is " + goal);
        
        Backtracking(goal, 3);        
        
        if (answer == Integer.MAX_VALUE)    answer = -1;
        return answer;
    }
    
    void Backtracking(int remain, int last) {
        // 필요한 B흔적보다 많으면 가지치기
        for (int i = 1; i <= 3; i++) {
            if (traceA[i].size() < arr[i])
                return;
        }
        
        if (remain <= 0) {
            // 필요한만큼 빼기
            int result = 0;
            for (int i = 1; i <= 3; i++) {
                for (int j = 0; j < arr[i]; j++) {
                    result += traceA[i].get(j);
                    if (result >= n)    return;
                }
            }
            answer = Math.min(answer, result);
            return;
        }
        
        for (int i = last; i >= 1; i--) {
            arr[i]++;
            Backtracking(remain - i, i);
            arr[i]--;
        }
    }
}
