package collections;
public class CustomArrayList {

	
		
		// TODO Auto-generated method stub
		public boolean add(Object o) {
			if(this.contains(o)) {
				
				return true;
			}
			else {
				return super.equals(o);
			}
		}
		private boolean contains(Object o) {
			// TODO Auto-generated method stub
			return false;
		}
		public static void main(String[] args) {
			CustomArrayList list2=new CustomArrayList();
			list2.add(1);
			list2.add(1);
			list2.add(1);
			list2.add(3);
			System.out.println(list2);
		}

	}


