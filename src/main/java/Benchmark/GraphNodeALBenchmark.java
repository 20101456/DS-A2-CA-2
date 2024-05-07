package Benchmark;

import Application.GraphNodeAL;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

@State(Scope.Thread)
public class GraphNodeALBenchmark {

    private GraphNodeAL<Integer> node1;
    private GraphNodeAL<Integer> node2;
    private GraphNodeAL<Integer> node3;
    private List<GraphNodeAL<?>> encountered;

    @Setup(Level.Trial)
    public void setup() {
        node1 = new GraphNodeAL<>(1);
        node2 = new GraphNodeAL<>(2);
        node3 = new GraphNodeAL<>(3);
        node1.connectToNodeUndirected(node2);
        node2.connectToNodeUndirected(node3);
        encountered = new ArrayList<>();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void testFindPathDepthFirst() {
        GraphNodeAL.findPathDepthFirst(node1, encountered, 3);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void testFindAllPathsDepthFirst() {
        GraphNodeAL.findAllPathsDepthFirst(node1, encountered, 3);
    }
}
