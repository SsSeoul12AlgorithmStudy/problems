
class Solution {
    private static int answer, n;
    private static int[][] q;
    private static int[] ans;
    private static int[] code;

    public int solution(int n, int[][] q, int[] ans) {
        this.n = n;
        this.q = q;
        this.ans = ans;
        answer = 0;
        code = new int[5]; // 원문 코드 예상

        dfs(1, 0); // 코드 시작 숫자, 현재까지 선택한 숫자의 개수

        return answer;
    }

    // 원문 코드 다 만들어보기
    private void dfs(int start, int idx) {
        if (idx == 5) {
            if (isValid()) answer++; 
            return;
        }

        // 오름차순으로 중복 없이 숫자를 선택
        for (int num = start; num <= n; num++) {
            code[idx] = num;
            dfs(num + 1, idx + 1);
        }
    }

    // 테스트 코드와 맞춰보면서 적합한 코드인지 확인
    private boolean isValid() {
        for (int i = 0; i < q.length; i++) {
            int cnt = 0;

            // 현재 비밀 코드와 i번째 테스트 코드의 공통 숫자 개수
            for (int num : code) {
                for (int target : q[i]) {
                    if (num == target) {
                        cnt++;
                        break;
                    }
                }
            }

            // 맞힌 개수가 다르면 제외
            if (cnt != ans[i]) return false;
        }

        return true;
    }
}
