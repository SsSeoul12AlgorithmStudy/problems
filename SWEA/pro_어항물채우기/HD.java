import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
/*
 4시간 초과 - 
  문제이해 - 1시간30분
  문제풀이 - 2시간30분? + a
 
 - 인접한 부분 조건식 잘못세워서 애먹음
 - 자료구조와 알고리즘이 명확히 안보였음
 - 일단 구현해봄
 
 1. 시간초과 18/25
 - 이해가 안돼서 구조물 설치확인 메서드 검증 로직 순서 변경
 -> 통과.
 */
class UserSolution {

	class FishBowl {
		int id;
		int[] height, topShape, floor;

		public FishBowl(int id, int[] height, int[] topShape, int[] floor) {
			this.id = id;
			this.height = height;
			this.topShape = topShape;
			this.floor = floor;
		}
	}

	HashMap<Integer, FishBowl> fishBowls;
	int[] sortedIds;
	int W, H;

	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
		W = mWidth;
		H = mHeight;

		fishBowls = new LinkedHashMap<>();
		sortedIds = new int[N];
		for (int i = 0; i < N; i++) {
			int id = mIDs[i];
			sortedIds[i] = id;
			int[] height = Arrays.copyOf(mLengths[i], mWidth);
			int[] topShape = Arrays.copyOf(mUpShapes[i], mWidth);
			int[] floor = new int[mHeight + 1];
			Arrays.fill(floor, mWidth);
			floor[0] = 0;
			for (int h : height) {
				for (int n = 1; n <= h; n++) {
					floor[n]--;
				}
			}

			FishBowl fb = new FishBowl(id, height, topShape, floor);
			fishBowls.put(id, fb);
		}

		Arrays.sort(sortedIds);
	}

	// 구조물 설치 확인 - 갯수 반환
	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		int point = 0;

		for (int id : sortedIds) {
			FishBowl fb = fishBowls.get(id);
			
			for (int r = 0; r <= W - 3; r++) {
				
				// 결합판 일치여부 체크
				if (fb.topShape[r] != mDownShapes[0] || 
					fb.topShape[r + 1] != mDownShapes[1] || 
					fb.topShape[r + 2] != mDownShapes[2]) {
					continue;
				}

				// 2. 구조물들 간 인접 체크
				int bottom0 = fb.height[r];
				int top0 = fb.height[r] + mLengths[0] - 1;

				int bottom1 = fb.height[r + 1];
				int top1 = fb.height[r + 1] + mLengths[1] - 1;

				int bottom2 = fb.height[r + 2];
				int top2 = fb.height[r + 2] + mLengths[2] - 1;

				boolean connected01 = bottom0 <= top1 && bottom1 <= top0;
				boolean connected12 = bottom1 <= top2 && bottom2 <= top1;

				if (!connected01 || !connected12) continue;

				// 3. 어항 높이 초과 체크
				if (top0 + 1 > H || top1 + 1 > H || top2 + 1 > H) continue;

				point++;
			}
		}

		return point;
	}

	// 구조물 설치 - 설치한 어항 ID 위치 반환
	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		for (int id : sortedIds) {
			FishBowl fb = fishBowls.get(id);

			for (int r = 0; r <= W - 3; r++) {

				// 결합판 일치여부 체크
				if (fb.topShape[r] != mDownShapes[0] || 
					fb.topShape[r + 1] != mDownShapes[1] || 
					fb.topShape[r + 2] != mDownShapes[2]) {
					continue;
				}

				// 2. 구조물들 간 인접 체크
				int bottom0 = fb.height[r];
				int top0 = fb.height[r] + mLengths[0] - 1;

				int bottom1 = fb.height[r + 1];
				int top1 = fb.height[r + 1] + mLengths[1] - 1;

				int bottom2 = fb.height[r + 2];
				int top2 = fb.height[r + 2] + mLengths[2] - 1;

				boolean connected01 = bottom0 <= top1 && bottom1 <= top0;
				boolean connected12 = bottom1 <= top2 && bottom2 <= top1;

				if (!connected01 || !connected12) continue;

				// 3. 어항 높이 초과 체크
				if (top0 + 1 > H || top1 + 1 > H || top2 + 1 > H) continue;

				// 구조물 설치
				for (int i = 0; i < 3; i++) {
					int beforeHeight = fb.height[r + i];

					for (int f = beforeHeight + 1; f <= beforeHeight + mLengths[i]; f++) {
						fb.floor[f]--;
					}
					
					// 어항 높이 및 상단 결합판 갱신
					fb.height[r + i] += mLengths[i];
					fb.topShape[r + i] = mUpShapes[i];
				}

				fishBowls.replace(id, fb);

				return fb.id * 1000 + (r + 1);
			}
		}

		return 0;
	}

	// 물붓기 - 최대 높이를 갖는 어항 반환
	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;

		for (int id : sortedIds) {
			int height = 0;
			int water = mWater;
			int[] floor = fishBowls.get(id).floor;
			for (int f = 1; f <= H; f++) {

				if (floor[f] == 0)
					continue;

				if (water >= floor[f]) {
					water -= floor[f];
					height = f;
				} else {
					break;
				}
			}

			int usedWater = mWater - water;

			if (ret.height < height) {
				ret.ID = id;
				ret.height = height;
				ret.used = usedWater;
			} else if (ret.height == height) {
				if (ret.used < usedWater) {
					ret.ID = id;
					ret.used = usedWater;
				} else if (ret.used == usedWater) {
					if (ret.ID > id) {
						ret.ID = id;
					}
				}
			}
		}

		return ret;
	}
}