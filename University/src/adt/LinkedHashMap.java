package adt;
import entity.*;




public class LinkedHashMap<K, V> implements LinkedHashMapInterface<K,V>{
     
     private Entry<K,V>[] table;   //Array of Entry.
     private int capacity= 4;  //Initial capacity of HashMap
     private Entry<K,V> header; //head of the doubly linked list.
     private Entry<K,V> last; //last of the doubly linked list.
     private static int numberOfEntries;
   
 
     /*
      * before and after are used for maintaining insertion order.
      */
     static class Entry<K, V> {
         K key;
         V value;
         Entry<K,V> next;
         Entry<K,V> before,after;
            
         public Entry(K key, V value, Entry<K,V> next){
             this.key = key;
             this.value = value;
             this.next = next;
         }
     }
     
 
    @SuppressWarnings("unchecked")
    public LinkedHashMap(){
       table = new Entry[capacity];
    }
 
   
 
    /**
     * Method allows you put key-value pair in LinkedHashMapCustom.
     * If the map already contains a mapping for the key, the old value is replaced.
     * Note: method does not allows you to put null key thought it allows null values.
     * Implementation allows you to put custom objects as a key as well.
     * Key Features: implementation provides you with following features:-
     *     >provide complete functionality how to override equals method.
     *  >provide complete functionality how to override hashCode method.
     * @param newKey
     * @param data
     */
    
    public void put(K newKey, V data){
        
       if(newKey==null)
           return;    //does not allow to store null.
      
       int hash=hash(newKey);
       
       Entry<K,V> newEntry = new Entry<K,V>(newKey, data, null);
       maintainOrderAfterInsert(newEntry);       
        if(table[hash] == null){
         table[hash] = newEntry;
         
        }else{
           Entry<K,V> previous = null;
           Entry<K,V> current = table[hash];
           while(current != null){ //we have reached last entry of bucket.
           if(current.key.equals(newKey)){                  
               if(previous==null){  //node has to be insert on first of bucket.
                     newEntry.next=current.next;
                     table[hash]=newEntry;
                     return;
               }
               
               else{
                   newEntry.next=current.next;
                   previous.next=newEntry;
                   return;
               }
           }
           previous=current;
             current = current.next;
         }
         previous.next = newEntry;
        }
        
        numberOfEntries++;
    }
 
   
    /**
     * below method helps us in ensuring insertion order of LinkedHashMapCustom
     * after new key-value pair is added.
     */
    private void maintainOrderAfterInsert(Entry<K, V> newEntry) {
           
       if(header==null){
           header=newEntry;
           last=newEntry;
           return;
       }
      
       if(header.key.equals(newEntry.key)){
           deleteFirst();
           insertFirst(newEntry);
           return;
       }
      
       if(last.key.equals(newEntry.key)){
           deleteLast();
           insertLast(newEntry);
           return;
       }
      
       Entry<K, V> beforeDeleteEntry=deleteSpecificEntry(newEntry);
       if(beforeDeleteEntry==null){
           insertLast(newEntry);
       }
       else{
           insertAfter(beforeDeleteEntry,newEntry);
       }
      
      
    }
 
    /**
     * below method helps us in ensuring insertion order of LinkedHashMapCustom,
     * after deletion of key-value pair.
     */
    private void maintainOrderAfterDeletion(Entry<K, V> deleteEntry) {
           
       if(header.key.equals(deleteEntry.key)){
           deleteFirst();
           return;
       }
      
       if(last.key.equals(deleteEntry.key)){
           deleteLast();
           return;
       }
      
       deleteSpecificEntry(deleteEntry);
      
    }
 
    /**
     * returns entry after which new entry must be added.
     */
    private void insertAfter(Entry<K, V> beforeDeleteEntry, Entry<K, V> newEntry) {
       Entry<K, V> current=header;
           while(current!=beforeDeleteEntry){
                  current=current.after;  //move to next node.
           }
           
           newEntry.after=beforeDeleteEntry.after;
           beforeDeleteEntry.after.before=newEntry;
           newEntry.before=beforeDeleteEntry;
           beforeDeleteEntry.after=newEntry;
           
    }
 
 
    
    /**
     * deletes entry from first.
     */
    private void deleteFirst(){
 
       if(header==last){ //only one entry found.
                  header=last=null;
                  return;
           }
           header=header.after;
           header.before=null;
           
    }
    
