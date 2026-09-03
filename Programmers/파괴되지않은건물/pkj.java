class Solution {
    public int solution(int[][] board, int[][] skill) {
        int n = board.length;
        int m = board[0].length;

        // 변경량만 기록, 변경값 시작점과 변경 끝점
        int[][] diff = new int[n + 1][m + 1];

        for (int[] s : skill) {
            int type = s[0];
            int r1 = s[1];
            int c1 = s[2];
            int r2 = s[3];
            int c2 = s[4];
            int degree = s[5];

            // 공격이면 음수, 회복이면 양수
            int value = (type == 1) ? -degree : degree;

            // 영역 모서리 변경값 저장
            diff[r1][c1] += value;
            diff[r1][c2 + 1] -= value;
            diff[r2 + 1][c1] -= value;
            diff[r2 + 1][c2 + 1] += value;
        }

        // 변경 복구 행
        for (int r = 0; r < n; r++) {
            for (int c = 1; c < m; c++) {
                diff[r][c] += diff[r][c - 1];
            }
        }

        // 변경 복구 열
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
