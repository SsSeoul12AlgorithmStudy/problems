import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class Solution {
	static int N, M;
	// 첫번째 0의 등장 빈도는 필요 없다.
	static final String[] RATIO = { "211", "221", "122", "411", "132", "231", "114", "312", "213", "112" };

	public static void main(String args[]) throws Exception {
		StringTokenizer st = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			int T;
			T = Integer.parseInt(br.readLine());

			for (int test_case = 1; test_case <= T; test_case++) {
				Set<Integer> set = new HashSet<>();
				st = new StringTokenizer(br.readLine());
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for (int i = 0; i < N; i++) {
					String s = br.readLine();
					// 한 줄을 통째로 2진수 문자열로 바꾸기.
					char[] carr = transformArr(s.toCharArray()).toCharArray();

					// 접근: 한 줄을 2진수 문자열로 만들어두고, 그 줄에 대해서
					// 1을 만나면 -> 8번 동안 1-0-1 개수 세기를 진행
					// 위 과정을 계속 반복
					int idx = 0;
					while (idx < carr.length) {
						// 의미 없는 0 넘기기
						while ((idx < carr.length) && (carr[idx] == '0'))
							idx++;
						if (idx >= carr.length)
							break;

						// 여기까지 왔다는 건 1을 만났다는 뜻
						int code = 0;
						for (int x = 0; x < 8; x++) {
							int cnt = 0;
							int[] ratios = new int[3];
							// 처음으로 마주하는 연속된 0들은 필요없으므로 넘기기
							while ((idx < carr.length) && (carr[idx] == '0'))
								idx++;
							// 1 개수 세기
							// while (carr[idx++] == '1') cnt++; -> 이렇게 하면 안됨. 틀릴 때도 while 문에서 비교하고 넘어감
							while (carr[idx] == '1') {
								cnt++;
								idx++;
							}
							ratios[0] =  cnt;
							cnt = 0;
							// 0 개수 세기
							while (carr[idx] == '0') {
								idx++;
								cnt++;
							}
							ratios[1] =  cnt;
							cnt = 0;
							// 1 개수 세기
							while (carr[idx] == '1') {
								idx++;
								cnt++;
							}
							ratios[2] =  cnt;
							
							// sb에 있는 녀석들 최솟값으로 나눠서 비율 형태로 만들기 -> ex) 224면 112로
							final int min = Arrays.stream(ratios).min().orElse(1);
							ratios = Arrays.stream(ratios).map(v -> v / min).toArray();
							// code 변수에 숫자 형태로 넣어주기
							code *= 10;
							code += decode(ratios);
						}
						set.add(code);
					}

				}

				int answer = 0;

				for (int c : set) {
					answer += validTest(c);
				}

				System.out.println("#" + test_case + " " + answer);

			}
		}

	}

	// 16진수 문자열 -> 2진수 String 변환
	static String transformArr(char[] hex) {
		StringBuilder sb = new StringBuilder();
		for (char c : hex) {
			String s = transformTwo(transformTen(c));
			sb.append(s);
		}

		return sb.toString();
	}

	// 16진수 하나 -> 10진수 변환
	static int transformTen(char c) {
		if (c >= 'A' && c <= 'F') {
			return 10 + c - 'A';
		}
		return c - '0';
	}

	// 10 진수 -> 2진수 변환 (String 반환)
	static String transformTwo(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 3; i >= 0; i--) {
			sb.append(num % 2);
			num /= 2;
		}

		return sb.reverse().toString();
	}
	
	// 주어진 등장 횟수 비율을 해독
	static int decode(int[] ratios) {
		int sum = 0;
		for (int r : ratios) {
			sum *= 10;
			sum += r;
		}
		for (int i = 0; i < RATIO.length; i++) {
			if (RATIO[i].equals(Integer.toString(sum)))
				return i;
		}

		return -1;
	}

	// 주어진 암호코드가 올바른 암호코드면 각 숫자의 합 반환, 그렇지 않다면 0 반환
	static int validTest(int code) {
		int odd_sum = 0;
		int even_sum = 0;
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				odd_sum += code % 10;
			} else {
				even_sum += code % 10;
			}
			code /= 10;
		}

		if ((even_sum * 3 + odd_sum) % 10 == 0) {
			return even_sum + odd_sum;
		}
		return 0;
	}
}