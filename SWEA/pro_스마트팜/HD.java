import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

// 19/25 테케
class UserSolution {

	int N, ID, currentTime;
	int[] growTime; // 품종별(0, 1, 2) 성장 시간
	int[][] time; // 심어놓은 위치별 지난 시간
	int[][] size; // 심어놓은 농작물 크기
	int[][] number; // 심어놓은 농작물 ID
	int[][] category; // 품종
	HashMap<Integer, int[]>[] points; // 품종별 ID(농작물ID) key , 심은위치(r,c)

	void init(int N, int mGrowthTime[]) {
		this.N = N;
		growTime = Arrays.copyOf(mGrowthTime, 3);
		number = new int[N][N];
		time = new int[N][N];
		size = new int[N][N];
		category = new int[N][N];
		points = new HashMap[3];
		for (int i = 0; i < 3; i++) {
			points[i] = new HashMap<>();
		}

		for (int i = 0; i < N; i++) {
			Arrays.fill(number[i], -1);
			Arrays.fill(time[i], -1);
			Arrays.fill(size[i], -1);
			Arrays.fill(category[i], -1);
		}

		ID = 1;
		currentTime = 0;
	}

	int sow(int mTime, int mRow, int mCol, int mCategory) {

		increaseTime(mTime);

		if (number[mRow][mCol] != -1) return 0;

		category[mRow][mCol] = mCategory;
		number[mRow][mCol] = ID;
		points[mCategory].put(ID++, new int[] { mRow, mCol });
		size[mRow][mCol] = 0;
		time[mRow][mCol] = 0;

		return 1;
	}

	int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {

		increaseTime(mTime);

		int cnt = 0;
		for (int r = mRow; r < mRow + mHeight; r++) {
			for (int c = mCol; c < mCol + mWidth; c++) {
				if (number[r][c] != -1) {
					cnt++;
					size[r][c] += G;
				}
			}
		}

		return cnt;
	}

	int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {

		increaseTime(mTime);
		Queue<int[]> rm = new ArrayDeque<>();
		// 수확 지역의 작물들의 크기가 L 이상인가

		for (int r = mRow; r < mRow + mHeight; r++) {
			for (int c = mCol; c < mCol + mWidth; c++) {
				// 작물이 없으면 무시함
				if (size[r][c] == -1)
					continue;

				// L미만이 존재하면
				if (size[r][c] < L) {
					return 0;
				}

				rm.offer(new int[] { r, c });
			}
		}

		int result = rm.size();
        
		while (!rm.isEmpty()) {
			int[] now = rm.poll();
			int r = now[0], c = now[1];

			size[r][c] = -1;
			time[r][c] = -1;
			int id = number[r][c];
			int k = category[r][c];
			points[k].remove(id);
			number[r][c] = -1;
			category[r][c] = -1;
		}

		return result;
	}


	void increaseTime(int mTime) {

		int elapsed = mTime - currentTime;

		for (int i = 0; i < 3; i++) {

			for (int[] p : points[i].values()) {
				int x = p[0];
				int y = p[1];

				int totalTime = time[x][y] + elapsed;

				size[x][y] += totalTime / growTime[i];
				time[x][y] = totalTime % growTime[i];
			}
		}

		currentTime = mTime;
	}
}
