import java.util.*;

class Solution {
    /*
    
    19:20 ~ 20:40
    
    라이언이 어피치를 큰 점수차로 이기기 위해서 과녁을 맞추는 경우를 구하여라
    
    ---
    
    완탐으로 풀려면?
    - 각 점수를 따는 경우와 아닌 경우로 나누어서 전체 경우를 따져본다
    - 2^11개의 경우의 수
    
    일부 테케에서 실패하는 이유는?
    - 반례가 있을까
    */
    
    private int[] answer;
    private int scoreDiff;
    private int[] shots;
    
    static int[] expect = new int[]{1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
    
    public int[] solution(int n, int[] info) {
        answer = new int[] { -1 };
        scoreDiff = 0;
        shots = new int[11];
        
        solve(0, n, info);
        
        return answer;
    }
    
    // 각 과녁에서 점수를 딸지 말지 결정하여 최종 점수를 계산
    private void solve(int depth, int arrows, int[] info){
        if(arrows < 0){
            return;
        }
        
        if(depth >= 11){ 
            if(arrows > 0){
                shots[10] = arrows;
            }
            
            int lion = 0;
            int apeach = 0;
            
            for(int i = 0; i < 10; i++){
                if(info[i] != 0 && info[i] >= shots[i]){
                    apeach += 10 - i;
                }
                if(shots[i] > info[i]){
                    lion += 10 - i;
                }
            }
            
            int diff = lion - apeach;
            
            // //debug
            // if(debugCond(shots, expect)){
            //     System.out.println(Arrays.toString(shots));
            //     System.out.println("diff = " + diff + ", scoreDiff = " + scoreDiff);
            // }
            
            if(diff > scoreDiff){
                scoreDiff = diff;
                answer = Arrays.copyOf(shots, shots.length);
            }
            
            if(diff > 0 && diff == scoreDiff){
                boolean moreLowScores = false;
                
                for(int i = 10; i >= 0; i--){
                    if(answer[i] < shots[i]){
                        moreLowScores = true;
                        break;
                    }
                    if(answer[i] > shots[i]){
                        break;
                    }
                }
                
                if(moreLowScores){
                    answer = Arrays.copyOf(shots, shots.length);
                }
            }
            
            if(arrows > 0){
                shots[10] = 0;
            }
            
            return;
        }
        
        int gain = 10 - depth;
        int shot = info[depth] + 1;
        
        shots[depth] = shot;
        solve(depth + 1, arrows - shot, info);
        shots[depth] = 0;
        
        solve(depth + 1, arrows, info);
    }
    
    private boolean debugCond(int[] a, int[] b){
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i]) return false;
        }
        return true;
    }
}