package java8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamsPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TestClass t=new TestClass();
			
		
		List<Integer> list=Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12);
		Stream<Integer> newStream=list.stream();
		Stream<Integer> filteredResult=newStream.filter(x->x%2==0);
		
		Stream<Integer> stream=Stream.of(1,2,34,5);
		
//		Stream<Integer> filteredResult=newStream.filter(t);

		
		filteredResult.forEach(x->{
			System.out.print(x+"    ");
		});

	}

}


class TestClass implements Predicate{
	public boolean test(Object t) {
		Integer num=(Integer)t;
		return num%2==0;
	}
}
