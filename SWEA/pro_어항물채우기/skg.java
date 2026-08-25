import java.util.Arrays;
import java.util.PriorityQueue;

/*
 * 안녕하세요 제가 오늘 면접이 있더라고요.. 그래서 이렇게나마 주석으로라도 함께 하려 합니다..
 * 
 * 이차원 배열이 아닌 input으로 주어진대로 높이 배열, 가장 위에 접합부 배열로 관리
 * checkStructures : 가능한 갯수 세기
 * addStructures : PQ에 가능한 녀석들 집어넣고 우선순위가 가장 높은 한 녀석에 붙이기.
 * 물붓기: 수면 높이를 점차 높여가면서 가능한 경우에 대해서 PQ에 넣고 우선순위가 가장 높은 한 녀석 뽑기.
 */
class Node {
	int idx, st;

	Node(int idx, int st) {
		this.idx = idx;
		this.st = st;
	}
}

class WNode  {
	int idx, used, h;
	WNode(int idx, int used, int h) {
		this.idx = idx;
		this.used = used;
		this.h =h;
	}
}

class UserSolution {
	int N;
	int width;
	int height;
	int IDs[];
	int lengths[][];
	int upShapes[][];

	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
		this.N = N;
		this.width = mWidth;
		this.height = mHeight;
		this.IDs = mIDs;
		this.lengths = mLengths;
		this.upShapes = mUpShapes;
	}

	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		int cnt = 0;

		// 모든 어항을 돌면서 가능한 거 PQ에 넣기
		for (int n = 0; n < N; n++) {
			// 각 어항의 width를 돌면서 가능 여부 찾기
			for (int i = 0; i < width - 2; i++) {
				// 접합 가능 여부 확인
				if (upShapes[n][i] != mDownShapes[0])
					continue;
				if (upShapes[n][i + 1] != mDownShapes[1])
					continue;
				if (upShapes[n][i + 2] != mDownShapes[2])
					continue;
				// 최소 한 셀의 면이 붙어있는지 확인 -> 두 기둥에서 낮은 녀석의 접합 후 높이가 높은 녀석의 원래 높이보다 커야 함.
				int[] prev_lens = { lengths[n][i], lengths[n][i + 1], lengths[n][i + 2] };
				int[] nxt_lens = { prev_lens[0] + mLengths[0], prev_lens[1] + mLengths[1], prev_lens[2] + mLengths[2] };

				if (Math.max(prev_lens[0], prev_lens[1]) >= Math.min(nxt_lens[0], nxt_lens[1]))
					continue;
				if (Math.max(prev_lens[1], prev_lens[2]) >= Math.min(nxt_lens[1], nxt_lens[2]))
					continue;
				// 높이 초과 확인
				if (Math.max(Math.max(nxt_lens[0], nxt_lens[1]), nxt_lens[2]) > height)
					continue;
				cnt++;
			}

		}
//		System.out.println("check: " + cnt);
		return cnt;
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		PriorityQueue<Node> PQ = new PriorityQueue<>((a, b) -> {
			if (IDs[a.idx] == IDs[b.idx]) {
				return Integer.compare(a.st, b.st);
			}
			return Integer.compare(IDs[a.idx], IDs[b.idx]);
		});
		
		// 모든 어항을 돌면서 가능한 거 PQ에 넣기
		for (int n = 0; n < N; n++) {
			// 각 어항의 width를 돌면서 가능 여부 찾기
			for (int i = 0; i < width - 2; i++) {
				// 접합 가능 여부 확인
				if (upShapes[n][i] != mDownShapes[0])
					continue;
				if (upShapes[n][i + 1] != mDownShapes[1])
					continue;
				if (upShapes[n][i + 2] != mDownShapes[2])
					continue;
				// 최소 한 셀의 면이 붙어있는지 확인 -> 두 기둥에서 낮은 녀석의 접합 후 높이가 높은 녀석의 원래 높이보다 커야 함.
				int[] prev_lens = { lengths[n][i], lengths[n][i + 1], lengths[n][i + 2] };
				int[] nxt_lens = { prev_lens[0] + mLengths[0], prev_lens[1] + mLengths[1], prev_lens[2] + mLengths[2] };

				if (Math.max(prev_lens[0], prev_lens[1]) >= Math.min(nxt_lens[0], nxt_lens[1]))
					continue;
				if (Math.max(prev_lens[1], prev_lens[2]) >= Math.min(nxt_lens[1], nxt_lens[2]))
					continue;

				// 높이 초과 확인
				if (Math.max(Math.max(nxt_lens[0], nxt_lens[1]), nxt_lens[2]) > height)
					continue;

				PQ.add(new Node(n, i));
			}

		}
		if (!PQ.isEmpty()) {
//			System.out.println("add: " + IDs[PQ.peek().idx] * 1000 + (PQ.peek().st + 1));
			for (int j = 0; j < 3; j++) {
				lengths[PQ.peek().idx][PQ.peek().st + j] += mLengths[j];
				upShapes[PQ.peek().idx][PQ.peek().st + j] = mUpShapes[j];
			}
			return IDs[PQ.peek().idx] * 1000 + PQ.peek().st + 1;
		}
//		System.out.println("add: " + 0);
		return 0;
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;
		
		PriorityQueue<WNode> PQ = new PriorityQueue<>((a, b) -> {
			if (a.h == b.h) {
				if (a.used == b.used) {
					return Integer.compare(IDs[a.idx], IDs[b.idx]);
				}
				return Integer.compare(b.used, a.used);
			}
			return Integer.compare(b.h, a.h);
		});
		
		
		for (int n = 0; n < N; n++) {
			int used = 0;
			int prev_used = 0;
			// 물 높이 1부터 채워보기
			for (int h = 1; h <= height + 1; h++) {
				prev_used = used;
				// 높이까지 물이 모두 채워진 경우
				if (h == height + 1 && prev_used != 0) {
					PQ.add(new WNode(n, prev_used, h-1));
//					System.out.println("p-added!: " + IDs[n] + "/" + (h-1) + "/" + prev_used);
					break;
				}
				// 들어올 수 있는 물의 양 계산
				for (int i = 0; i < width; i++) {
					if (lengths[n][i] < h) {
						used++;
					}
				}
				if (used > mWater) {
					if (prev_used != 0)	{
						PQ.add(new WNode(n, prev_used, h-1));
//						System.out.println("p-added!: " + IDs[n] + "/" + (h-1) + "/" + prev_used);
					}
					break;
				}
			}
		}

		if (PQ.isEmpty())	{
//			System.out.println("pull: " + ret.ID + "/" + ret.height + "/" + ret.used);
			return ret;
		}
		WNode r = PQ.peek();
		ret.ID = IDs[r.idx];
		ret.height = r.h;
		ret.used = r.used;
//		System.out.println("pull: " + ret.ID + "/" + ret.height + "/" + ret.used);
		return ret;
	}

}
