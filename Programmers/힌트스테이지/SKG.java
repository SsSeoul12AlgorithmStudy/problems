/*
그래프?
아님 DP인가..
근데 1번 스테이지는 무조건 힌트 안쓰고 해결해야 하는 거 아닌가?

16스테이지, 힌트 번들 최대 19개
그리디로 생각하면 안되는 게 당장 힌트 번들 판매가격보다 힌트권 사용으로 싸지는 게 적다고 해도 
나중에 해결 비용이 팍 떨어질 수도 있음.

각 스테이지마다 할 수 있는 선택은 두 가지 중 하나뿐. 힌트 번들 사거나 말거나..
그러면 최대 16스테이지니까 나올 수 있는 경우의 수는 2^16.. 너무 많은디

힌트 번들에 쓴 돈 - 힌트권 사용으로 절약한 돈 -> 이 최소가 되어야 함.

20 20 10 50
20 10 140 110
30 10 60 40
40 40 10 50
40 10 30 10

흠 졸리군
백트래킹 프루닝으로 풀 수 있나?
......
결국 끝까지 다 봐야하니까 못 할 것 같은데
특정 스테이지에 도달해도 힌트 "번들"이니까 힌트 가격을 매기는 게 애매함.

2^16 = 1024 * 2^6 = 1024 * 64 = 대략 60000? 생각보다 적은가?
오잉 되네?
*/

class Solution {
    int hint_n; // 힌트권 개수
    int stage_n; // 스테이지 개수
    
    int[] hint_per_stage; // 지금까지 모은 스테이지별 힌트 개수
    
    int min_cost = Integer.MAX_VALUE;
    int[][] cost;
    int[][] hint;
    
    public int solution(int[][] cost, int[][] hint) {
        int answer = 0;
        this.cost = cost;
        this.hint = hint;
        
        stage_n = cost.length;
        hint_n = hint[0].length - 1;
        
        hint_per_stage = new int[stage_n+1];
        
        backtracking(1, 0);
        
        return min_cost;
    }
    
    
    public void backtracking(int stage, int cur_cost) {
        if (stage == stage_n) {
            min_cost = Math.min(min_cost, cur_cost + cost[stage-1][Math.min(stage_n-1, hint_per_stage[stage])]);
            
            return;
        }
        
        // 지금까지 모인 힌트를 보고 비용 결정
        int my_hint = hint_per_stage[stage];
        int my_cost = cost[stage-1][Math.min(stage_n - 1, my_hint)];
        
        // 힌트 구매O
        int my_hint_cost = -1;
        for (int h : hint[stage-1]) {
            if (my_hint_cost < 0) {
                my_hint_cost = h;
            } else {
                hint_per_stage[h]++;
            }
        }
        backtracking(stage+1, cur_cost + my_cost + my_hint_cost);
        // 힌트 구매X
        my_hint_cost = -1;
        for (int h : hint[stage-1]) {
            if (my_hint_cost < 0) {
                my_hint_cost = h;
            } else {
                hint_per_stage[h]--;
            }
        }
        backtracking(stage+1, cur_cost + my_cost);
    }
}
