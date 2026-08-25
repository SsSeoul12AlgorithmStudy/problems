import java.util.*;

/*
 * 그리드로 접근하기 (Bucket)
 * mLimit/10을 그리드 사이즈로 두고, 인접 9칸만 살펴본다.
 * 어떤 칸에 있는 라디오를 살펴보고 필요한 파워를 PQ에 넣는다.
 */

class Radio {
	int id, freq, x, y;

	Radio(int id, int freq, int x, int y) {
		this.id = id;
		this.freq = freq;
		this.x = x;
		this.y = y;
	}
}

class RadioI {
	int power, id;

	RadioI(int power, int id) {
		this.power = power;
		this.id = id;
	}
}

class Node {
	int x, y;

	Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class UserSolution {

	int grid_size, grid_N;
	List<Radio>[][] grid;
	// id별로 저장된 Radio를 나타내는 맵
	Map<Integer, Radio> id_map;
	int power_limit;

	void init(int N, int mLimit) {
		int max_dist = mLimit / 10;
		grid_size = max_dist;
		grid_N = (N - 1) / grid_size + 1;

		grid = new List[grid_N][grid_N];
		for (int i = 0; i < grid_N; i++) {
			for (int j = 0; j < grid_N; j++) {
				grid[i][j] = new ArrayList<Radio>();
			}
		}

		power_limit = mLimit;
		id_map = new HashMap<>();
	}

	// 해당되는 그리드에 정보 넣기.
	void addRadio(int K, int mID[], int mFreq[], int mY[], int mX[]) {
		for (int i = 0; i < K; i++) {
			Radio r = new Radio(mID[i], mFreq[i], mY[i], mX[i]);
			int gx = r.x / grid_size;
			int gy = r.y / grid_size;
			grid[gx][gy].add(r);
			id_map.put(r.id, r);
		}
	}

	final int[][] dir = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 },
			{ 0, 0 } };


	PriorityQueue<RadioI> PQ;

	int getMinPower(int mID, int mCount) {
		int result = 0;

		Radio target = id_map.get(mID);
		PQ = new PriorityQueue<>((a, b) -> {
			if (a.power == b.power) {
				return Integer.compare(a.id, b.id);
			}
			return Integer.compare(a.power, b.power);
		});

		for (int[] d : dir) {
			int gx = d[0] + (target.x / grid_size);
			int gy = d[1] + (target.y / grid_size);
			if (gx < 0 || gy < 0 || gx >= grid_N || gy >= grid_N)
				continue;
			addRadiosPQ(target, gx, gy);
		}

		for (int i = 0; i < mCount; i++) {
			RadioI cur = PQ.poll();
			result += cur.power;
		}

		// System.out.println("get minPower: " + result);
		return result;
	}

	// (gx, gy)에 해당하는 그리드의 모든 라디오를 비교해서 power_limit보다 파워가 낮으면 PQ에 추가.
	void addRadiosPQ(Radio target, int gx, int gy) {
		for (Radio r : grid[gx][gy]) {
			if (target.id == r.id)
				continue;
			int power = (Math.abs(target.x - r.x) + Math.abs(target.y - r.y)) * 10 + (target.freq != r.freq ? 1000 : 0);
			if (power > power_limit)
				continue;
			PQ.add(new RadioI(power, r.id));
		}
	}
}
