import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 초기 아이디어 이차원 ArrayList에 각 공유기간 필요한 파워양 계산 
 * (아래쪽 반삼각형만 쓴다 -> (a, b)에서 a < b여야 함. 이래야 한 행만 추가한다) 
 * 이러고 나중에 특정 idx를 살펴봐야 되면 그 열을 살펴보면 될 것.
 * 각 idx에 해당하는 mId 저장해놓기 (mId -> idx가 되는 Map형태)
 */
// 아니면 PriorityQueue 여러 개 쓰는 게 나을 수도 있겠다. 
// Key: mID, Value: PQ<{int mID, int power}> - power 기준 정렬
// 근데 이러면 특정 mID 기준으로 못 살펴본다. (그러려면 양쪽 PQ에 넣어야하는데 그건 좀..)

/**
 * 도저히 모르겠어서 제미나이 찬스 -> 버켓 단위로 관리해라.
 * p = dist * 10 + a <= mLimit이므로 
 * 물리적 맨해튼 거리(dist)는 최대 Math.floor(mLimit/10)을 넘을 수 없다.
 * 따라서 모든 칸을 mLimit/10 x mLimit/10 크기의 격자로 나누고,
 * 인접한 9칸에 있는 공유기만 파워를 계산하자.
 */

/**
 * 12개까지 맞춘 게 한계 (6시간의 전투 끝에 장렬히 전사)
 */
class Node {
	int x, y;

	Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class UserSolution {
	int N, mLimit;
	final int INF_POWER = 10000 * 2 + 1000;
	int bucketSize, bucketCnt;
	final int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0},
			{1,1}, {1,-1}, {-1,1}, {-1,-1}, {0,0}
	};
	
	List<List<Integer>> powerArr;
	// id -> i
	Map<Integer, Integer> idMap;
	// index -> freq
	Map<Integer, Integer> freqMap;
	// 위치 정보를 저장하는 배열
	ArrayList<Node> pos;
	// minPower 결과를 저장한 ArrayList arr[i][j] : idx가 i인 공유기의 mCount가 j일 때 값
//	List<List<Integer>> minPower;
	List<Integer>[][] bucket;
	
	void init(int N, int mLimit) {
		this.N = N;
		this.mLimit = mLimit;
		powerArr = new ArrayList<>();
		idMap = new HashMap<>();
		freqMap = new HashMap<>();
		pos = new ArrayList<>();
		
		this.bucketSize = mLimit/10;
		this.bucketCnt = (N / this.bucketSize) + (N % bucketSize > 0 ? 1 : 0);
		bucket = new ArrayList[bucketCnt][bucketCnt];
		for (int i = 0; i < bucketCnt; i++) {
			for (int j = 0; j < bucketCnt; j++) {
				bucket[i][j] = new ArrayList<>();
			}
		}
	}

	void addRadio(int K, int mID[], int mFreq[], int mY[], int mX[])
	{
		for (int i = 0; i < K; i++) {
			int curIdx = powerArr.size();
			// mID 저장
			idMap.put(mID[i], curIdx);
			// freq 저장
			freqMap.put(curIdx, mFreq[i]);
			// 위치 정보 저장
			pos.add(new Node(mY[i], mX[i]));
			powerArr.add(new ArrayList<>());
			
			int x = mX[i] / bucketSize;
			int y= mY[i] / bucketSize;
			
			// 인접 9 버켓만 순회
			for (int[] d: dir) {
				int dx = x + d[0];
				int dy = y + d[1];
				if (dx < 0 || dy < 0 || dx >= bucketCnt || dy >= bucketCnt)	continue;
				List<Integer> b = bucket[dx][dy];
				for (int j : b) {
					int dist = Math.abs(pos.get(curIdx).x - pos.get(j).x) + Math.abs(pos.get(curIdx).y - pos.get(j).y);
					int p = dist * 10 + (Integer.compare(freqMap.get(curIdx), freqMap.get(j)) != 0 ? 1000 : 0);
					if (p > mLimit)	continue;
					powerArr.get(curIdx).add(p);
					powerArr.get(j).add(p);
				}
			}
			bucket[x][y].add(curIdx);
//			for (int j = 0; j < curIdx; j++) {
//				// i번째 공유기와 j번째 공유기가 연결될 때 드는 power 계산
//				int dist = Math.abs(pos.get(curIdx).x - pos.get(j).x) + Math.abs(pos.get(curIdx).y - pos.get(j).y);
//				int p = dist * 10 + (Integer.compare(freqMap.get(curIdx), freqMap.get(j)) != 0 ? 1000 : 0);
//				if (p > mLimit)	continue;
//				powerArr.get(curIdx).add(p);
//				powerArr.get(j).add(p);
////				System.out.printf("%d-%d of power : %d\n", curIdx, j, p);
////				System.out.printf("freq: %d and %d, is %b\n", freqMap.get(curIdx), freqMap.get(j), Integer.compare(freqMap.get(curIdx), freqMap.get(j)) != 0);
//			}
		}
	}

	int getMinPower(int mID, int mCount) {
		List<Integer> arr = powerArr.get(idMap.get(mID));
		// 매번 정렬하는 게 문제인 것 같은데
		arr.sort((a, b) -> Integer.compare(a, b));
		int sum = 0;
		for (int i = 0; i < mCount; i++) {
			sum += arr.get(i);
		}
		
		return sum;
	}
	

}