    /**
     * inserts entry at first.
     */
    private void insertFirst(Entry<K, V> newEntry){      
           
           if(header==null){ //no entry found
                  header=newEntry;
                  last=newEntry;
                  return;
           }
           
           newEntry.after=header;
           header.before=newEntry;
           header=newEntry;
           
    }
 
    /**
     * inserts entry at last.
     */
    private void insertLast(Entry<K, V> newEntry){
           
           if(header==null){
                  header=newEntry;
                  last=newEntry;
                  return;
           }
           last.after=newEntry;
           newEntry.before=last;
           last=newEntry;
                  
    }
    
    /**
     * deletes entry from last.
     */
    private void deleteLast(){
           
           if(header==last){
                  header=last=null;
                  return;
           }
           
           last=last.before;
           last.after=null;
    }
    
    public boolean isEmpty(){
    return numberOfEntries==0;
    }
 
    /**
     * deletes specific entry and returns before entry.
     */
    private Entry<K, V> deleteSpecificEntry(Entry<K, V> newEntry){
                        
           Entry<K, V> current=header;
           while(!current.key.equals(newEntry.key)){
                  if(current.after==null){   //entry not found
                        return null;
                  }
                  current=current.after;  //move to next node.
           }
           
           Entry<K, V> beforeDeleteEntry=current.before;
           current.before.after=current.after;
           current.after.before=current.before;  //entry deleted
           return beforeDeleteEntry;
    }
 
 
    /**
     * Method returns value corresponding to key.
     * @param key
     */
    public V get(K key){
        int hash = hash(key);
        if(table[hash] == null){
         return null;
        }else{
         Entry<K,V> temp = table[hash];
         while(temp!= null){
             if(temp.key.equals(key))
                 return temp.value;
             temp = temp.next; //return value corresponding to key.
         }         
         return null;   //returns null if key is not found.
        }
    }
 
    public ListInterface<K> getAllKeys() {
        ListInterface<K> keys = new ArrayList<>();
        Entry<K, V> current = header;
        while (current != null) {
            keys.add(current.key);
            current = current.after;
        }
        return keys;
    }
  
    /**
     * Method removes key-value pair from HashMapCustom.
     * @param key
     */
    public boolean remove(K deleteKey){
       
       int hash=hash(deleteKey);
              
      if(table[hash] == null){
            return false;
      }else{
        Entry<K,V> previous = null;
        Entry<K,V> current = table[hash];
        
        while(current != null){ //we have reached last entry node of bucket.
           if(current.key.equals(deleteKey)){
               maintainOrderAfterDeletion(current);
               if(previous==null){  //delete first entry node.
                     table[hash]=table[hash].next;
                      numberOfEntries--;
                     return true;
               }
               else{
                     previous.next=current.next;
                      numberOfEntries--;
                   return true;
               }
           }
           previous=current;
             current = current.next;
            
          }
        return false;
      }
    
    }
    
    public int size(){
        return numberOfEntries;
    }
    
   public boolean containsKey(K keys){
       
       Entry<K, V> current=header;
           while(current !=null ){
                  if(current.key.equals(keys))
                      return true;
                  
                  current=current.after;  //move to next node.
           }
       
       
       return false;
       
       
   }
 
    /**
     * Method displays all key-value pairs present in HashMapCustom.,
     * insertion order is not guaranteed, for maintaining insertion order
     * refer linkedHashMapCustom.
     * @param key
     */
   
   public void displayKeys(){
       
        Entry<K, V> currentEntry=header;
       while(currentEntry!=null){
           
           System.out.print(currentEntry.key+"" +" \n");
                   
           currentEntry=currentEntry.after;
       }
   }
   
  
   
    public void displayValues(){
       
       Entry<K, V> currentEntry=header;
       while(currentEntry!=null){
           
           System.out.print(currentEntry.value +" \n");
           
           
           
           currentEntry=currentEntry.after;
       }
    
    }
    
    
    
    
    /**
     * Method implements hashing functionality, which helps in finding the appropriate
     * bucket location to store our data.
     * This is very important method, as performance of HashMapCustom is very much
     * dependent on this method's implementation.
     * @param key
     */
    private int hash(K key){
        return Math.abs(key.hashCode()) % capacity;
    }
 
}