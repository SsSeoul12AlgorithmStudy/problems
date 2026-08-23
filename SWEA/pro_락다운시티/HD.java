package swea.락다운시티;

import java.util.*;

/*
 3시간 - dfs 백트래킹으로 조합 생각 -> 시간초과 생각함
 어떤 알고리즘을 사용해야할지 떠오르지 않음.
 
 [4시간 초과]
 
 1. 25 테케중 17 시간초과.
 -> 200억 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
 
 */
class UserSolution {

	int[][] rank; // 보안등급을 점수화한 map
	int[] pow27; // 알파벳 27진수
	boolean[] existGrade;
	boolean[][] visited;
	List<Integer> gradeScores;
	HashMap<Integer, String> scoreToGrades;

	void init(int N, int M, String mGrade[][]) {
//		System.out.println("init 호출--");
		pow27 = new int[3];
		for (int i = 0; i < 3; i++) {
			pow27[i] = (int) Math.pow(27, 2 - i);
		}
//		System.out.println(Arrays.toString(pow27));

		rank = new int[N][N];

		gradeScores = new ArrayList<>();
		scoreToGrades = new LinkedHashMap<>();
		existGrade = new boolean[19683];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				String grade = mGrade[i][j];
				int score = gradeToScore(grade);
				rank[i][j] = score;

				if (!existGrade[score]) {
					existGrade[score] = true;
					gradeScores.add(score);
					scoreToGrades.put(score, grade);
				}
			}
		}
//		for (int i = 0; i < N; i++) {
//			System.out.println(Arrays.toString(gradeMap[i]));
//		}

		Collections.sort(gradeScores);

//		System.out.println("list - " + gradeScores);
	}

	void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
//		System.out.println("change 호출 -- ");
		int score = gradeToScore(mGrade);

		for (int i = 0; i < mLength; i++) {
			if (mDir == 0) {
				rank[mRow + i][mCol] = score;
			} else {
				rank[mRow][mCol + i] = score;
			}
		}

		if (!existGrade[score]) {
			existGrade[score] = true;
			gradeScores.add(score);
			Collections.sort(gradeScores);
			scoreToGrades.put(score, mGrade);
		}
	}

	String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
//		System.out.println("calculate 호출-- ");
		// s 위치의 등급을 포함한 높은 녀석들로 검사 진행
		// 낮은 등급에서 접근이 가능하면 반복문 종료
		// 방법이 없으면 될때까지
		int startScore = rank[sRow][sCol];

		int startIdx = 0;

		// 시작점 등급 위치 찾기
		for (int i = 0; i < gradeScores.size(); i++) {
			if (gradeScores.get(i) == startScore) {
				startIdx = i;
				break;
			}
		}

		// 시작점 등급부터 낮은 등급 방향으로 탐색
		for (int i = gradeScores.size() - 1; i >= 0; i--) {
			int candidate = gradeScores.get(i);

//			System.out.println(scoreToGrades.get(candidate));
			if (bfs(L, sRow, sCol, eRow, eCol, candidate)) {
				return scoreToGrades.get(candidate);
			}
		}

		return "";
	}

	int[] dr = { -1, 0, 1, 0 }, dc = { 0, -1, 0, 1 };

	boolean bfs(int L, int sRow, int sCol, int eRow, int eCol, int candidate) {

		Queue<int[]> q = new ArrayDeque<>();
		int N = rank.length;
		visited = new boolean[N][N];

		q.offer(new int[] { sRow, sCol, 0 });
		visited[sRow][sCol] = true;

		while (!q.isEmpty()) {
			int[] now = q.poll();

			if (now[0] == eRow && now[1] == eCol) {
				return true;
			}

			if (now[2] >= L)
				continue;

			for (int i = 0; i < 4; i++) {
				int nr = now[0] + dr[i];
				int nc = now[1] + dc[i];

				if (nr < 0 || nc < 0 || nr >= N || nc >= N)
					continue;

				// 방문하지 않았음 && 현재 등급보다 같거나 작은경우
				if (!visited[nr][nc] && rank[nr][nc] >= candidate) {
					visited[nr][nc] = true;
					q.offer(new int[] { nr, nc, now[2] + 1 });
				}
			}
		}

		return false;
	}

	int gradeToScore(String grade) {

		int score = 0;

		for (int i = 0; i < grade.length(); i++) {
			int val = grade.charAt(i) - 'A' + 1;
			score += val * pow27[i];
		}

		return score;
	}

}