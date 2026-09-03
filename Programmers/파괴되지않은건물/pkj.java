/*
    변경된 사항을 매번 연산하지 않고 모아뒀다가 한 번에 연산하는 로직
    영역의 모서리에만 이전 위치와 비교한 변경값을 저장해둠
    누적합으로 모든 변경값 업데이트
    그후에 계산 
*/

class Solution {
    public int solution(int[][] board, int[][] skill) {
        int n = board.length;
        int m = board[0].length;

        // 변경될 수치를 영역의 모서리에만 저장해둠 
        // 크기를 1씩 늘린 이유는 diff엔 끝+1로 표시해두기 때문 자바의 substring을 생각하면 됨 
        // 이전 위치에 비해 값이 얼마나 변경됐는지를 저장해둠 
        int[][] diff = new int[n + 1][m + 1];

        for (int[] s : skill) {
            int type = s[0]; // 1은 공격, 2는 회복 
            int r1 = s[1]; // 시작 행
            int c1 = s[2]; // 시작 열
            int r2 = s[3]; // 끝 행
            int c2 = s[4]; // 끝 열
            int degree = s[5]; // 스킬 값

            // 공격이면 음수, 회복이면 양수
            int value = (type == 1) ? -degree : degree;

            // 영역 모서리 변경값 저장
            diff[r1][c1] += value; // 시작점
            diff[r1][c2 + 1] -= value; // 우상단
            diff[r2 + 1][c1] -= value; // 좌하단 
            diff[r2 + 1][c2 + 1] += value; // 우하단
        }

        // 가로 누적합 
        for (int r = 0; r < n; r++) {
            for (int c = 1; c < m; c++) {
                diff[r][c] += diff[r][c - 1];
            }
        }

        // 세로 누적합  
        for (int c = 0; c < m; c++) {
            for (int r = 1; r < n; r++) {
                diff[r][c] += diff[r - 1][c];
            }
        }

        // 최종 체력 계산 및 생존 건물 개수
        int answer = 0;

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (board[r][c] + diff[r][c] > 0) {
                    answer++;
                }
            }
        }

        return answer;
    }
}
