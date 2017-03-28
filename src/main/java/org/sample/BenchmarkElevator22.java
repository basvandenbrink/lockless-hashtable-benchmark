package org.sample;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import nl.utwente.csc.fmt.locklesshashtable.spehashtable.Hashtable;
import nl.utwente.csc.fmt.locklesshashtable.test.Test;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BenchmarkElevator22 {
	public static final int STEP_SIZE = 129600;
	
	
	@State(Scope.Group)
	public static class SharedData{
		Hashtable hashtable;
		int[][] vectors;
		private AtomicInteger index;
		
		@Setup
		public void setup() throws FileNotFoundException{
			vectors = Test.readVectors(getClass().getResourceAsStream("/elevator2.2.txt"));
			hashtable = new Hashtable(vectors.length);
			index = new AtomicInteger();
		}
		
		public int getStartIndex(){
			int result = index.getAndIncrement();
			return result;
		}
	}
	
	@State(Scope.Thread)
	public static class ThreadData{
		Hashtable hashtable;
		int[][] vectors;
		int startIndex;
		int endIndex;
		
		@Setup
		public void setup(SharedData shared){
			hashtable = shared.hashtable;
			vectors = shared.vectors;
			startIndex = shared.getStartIndex() * STEP_SIZE;
			endIndex = startIndex + STEP_SIZE;
		}
		
	}
	
    @GenerateMicroBenchmark
    @Group("t1")
    @GroupThreads(1)
    @OperationsPerInvocation(STEP_SIZE)
    public boolean benchmark1(ThreadData data){
    	boolean result = true;
    	for(int i = data.startIndex; i < data.endIndex; i++){
    		result ^= data.hashtable.lookup(data.vectors[i]);
    	}
    	return result;
    }
	
    @GenerateMicroBenchmark
    @Group("t2")
    @GroupThreads(2)
    @OperationsPerInvocation(STEP_SIZE)
    public boolean benchmark2(ThreadData data){
    	boolean result = true;
    	for(int i = data.startIndex; i < data.endIndex; i++){
    		result ^= data.hashtable.lookup(data.vectors[i]);
    	}
    	return result;
    }
	
    @GenerateMicroBenchmark
    @Group("t4")
    @GroupThreads(4)
    @OperationsPerInvocation(STEP_SIZE)
    public boolean benchmark4(ThreadData data){
    	boolean result = true;
    	for(int i = data.startIndex; i < data.endIndex; i++){
    		result ^= data.hashtable.lookup(data.vectors[i]);
    	}
    	return result;
    }
    
    @GenerateMicroBenchmark
    @Group("t6")
    @GroupThreads(6)
    @OperationsPerInvocation(STEP_SIZE)
    public boolean benchmark6(ThreadData data){
    	boolean result = true;
    	for(int i = data.startIndex; i < data.endIndex; i++){
    		result ^= data.hashtable.lookup(data.vectors[i]);
    	}
    	return result;
    }
    
    @GenerateMicroBenchmark
    @Group("t8")
    @GroupThreads(8)
    @OperationsPerInvocation(STEP_SIZE)
    public boolean benchmark(ThreadData data){
    	boolean result = true;
    	for(int i = data.startIndex; i < data.endIndex; i++){
    		result ^= data.hashtable.lookup(data.vectors[i]);
    	}
    	return result;
    }
    
    public static void main(String[] args) throws RunnerException{
        Options opt = new OptionsBuilder()
        .include(".*" + BenchmarkElevator22.class.getSimpleName() + ".*")
        .warmupIterations(5)
        .measurementIterations(5)
        .forks(1)
        .build();
        
        new Runner(opt).run();
    }

}
