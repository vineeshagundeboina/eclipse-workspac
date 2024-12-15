package collection;
import java.util.*;

public class StackPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Stack<String> books=new Stack<>();
		/*books.add("red");
		books.add("yellow");
		books.add(0,"white");*/
		
		//books.remove("yellow");
		//books.set(1, "orange");
		//System.out.println(books.get(2));
		
		books.push("white");
		books.push("red");
		books.push("yellow");
		System.out.println(books);
		System.out.println(books.peek());
		//System.out.println(books.pop());
		System.out.println(books.search("yellow"));
		System.out.println(books.indexOf("yellow"));
		System.out.println(books.isEmpty());
		System.out.println(books.empty());




		
		System.out.println(books);

		
 	}

}
