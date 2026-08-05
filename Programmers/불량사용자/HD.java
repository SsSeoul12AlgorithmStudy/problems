import java.util.HashSet;
import java.util.Set;
/*
 1) 정지ID 별로 가능한 유저ID를 boolean 타입의 2차원 배열로 표현 - included
 2) 백트래킹으로 조합 찾으며 마지막에 visited에 true인 값들을 String으로 묶어 Set에 넣으며 중복제거
 
 고민사항 - 2번에서 중복제거한 방법이 효율적인가? 싶음. 더 좋은 방법 제시해주십쇼.!
 */
class Solution {

	static int B, U;
	static Set<String> set;
	static boolean[] visited;
	static boolean[][] included;

	public int solution(String[] user_id, String[] banned_id) {

		B = banned_id.length;
		U = user_id.length;

		included = new boolean[B][U];
		visited = new boolean[U];
		set = new HashSet<>();
		// 1) banId별 가능한 userId 연결
		findPossibleBanUser(banned_id, user_id);
		// 2) 백트래킹
		findComb(0, 0);

		return set.size();
	}

	// 1) banId별 가능한 userId 연결
	private static void findPossibleBanUser(String[] banned_id, String[] user_id) {
		for (int i = 0; i < B; i++) {
			String ban = banned_id[i];

			for (int j = 0; j < U; j++) {
				String user = user_id[j];

				if (user.length() != ban.length()) continue;

				int diff = 0;
				for (int k = 0; k < ban.length(); k++) {
					if (ban.charAt(k) != '*' && user.charAt(k) != ban.charAt(k)) {
						diff++;
						break;
					}
				}

				if (diff == 0) included[i][j] = true;
			}
		}
	}

	// 2) 조합 찾기 - 백트래킹
	private static void findComb(int node, int depth) {

		if (depth == B) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < U; i++) {
				if (visited[i]) {
					sb.append(i);
				}
			}

			if (!set.contains(sb)) set.add(sb.toString());
			
			return;
		}

		for (int i = 0; i < U; i++) {
			if (included[node][i] && !visited[i]) {
				visited[i] = true;
				findComb(node + 1, depth + 1);
				visited[i] = false;
			}
		}
	}
}