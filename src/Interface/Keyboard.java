package Interface;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import javax.swing.JTextArea;

//public class Keyboard{
//	public LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
//}

public enum Keyboard {
	KEYBOARD;
	private final Lock lock = new ReentrantLock();
	private final Condition isWaiting = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	private final Condition outputUpdate = lock.newCondition();
	private final Condition outputEdited = lock.newCondition();
	private boolean isReallyWaiting = false;
	public LinkedList<String> currentChoice = new LinkedList<String>();
	public String modeActuel = ModeProgramme.ModesProgramme.MODE_DEFAULT.name();
	public LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>(1);
	private String output="";
	private boolean edited=false;
	
	public void myPut(String s){
	  lock.lock();
	  try {
	    while (!isReallyWaiting){
	      isWaiting.awaitUninterruptibly();
	    }
	    //System.out.print("// Here you go : ");
	    input.put(s);
	    notEmpty.signalAll();
	    isReallyWaiting=false; //Avoids two consecutive calls to myPut.
	  } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
	    lock.unlock();
	  }
	}
	
	public String myTake(){
	  String result="Error in myTake !";
	  lock.lock();
	  try {
	    isReallyWaiting=true;
  	  //System.out.print(this.modeActuel+" - I'm waiting ! ");
  	  isWaiting.signalAll();
  	  while(input.isEmpty()){
  	    notEmpty.awaitUninterruptibly();
  	  }
      result = input.take();
      System.out.println(result);
      //System.out.print(result+" // Thanks ! ");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
	  isReallyWaiting=false;
	  return result;
	}

  public void waitUntilReady() {
    lock.lock();
    try{
      while (!isReallyWaiting){
        isWaiting.awaitUninterruptibly();
      }
    } finally {
      lock.unlock();
    }
  }
  
  public String getChoices(){
    String choices="";
    while (!this.currentChoice.isEmpty()){
      choices+="\n"+this.currentChoice.removeFirst();
    }
    return choices;
  }
  
  public void editOutput(String s){
    lock.lock();
    try{
      output=s+"\n"+SwingMain.OUTPUT_STRING;
      outputUpdate.signalAll();
//      System.out.println("Output update signal");
      while (!edited)
        outputEdited.awaitUninterruptibly();
      edited=false;
    } finally {
      lock.unlock();
    }
  }

  public void updateOutput(JTextArea output) {
    lock.lock();
    try{
      while (true){
        while(this.output.equals(""))
          outputUpdate.awaitUninterruptibly();
        output.setText(this.output); 
        this.output = "";
        edited = true;
//        System.out.println("Output update written");
        outputEdited.signalAll();
      }   
    } finally {
      lock.unlock();
    }  
  }
  
  public void println(String s){
    System.out.println(s);
    this.editOutput(s);
//    System.out.println("Output updated");
  }
}