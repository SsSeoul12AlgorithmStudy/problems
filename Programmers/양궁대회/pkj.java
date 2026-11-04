/*
dfs를 통한 완탐
라이언이 어피치를 이길 수 있는 최소 화살 수를 lion배열에 저장해두고
해당 점수에서 이긴 경우와 진 경우를 재귀 돌면서 해당 점수 lion값을 0으로 바꾸던지 냅두던지
탐색이 끝나면 이전 최대 점수 차와 비교해서 크다면 lion 값으로 answer를 갱신
*/

class Solution {
    static int[] lion;
    static int[] answer;
    static int max;

    public int[] solution(int n, int[] info) {
        lion = new int[11]; //라이언이 이기기 위해 필요한 최소 화살 수
        answer = new int[11];
        max = 0; //최대 점수 차
        
        for (int i = 0; i < 11; i++) {
            lion[i] = info[i] + 1;
        }

        //탐색 중인 점수, 남은 화살, 라이언 점수, 어피치 점수, 어피치 화살 목록
        dfs(0, n, 0, 0, info);

        return max == 0 ? new int[]{-1} : answer;
    }

    void dfs(int idx, int remain, int lionScore, int apeachScore, int[] info) {
        if (idx == 11) {
            lion[10] += remain; //남은 화살은 0점에
            int gap = lionScore - apeachScore; //점수 차

            //점수 차가 이전 점수차보다 크거나
            //점수 차가 이전과 같지만 낮은 점수가 더 많은 경우
            if (gap > max || (gap == max && gap > 0 && isBetter())) {
                max = gap; //최대 차 갱신
                answer = lion.clone(); //최대 구성 갱신
            }

            lion[10] -= remain; //배열 복구
            return;
        }

        //화살이 충분하면
        if (remain >= lion[idx]) { 
            int need = lion[idx]; //필요한 화살

            //라이언이 이기는 경우
            dfs(idx + 1, remain - need, lionScore + (10 - idx), apeachScore, info);
        }

        //배열 원복을 위한 현재 원소값 저장
        int temp = lion[idx]; 
        lion[idx] = 0;

        //라이언이 지는 경우
        dfs(idx + 1, remain, lionScore, apeachScore + (info[idx] > 0 ? 10 - idx : 0), info);
        
        lion[idx] = temp; //배열 복구
    }

    //동점일 경우 낮은 점수를 더 많이 맞췄는지 판단 
    boolean isBetter() {
        for (int i = 10; i >= 0; i--) {
            if (lion[i] > answer[i]) return true;
            if (lion[i] < answer[i]) return false;
        }

        return false;
    }
}
