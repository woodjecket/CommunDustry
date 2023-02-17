package cd.type.valence;

import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.struct.Seq;
import mindustry.type.Item;

import java.util.concurrent.atomic.AtomicReference;

public class ResultMap2D implements RMap {
    private final QuadTree<ResultObject> tree;
    public int max;
    public Seq<Item> results;

    public ResultMap2D(int max) {
        this.max = max;
        results = new Seq<>(max + 1);
        tree = new QuadTree<>(new Rect(-max, -max, max * 2, max * 2));
    }

    public void putResult(Result result, Rect range) {
        tree.insert(new ResultObject(range.x, range.y, range.width, range.height, result));
        results.add(result.potentialItem);
    }

    public void putResult(Result result, float x, float y) {
        putResult(result, new Rect(x, y, 1, 1));
    }

    public Result getResult(float x, float y) {
        AtomicReference<Result> result = new AtomicReference<>();
        tree.intersect(new Rect().setCentered(x, y, 0.5f, 0.5f), r -> {
            result.set(r.result);
        });
        return result.get();
    }

    public QuadTree<ResultObject> tree() {
        return tree;
    }

    public static class ResultObject implements QuadTree.QuadTreeObject {
        public float x;
        public float y;
        public float width;
        public float height;
        public Result result;

        public ResultObject(float x, float y, float width, float height, Result result) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.result = result;
        }

        @Override
        public void hitbox(Rect out) {
            out.set(x, y, width, height);
        }
    }
}
