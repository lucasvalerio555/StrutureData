package app.list.app;

@SuppressWarnings("unchecked")
public class ListLinked <T> {
    private Node next = null;
    private Node start = null;
    private StringBuilder bilder = null;
    private int size = 0;
    private int count = 0;
    private Node[] DataElement =new Node[10]; 
    
    public boolean isEmpty(){
        return this.start == null;
    }

    public int Size(){
        return size;
    }


    private Node orderMatrix(int index){
        return null;
    }
    

    private Node desorderMatrix(int index){
        return null;
    }


    public Node search(int index){
        int rigth = DataElement.length -1;  
        int i = 0;  

           if(index == 0){
               return DataElement[0];
                
           }else if(index == rigth){ 
               return DataElement[rigth];

           }else if(index < size){         
                if(index < 10)  
                   return DataElement[index];   
                           
                i = (int)(Math.round(Math.log10(index)));
           }

         return orderMatrix(i); 
    }


    public void allArray(Node node){
        if(DataElement[0] == null){
           count = 0;
           this.bilder =new StringBuilder();
           this.bilder.append("");
        }
        
        if(count < 10 && node != null){
           DataElement[count] = node;
           count++;   
           //LinkendString(node.getData(), ",");

           this.bilder.append(node.getData());
       }  
    } 

    /*

    private void LinkendString(String bilder , String str){
       .append(str);
    } 
    */

    public T get(int index){
         if (index == 0)
            return (T)(this.start.getData());
      
        return (T)(search(index).getData());   
       
    }

    public String showData(){
        return "["+this.bilder.toString()+"]";       
    }


    public void add(T element){
        Node current =new Node();

        if(isEmpty()){
            this.start = current;
            this.start.setData(element);
            this.next = this.start;           

        }else{
            this.next.setNode(current);
            this.next = this.next.getNode();
            this.next.setData(element);
            allArray(this.next);
        }

       size++;
    }

    public void clear(){
       this.start = null;   
    }


   public void remove(int index){
        if(!isEmpty() && index < size){
            if (index != 0 && index < 10){
                this.next = DataElement[index];
                this.next = this.next.getNode();
                DataElement[index] = this.next;
                DataElement[index + 1] = DataElement[index].getNode();
            }   
        }else{
            throw new 
                IndexOutOfBoundsException("Index is out of range");
        }        
    }
 }

class Node {
        private Node next = null;
        private Object Data;

        public void setNode(Node next) {
            this.next = next;
        }

        public Node getNode(){
            return this.next;
        }

        public void setData(Object Data) {
            this.Data = Data;
        }

        public Object getData(){
            return this.Data;
        }
}

